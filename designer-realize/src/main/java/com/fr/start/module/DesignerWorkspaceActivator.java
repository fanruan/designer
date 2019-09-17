package com.fr.start.module;

import com.fr.concurrent.NamedThreadFactory;
import com.fr.design.file.HistoryTemplateListCache;
import com.fr.event.Event;
import com.fr.event.Listener;
import com.fr.module.Activator;
import com.fr.start.server.FineEmbedServer;
import com.fr.workspace.Workspace;
import com.fr.workspace.WorkspaceEvent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by juhaoyu on 2019-06-14.
 */
public class DesignerWorkspaceActivator extends Activator {

    @Override
    public void start() {

        registerEnvListener();
    }

    /**
     * 注册切换环境前后事件监听
     */
    private void registerEnvListener() {

        /*切换环境前，关闭所有相关模块，最后执行*/
        listenEvent(WorkspaceEvent.BeforeSwitch, new Listener<Workspace>(Integer.MIN_VALUE) {

            @Override
            public void on(Event event, Workspace current) {

                stopSub(EnvBasedModule.class);
            }
        });
        /*切换环境后，重新启动所有相关模块，最先执行*/
        listenEvent(WorkspaceEvent.AfterSwitch, new Listener<Workspace>(Integer.MAX_VALUE) {

            @Override
            public void on(Event event, Workspace current) {

                startSub(EnvBasedModule.class);
                startServer(current);
            }
        });
        /*切换环境前，存储一下打开的所有文件对象，要先于 关闭相关模块部分 被触发*/
        listenEvent(WorkspaceEvent.BeforeSwitch, new Listener<Workspace>(Integer.MAX_VALUE) {

            @Override
            public void on(Event event, Workspace workspace) {

                HistoryTemplateListCache.getInstance().stash();
            }
        });

        /*切换环境后，装载一下打开的所有文件对象，优先级低于默认优先级，要后于 启动相关模块部分 被触发*/
        listenEvent(WorkspaceEvent.AfterSwitch, new Listener<Workspace>(Integer.MIN_VALUE) {

            @Override
            public void on(Event event, Workspace workspace) {

                HistoryTemplateListCache.getInstance().load();
            }
        });
    }

    private void startServer(Workspace current) {

        // 切换后的环境是本地环境才启动内置服务器
        if (current.isLocal()) {
            ExecutorService service = Executors.newSingleThreadExecutor(new NamedThreadFactory("DesignerWorkspaceActivator"));
            service.submit(new Runnable() {

                @Override
                public void run() {
                    FineEmbedServer.start();
                }
            });
            service.shutdown();
        }
    }

    @Override
    public void stop() {

    }
}
