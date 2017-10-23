package com.fr.design.widget.ui.designer.component;

import com.fr.design.designer.creator.XCreator;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.widget.WidgetBoundsPaneFactory;
import com.fr.form.ui.container.WLayout;
import com.fr.general.Inter;

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

    public void initBoundPane() {
        x = new UISpinner(0, Integer.MAX_VALUE, 1);
        y = new UISpinner(0, Integer.MAX_VALUE, 1);
        width = new UISpinner(0, Integer.MAX_VALUE, 1);
        height = new UISpinner(0, Integer.MAX_VALUE, 1);
        x.setGlobalName(Inter.getLocText("FR-Designer_Coords_And_Size"));
        y.setGlobalName(Inter.getLocText("FR-Designer_Coords_And_Size"));
        width.setGlobalName(Inter.getLocText("FR-Designer_Coords_And_Size"));
        height.setGlobalName(Inter.getLocText("FR-Designer_Coords_And_Size"));
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
        WLayout wabs = parent.toData();
        wabs.setBounds(creator.toData(), bounds);
        creator.setBounds(bounds);
    }


    public void limitWidth(WLayout wabs, int w, Rectangle bounds, Rectangle rec){
        bounds.width = w;
    }

    public void limitHeight(WLayout wabs, int h, Rectangle bounds, Rectangle rec){
        bounds.height = h;
    }


    protected String title4PopupWindow() {
        return "absoluteBound";
    }

    public void populate() {
        super.populate();
        Rectangle bounds = new Rectangle(creator.getBounds());
        x.setValue(bounds.x);
        y.setValue(bounds.y);
    }
}
