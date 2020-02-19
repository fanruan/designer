package com.fr.van.chart.scatter.component;

import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.i18n.Toolkit;
import com.fr.plugin.chart.base.VanChartAttrLine;
import com.fr.plugin.chart.type.LineStyle;
import com.fr.stable.Constants;
import com.fr.van.chart.designer.component.VanChartLineTypePane;

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
    protected Component[][] createContentComponent(Component[] lineStyleComponent, Component[] nullValueBreakComponent) {
        return new Component[][]{
                //线型支持虚线 恢复用注释。下面2行删除。
                new Component[]{null, null},
                new Component[]{new UILabel(Toolkit.i18nText("Fine-Design_Chart_Line_Style")), lineWidth},
                lineStyleComponent
        };
    }

    @Override
    protected VanChartAttrLine initVanChartAttrLine() {
        VanChartAttrLine attrLine = new VanChartAttrLine();
        //默认为无线型，且默認空值不斷開
        //线型支持虚线 恢复用注释。下面1行删除。
        attrLine.setLineWidth(Constants.LINE_NONE);
        //线型支持虚线 恢复用注释。取消注释。
        //attrLine.setLineType(LineType.NONE);
        attrLine.setNullValueBreak(false);
        return attrLine;
    }

}