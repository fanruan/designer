package com.fr.design.widget.ui.designer.component;

import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XWAbsoluteLayout;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.WidgetPropertyPane;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.design.widget.WidgetBoundsPaneFactory;
import com.fr.form.ui.container.WLayout;


import java.awt.Rectangle;


/**
 * Created by ibm on 2017/8/3.
 */
public class WidgetAbsoluteBoundPane extends WidgetBoundPane {
    private UISpinner x;
    private UISpinner y;

    public WidgetAbsoluteBoundPane(XCreator source){
        super(source);
    }

    @Override
    public void initBoundPane() {
        x = new UIBoundSpinner(0, Integer.MAX_VALUE, 1, 0d);
        y = new UIBoundSpinner(0, Integer.MAX_VALUE, 1, 0d);
        width = new UIBoundSpinner(0, Integer.MAX_VALUE, 1, 0d);
        height = new UIBoundSpinner(0, Integer.MAX_VALUE, 1, 0d);
        x.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Coords_And_Size"));
        y.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Coords_And_Size"));
        width.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Coords_And_Size"));
        height.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Coords_And_Size"));
        this.add(WidgetBoundsPaneFactory.createAbsoluteBoundsPane(x, y, width, height));
    }

    @Override
    public void update() {
        FormDesigner formDesigner = WidgetPropertyPane.getInstance().getEditingFormDesigner();
        formDesigner.getSelectionModel().getSelection().backupBounds();
        super.update();
        Rectangle bounds = new Rectangle(creator.getBounds());
        bounds.x = (int) x.getValue();
        bounds.y = (int) y.getValue();
        if (parent == null) {
            return;
        }
        WLayout wabs = parent.toData();
        wabs.setBounds(creator.toData(), bounds);
        creator.setBounds(bounds);
        LayoutUtils.layoutContainer(creator);
        XWAbsoluteLayout layout = (XWAbsoluteLayout) parent;
        layout.updateBoundsWidget(creator);
    }

    @Override
    public void limitWidth(WLayout wabs, int w, Rectangle bounds, Rectangle rec){
        bounds.width = w;
        creator.setBounds(bounds);
    }

    @Override
    public void limitHeight(WLayout wabs, int h, Rectangle bounds, Rectangle rec){
        bounds.height = h;
        creator.setBounds(bounds);
    }
    @Override
    protected String title4PopupWindow() {
        return "absoluteBound";
    }

    @Override
    public void populate() {
        super.populate();
        Rectangle bounds = new Rectangle(creator.getBounds());
        x.setValue(bounds.x);
        y.setValue(bounds.y);
    }
}
