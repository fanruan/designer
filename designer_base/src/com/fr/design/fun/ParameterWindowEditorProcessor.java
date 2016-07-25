package com.fr.design.fun;

import com.fr.stable.fun.mark.Immutable;
import java.beans.PropertyDescriptor;


/**
 * Created by zpc on 16/7/21.
 */
public interface ParameterWindowEditorProcessor extends Immutable {

    String MARK_STRING = "ShowParameter";

    int CURRENT_LEVEL = 1;

    /**
     * 生成属性表
     */
    PropertyDescriptor[] createPropertyDescriptor(Class<?> temp);
}

