package com.fr.plugin.chart.designer.component;

import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;
import com.fr.plugin.chart.designer.TableLayout4VanChartHelper;

import javax.swing.*;
import java.awt.*;

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
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_LineStyle")), lineWidth},
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_Null_Value_Break")), nullValueBreak},
        };

        return TableLayoutHelper.createTableLayoutPane(components, row, col);
    }
}