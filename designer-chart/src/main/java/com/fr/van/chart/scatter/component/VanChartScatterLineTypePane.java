package com.fr.van.chart.scatter.component;

import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayoutHelper;

import com.fr.plugin.chart.base.VanChartAttrLine;
import com.fr.plugin.chart.type.LineStyle;
import com.fr.stable.Constants;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;
import com.fr.van.chart.designer.component.VanChartLineTypePane;

import javax.swing.JPanel;
import java.awt.Component;

/**
 * line相关设置
 */
public class VanChartScatterLineTypePane extends VanChartLineTypePane {

    @Override
    protected void createLineStyle() {
        String[] textArray = new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Normal_Line"),
                                            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_CurveLine")};
        lineStyle = new UIButtonGroup<LineStyle>(textArray, new LineStyle[]{LineStyle.NORMAL, LineStyle.CURVE});
    }

    @Override
    protected JPanel createContentPane(double p, double f) {
        double[] row = {p,p,p};
        double e = TableLayout4VanChartHelper.EDIT_AREA_WIDTH;
        double[] col = {f, e};
        Component[][] components = new Component[][]{
                new Component[]{null,null},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Line_Style")), lineWidth},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Style_Present")), lineStyle},
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