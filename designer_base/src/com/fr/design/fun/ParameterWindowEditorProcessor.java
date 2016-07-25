package com.fr.design.fun;

import com.fr.design.designer.creator.CRPropertyDescriptor;
import com.fr.stable.fun.mark.Immutable;

/**
 * Created by zpc on 16/7/21.
 */
public interface ParameterWindowEditorProcessor extends Immutable {

    String MARK_STRING = "ParameterWindowEditorProcessor";

    int CURRENT_LEVEL = 1;

    /**
     * 生成属性表
     */
    CRPropertyDescriptor[] createPropertyDescriptor(Class<?> temp);
}

