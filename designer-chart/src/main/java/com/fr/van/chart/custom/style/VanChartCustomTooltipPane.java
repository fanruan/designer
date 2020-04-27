package com.fr.van.chart.custom.style;

import com.fr.chart.chartattr.Chart;
import com.fr.chartx.config.info.constant.ConfigType;
import com.fr.design.dialog.BasicScrollPane;
import com.fr.design.mainframe.chart.info.ChartInfoCollector;
import com.fr.plugin.chart.custom.VanChartCustomPlot;
import com.fr.van.chart.designer.style.VanChartStylePane;

import javax.swing.JPanel;
import java.awt.BorderLayout;

/**
 * Created by Fangjie on 2016/4/22.
 */
public class VanChartCustomTooltipPane extends BasicScrollPane<Chart> {

    private static final long serialVersionUID = -2974722365840564105L;
    private VanChartCustomPlotTooltipTabPane tooltipPane;
    private Chart chart;
    protected VanChartStylePane parent;


    public VanChartCustomTooltipPane(VanChartStylePane parent){
        super();
        this.parent = parent;
    }

    @Override
    protected JPanel createContentPane() {
        JPanel contentPane = new JPanel(new BorderLayout());
        if(chart == null) {
            return contentPane;
        }
        initTooltipPane((VanChartCustomPlot) chart.getPlot());
        contentPane.add(tooltipPane, BorderLayout.NORTH);
        return contentPane;
    }

    private void initTooltipPane(VanChartCustomPlot plot) {
        tooltipPane = new VanChartCustomPlotTooltipTabPane(plot, parent);
    }


    @Override
    public void populateBean(Chart chart) {
        this.chart = chart;

        if(tooltipPane == null){
            this.remove(leftcontentPane);
            layoutContentPane();
            parent.initAllListeners();
        }

        if(tooltipPane != null) {
            tooltipPane.populateBean((VanChartCustomPlot) chart.getPlot());
        }
    }

    @Override
    public void updateBean(Chart chart) {
        if(chart == null) {
            return;
        }

        tooltipPane.updateBean((VanChartCustomPlot)chart.getPlot());
        //提示埋点
        ChartInfoCollector.getInstance().updateChartConfig(chart, ConfigType.TOOLTIP, chart.getPlot().getBuryingPointTooltipConfig());
    }

    @Override
    protected String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Tooltip");
    }
}
