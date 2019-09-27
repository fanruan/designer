/**
 *
 */
package com.fr.design.mainframe.bbs;

import com.fr.base.FRContext;
import com.fr.config.MarketConfig;
import com.fr.design.DesignerEnvManager;
import com.fr.design.bbs.BBSLoginUtils;
import com.fr.design.extra.LoginContextListener;
import com.fr.design.extra.LoginWebBridge;
import com.fr.design.extra.PluginWebBridge;
import com.fr.design.extra.UserLoginContext;
import com.fr.design.extra.WebViewDlgHelper;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.imenu.UIMenuItem;
import com.fr.design.gui.imenu.UIPopupMenu;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.upm.event.CertificateEvent;
import com.fr.design.utils.concurrent.ThreadFactoryBuilder;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.event.Event;
import com.fr.event.EventDispatcher;
import com.fr.event.Listener;
import com.fr.general.CloudCenter;
import com.fr.general.ComparatorUtils;
import com.fr.general.DateUtils;
import com.fr.general.http.HttpClient;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.EncodeConstants;
import com.fr.stable.os.Arch;
import com.fr.stable.os.OperatingSystem;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;

import javax.swing.SwingConstants;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author neil
 * @date: 2015-3-4-上午9:05:52
 */
public class UserInfoLabel extends UILabel {

    private static final int VERSION_8 = 8;
    //默认查询消息时间, 30s
    private static final long CHECK_MESSAGE_TIME = 30 * 1000L;
    //默认论坛检测到更新后的弹出延迟时间
    private static final long DELAY_TIME = 2 * 1000L;
    private static final String MESSAGE_KEY = "messageCount";

    private static final int MIN_MESSAGE_COUNT = 1;
    private static final int MENU_HEIGHT = 20;

    private static final int DEFAULT_BBS_UID = 0;

    //用户名
    private String userName;
    //消息条数
    private int messageCount;

