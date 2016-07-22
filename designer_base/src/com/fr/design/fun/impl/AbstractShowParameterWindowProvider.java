package com.fr.design.fun.impl;

import com.fr.design.fun.ShowParameterWindow;
import com.fr.stable.fun.mark.API;


/**
 * Created by zpc on 2016/7/21.
 */
@API(level = ShowParameterWindow.CURRENT_LEVEL)
public abstract class AbstractShowParameterWindowProvider implements ShowParameterWindow {

    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

    public int layerIndex() {
        return DEFAULT_LAYER_INDEX;
    }



    /**
     * 生成属性表
     */
    @Override
    public void add() {

    }
}