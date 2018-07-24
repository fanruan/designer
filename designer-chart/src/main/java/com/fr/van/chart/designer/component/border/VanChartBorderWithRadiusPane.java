package com.fr.van.chart.designer.component.border;

import com.fr.chart.base.AttrBorder;
import com.fr.chart.chartglyph.GeneralInfo;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;


import javax.swing.JSeparator;
import java.awt.BorderLayout;
import java.awt.Component;

/**
 * 线型 + 颜色 + 圆角半径
 */
public class VanChartBorderWithRadiusPane extends VanChartBorderPane {
    private static final long serialVersionUID = -3937853702118283803L;
    private UISpinner radius;

    @Override
    protected void initComponents() {
        radius = new UISpinner(0,1000,1,0);
        this.add(new JSeparator(), BorderLayout.SOUTH);
        super.initComponents();
    }

    @Override
    protected Component[][] getUseComponent() {
        return new Component[][]{
                new Component[]{null,null},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Plugin-ChartF_LineStyle")),currentLineCombo},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("FR-Chart-Color_Color")),currentLineColorPane},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("plugin-ChartF_Radius")),radius}
        } ;
    }

    public void populate(GeneralInfo attr) {
        super.populate(attr);
        if(attr == null) {
            return;
        }
        radius.setValue(attr.getRoundRadius());
    }

    public void update(GeneralInfo attr) {
        super.update(attr);
        attr.setRoundRadius((int)radius.getValue());
    }

    public void update(AttrBorder attrBorder) {
        super.update(attrBorder);
        attrBorder.setRoundRadius((int)radius.getValue());
    }

    public void populate(AttrBorder attr) {
        super.populate(attr);
        if(attr == null) {
            return;
        }
        radius.setValue(attr.getRoundRadius());
    }

    public AttrBorder update() {
        AttrBorder attr = super.update();
        attr.setRoundRadius((int)radius.getValue());
        return attr;
    }
}