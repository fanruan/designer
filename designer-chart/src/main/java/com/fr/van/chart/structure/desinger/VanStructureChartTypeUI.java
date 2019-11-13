package com.fr.van.chart.structure.desinger;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.chartx.AbstractVanSingleDataPane;
import com.fr.design.chartx.fields.diff.StructureCellDataFieldsPane;
import com.fr.design.chartx.fields.diff.StructureDataSetFieldsPane;
import com.fr.design.chartx.single.SingleDataPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.i18n.Toolkit;
import com.fr.design.mainframe.chart.AbstractChartAttrPane;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.data.report.AbstractReportDataContentPane;
import com.fr.design.mainframe.chart.gui.data.table.AbstractTableDataContentPane;
import com.fr.design.mainframe.chart.gui.type.AbstractChartTypePane;
import com.fr.van.chart.designer.other.VanChartInteractivePaneWithOutSort;
import com.fr.van.chart.designer.other.VanChartOtherPane;
import com.fr.van.chart.designer.other.zoom.ZoomPane;
import com.fr.van.chart.designer.other.zoom.ZoomPaneWithOutMode;
import com.fr.van.chart.designer.style.VanChartStylePane;
import com.fr.van.chart.structure.desinger.data.StructurePlotReportDataContentPane;
import com.fr.van.chart.structure.desinger.data.StructurePlotTableDataContentPane;
import com.fr.van.chart.structure.desinger.other.VanChartStructureConditionPane;
import com.fr.van.chart.structure.desinger.style.VanChartStructureSeriesPane;
import com.fr.van.chart.structure.desinger.type.VanChartStructureTypePane;
import com.fr.van.chart.vanchart.AbstractIndependentVanChartUI;

/**
 * Created by shine on 2017/2/15.
 */
public class VanStructureChartTypeUI extends AbstractIndependentVanChartUI {
    @Override
    public AbstractChartTypePane getPlotTypePane() {
        return new VanChartStructureTypePane();
    }

    @Override
    public String getName() {
        return Toolkit.i18nText("Fine-Design_Chart_New_Structure");
    }

    @Override
    public String[] getSubName() {
        return new String[]{
                Toolkit.i18nText("Fine-Design_Chart_Vertical_Structure"),
                Toolkit.i18nText("Fine-Design_Chart_Horizontal_Structure"),
                Toolkit.i18nText("Fine-Design_Chart_Radial_Structure")
        };
    }

    @Override
    public String[] getDemoImagePath() {
        return new String[]{
                "com/fr/plugin/chart/demo/image/45.png",
                "com/fr/plugin/chart/demo/image/46.png",
                "com/fr/plugin/chart/demo/image/47.png"
        };
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
                    protected ZoomPane createZoomPane() {
                        return new ZoomPaneWithOutMode();
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

    @Override
    public ChartDataPane getChartDataPane(AttributeChangeListener listener) {
        return new AbstractVanSingleDataPane(listener) {
            @Override
            protected SingleDataPane createSingleDataPane() {
                return new SingleDataPane(new StructureDataSetFieldsPane(), new StructureCellDataFieldsPane());
            }
        };
    }
}
