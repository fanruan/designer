package com.fr.van.chart.designer.style;

import com.fr.chart.chartattr.Plot;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.plugin.chart.attr.plot.VanChartAxisPlot;
import com.fr.van.chart.designer.style.axis.VanChartAxisPane;
import com.fr.van.chart.designer.style.background.VanChartAreaPane;
import com.fr.van.chart.designer.style.datasheet.VanChartDataSheetPane;
import com.fr.van.chart.designer.style.label.VanChartLabelPane;
import com.fr.van.chart.designer.style.tooltip.VanChartTooltipPane;

import java.util.ArrayList;
import java.util.List;

public class VanChartStylePane extends ChartStylePane {

    private static final long serialVersionUID = 186776958263021761L;

    public VanChartStylePane(AttributeChangeListener listener) {
        super(listener);
    }

    protected java.util.List<BasicPane> getPaneList() {
        java.util.List<BasicPane> paneList = new ArrayList<BasicPane>();
        Plot plot = getChart().getPlot();
        paneList.add(new VanChartTitlePane(VanChartStylePane.this));
        if(plot.isSupportLegend()){
            paneList.add(new VanChartLegendPane(VanChartStylePane.this));
        }

        createVanChartLabelPane(paneList);

        paneList.add(createChartSeriesPane());

        if(plot.isHaveAxis()){
            if(plot instanceof VanChartAxisPlot) {
                createVanChartAxisPane(paneList, (VanChartAxisPlot) plot);
            }

            addOtherAxisPane(paneList, plot);

            if(plot.isSupportDataSheet()) {
                paneList.add(new VanChartDataSheetPane());
            }
        }

        addVanChartAreaPane(paneList);

        addVanChartTooltipPane(paneList);

        return paneList;
    }

    protected void addOtherAxisPane(java.util.List<BasicPane> paneList, Plot plot) {
    }

    protected void addVanChartAreaPane(List<BasicPane> paneList) {
        paneList.add(new VanChartAreaPane(getChart().getPlot(), VanChartStylePane.this));
    }

    protected void createVanChartAxisPane(List<BasicPane> paneList, VanChartAxisPlot plot) {
        paneList.add(new VanChartAxisPane(plot, VanChartStylePane.this));
    }

    protected void createVanChartLabelPane(List<BasicPane> paneList) {
        paneList.add(new VanChartLabelPane(VanChartStylePane.this));
    }

    protected void addVanChartTooltipPane(List<BasicPane> paneList){
        paneList.add(new VanChartTooltipPane(VanChartStylePane.this));
    }
}