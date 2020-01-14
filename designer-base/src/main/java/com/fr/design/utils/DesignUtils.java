package com.fr.design.utils;

import com.fr.base.FeedBackInfo;
import com.fr.base.ServerConfig;
import com.fr.concurrent.NamedThreadFactory;
import com.fr.design.DesignerEnvManager;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.dialog.FineJOptionPane;
import com.fr.design.fun.DesignerEnvProcessor;
import com.fr.design.gui.UILookAndFeel;
import com.fr.design.mainframe.DesignerContext;
import com.fr.file.FileFILE;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRFont;
import com.fr.general.GeneralContext;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.ArrayUtils;
import com.fr.stable.CommonCodeUtils;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;
import com.fr.start.ServerStarter;
import com.fr.workspace.WorkContext;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.Desktop;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Some util method of Designer
 */
public class DesignUtils {
    private static int port = DesignerPort.MESSAGE_PORT;

    private DesignUtils() {
    }


    public synchronized static void setPort(int port) {
        DesignUtils.port = port;
    }

    public synchronized static int getPort() {
        return port;
    }

    /**
     * 通过端口是否被占用判断设计器有没有启动
     * s
     *
     * @return 启动了返回true
     */
    public static boolean isStarted() {
        try (Socket socket = new Socket("localhost", port)) {
            return true;
        } catch (Exception ignored) {
        }
        return false;
    }

