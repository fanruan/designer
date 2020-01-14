package com.fr.design.mainframe.mobile.ui;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.fun.impl.AbstractMobileBookMarkStyleProvider;
import com.fr.form.ui.mobile.impl.DefaultMobileBookMarkStyle;
import com.fr.form.ui.mobile.MobileBookMarkStyle;

/**
 * @author hades
 * @version 10.0
 * Created by hades on 2019/12/24
 */
public class DefaultMobileBookMarkStyleProvider extends AbstractMobileBookMarkStyleProvider {

    @Override
    public Class<? extends MobileBookMarkStyle> classForMobileBookMarkStyle() {
        return DefaultMobileBookMarkStyle.class;
    }

    @Override
    public Class<? extends BasicBeanPane<MobileBookMarkStyle>> classForMobileBookMarkStyleAppearance() {
        return DefaultMobileBookMarkStyleCustomDefinePane.class;
    }

    @Override
    public String displayName() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Mobile_None_BookMark_Style");
    }
}
