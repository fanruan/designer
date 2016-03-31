package com.fr.design.fun.impl;

import com.fr.design.fun.DesignerEnvProcessor;

/**
 * Created by Administrator on 2016/3/31/0031.
 */
public abstract class AbstractDesignerEnvProcessor implements DesignerEnvProcessor {

    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

    public String changeEnvPathBeforeConnect(String userName, String password, String path){
        return path;
    }

}
