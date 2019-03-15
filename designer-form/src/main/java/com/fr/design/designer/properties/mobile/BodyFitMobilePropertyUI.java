package com.fr.design.designer.properties.mobile;

import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.dialog.BasicPane;
import com.fr.design.widget.ui.designer.mobile.BodyFitMobileDefinePane;


/**
 * 自适应对应body对应的移动端属性
 */
public class BodyFitMobilePropertyUI extends BodyMobilePropertyUI {

    public BodyFitMobilePropertyUI(XLayoutContainer xwFitLayout) {
        super(xwFitLayout);
    }

    @Override
    public BasicPane createWidgetAttrPane() {
        return new BodyFitMobileDefinePane(getxCreator());
    }

}
