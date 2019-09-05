package com.fr.van.chart.radar;

import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.i18n.Toolkit;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.type.AbstractChartTypePane;
import com.fr.van.chart.vanchart.AbstractIndependentVanChartUI;

/**
 * Created by Mitisky on 15/12/28.
 */
public class RadarIndependentVanChartInterface extends AbstractIndependentVanChartUI {
    @Override
    public String getIconPath() {
        return "com/fr/design/images/form/toolbar/radar.png";
    }

    @Override
    public String getName() {
        return Toolkit.i18nText("Fine-Design_Chart_New_Radar");
    }

    @Override
    public String[] getSubName() {
        return new String[]{
                Toolkit.i18nText("Fine-Design_Chart_Radar_Chart"),
                Toolkit.i18nText("Fine-Design_Chart_StackColumn_Radar")
        };
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
}