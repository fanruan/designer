package com.fr.van.chart.custom.style;

import com.fr.chart.chartattr.Chart;
import com.fr.chartx.config.info.constant.ConfigType;
import com.fr.design.dialog.BasicScrollPane;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.design.mainframe.chart.info.ChartInfoCollector;
import com.fr.plugin.chart.custom.VanChartCustomPlot;
import com.fr.plugin.chart.vanchart.VanChart;
import com.fr.van.chart.designer.style.VanChartStylePane;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;


/**
 * Created by Fangjie on 2016/4/27.
 */
public class VanChartCustomAxisPane extends BasicScrollPane<Chart> {
    private static final long serialVersionUID = -2974722365840564105L;
    private VanChartCustomAxisTabPane axisPane;
    private Chart chart;
    protected VanChartStylePane parent;

    public VanChartCustomAxisPane(VanChartStylePane parent) {
        super();
        this.parent = parent;
    }

    @Override
    protected JPanel createContentPane() {
        JPanel contentPane = new JPanel(new BorderLayout());
        if(chart == null) {
            return contentPane;
        }
        initAxisPane((VanChartCustomPlot) chart.getPlot());
        contentPane.add(axisPane, BorderLayout.CENTER);
        return contentPane;
    }

    private void initAxisPane(VanChartCustomPlot plot) {
        axisPane = new VanChartCustomAxisTabPane(plot, parent);
    }

    @Override
    public void populateBean(Chart chart) {
        this.chart = chart;

        if(axisPane == null){
            this.remove(leftcontentPane);
            layoutContentPane();
            parent.initAllListeners();
        }

        if(axisPane != null) {
            axisPane.populateBean((VanChartCustomPlot) chart.getPlot());
        }
    }

    @Override
    public void updateBean(Chart chart){
        if(chart == null){
            return;
        }
        VanChartCustomPlot plot = (VanChartCustomPlot) chart.getPlot();
        axisPane.updateBean(plot);
        //坐标轴埋点
        ChartInfoCollector.getInstance().updateChartConfig(chart, ConfigType.AXIS, chart.getPlot().getBuryingPointAxisConfig());
    }

    @Override
    protected void layoutContentPane() {
        leftcontentPane = createContentPane();
        leftcontentPane.setBorder(BorderFactory.createEmptyBorder());
        this.setLayout(new BorderLayout());
        this.add(leftcontentPane, BorderLayout.CENTER);
    }

    @Override
    public VanChart updateBean() {
        return null;
    }

    @Override
    protected String title4PopupWindow() {
        return PaneTitleConstants.CHART_STYLE_AXIS_TITLE;
    }
}
