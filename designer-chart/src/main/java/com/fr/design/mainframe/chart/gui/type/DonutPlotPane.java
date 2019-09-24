package com.fr.design.mainframe.chart.gui.type;

import com.fr.base.CoreDecimalFormat;
import com.fr.chart.base.AttrBorder;
import com.fr.chart.base.AttrContents;
import com.fr.chart.base.ChartConstants;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Donut2DPlot;
import com.fr.chart.chartattr.Donut3DPlot;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartglyph.ConditionCollection;
import com.fr.chart.charttypes.DonutIndependentChart;
import com.fr.design.i18n.Toolkit;
import com.fr.stable.Constants;

import java.text.DecimalFormat;
import java.awt.Color;

/**
 * 圆环图的类型界面
 * @author eason
 *
 */
public class DonutPlotPane extends AbstractDeprecatedChartTypePane {
    private static final long serialVersionUID = -7084314809934346710L;
    private static final int DONUT_CHART = 0; //2d圆环图
    private static final int THREE_D_DONUT_CHART = 1; //3D圆环图

    @Override
    protected String[] getTypeIconPath() {
        return new String[]{"/com/fr/design/images/chart/DonutPlot/type/0.png",
                "/com/fr/design/images/chart/DonutPlot/type/1.png",
        };
    }

    @Override
    protected String[] getTypeTipName() {
        String donut = Toolkit.i18nText("Fine-Design_Chart_Type_Donut");
        return new String[]{
                donut,
                Toolkit.i18nText("Fine-Design_Chart_3D") + donut
        };
    }

    @Override
    protected String[] getTypeLayoutPath() {
        return new String[]{"/com/fr/design/images/chart/DonutPlot/layout/0.png",
                "/com/fr/design/images/chart/DonutPlot/layout/1.png",
                "/com/fr/design/images/chart/DonutPlot/layout/2.png",
                "/com/fr/design/images/chart/DonutPlot/layout/3.png",
        };
    }

    @Override
    protected String[] getTypeLayoutTipName() {
        return getNormalLayoutTipName();
    }

    /**
     * 更新界面内容
     */
    public void populateBean(Chart chart) {
        super.populateBean(chart);
        for(ChartImagePane imagePane : typeDemo) {
            imagePane.isPressing = false;
        }
        Plot plot = chart.getPlot();
        if(plot instanceof Donut3DPlot) {
            typeDemo.get(THREE_D_DONUT_CHART).isPressing = true;
        } else {
            typeDemo.get(DONUT_CHART).isPressing = true;
        }

        checkDemosBackground();
    }

    protected Plot getSelectedClonedPlot(){
        Plot newPlot;
        if(typeDemo.get(DONUT_CHART).isPressing) {
            newPlot = new Donut2DPlot();
        } else {
            newPlot = new Donut3DPlot();
        }
        createDonutCondition(newPlot);
        return newPlot;
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
        return ChartConstants.DONUT_CHART;
    }

    /**
     *界面标题
     * @return 标题
     */
    public String title4PopupWindow() {
        return Toolkit.i18nText("Fine-Design_Chart_Type_Donut");
    }

    /**
     * 是否有坐标轴
     * @return 没有坐标轴
     */
    public boolean isHaveAxis() {
        return false;
    }

    private void createDonutCondition(Plot plot) {
        ConditionCollection collection = plot.getConditionCollection();
        AttrBorder attrBorder = (AttrBorder) collection.getDefaultAttr().getExisted(AttrBorder.class);
        if (attrBorder == null) {
            attrBorder = new AttrBorder();
            collection.getDefaultAttr().addDataSeriesCondition(attrBorder);
        }
        attrBorder.setBorderColor(Color.WHITE);
        attrBorder.setBorderStyle(Constants.LINE_THIN);

        AttrContents attrContents = new AttrContents("${VALUE}${PERCENT}");
        plot.setHotTooltipStyle(attrContents);
        attrContents.setFormat(new CoreDecimalFormat(new DecimalFormat(), "#.##"));
        attrContents.setPercentFormat(new CoreDecimalFormat(new DecimalFormat(), "#.##%"));
    }


    public Chart getDefaultChart() {
        return DonutIndependentChart.donutChartTypes[0];
    }
}