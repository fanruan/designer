package com.fr.van.chart.designer.data;

import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartdata.NormalReportDataDefinition;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.report.CategoryPlotMoreCateReportDataContentPane;
import com.fr.plugin.chart.attr.plot.VanChartPlot;
import com.fr.plugin.chart.attr.plot.VanChartRectanglePlot;

/**
 * Created by mengao on 2017/7/3.
 */
public class VanChartMoreCateReportDataContentPane extends CategoryPlotMoreCateReportDataContentPane {
    private boolean isSupportMultiCategory;

    public VanChartMoreCateReportDataContentPane(ChartDataPane parent) {
        super(parent);
    }

    @Override
    public void populateBean(ChartCollection collection) {
        super.populateBean(collection);
        VanChartRectanglePlot plot = (VanChartRectanglePlot) collection.getSelectedChart().getPlot();
        isSupportMultiCategory = plot.isSupportMultiCategory();
        checkBoxList(isSupportMultiCategory);
    }

    protected void updateMoreCate(NormalReportDataDefinition reportDefinition, Plot plot) {
        super.updateMoreCate(reportDefinition, plot);
        ((VanChartPlot) plot).setCategoryNum(getFormualList().size() + 1);
        if (!getFormualList().isEmpty()) {
            plot.getDataSheet().setVisible(false);
        }
    }

    protected void checkComponent() {
        super.checkComponent();
        checkBoxList(isSupportMultiCategory);
    }

    private void checkBoxList(boolean isSupportMulticategory) {
        if (getFormualList().size() != 0) {
            for (int i = 0; i < getFormualList().size(); i++) {
                getFormualList().get(i).setEnabled(isSupportMulticategory);
            }
        }
    }

}
