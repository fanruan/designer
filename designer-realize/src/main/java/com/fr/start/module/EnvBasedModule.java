package com.fr.start.module;

import com.fr.module.Activator;
import com.fr.start.server.FineEmbedServer;

/**
 * Created by juhaoyu on 2018/6/6.
 * 基于env的模块启动关闭
 */
public class EnvBasedModule extends Activator {
    
    @Override
    public void start() {
    }
    
    @Override
    public void stop() {
        //先关闭tomcat(如果已经启动了的话)
        FineEmbedServer.stop();
    }
}
