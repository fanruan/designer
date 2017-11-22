package com.fr.plugin.chart.radar;

import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.type.AbstractChartTypePane;
import com.fr.plugin.chart.vanchart.AbstractIndependentVanChartUI;

/**
 * Created by Mitisky on 15/12/28.
 */
public class RadarIndependentVanChartInterface extends AbstractIndependentVanChartUI {
    @Override
    public String getIconPath() {
        return "com/fr/design/images/form/toolbar/radar.png";
    }

    @Override
    public AbstractChartTypePane getPlotTypePane() {
        return new VanChartRadarPlotPane();
    }

    public BasicBeanPane<Plot> getPlotSeriesPane(ChartStylePane parent, Plot plot){
        return new VanChartRadarSeriesPane(parent, plot);
    }

    public ConditionAttributesPane getPlotConditionPane(Plot plot){
        return new VanChartRadarConditionPane(plot);
    }

    public String getPlotTypeTitle4PopupWindow(){
        return VanChartRadarPlotPane.TITLE;
    }
}