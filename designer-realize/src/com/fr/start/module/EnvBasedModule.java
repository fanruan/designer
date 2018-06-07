package com.fr.start.module;

import com.fr.module.Activator;
import com.fr.stable.CoreActivator;
import com.fr.start.server.FineEmbedServerActivator;

/**
 * Created by juhaoyu on 2018/6/6.
 * 基于env的模块启动关闭
 */
public class EnvBasedModule extends Activator {
    
    @Override
    public void start() {
        //core和设计器启动
        startSub(CoreActivator.class);
        getSub("designer").start();
        //这里不启动tomcat，由客户手动触发
    }
    
    @Override
    public void stop() {
        //先关闭tomcat(如果已经启动了的话)
        stopSub(FineEmbedServerActivator.class);
        //倒叙关闭其他模块
        getSub("designer").stop();
        stopSub(CoreActivator.class);
    }
}
