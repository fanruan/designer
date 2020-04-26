package com.fr.design.mainframe.mobile.ui;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.fun.impl.AbstractMobileBookMarkStyleProvider;
import com.fr.design.i18n.Toolkit;
import com.fr.form.ui.mobile.MobileBookMarkStyle;
import com.fr.form.ui.mobile.impl.SidebarMobileBookMarkStyle;

/**
 * @author Starryi
 * @version 10.0
 * Created by Starryi on 2020/02/28
 */
public class SidebarMobileBookMarkStyleProvider extends AbstractMobileBookMarkStyleProvider {

    @Override
    public Class<? extends MobileBookMarkStyle> classForMobileBookMarkStyle() {
        return SidebarMobileBookMarkStyle.class;
    }

    @Override
    public Class<? extends BasicBeanPane<MobileBookMarkStyle>> classForMobileBookMarkStyleAppearance() {
        return SidebarMobileBookMarkStyleCustomDefinePane.class;
    }

    @Override
    public String displayName() {
        return Toolkit.i18nText("Fine-Design_Mobile_BookMark_Style_Sidebar");
    }
}
