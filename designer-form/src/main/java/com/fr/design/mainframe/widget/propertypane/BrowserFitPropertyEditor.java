package com.fr.design.mainframe.widget.propertypane;

import com.fr.design.designer.creator.CRPropertyDescriptor;
import com.fr.design.form.util.XCreatorConstants;
import com.fr.main.ReportFitConfig;
import com.fr.report.fun.ReportFitAttrProvider;

import java.beans.IntrospectionException;

/**
 * Created by zhouping on 2015/9/10.
 */
public class BrowserFitPropertyEditor {

    /**
     * 生成属性表
     *
     * @param temp          传入当前操作的class
     * @param reportFitAttr 传入的自适应属性
     * @return 返回属性表
     */
    public CRPropertyDescriptor createPropertyDescriptor(Class<?> temp, ReportFitAttrProvider reportFitAttr) {
        if (getFitStateInPC(reportFitAttr) == 0) {
            return null;
        }
        try {
            CRPropertyDescriptor propertyDescriptors = new CRPropertyDescriptor("fitStateInPC", temp).setEditorClass(BrowserFitEditor.class)
                    .setRendererClass(BrowserFitRender.class).setI18NName(com.fr.design.i18n.Toolkit.i18nText("Fine-Designer_Fit-In-Web"))
                    .putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Advanced");
            return propertyDescriptors;
        } catch (IntrospectionException e) {
            return null;
        }
    }

    public int getFitStateInPC(ReportFitAttrProvider fitAttrProvider) {
        if (fitAttrProvider != null) {
            return fitAttrProvider.fitStateInPC();
        }
        return ReportFitConfig.getInstance().getFrmFitAttr().fitStateInPC();

    }


}