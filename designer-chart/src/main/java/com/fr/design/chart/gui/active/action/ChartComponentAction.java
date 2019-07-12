package com.fr.design.chart.gui.active.action;

import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartglyph.AxisGlyph;
import com.fr.design.actions.UpdateAction;
import com.fr.design.chart.gui.ChartComponent;

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

    public ChartCollection getChartCollection() {
        return chartComponent.getChartCollection();
    }

    public AxisGlyph getActiveAxisGlyph() {
        return chartComponent.getActiveAxisGlyph();
    }
}