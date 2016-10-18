package com.fr.design.fun.impl;

import com.fr.design.fun.FormElementRefreshCaseEditorProcessor;
import com.fr.stable.fun.RefreshProvider;
import com.fr.stable.fun.ReportRefreshAttrProvider;
import com.fr.stable.fun.mark.API;

import java.beans.PropertyDescriptor;

/**
 * Created by Slpire on 16/10/12.
 */
@API(level = FormElementRefreshCaseEditorProcessor.CURRENT_LEVEL)
public abstract class AbstractFormElementRefreshCaseEditorProcessor implements FormElementRefreshCaseEditorProcessor {

    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

    public int layerIndex() {
        return DEFAULT_LAYER_INDEX;
    }

    @Override
    public PropertyDescriptor[] createPropertyDescriptor(Class<?> temp, ReportRefreshAttrProvider refreshAttrProvider) {
        return new PropertyDescriptor[0];
    }
}
