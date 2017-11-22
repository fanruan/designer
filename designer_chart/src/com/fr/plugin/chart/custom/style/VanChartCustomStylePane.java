package com.fr.plugin.chart.custom.style;

import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.mainframe.chart.gui.style.series.ChartSeriesPane;
import com.fr.plugin.chart.attr.plot.VanChartAxisPlot;
import com.fr.plugin.chart.custom.CustomPlotFactory;
import com.fr.plugin.chart.custom.VanChartCustomPlot;
import com.fr.plugin.chart.designer.style.VanChartStylePane;

import java.util.List;

/**
 * Created by Fangjie on 2016/4/22.
 */
public class VanChartCustomStylePane extends VanChartStylePane {
    public VanChartCustomStylePane(AttributeChangeListener listener) {
        super(listener);
    }

    /**
     * 根据不同的plot类型创建不同的标签界面
     * @param paneList
     */
    @Override
    protected void createVanChartLabelPane(List<BasicPane> paneList) {
        paneList.add(new VanChartCustomLabelPane(VanChartCustomStylePane.this));
    }

    /**
     * 根据不同的plot类型创建不同的数据点提示界面
     * @param paneList
     */
    @Override
    protected void addVanChartTooltipPane(List<BasicPane> paneList){
        paneList.add(new VanChartCustomTooltipPane(VanChartCustomStylePane.this));
    }

    /**
     * 组合图以外的新图表类型都是使用ChartSeriesPane然后通过接口创建不同的seriesStyleContentPane
     * 组合图类型不走那个接口，因为需要的系列界面完全是新的，需要像标签界面那样处理
     * @return
     */
    @Override
    protected ChartSeriesPane createChartSeriesPane() {
        return new VanChartCustomSeriesPane(VanChartCustomStylePane.this);
    }

    /**
     * 当所有图表都不支持坐标轴时，则无坐标轴选项，否则创建和自定义柱形图一样的坐标轴
     * @param paneList
     */
    @Override
    protected void createVanChartAxisPane(List<BasicPane> paneList, VanChartAxisPlot plot) {
        paneList.add(new VanChartCustomAxisPane(VanChartCustomStylePane.this));
    }


    @Override
    protected void addVanChartAreaPane(List<BasicPane> paneList) {
        paneList.add(new VanChartCustomAreaPane(getChart().getPlot(), VanChartCustomStylePane.this));
    }

    @Override
    public void update(ChartCollection collection) {
        super.update(collection);
        CustomPlotFactory.dataSheetSynchronization((VanChartCustomPlot) collection.getSelectedChart().getPlot());
    }

}
