package com.fr.design.fun;

import com.fr.design.mainframe.mobile.ui.MobileStyleCustomDefinePane;
import com.fr.form.ui.mobile.MobileStyle;
import com.fr.stable.fun.mark.Mutable;

/**
 * 移动端组件样式扩展接口
 */
public interface MobileWidgetStyleProvider extends Mutable {

    String XML_TAG = "MobileWidgetStyleProvider";

    int CURRENT_LEVEL = 1;

    Class<? extends MobileStyle> classForMobileStyle();

    Class<? extends MobileStyleCustomDefinePane> classForWidgetAppearance();

    String xTypeForWidget();

    String displayName();

}
