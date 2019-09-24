package com.fr.design.mainframe.chart.gui.type;

import com.fr.base.CoreDecimalFormat;
import com.fr.chart.base.AttrContents;
import com.fr.chart.base.ChartConstants;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.FunnelPlot;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.charttypes.FunnelIndependentChart;
import com.fr.design.i18n.Toolkit;

import java.text.DecimalFormat;

/**
 * Created with IntelliJ IDEA.
 * User: Randost
 * Date: 14-12-2
 * Time: 下午2:50
 * To change this template use File | Settings | File Templates.
 */
public class FunnelPlotPane extends AbstractDeprecatedChartTypePane {

    private static final int FUNNEL_CHART = 0;

    @Override
    protected String[] getTypeIconPath() {
        return new String[]{"/com/fr/design/images/chart/FunnelPlot/type/0.png",
        };
    }

    @Override
    protected String[] getTypeTipName() {
        return new String[]{
                Toolkit.i18nText("Fine-Design_Chart_Type_Funnel")
        };
    }

    @Override
    protected String[] getTypeLayoutPath() {
        return new String[0];
    }

    @Override
    protected String[] getTypeLayoutTipName() {
        return new String[0];
    }

    /**
     * 返回界面标题
     * @return 界面标题
     */
    public String title4PopupWindow() {
        return Toolkit.i18nText("Fine-Design_Chart_Type_Funnel");
    }

    /**
     * 更新界面内容
     */
    public void populateBean(Chart chart) {
        super.populateBean(chart);
        typeDemo.get(FUNNEL_CHART).isPressing = true;

        checkDemosBackground();
    }

    /**
     * 保存界面属性
     */
    public void updateBean(Chart chart) {
        chart.switchPlot(getSelectedClonedPlot());
        super.updateBean(chart);
    }

    @Override
    protected String getPlotTypeID() {
        return ChartConstants.FUNNEL_CHART;
    }

    protected Plot getSelectedClonedPlot(){
        Plot newPlot = new FunnelPlot();
        createFunnelCondition(newPlot);
        return newPlot;
    }

    private void createFunnelCondition(Plot plot) {
        AttrContents attrContents = new AttrContents("${VALUE}${PERCENT}");
        plot.setHotTooltipStyle(attrContents);
        attrContents.setFormat(new CoreDecimalFormat(new DecimalFormat(), "#.##"));
        attrContents.setPercentFormat(new CoreDecimalFormat(new DecimalFormat(), "#.##%"));
    }

    public Chart getDefaultChart() {
        return FunnelIndependentChart.funnelChartTypes[0];
    }
}