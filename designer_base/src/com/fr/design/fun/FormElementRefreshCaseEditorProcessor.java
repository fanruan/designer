package com.fr.design.fun;

import com.fr.stable.fun.ReportRefreshAttrProvider;
import com.fr.stable.fun.mark.Immutable;

import java.beans.PropertyDescriptor;

/**
 * Created by Slpire on 16/10/12.
 */
public interface FormElementRefreshCaseEditorProcessor extends Immutable {

    String MARK_STRING = "PropertyEditor";

    int CURRENT_LEVEL = 1;

    PropertyDescriptor[] createPropertyDescriptor(Class<?> temp, ReportRefreshAttrProvider reportefreshAttr);
}
