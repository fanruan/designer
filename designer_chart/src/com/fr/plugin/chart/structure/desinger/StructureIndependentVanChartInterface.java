package com.fr.plugin.chart.structure.desinger;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.mainframe.chart.AbstractChartAttrPane;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.data.report.AbstractReportDataContentPane;
import com.fr.design.mainframe.chart.gui.data.table.AbstractTableDataContentPane;
import com.fr.design.mainframe.chart.gui.type.AbstractChartTypePane;
import com.fr.general.Inter;
import com.fr.plugin.chart.base.VanChartConstants;
import com.fr.plugin.chart.designer.other.VanChartInteractivePaneWithOutSort;
import com.fr.plugin.chart.designer.other.VanChartOtherPane;
import com.fr.plugin.chart.designer.style.VanChartStylePane;
import com.fr.plugin.chart.structure.desinger.data.StructurePlotReportDataContentPane;
import com.fr.plugin.chart.structure.desinger.data.StructurePlotTableDataContentPane;
import com.fr.plugin.chart.structure.desinger.other.VanChartStructureConditionPane;
import com.fr.plugin.chart.structure.desinger.style.VanChartStructureSeriesPane;
import com.fr.plugin.chart.structure.desinger.type.VanChartStructureTypePane;
import com.fr.plugin.chart.vanchart.AbstractIndependentVanChartUI;

/**
 * Created by shine on 2017/2/15.
 */
public class StructureIndependentVanChartInterface extends AbstractIndependentVanChartUI {
    @Override
    public AbstractChartTypePane getPlotTypePane() {
        return new VanChartStructureTypePane();
    }

    @Override
    public String getIconPath() {
        return "com/fr/design/images/form/toolbar/structure.png";
    }

    @Override
    public AbstractTableDataContentPane getTableDataSourcePane(Plot plot, ChartDataPane parent) {
        return new StructurePlotTableDataContentPane();
    }

    @Override
    public AbstractReportDataContentPane getReportDataSourcePane(Plot plot, ChartDataPane parent) {
        return new StructurePlotReportDataContentPane();
    }

    @Override
    public BasicBeanPane<Plot> getPlotSeriesPane(ChartStylePane parent, Plot plot) {
        return new VanChartStructureSeriesPane(parent, plot);
    }

    @Override
    public AbstractChartAttrPane[] getAttrPaneArray(AttributeChangeListener listener) {
        VanChartStylePane stylePane = new VanChartStylePane(listener);
        VanChartOtherPane otherPane = new VanChartOtherPane(){
            @Override
            protected BasicBeanPane<Chart> createInteractivePane() {
                return new VanChartInteractivePaneWithOutSort(){

                    @Override
                    protected String[] getNameArray() {
                        return new String[]{Inter.getLocText("Plugin-ChartF_XYAxis"), Inter.getLocText("Chart-Use_None")};

                    }

                    @Override
                    protected String[] getValueArray() {
                        return new String[]{VanChartConstants.ZOOM_TYPE_XY, VanChartConstants.ZOOM_TYPE_NONE};
                    }

                };
            }
        };
        return new AbstractChartAttrPane[]{stylePane, otherPane};
    }

    @Override
    public ConditionAttributesPane getPlotConditionPane(Plot plot) {
        return new VanChartStructureConditionPane(plot);
    }
}
