package com.fr.van.chart.designer.component;

import java.awt.Component;

/**
 * 线型+线宽+空值断开
 */
public class VanChartLineWidthPane extends VanChartLineTypePane {
    private static final long serialVersionUID = 4537158946119294689L;

    @Override
    protected Component[][] createContentComponent(Component[] lineStyleComponent, Component[] nullValueBreakComponent) {
        return new Component[][]{
                nullValueBreakComponent
        };
    }
}