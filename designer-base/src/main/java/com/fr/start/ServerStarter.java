package com.fr.start;

import com.fr.base.ServerConfig;
import com.fr.concurrent.NamedThreadFactory;
import com.fr.design.DesignerEnvManager;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.utils.BrowseUtils;
import com.fr.general.GeneralContext;
import com.fr.start.server.FineEmbedServer;
import com.fr.start.server.FineEmbedServerMonitor;
import com.fr.workspace.WorkContext;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerStarter {

    /**
     * 预览Demo
     * 找默认工作目录，不应该按照名字去找，而应该按照安装路径，因为默认工作目录的名字可能会改变。
     */
    public static void browserDemoURL() {

        if (!WorkContext.getCurrent().isLocal()) {
            //有问题，这里拿不到远程的http端口
            BrowseUtils.browser(WorkContext.getCurrent().getPath());
        } else {
            browserURLWithLocalEnv("http://localhost:"
                    + DesignerEnvManager.getEnvManager().getEmbedServerPort()
                    + "/" + GeneralContext.getCurrentAppNameOfEnv()
                    + "/" + ServerConfig.getInstance().getServletName());
        }
    }

    /**
     * 本地环境浏览url
     *
     * @param url 指定路径
     */
    public static void browserURLWithLocalEnv(final String url) {
        // 内置服务器没有启动并且设计器已经打开，可以使用带进度条的启动方式
        if (!FineEmbedServer.isRunning() && DesignerContext.getDesignerFrame().isDesignerOpened()) {
            FineEmbedServerMonitor.getInstance().monitor();
            ExecutorService service = Executors.newSingleThreadExecutor(new NamedThreadFactory("ServerStarter"));
            service.submit(new Runnable() {

                @Override
                public void run() {
                    try {
                        FineEmbedServer.start();
                    } finally {
                        FineEmbedServerMonitor.getInstance().setComplete();
                    }
                    BrowseUtils.browser(url);
                }
            });
            service.shutdown();
        } else if (!FineEmbedServer.isRunning()) {
            // 普通方式启动内置服务器
            try {
                FineEmbedServer.start();
            } finally {
                BrowseUtils.browser(url);
            }
        } else {
            // 已经启动内置服务器只需打开链接
            BrowseUtils.browser(url);
        }
    }
}
