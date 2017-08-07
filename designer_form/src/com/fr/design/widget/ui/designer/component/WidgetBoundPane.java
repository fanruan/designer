package com.fr.design.widget.ui.designer.component;

import com.fr.design.designer.creator.*;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.widget.WidgetBoundsPaneFactory;

import java.awt.*;

/**
 * Created by ibm on 2017/7/30.
 */

public class WidgetBoundPane extends BasicPane {
    protected XCreator creator;
    protected UISpinner width;
    protected UISpinner height;

    public WidgetBoundPane(XCreator source) {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.creator = source;
        initBoundPane();
    }

    public XLayoutContainer getParent(XCreator source) {
        XLayoutContainer container = XCreatorUtils.getParentXLayoutContainer(source);
        if (source.acceptType(XWFitLayout.class) || source.acceptType(XWParameterLayout.class)) {
            container = null;
        }
        return container;
    }

    public void initBoundPane() {
        width = new UISpinner(0, 1200, 1);
        height = new UISpinner(0, 1200, 1);
        this.add(WidgetBoundsPaneFactory.createBoundsPane(width, height));
    }


    public void update() {
        Rectangle bounds = new Rectangle(creator.getBounds());
        bounds.width = (int) width.getValue();
        bounds.height = (int) height.getValue();
        creator.setBounds(bounds);
    }

    protected String title4PopupWindow() {
        return "";
    }

    public void populate() {
        Rectangle bounds = new Rectangle(creator.getBounds());
        width.setValue(bounds.width);
        height.setValue(bounds.height);
    }
}
