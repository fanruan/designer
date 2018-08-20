package com.fr.start;

import com.fr.base.FRContext;
import com.fr.base.ServerConfig;
import com.fr.design.DesignerEnvManager;
import com.fr.general.ComparatorUtils;
import com.fr.general.GeneralContext;

import com.fr.log.FineLoggerFactory;
import com.fr.stable.OperatingSystem;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;
import com.fr.start.server.FineEmbedServer;
import com.fr.workspace.WorkContext;


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
    
        if (!WorkContext.getCurrent().isLocal()) {
            //有问题，这里拿不到远程的http端口
            browser(WorkContext.getCurrent().getPath());
        }
        else if (ComparatorUtils.equals(StableUtils.getInstallHome(), ".")) {//august:供代码使用
            String web = GeneralContext.getCurrentAppNameOfEnv();
            browserURLWithLocalEnv("http://localhost:" + DesignerEnvManager.getEnvManager().getEmbedServerPort() + "/" + web + "/" + ServerConfig.getInstance().getServletName());
        }else{
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
                JOptionPane.showMessageDialog(null, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Set_Default_Browser_Duplicate"));
                FineLoggerFactory.getLogger().error(e.getMessage(), e);
            }
        } else {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
    }


}
