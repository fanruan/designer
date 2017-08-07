package com.fr.design.widget.ui.designer.component;

import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWAbsoluteLayout;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.widget.WidgetBoundsPaneFactory;
import com.fr.form.ui.container.WAbsoluteLayout;

import java.awt.*;

/**
 * Created by ibm on 2017/8/3.
 */
public class WidgetAbsoluteBoundPane extends WidgetBoundPane {
    protected XWAbsoluteLayout parent;
    private UISpinner x;
    private UISpinner y;

    public WidgetAbsoluteBoundPane(XCreator source){
        super(source);
        XLayoutContainer xLayoutContainer = getParent(source);
        this.parent = (XWAbsoluteLayout) xLayoutContainer;
    }

    public void initBoundPane() {
        x = new UISpinner(0, 1200, 1);
        y = new UISpinner(0, 1200, 1);
        width = new UISpinner(0, 1200, 1);
        height = new UISpinner(0, 1200, 1);
        this.add(WidgetBoundsPaneFactory.createAbsoluteBoundsPane(x, y, width, height));
    }


    public void update() {
        super.update();
        Rectangle bounds = new Rectangle(creator.getBounds());
        bounds.x = (int) x.getValue();
        bounds.y = (int) y.getValue();
        if (parent == null) {
            return;
        }
        WAbsoluteLayout wabs = parent.toData();
        wabs.setBounds(creator.toData(), bounds);
        creator.setBounds(bounds);
    }

    protected String title4PopupWindow() {
        return "";
    }

    public void populate() {
        super.populate();
        Rectangle bounds = new Rectangle(creator.getBounds());
        x.setValue(bounds.x);
        y.setValue(bounds.y);
    }
}