    private UserInfoPane userInfoPane;
    private BBSLoginDialog bbsLoginDialog;
    private MouseAdapter userInfoAdapter = new MouseAdapter() {

        @Override
        public void mouseEntered(MouseEvent e) {
            UserInfoLabel.this.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            userName = MarketConfig.getInstance().getBbsUsername();
            if (StringUtils.isNotEmpty(userName)) {
                UIPopupMenu menu = new UIPopupMenu();
                menu.setOnlyText(true);
                menu.setPopupSize(userInfoPane.getWidth(), userInfoPane.getHeight() * 3);

                //私人消息
                UIMenuItem priviteMessage = new UIMenuItem(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_BBSLogin_Privite_Message"));
                priviteMessage.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (StringUtils.isNotEmpty(userName)) {
                            try {
                                String loginUrl = CloudCenter.getInstance().acquireUrlByKind("bbs.default");
                                Desktop.getDesktop().browse(new URI(loginUrl));
                            } catch (Exception exp) {
                                FineLoggerFactory.getLogger().info(exp.getMessage());
                            }
                        }
                    }

                });
                //切换账号
                UIMenuItem closeOther = new UIMenuItem(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_BBSLogin_Switch_Account"));
                closeOther.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        BBSLoginUtils.bbsLogout();
                        UserLoginContext.fireLoginContextListener();
                    }
                });
                menu.add(priviteMessage);
                menu.add(closeOther);
                GUICoreUtils.showPopupMenu(menu, UserInfoLabel.this, 0, MENU_HEIGHT);
            } else {
                UserLoginContext.fireLoginContextListener();
            }
        }
    };

    public UserInfoLabel(UserInfoPane userInfoPane) {
        init(userInfoPane);
    }

    /**
     * showBBSDialog 弹出BBS资讯框
     */
    public static void showBBSDialog() {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("bbs-dlg-thread-%s").build();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1), namedThreadFactory);
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                // vito:最新mac10.12和javafx弹出框初始化时会有大几率卡死在native方法，这里先屏蔽一下。
                //ARM下暂时也不能用javafx
                if (!shouldShowBBSDialog()) {
                    return;
                }
                String lastBBSNewsTime = DesignerEnvManager.getEnvManager().getLastShowBBSNewsTime();
                try {
                    String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                    if (ComparatorUtils.equals(lastBBSNewsTime, today)) {
                        return;
                    }
                    Thread.sleep(DELAY_TIME);
                } catch (InterruptedException e) {
                    FineLoggerFactory.getLogger().error(e.getMessage(), e);
                }
                HttpClient hc = new HttpClient(CloudCenter.getInstance().acquireUrlByKind("bbs.popup"));
                if (!hc.isServerAlive()) {
                    return;
                }
                String res = hc.getResponseText();
                if (!res.contains(BBSConstants.UPDATE_KEY)) {
                    return;
                }
                try {
                    Class<?> clazz = Class.forName("com.fr.design.mainframe.bbs.BBSDialog");
                    Constructor constructor = clazz.getConstructor(Frame.class);
                    Object instance = constructor.newInstance(DesignerContext.getDesignerFrame());
                    Method showWindow = clazz.getMethod("showWindow", String.class);
                    showWindow.invoke(instance, CloudCenter.getInstance().acquireUrlByKind("bbs.popup"));
                    DesignerEnvManager.getEnvManager().setLastShowBBSNewsTime(DateUtils.DATEFORMAT2.format(new Date()));
                } catch (Throwable ignored) {
                    // ignored
                }
            }
        });
    }

    public UserInfoPane getUserInfoPane() {
        return userInfoPane;
    }

    public void setUserInfoPane(UserInfoPane userInfoPane) {
        this.userInfoPane = userInfoPane;
    }

    public BBSLoginDialog getBbsLoginDialog() {
        return bbsLoginDialog;
    }

    public void setBbsLoginDialog(BBSLoginDialog bbsLoginDialog) {
        this.bbsLoginDialog = bbsLoginDialog;
    }

    private void init(UserInfoPane userInfoPane) {
        this.userInfoPane = userInfoPane;

        this.addMouseListener(userInfoAdapter);
        this.setHorizontalAlignment(SwingConstants.CENTER);

        if (StableUtils.getMajorJavaVersion() == VERSION_8) {
            LoginWebBridge.getHelper().setUILabel(UserInfoLabel.this);
            PluginWebBridge.getHelper().setUILabel(UserInfoLabel.this);
        }

        UserLoginContext.addLoginContextListener(new LoginContextListener() {
            @Override
            public void showLoginContext() {
                WebViewDlgHelper.createLoginDialog();
                clearLoginInformation();
                updateInfoPane();
            }
        });
        EventDispatcher.listen(CertificateEvent.LOGIN, new Listener<String>() {
            @Override
            public void on(Event event, String text) {
                setText(text);
            }
        });
        EventDispatcher.listen(CertificateEvent.LOGOUT, new Listener<String>() {
            @Override
            public void on(Event event, String text) {
                setText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Base_UnSignIn"));
            }
        });
    }

    private void clearLoginInformation() {
        BBSLoginUtils.bbsLogout();
    }

    private void updateInfoPane() {
        userInfoPane.markUnSignIn();
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
    }

    private static boolean shouldShowBBSDialog(){
        return FRContext.isChineseEnv() && !OperatingSystem.isMacos() && Arch.getArch() != Arch.ARM;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        if (StringUtils.isEmpty(userName)) {
            return;
        }

        this.userName = userName;
    }

    /**
     * 重置当前用户名
     */
    public void resetUserName() {
        this.userName = StringUtils.EMPTY;
    }

    private String encode(String str) {
        try {
            return URLEncoder.encode(str, EncodeConstants.ENCODING_UTF_8);
        } catch (UnsupportedEncodingException e) {
            return str;
        }
    }

    public int getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(int messageCount) {
        // 当只有一条消息时，阅读之后，消息面板重置为只含用户名的状态
        if (this.messageCount == MIN_MESSAGE_COUNT && messageCount < MIN_MESSAGE_COUNT) {
            this.setText(this.userName);
            return;
        }
        if (this.messageCount == messageCount || messageCount < MIN_MESSAGE_COUNT) {
            return;
        }

        this.messageCount = messageCount;
        String sb = StringUtils.BLANK + this.userName +
                "(" + this.messageCount +
                ")" + StringUtils.BLANK;
        //内容eg: aaa(11)

        //更新面板Text
        this.setText(sb);
    }
}
