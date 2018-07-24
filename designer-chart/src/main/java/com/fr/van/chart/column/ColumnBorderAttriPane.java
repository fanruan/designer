package com.fr.van.chart.column;

import com.fr.design.chart.comp.BorderAttriPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;


import java.awt.Dimension;

/**
 * Created by hufan on 2016/8/11.
 */
public class ColumnBorderAttriPane extends BorderAttriPane {
    private UISpinner radius;

    public ColumnBorderAttriPane() {
        this(com.fr.design.i18n.Toolkit.i18nText("plugin-ChartF_Radius"));
    }

    public ColumnBorderAttriPane(String radiusString) {
        this.add(new UILabel(radiusString + ":"));
        radius = new UISpinner(0,1000,1,0);
        this.add(radius);
        radius.setPreferredSize(new Dimension(60, 18));
    }

    public void setRadius(int value) {
        this.radius.setValue(value);
    }

    public double getRadius() {
        return this.radius.getValue();
    }

    @Override
    protected String title4PopupWindow() {
        return "Border";
    }
}
