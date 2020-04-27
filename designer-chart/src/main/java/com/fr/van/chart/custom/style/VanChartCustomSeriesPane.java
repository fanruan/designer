package com.fr.van.chart.custom.style;

import com.fr.chart.chartattr.Chart;
import com.fr.chartx.config.info.constant.ConfigType;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.style.series.ChartSeriesPane;
import com.fr.design.mainframe.chart.info.ChartInfoCollector;
import com.fr.plugin.chart.attr.plot.VanChartPlot;
import com.fr.plugin.chart.custom.VanChartCustomPlot;
import com.fr.van.chart.designer.component.VanChartBeautyPane;
import com.fr.van.chart.designer.component.VanChartFillStylePane;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;


/**
 * Created by Fangjie on 2016/4/22.
 */
public class VanChartCustomSeriesPane extends ChartSeriesPane {

    private JPanel seriesPane;
    protected VanChartCustomPlotSeriesTabPane plotSeriesPane;
    private VanChartFillStylePane fillStylePane;//配色
    private VanChartBeautyPane stylePane;//风格



    public VanChartCustomSeriesPane(ChartStylePane parent) {
        super(parent);
    }

    protected JPanel createContentPane() {
        JPanel contentPane = new JPanel(new BorderLayout());
        if(chart == null) {
            return contentPane;
        }
        initSeriesPane((VanChartCustomPlot) chart.getPlot());

        plotSeriesPane.setBorder(BorderFactory.createEmptyBorder());

        //公共使用的部分
        seriesPane.add(plotSeriesPane, BorderLayout.CENTER);

        //每种不同的图表单独的设置部分
        if(seriesPane != null) {
            contentPane.add(seriesPane, BorderLayout.CENTER);
        }
        return contentPane;
    }

    /**
     * 创建組合图系列界面
     */
    private void initSeriesPane(VanChartCustomPlot plot) {

        seriesPane = new JPanel(new BorderLayout(0, 10));
        //获取公共的属性面板
        seriesPane.add(initCommonPane(), BorderLayout.NORTH);

        plotSeriesPane = new VanChartCustomPlotSeriesTabPane(plot, parent);
    }

    private JPanel initCommonPane() {

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] row = {p,p};
        double[] col = {f};

        fillStylePane = new VanChartFillStylePane();

        stylePane = new VanChartBeautyPane();

        Component[][] components = new Component[][]{
                new Component[]{fillStylePane}, //配色
                new Component[]{stylePane},//风格
        };

        JPanel panel = TableLayoutHelper.createTableLayoutPane(components, row, col);
        panel.setBorder(BorderFactory.createEmptyBorder(5,5,0,5));
        return panel;
    }

    /**
     * 保存界面属性
     */
    @Override
    public void updateBean(Chart chart) {

        if(chart == null) {
            return;
        }

        VanChartCustomPlot plot = (VanChartCustomPlot) chart.getPlot();

        if (fillStylePane != null){
            plot.setPlotFillStyle(fillStylePane.updateBean());
        }
        if(stylePane != null) {
            plot.setPlotStyle(stylePane.updateBean());

            //风格属性传递
            for (int i = 0; i < plot.getCustomPlotList().size(); i++){
                VanChartPlot vanChartPlot = plot.getCustomPlotList().get(i);
                vanChartPlot.setPlotStyle(plot.getPlotStyle());
            }

        }
        if (seriesPane != null){
            plotSeriesPane.updateBean(plot);
        }
        //系列埋点
        ChartInfoCollector.getInstance().updateChartConfig(chart, ConfigType.SERIES, chart.getPlot().getBuryingPointSeriesConfig());
    }

    /**
     * 更新界面
     */
    @Override
    public void populateBean(Chart chart) {
        this.chart = chart;
        if(seriesPane == null) {
            this.remove(leftcontentPane);
            layoutContentPane();
            parent.initAllListeners();
        }
        if(seriesPane != null) {
            //更新渐变色和风格
            VanChartCustomPlot plot = (VanChartCustomPlot) chart.getPlot();
            if(plot == null) {
                return;
            }


            if(fillStylePane != null) {//配色
                fillStylePane.populateBean(plot.getPlotFillStyle());
            }
            if(stylePane != null){//风格
                stylePane.populateBean(plot.getPlotStyle());
            }

            //更新不同点的系列界面
            plotSeriesPane.populateBean(plot);
        }
    }

}
