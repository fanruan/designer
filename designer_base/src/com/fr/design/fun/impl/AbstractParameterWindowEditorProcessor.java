package com.fr.design.fun.impl;

import com.fr.design.designer.creator.CRPropertyDescriptor;
import com.fr.design.fun.ParameterWindowEditorProcessor;
import com.fr.stable.fun.mark.API;


/**
 * Created by zpc on 2016/7/21.
 */
@API(level = ParameterWindowEditorProcessor.CURRENT_LEVEL)
public abstract class AbstractParameterWindowEditorProcessor implements ParameterWindowEditorProcessor {

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
    public CRPropertyDescriptor[] createPropertyDescriptor(Class<?> temp) {
        return new CRPropertyDescriptor[0];
    }
}