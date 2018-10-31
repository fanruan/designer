package com.fr.design.fun;

import com.fr.form.FormProvider;
import com.fr.form.ui.ElementCaseEditorProvider;
import com.fr.stable.fun.mark.Mutable;

import java.beans.PropertyDescriptor;

/**
 * Created by zhouping on 2015/9/10.
 */
public interface FormElementCaseEditorProvider extends Mutable {

    String MARK_STRING = "PropertyEditor";

    int CURRENT_LEVEL = 1;

    PropertyDescriptor[] createPropertyDescriptor(Class<?> temp, FormProvider formProvider, ElementCaseEditorProvider editor);

}