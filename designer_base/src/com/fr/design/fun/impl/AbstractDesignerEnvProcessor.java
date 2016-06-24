package com.fr.design.fun.impl;

import com.fr.design.fun.DesignerEnvProcessor;
import com.fr.stable.fun.mark.API;

/**
 * Created by Administrator on 2016/3/31/0031.
 */
@API(level = DesignerEnvProcessor.CURRENT_LEVEL)
public abstract class AbstractDesignerEnvProcessor implements DesignerEnvProcessor {

    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

    public int layerIndex() {
        return DEFAULT_LAYER_INDEX;
    }

    public String changeEnvPathBeforeConnect(String userName, String password, String path){
        return path;
    }

    public String getWebBrowserURL(String envPath){
        return envPath;
    }

}
