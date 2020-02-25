package com.fr.van.chart.designer.component;

import com.fr.design.gui.ilable.UILabel;
import com.fr.design.i18n.Toolkit;

import java.awt.Component;

/**
 * 线型+线宽+空值断开
 */
public class VanChartLineWidthPane extends VanChartLineTypePane {
    private static final long serialVersionUID = 4537158946119294689L;

    @Override
    protected Component[][] createContentComponent(Component[] lineStyleComponent, Component[] nullValueBreakComponent) {
        return new Component[][]{
                //线型支持虚线 恢复用注释。下面2行删除。
                new Component[]{null, null},
                new Component[]{new UILabel(Toolkit.i18nText("Fine-Design_Chart_Line_Style")), lineWidth},
                nullValueBreakComponent
        };
    }
}