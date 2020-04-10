package com.fr.van.chart.range.component;

import com.fr.design.gui.frpane.AbstractAttrNoScrollPane;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.mainframe.chart.gui.ColorSelectBoxWithOutTransparent;
import com.fr.design.mainframe.chart.gui.style.series.MapColorPickerPaneWithFormula;
import com.fr.design.style.color.ColorSelectBox;

import java.awt.Component;

/**
 * Created by IntelliJ IDEA.
 * Author : hu
 * Version: 7.1.1
 */
public class SectionIntervalConfigPane extends MapColorPickerPaneWithFormula {

    public SectionIntervalConfigPane(AbstractAttrNoScrollPane container) {
        super(container);
    }

    protected double[] getRowSIze () {
        double p = TableLayout.PREFERRED;
        return new double[]{p, p, p, p};
    }

    @Override
    protected Component[][] createComponents(){
        return new Component[][]{
                new Component[]{null,null},
                new Component[]{new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Subject_Color")), getFillStyleCombox()},
                new Component[]{new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Value_Divided_Stage")), getRegionNumPane()},
                new Component[]{new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Range_Num")),getDesignTypeButtonGroup()},

        };
    }

    @Override
    protected ColorSelectBox getColorSelectBox() {
        return new ColorSelectBoxWithOutTransparent(100);
    }
}