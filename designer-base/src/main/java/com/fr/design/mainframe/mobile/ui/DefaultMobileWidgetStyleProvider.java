package com.fr.design.mainframe.mobile.ui;

import com.fr.design.fun.impl.AbstractMobileWidgetStyleProvider;
import com.fr.form.ui.mobile.DefaultMobileStyle;
import com.fr.form.ui.mobile.MobileStyle;
import com.fr.locale.InterProviderFactory;

public class DefaultMobileWidgetStyleProvider extends AbstractMobileWidgetStyleProvider {

    @Override
    public Class<? extends MobileStyle> classForMobileStyle() {
        return DefaultMobileStyle.class;
    }

    @Override
    public Class<? extends MobileStyleCustomDefinePane> classForWidgetAppearance() {
        return DefaultMobileStyleCustomDefinePane.class;
    }

    @Override
    public String xTypeForWidget() {
        return null;
    }

    @Override
    public String displayName() {
        return InterProviderFactory.getProvider().getLocText("Fine-Engine_Report_DEFAULT");
    }

}
