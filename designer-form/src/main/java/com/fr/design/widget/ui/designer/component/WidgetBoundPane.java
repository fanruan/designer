package com.fr.design.widget.ui.designer.component;

import com.fr.design.designer.beans.AdapterBus;
import com.fr.design.designer.beans.adapters.layout.FRFitLayoutAdapter;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XCreatorUtils;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWFitLayout;
import com.fr.design.designer.creator.XWParameterLayout;
import com.fr.design.designer.creator.cardlayout.XWCardLayout;
import com.fr.design.designer.creator.cardlayout.XWCardTagLayout;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.WidgetPropertyPane;
import com.fr.design.utils.ComponentUtils;
import com.fr.design.widget.WidgetBoundsPaneFactory;
import com.fr.form.ui.PaddingMargin;
import com.fr.form.ui.container.WFitLayout;
import com.fr.form.ui.container.WLayout;


import javax.swing.JOptionPane;
import java.awt.Rectangle;


/**
 * Created by ibm on 2017/7/30.
 */

public class WidgetBoundPane extends BasicPane {
    private static final int MINHEIGHT = WLayout.MIN_HEIGHT;
    private static final int MINWIDTH = WLayout.MIN_WIDTH;
    protected XLayoutContainer parent;
    protected XCreator creator;
    protected UISpinner width;
    protected UISpinner height;

    public WidgetBoundPane(XCreator source) {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.creator = source;
        this.parent = getParent(source);
        initBoundPane();
    }

    public XLayoutContainer getParent(XCreator source) {
        if(source.acceptType(XWCardTagLayout.class)){
            return (XLayoutContainer)source.getParent();
        }
        XLayoutContainer container = XCreatorUtils.getParentXLayoutContainer(source);
        if (source.acceptType(XWFitLayout.class) || source.acceptType(XWParameterLayout.class)) {
            container = null;
        }
        return container;
    }

    public void initBoundPane() {
        width = new UIBoundSpinner(0, Integer.MAX_VALUE, 1, 0d);
        height = new UIBoundSpinner(0, Integer.MAX_VALUE, 1, 0d);
        width.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Coords_And_Size"));
        height.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Coords_And_Size"));
        if (creator.acceptType(XWCardLayout.class)) {
            width.setEnabled(false);
            height.setEnabled(false);
        }
        this.add(WidgetBoundsPaneFactory.createBoundsPane(width, height));
    }


    public void update() {
        fix();
    }

    @Override
    protected String title4PopupWindow() {
        return "widgetBound";
    }

    public void populate() {
        Rectangle bounds = new Rectangle(creator.getBounds());
        width.setValue(bounds.width);
        height.setValue(bounds.height);
    }

    public void fix() {
        Rectangle bounds = new Rectangle(creator.getBounds());
        creator.setBackupBound(creator.getBounds());
        int w = (int) width.getValue();
        int h = (int) height.getValue();
        Rectangle rec = ComponentUtils.getRelativeBounds(parent);
        WLayout wabs = parent.toData();
        if (bounds.width != w) {
            limitWidth(wabs, w, bounds, rec);
        }
        if (bounds.height != h) {
            limitHeight(wabs, h, bounds, rec);
        }
    }


    public void adjustComponents(Rectangle bounds, int difference, int row) {
        FormDesigner formDesigner = WidgetPropertyPane.getInstance().getEditingFormDesigner();
        Rectangle backupBounds = getBound();
        FRFitLayoutAdapter layoutAdapter = (FRFitLayoutAdapter) AdapterBus.searchLayoutAdapter(formDesigner, creator);
        if (layoutAdapter != null) {
            layoutAdapter.setEdit(true);
            layoutAdapter.calculateBounds(backupBounds, bounds, creator, row, difference);
        }
    }

    public void limitWidth(WLayout wabs, int w, Rectangle bounds, Rectangle rec) {
        int difference = 0;
        int minWidth = (int) (MINWIDTH * ((WFitLayout) wabs).getResolutionScaling());
        PaddingMargin margin = wabs.getMargin();
        if (bounds.width != w) {
            if (bounds.width == rec.width - margin.getLeft() - margin.getRight()) {
                JOptionPane.showMessageDialog(null, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Beyond_Bounds"));
                width.setValue(bounds.width);
                return;
            } else if (w < minWidth) {
                JOptionPane.showMessageDialog(null, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Min_Width") + Integer.toString(minWidth));
                width.setValue(bounds.width);
                return;
            }
            difference = bounds.width - w;
            bounds.width = w;
        }
        wabs.setBounds(creator.toData(), bounds);
        adjustComponents(bounds, difference, 0);
    }

    public void limitHeight(WLayout wabs, int h, Rectangle bounds, Rectangle rec) {
        int difference = 0;
        PaddingMargin margin = wabs.getMargin();
        int minHeight = (int) (MINHEIGHT * ((WFitLayout) wabs).getResolutionScaling());
        if (bounds.height != h) {
            if (bounds.height == rec.height - margin.getTop() - margin.getBottom()) {
                JOptionPane.showMessageDialog(null, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Beyond_Bounds"));
                height.setValue(bounds.height);
                return;
            } else if (h < minHeight) {
                JOptionPane.showMessageDialog(null, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Min_Height") + Integer.toString(minHeight));
                height.setValue(bounds.height);
                return;
            }
            difference = bounds.height - h;
            bounds.height = h;
        }
        wabs.setBounds(creator.toData(), bounds);
        adjustComponents(bounds, difference, 1);
    }


    public Rectangle getBound() {
        Rectangle bounds = new Rectangle(creator.getBounds());
        if (parent == null) {
            return bounds;
        }
        Rectangle rec = ComponentUtils.getRelativeBounds(parent);
        bounds.x += rec.x;
        bounds.y += rec.y;
        return bounds;

    }

}
