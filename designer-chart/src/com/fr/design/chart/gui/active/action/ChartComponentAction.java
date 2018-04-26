package com.fr.design.chart.gui.active.action;

import com.fr.base.chart.BaseChart;
import com.fr.chart.chartattr.Axis;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartglyph.AxisGlyph;
import com.fr.design.chart.gui.ChartComponent;
import com.fr.design.actions.UpdateAction;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date   : 11-11-22
 * Time   : 下午5:05
 */
public abstract class ChartComponentAction extends UpdateAction {
    protected ChartComponent chartComponent;

    public ChartComponentAction(ChartComponent chartComponent) {
        this.chartComponent = chartComponent;
    }

    public void reset() {
        chartComponent.reset();
    }

    public void repaint() {
        chartComponent.repaint();
    }

    public BaseChart getEditingChart() {
        return chartComponent.getEditingChart();
    }

    public ChartCollection getChartCollection() {
        return chartComponent.getChartCollection();
    }

    public Axis getActiveAxis() {
        return chartComponent.getActiveAxis();
    }

    public AxisGlyph getActiveAxisGlyph() {
        return chartComponent.getActiveAxisGlyph();
    }
}