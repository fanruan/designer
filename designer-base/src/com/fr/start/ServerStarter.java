package com.fr.start;

import com.fr.base.FRContext;
import com.fr.base.ServerConfig;
import com.fr.design.DesignModelAdapter;
import com.fr.design.DesignerEnvManager;
import com.fr.design.data.datapane.TableDataTreePane;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.file.TemplateTreePane;
import com.fr.design.gui.itextarea.UITextArea;
import com.fr.design.mainframe.DesignerContext;
import com.fr.env.RemoteEnv;
import com.fr.env.SignIn;
import com.fr.general.ComparatorUtils;
import com.fr.general.GeneralContext;
import com.fr.general.Inter;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.OperatingSystem;
import com.fr.stable.ProductConstants;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;
import com.fr.start.server.FineEmbedServer;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class ServerStarter {
  
    /**
     * 预览Demo
     * 找默认工作目录，不应该按照名字去找，而应该按照安装路径，因为默认工作目录的名字可能会改变。
     */
    public static void browserDemoURL() {
    
        if (FRContext.getCurrentEnv() instanceof RemoteEnv) {
            browser(FRContext.getCurrentEnv().getPath());
            return;
        }
        if (ComparatorUtils.equals(StableUtils.getInstallHome(), ".")) {//august:供代码使用
            String web = GeneralContext.getCurrentAppNameOfEnv();
            browserURLWithLocalEnv("http://localhost:" + DesignerEnvManager.getEnvManager().getEmbedServerPort() + "/" + web + "/" + ServerConfig.getInstance().getServletName());
            return;
        }
        DesignerEnvManager envManager = DesignerEnvManager.getEnvManager();
        if (!envManager.isCurrentEnvDefault()) {
            InformationPane inf = new InformationPane(envManager.getDefaultEnvName());
            inf.showSmallWindow(DesignerContext.getDesignerFrame(), new DialogActionAdapter() {
    
                @Override
                public void doOk() {
        
                    try {
                        SignIn.signIn(DesignerEnvManager.getEnvManager().getDefaultEnv());
                        TemplateTreePane.getInstance().refreshDockingView();
                        TableDataTreePane.getInstance(DesignModelAdapter.getCurrentModelAdapter());
                    } catch (Exception e) {
                        FineLoggerFactory.getLogger().error(e.getMessage());
                    }
                    initDemoServerAndBrowser();
                }
    
            }).setVisible(true);
        } else {
            initDemoServerAndBrowser();
        }
    }
    
    private static void initDemoServerAndBrowser() {
        
        try {
            FineEmbedServer.start();
        } finally {
            //先访问Demo, 后访问报表, 不需要重置服务器.
            browser("http://localhost:" + DesignerEnvManager.getEnvManager().getEmbedServerPort() + "/" + GeneralContext.getCurrentAppNameOfEnv() + "/" + ServerConfig.getInstance().getServletName());
        }
    }
    
    /**
     * 本地环境浏览url
     *
     * @param url 指定路径
     */
    public static void browserURLWithLocalEnv(String url) {
    
        FineEmbedServer.start();
        browser(url);
    }
    
    private static void browser(String uri) {
        
        if (StringUtils.isEmpty(uri)) {
            FRContext.getLogger().info("The URL is empty!");
            return;
        }
        try {
            Desktop.getDesktop().browse(new URI(uri));
    
        } catch (IOException e) {
            startBrowserFromCommand(uri, e);
        } catch (URISyntaxException e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
            FineLoggerFactory.getLogger().error("Can not open the browser for URL:  " + uri);
        }
    }
    
    private static void startBrowserFromCommand(String uri, IOException e) {
        
        if (OperatingSystem.isWindows()) {
            try {
                // win10 内存用到到80%左右的时候, Desktop.browser经常提示"存储空间不足, 无法处理改命令", 用rundll32可以打开.
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + uri);
            } catch (IOException ee) {
                JOptionPane.showMessageDialog(null, Inter.getLocText("FR-Designer_Set_default_browser"));
                FineLoggerFactory.getLogger().error(e.getMessage(), e);
            }
        } else {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
    }
    
    private static class InformationPane extends BasicPane {
    
        private static final long serialVersionUID = 1L;
    
        private static final int FREE_STYLE_TOP = 15;
    
        private static final int FREE_STYLE_OTHER = 5;
    
        InformationPane(String message) {
            
            init(message);
        }
    
        private void init(String message) {
        
            this.setLayout(new BorderLayout(10, 10));
            this.setBorder(BorderFactory.createEmptyBorder(FREE_STYLE_TOP, FREE_STYLE_OTHER, FREE_STYLE_OTHER, FREE_STYLE_OTHER));
            String text;
            if (!ComparatorUtils.equals(message, Inter.getLocText(new String[]{"Default", "Utils-Report_Runtime_Env"}))) {
                text = Inter.getLocText("FR-Designer_Open") +
                    ProductConstants.APP_NAME +
                    Inter.getLocText("FR-Designer_Utils-OpenDemoEnv") +
                    message + Inter.getLocText("FR-Designer_Utils-switch");
            } else {
                text = Inter.getLocText("FR-Designer_Open") +
                    ProductConstants.APP_NAME +
                    Inter.getLocText("FR-Designer_Utils-NewDemoEnv") +
                    message + Inter.getLocText("FR-Designer_Utils-switch");
            }
            UITextArea a = new UITextArea(text);
            a.setFont(new Font("Dialog", Font.PLAIN, 12));
            a.setEditable(false);
            a.setBackground(this.getBackground());
            a.setLineWrap(true);
            this.add(a);
        }
    
        @Override
        protected String title4PopupWindow() {
        
            return Inter.getLocText("FR-Designer_Tooltips");
        }
    
    }
    
}