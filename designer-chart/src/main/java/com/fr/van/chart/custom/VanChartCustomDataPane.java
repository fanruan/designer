package com.fr.van.chart.custom;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.plugin.chart.custom.VanChartCustomPlot;

import javax.swing.JPanel;
import java.awt.BorderLayout;

/**
 * Created by Fangjie on 2016/4/29.
 */
public class VanChartCustomDataPane extends ChartDataPane {
    private VanChartCustomPlotDataContentsTabPane contentsTabPane;
    private Chart chart;
    public VanChartCustomDataPane(AttributeChangeListener listener) {
        super(listener);
    }

    @Override
    protected JPanel createContentPane() {
        JPanel content = new JPanel(new BorderLayout());
        if (chart == null) {
            return content;
        }

        contentsTabPane = new VanChartCustomPlotDataContentsTabPane((VanChartCustomPlot)chart.getPlot(), VanChartCustomDataPane.this, listener);

        content.add(contentsTabPane, BorderLayout.CENTER);
        return content;


    }

    public void populate(ChartCollection collection) {
        this.chart = collection.getSelectedChart();
        this.remove(leftContentPane);
        initContentPane();
        this.removeAttributeChangeListener();
        contentsTabPane.populateBean(collection);
        this.addAttributeChangeListener(listener);
        this.initAllListeners();

    }

    @Override
    /**
     * 返回绑定的属性事件.
     * @param listener  增加监听
     */
    public void addAttributeChangeListener(AttributeChangeListener listener) {
        super.addAttributeChangeListener(listener);
        contentsTabPane.addAttributeChangeListener(listener);
    }

    @Override
    /**
     * 保存 数据界面内容
     */
    public void update(ChartCollection collection){
        if(contentsTabPane != null) {
            contentsTabPane.updateBean(collection);
            updateBuryingPoint(collection);
        }
    }
}
