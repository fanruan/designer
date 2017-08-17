package com.fr.plugin.chart.map.designer.style;

import com.fr.chart.chartattr.Plot;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.general.ComparatorUtils;
import com.fr.plugin.chart.designer.style.VanChartStylePane;
import com.fr.plugin.chart.type.MapType;
import com.fr.plugin.chart.map.VanChartMapPlot;
import com.fr.plugin.chart.map.designer.style.label.VanChartMapLabelPane;
import com.fr.plugin.chart.map.designer.style.tooltip.VanChartMapTooltipPane;

import java.util.List;

/**
 * Created by Mitisky on 16/5/20.
 */
public class VanChartMapStylePane extends VanChartStylePane {
    public VanChartMapStylePane(AttributeChangeListener listener) {
        super(listener);
    }

    protected void createVanChartLabelPane(List<BasicPane> paneList) {
        Plot plot = getChart().getPlot();
        if(!isLineMapPlot(plot)) {
            paneList.add(new VanChartMapLabelPane(VanChartMapStylePane.this));
        }
    }

    private boolean isLineMapPlot(Plot plot) {
        return plot instanceof VanChartMapPlot && ComparatorUtils.equals(((VanChartMapPlot) plot).getMapType(), MapType.LINE);
    }

    protected void addVanChartTooltipPane(List<BasicPane> paneList){
        paneList.add(new VanChartMapTooltipPane(VanChartMapStylePane.this));
    }
}
