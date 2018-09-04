package com.fr.start;

import com.fr.base.ServerConfig;
import com.fr.design.DesignerEnvManager;
import com.fr.design.utils.BrowseUtils;
import com.fr.general.ComparatorUtils;
import com.fr.general.GeneralContext;
import com.fr.stable.StableUtils;
import com.fr.start.server.FineEmbedServer;
import com.fr.workspace.WorkContext;


public class ServerStarter {
  
    /**
     * 预览Demo
     * 找默认工作目录，不应该按照名字去找，而应该按照安装路径，因为默认工作目录的名字可能会改变。
     */
    public static void browserDemoURL() {
    
        if (!WorkContext.getCurrent().isLocal()) {
            //有问题，这里拿不到远程的http端口
            BrowseUtils.browser(WorkContext.getCurrent().getPath());
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
            BrowseUtils.browser("http://localhost:" + DesignerEnvManager.getEnvManager().getEmbedServerPort() + "/" + GeneralContext.getCurrentAppNameOfEnv() + "/" + ServerConfig.getInstance().getServletName());
        }
    }
    
    /**
     * 本地环境浏览url
     *
     * @param url 指定路径
     */
    public static void browserURLWithLocalEnv(String url) {
    
        FineEmbedServer.start();
        BrowseUtils.browser(url);
    }
}
