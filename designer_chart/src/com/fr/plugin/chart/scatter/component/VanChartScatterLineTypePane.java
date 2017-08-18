package com.fr.plugin.chart.scatter.component;

import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;
import com.fr.plugin.chart.base.VanChartAttrLine;
import com.fr.plugin.chart.designer.component.VanChartLineTypePane;
import com.fr.plugin.chart.type.LineStyle;
import com.fr.stable.Constants;

import javax.swing.*;
import java.awt.*;

/**
 * line相关设置
 */
public class VanChartScatterLineTypePane extends VanChartLineTypePane {

    @Override
    protected void createLineStyle() {
        String[] textArray = new String[]{Inter.getLocText("Plugin-ChartF_NormalLine"),
                                            Inter.getLocText("Plugin-ChartF_CurveLine")};
        lineStyle = new UIButtonGroup<LineStyle>(textArray, new LineStyle[]{LineStyle.NORMAL, LineStyle.CURVE});
    }

    @Override
    protected JPanel createContentPane(double p, double f) {
        double[] row = {p,p,p};
        double[] col = {p,f};

        Component[][] components = new Component[][]{
                new Component[]{null,null},
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_LineStyle")), lineWidth},
                new Component[]{new UILabel(Inter.getLocText("FR-Chart-Style_Present")), lineStyle},
        };

        return TableLayoutHelper.createTableLayoutPane(components, row, col);
    }

    @Override
    protected VanChartAttrLine initVanChartAttrLine() {
        VanChartAttrLine attrLine =  new VanChartAttrLine();
        //默认为无线型，且默認空值不斷開
        attrLine.setLineWidth(Constants.LINE_NONE);
        attrLine.setNullValueBreak(false);
        return attrLine;
    }

}