package com.fr.plugin.chart.designer.data;

import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartdata.NormalTableDataDefinition;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.table.CategoryPlotMoreCateTableDataContentPane;
import com.fr.plugin.chart.attr.plot.VanChartPlot;
import com.fr.plugin.chart.attr.plot.VanChartRectanglePlot;

/**
 * Created by mengao on 2017/7/3.
 */
public class VanChartMoreCateTableDataContentPane extends CategoryPlotMoreCateTableDataContentPane {
    private boolean isSupportMultiCategory;

    public VanChartMoreCateTableDataContentPane(ChartDataPane parent) {
        super(parent);
    }


    @Override
    public void populateBean(ChartCollection collection) {
        super.populateBean(collection);
        VanChartRectanglePlot plot = (VanChartRectanglePlot) collection.getSelectedChart().getPlot();
        isSupportMultiCategory = plot.isSupportMultiCategory();
        checkBoxList(isSupportMultiCategory);
    }

    protected void updateMoreCate(NormalTableDataDefinition normal, Plot plot) {
        super.updateMoreCate(normal, plot);
        ((VanChartPlot) plot).setCategoryNum(getBoxList().size() + 1);
        if (!getBoxList().isEmpty()) {
            plot.getDataSheet().setVisible(false);
        }

    }

    protected void checkComponent() {
        super.checkComponent();
        checkBoxList(isSupportMultiCategory);
    }

    private void checkBoxList(boolean isSupportMulticategory) {
        if (getBoxList().size() != 0) {
            for (int i = 0; i < getBoxList().size(); i++) {
                getBoxList().get(i).setEnabled(isSupportMulticategory);
            }
        }
    }
}
