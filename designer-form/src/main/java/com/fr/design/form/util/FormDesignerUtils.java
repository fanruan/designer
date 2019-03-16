package com.fr.design.form.util;

import com.fr.design.mainframe.FormDesigner;
import com.fr.form.ui.container.WFitLayout;

public class FormDesignerUtils {
    /**
     * body布局是否设置了手机重布局
     *
     * @param designer
     * @return
     */
    public static boolean isAppRelayout(FormDesigner designer) {
        return ((WFitLayout) designer.getRootComponent().toData()).isAppRelayout();
    }

    /**
     * body布局是否设置了绝对布局
     *
     * @param designer
     * @return
     */
    public static boolean isBodyAbsolute(FormDesigner designer) {
        WFitLayout root = ((WFitLayout) designer.getRootComponent().toData());
        return root.getBodyLayoutType() == com.fr.form.ui.container.WBodyLayoutType.ABSOLUTE;
    }

}