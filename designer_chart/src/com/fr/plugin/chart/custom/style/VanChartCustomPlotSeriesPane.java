package com.fr.plugin.chart.custom.style;

import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.style.series.AbstractPlotSeriesPane;

import java.awt.*;

/**
 * Created by Fangjie on 2016/4/26.
 */
public class VanChartCustomPlotSeriesPane extends BasicBeanPane<Plot> {
    private static final int WIDTH = 236;
    private static final int DELTA_HEIGHT = 300;
    private BasicBeanPane<Plot> axisPane;
    private AbstractPlotSeriesPane seriesPane;
    public VanChartCustomPlotSeriesPane(BasicBeanPane<Plot> axisPane, AbstractPlotSeriesPane seriesPane) {
        this.axisPane = axisPane;
        this.seriesPane = seriesPane;

        initContentPane();
    }

    private void initContentPane() {

        seriesPane.setPreferredSize(new Dimension(WIDTH, (int) (seriesPane.getPreferredSize().getHeight() + DELTA_HEIGHT)));

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;

        double[] columnSize = {f};
        double[] rowSize = {p, p, p};

        if (axisPane == null) {
            this.add(seriesPane);
        }else {
            Component[][] components = new Component[][]{
                    new Component[]{axisPane},
                    new Component[]{seriesPane}
            };
            this.add(TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize));
        }

    }


    @Override
    protected String title4PopupWindow() {
        return null;
    }

    @Override
    public void populateBean(Plot plot) {
        //获取位置信息
        if (axisPane != null) {
            axisPane.populateBean(plot);
        }
        //获取界面信息
        seriesPane.populateBean(plot);
    }

    @Override
    public void updateBean(Plot plot) {
        if(plot == null) {
            return;
        }
        if(seriesPane != null) {
            seriesPane.updateBean(plot);
        }
        if (axisPane != null){
            axisPane.updateBean(plot);
        }
    }

    @Override
    public Plot updateBean() {
        return null;
    }

}
