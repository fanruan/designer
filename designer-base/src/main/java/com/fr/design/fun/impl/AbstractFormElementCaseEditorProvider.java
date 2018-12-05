package com.fr.design.fun.impl;

import com.fr.design.fun.FormElementCaseEditorProvider;
import com.fr.form.FormProvider;
import com.fr.form.ui.ElementCaseEditorProvider;
import com.fr.stable.fun.mark.API;

import java.beans.PropertyDescriptor;

/**
 * Created by zhouping on 2015/9/10.
 */
@API(level = FormElementCaseEditorProvider.CURRENT_LEVEL)
public abstract class AbstractFormElementCaseEditorProvider implements FormElementCaseEditorProvider {

    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

    @Override
    public String mark4Provider() {
        return this.getClass().getName();
    }

    @Override
    public PropertyDescriptor[] createPropertyDescriptor(Class<?> temp, FormProvider reportAttr, ElementCaseEditorProvider editor) {
        return new PropertyDescriptor[0];
    }

}