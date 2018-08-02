package com.fr.van.chart.designer.component;

import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayoutHelper;

import com.fr.van.chart.designer.TableLayout4VanChartHelper;

import javax.swing.JPanel;
import java.awt.Component;

/**
 * 线-线型
 */
public class VanChartLineWidthPane extends VanChartLineTypePane {
    private static final long serialVersionUID = 4537158946119294689L;

    protected JPanel createContentPane(double p, double f) {
        double[] row = {p, p, p};
        double e = TableLayout4VanChartHelper.EDIT_AREA_WIDTH;
        double[] col = {f, e};

        Component[][] components = new Component[][]{
                new Component[]{null,null},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Line_Style")), lineWidth},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Null_Value_Break")), nullValueBreak},
        };

        return TableLayoutHelper.createTableLayoutPane(components, row, col);
    }
}