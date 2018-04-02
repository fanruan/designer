package com.fr.van.chart.designer.other;

import com.fr.design.layout.TableLayout;

import java.awt.Component;

/**
 * Created by Mitisky on 16/5/20.
 * 没有排序选择
 */
public class VanChartInteractivePaneWithOutSort extends VanChartInteractivePane {

    @Override
    protected Component[][] createToolBarComponents() {
        return super.createToolBarComponentsWithOutSort();
    }

    @Override
    protected double[] getToolBarRowSize() {
        double p = TableLayout.PREFERRED;
        return new double[]{p, p, p};
    }
}
