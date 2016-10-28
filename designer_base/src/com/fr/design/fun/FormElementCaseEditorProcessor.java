package com.fr.design.fun;

import com.fr.stable.fun.ReportFitAttrProvider;
import com.fr.stable.fun.mark.Immutable;

import java.beans.PropertyDescriptor;

/**
 * Created by Slpire on 2016/10/28.
 */
public interface FormElementCaseEditorProcessor extends Immutable {
    String MARK_STRING = "PropertyEditor";

    int CURRENT_LEVEL = 1;


    /**
     * 生成属性表
     * @param temp 传入当前操作的class
     * @param reportFitAttr 传入的自适应属性
     * @return 返回属性表
     */
    PropertyDescriptor[] createPropertyDescriptor(Class<?> temp, ReportFitAttrProvider reportFitAttr);

    /**
     * 返回pc自适应属性值
     * @param fitAttrProvider 传入的自适应属性
     * @return 返回pc自适应属性值
     */
    int getFitStateInPC(ReportFitAttrProvider fitAttrProvider);
}