    /**
     * 向服务器发送命令行，给服务器端处理
     *
     * @param lines 命令行
     */
    public static void clientSend(String[] lines) {
        if (lines == null || lines.length == 0) {
            return;
        }
        Socket socket = null;
        PrintWriter writer = null;
        try {
            socket = new Socket("localhost", port);

            writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8)));
            for (int i = 0; i < lines.length; i++) {
                writer.println(lines[i]);
            }
            writer.flush();
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                FineLoggerFactory.getLogger().error(e.getMessage(), e);
            }
        }
    }

    /**
     * 建立监听端口
     *
     * @param startPort 端口
     * @param suffixs   文件后缀
     */
    @SuppressWarnings("squid:S2095")
    public static void createListeningServer(final int startPort, final String[] suffixs) {
        ExecutorService service = Executors.newSingleThreadExecutor(new NamedThreadFactory("DesignClientListener"));
        service.execute(new Runnable() {
            @Override
            public void run() {
                ServerSocket serverSocket = null;
                try {
                    serverSocket = new ServerSocket(startPort);
                } catch (IOException e1) {
                    FineLoggerFactory.getLogger().error("Cannot create server socket on " + port);
                }
                while (true) {
                    try {
                        if (serverSocket != null) {
                            // 接收客户连接
                            Socket socket = serverSocket.accept();
                            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                            String line;
                            while ((line = reader.readLine()) != null) {
                                if (line.startsWith("demo")) {
                                    DesignerEnvManager.getEnvManager().setCurrentEnv2Default();
                                    ServerStarter.browserDemoURL();
                                } else if (StringUtils.isNotEmpty(line)) {
                                    File f = new File(line);
                                    String path = f.getAbsolutePath();

                                    boolean isMatch = false;
                                    for (String suffix : suffixs) {
                                        isMatch = isMatch || path.endsWith(suffix);
                                    }
                                    if (isMatch) {
                                        DesignerContext.getDesignerFrame().openTemplate(new FileFILE(f));
                                    }
                                }
                            }
                            reader.close();
                            socket.close();
                        } else {
                            FineLoggerFactory.getLogger().error("Cannot create server socket on " + port);
                            break;
                        }
                    } catch (IOException ignored) {
                    }
                }
            }
        });

    }

    /**
     * 弹出对话框,显示报错
     *
     * @param message 报错信息
     */
    public static void errorMessage(String message) {
        final String final_msg = message;
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                FineJOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), final_msg);
            }
        });
    }

    public static void refreshDesignerFrame() {

        // 刷新DesignerFrame里面的面板
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                if (DesignerContext.getDesignerFrame() == null) {
                    return;
                }
                DesignerContext.getDesignerFrame().refreshEnv();
                DesignerContext.getDesignerFrame().repaint();// kunsnat: 切换环境后 刷新下 报表. 比如图表某些风格改变.
            }
        });
    }

    /**
     * p:初始化look and feel, 把一切放到这个里面.可以让多个地方调用.
     */
    public static void initLookAndFeel() {
        // p:隐藏对话框的系统标题风格，用look and feel定义的标题风格.
        try {
            UIManager.setLookAndFeel(UILookAndFeel.class.getName());
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error("Substance Raven Graphite failed to initialize");
        }
        //获取当前系统语言下设计器用的默认字体
        FRFont guiFRFont = getCurrentLocaleFont();
        //指定UIManager中字体
        Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement().toString();

            if (key.endsWith(".font")) {
                UIManager.put(key, isTextField(key) ? getNamedFont("Dialog") : guiFRFont);
            }
        }
    }

    private static boolean isTextField(String key) {
        return key.startsWith("TextField.") || key.startsWith("PasswordField.");
    }

    private static FRFont getCurrentLocaleFont() {
        FRFont guiFRFont;
        Locale defaultLocale = Locale.getDefault();

        if (isDisplaySimSun(defaultLocale)) {
            guiFRFont = getNamedFont("SimSun");
        } else if (isDisplayDialog(defaultLocale)) {
            guiFRFont = getNamedFont("Dialog");
        } else {
            guiFRFont = getNamedFont("Tahoma");
        }

        //先初始化的设计器locale, 后初始化lookandfeel.如果顺序改了, 这边也要调整.
        Locale designerLocale = GeneralContext.getLocale();
        String file = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_File");
        char displayChar = file.charAt(0);
        if (!guiFRFont.canDisplay(displayChar)) {
            //如果不能用默认的语言显示字体, 比如想在英文系统里用中文设计器
            //默认语言(中文:宋体, 英文:Tahoma, 其他:Dialog)
            guiFRFont = getNamedFont("SimSun");
            if (!guiFRFont.canDisplay(displayChar)) {
                //比如想在中文或英文系统里用韩文设计器
                guiFRFont = getNamedFont("Dialog");
                if (!guiFRFont.canDisplay(displayChar)) {
                    FineLoggerFactory.getLogger().error(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Base_SimSun_Not_Found"));
                }
            }
        }

        return guiFRFont;
    }

    private static FRFont getNamedFont(String name) {
        return FRFont.getInstance(name, Font.PLAIN, 12);
    }

    private static boolean isDisplaySimSun(Locale defaultLocale) {
        return ComparatorUtils.equals(defaultLocale, Locale.SIMPLIFIED_CHINESE);
    }

    private static boolean isDisplayDialog(Locale defaultLocale) {
        return ComparatorUtils.equals(defaultLocale, Locale.TRADITIONAL_CHINESE)
                || ComparatorUtils.equals(defaultLocale, Locale.JAPANESE)
                || ComparatorUtils.equals(defaultLocale, Locale.JAPAN)
                || ComparatorUtils.equals(defaultLocale, Locale.KOREAN)
                || ComparatorUtils.equals(defaultLocale, Locale.KOREA);
    }

    /**
     * 访问服务器环境-空参数
     */
    public static void visitEnvServer() {
        visitEnvServerByParameters(StringUtils.EMPTY, new String[]{}, new String[]{});
    }

    /**
     * 访问服务器环境
     *
     * @param names  参数名字
     * @param values 参数值
     */
    public static void visitEnvServerByParameters(String baseRoute, String[] names, String[] values) {
        int len = Math.min(ArrayUtils.getLength(names), ArrayUtils.getLength(values));
        String[] nameValuePairs = new String[len];
        for (int i = 0; i < len; i++) {
            //设计器里面据说为了改什么界面统一, 把分隔符统一用File.separator, 意味着在windows里面报表路径变成了\
            //以前的超链, 以及预览url什么的都是/, 产品组的意思就是用到的地方替换下, 真恶心.
            String value = values[i].replaceAll("\\\\", "/");
            // 两次 encode 支持中文和特殊符号，避免跳转登录后预览400报错
            nameValuePairs[i] =
                    CommonCodeUtils.encodeURIComponent(CommonCodeUtils.encodeURIComponent(names[i])) +
                            "=" + CommonCodeUtils.encodeURIComponent(CommonCodeUtils.encodeURIComponent(value));
        }
        String postfixOfUri = (nameValuePairs.length > 0 ? "?" + StableUtils.join(nameValuePairs, "&") : StringUtils.EMPTY);

        if (!WorkContext.getCurrent().isLocal()) {
            try {
                String urlPath = getWebBrowserPath();
                Desktop.getDesktop().browse(new URI(urlPath + baseRoute + postfixOfUri));
            } catch (Exception e) {
                FineLoggerFactory.getLogger().error("cannot open the url Successful", e);
            }
        } else {
            try {
                String web = GeneralContext.getCurrentAppNameOfEnv();
                String url = "http://localhost:" + DesignerEnvManager.getEnvManager().getEmbedServerPort()
                        + "/" + web + "/" + ServerConfig.getInstance().getServletName() + baseRoute
                        + postfixOfUri;
                ServerStarter.browserURLWithLocalEnv(url);
            } catch (Throwable ignored) {
            }
        }
    }

    private static String getWebBrowserPath() {
        String urlPath = WorkContext.getCurrent().getPath();
        DesignerEnvProcessor processor = ExtraDesignClassManager.getInstance().getSingle(DesignerEnvProcessor.XML_TAG);
        if (processor != null) {
            //cas访问的时候, url要处理下.
            urlPath = processor.getWebBrowserURL(urlPath);
        }
        return urlPath;
    }

    /**
     * 将用户反馈发送至服务器
     *
     * @param feedBackInfo 用户反馈
     * @return 发送成功返回true
     * @throws Exception 异常
     */
    public static boolean sendFeedBack(FeedBackInfo feedBackInfo) throws Exception {
        return true;
    }
}
