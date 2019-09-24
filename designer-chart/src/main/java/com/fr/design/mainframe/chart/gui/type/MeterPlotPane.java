package com.fr.design.mainframe.chart.gui.type;

import com.fr.chart.base.ChartConstants;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.MeterPlot;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartglyph.MeterStyle;
import com.fr.chart.charttypes.MeterIndependentChart;
import com.fr.design.i18n.Toolkit;
import com.fr.log.FineLoggerFactory;


/**
 * 仪表盘, 属性表 类型选择 界面.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2012-12-26 上午10:48:57
 */
public class MeterPlotPane extends AbstractDeprecatedChartTypePane {

    private static final int METER = 0;
    private static final int BLUE_METER =1;
    private static final int SIMPLE_METER = 2;


    @Override
    protected String[] getTypeIconPath() {
        return new String[]{"/com/fr/design/images/chart/MeterPlot/type/0.png",
                "/com/fr/design/images/chart/MeterPlot/type/1.png",
                "/com/fr/design/images/chart/MeterPlot/type/2.png",
        };
    }

    @Override
    protected String[] getTypeTipName() {
        String meter = Toolkit.i18nText("Fine-Design_Chart_Type_Meter");
        return new String[]{
                Toolkit.i18nText("Fine-Design_Chart_Mode_Custom") + meter,
                meter + "1",
                meter + "2"
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

    protected Plot getSelectedClonedPlot(){
        Chart[] meterChart = MeterIndependentChart.meterChartTypes;

        MeterPlot newPlot = null;
        if(typeDemo.get(METER).isPressing) {
            newPlot = (MeterPlot)meterChart[METER].getPlot();
        }
        else if(typeDemo.get(SIMPLE_METER).isPressing) {
            newPlot = (MeterPlot)meterChart[SIMPLE_METER].getPlot();
        }
        else if(typeDemo.get(BLUE_METER).isPressing) {
            newPlot = (MeterPlot)meterChart[BLUE_METER].getPlot();
        }

        Plot cloned = null;
        if(newPlot != null) {
            try {
                cloned = (Plot) newPlot.clone();
            } catch (CloneNotSupportedException e) {
                FineLoggerFactory.getLogger().error("Error In ColumnChart");
            }
        }
        return cloned;
    }

	/**
	 * 界面标题
     * @return 界面标题
	 */
	public String title4PopupWindow() {
        return Toolkit.i18nText("Fine-Design_Chart_Type_Meter");
    }

    /**
     * 保存界面属性
     */
    public void updateBean(Chart chart) {
        if(needsResetChart(chart)){
            resetChart(chart);
        }
        chart.switchPlot(getSelectedClonedPlot());
	}

    @Override
    protected String getPlotTypeID() {
        return ChartConstants.METER_CHART;
    }

    /**
	 * 更新界面内容
	 */
	public void populateBean(Chart chart) {
      	MeterPlot meterPlot = (MeterPlot)chart.getPlot();
        MeterStyle meterStyle = meterPlot.getMeterStyle();

        if(meterStyle.getMeterType() == MeterStyle.METER_NORMAL){
            typeDemo.get(METER).isPressing = true;
            typeDemo.get(BLUE_METER).isPressing = false;
            typeDemo.get(SIMPLE_METER).isPressing = false;
        } else if(meterStyle.getMeterType() == MeterStyle.METER_BLUE) {
            typeDemo.get(METER).isPressing = false;
            typeDemo.get(BLUE_METER).isPressing = true;
            typeDemo.get(SIMPLE_METER).isPressing = false;
        } else {
            typeDemo.get(METER).isPressing = false;
            typeDemo.get(BLUE_METER).isPressing = false;
            typeDemo.get(SIMPLE_METER).isPressing = true;
        }
		checkDemosBackground();
	}

    public Chart getDefaultChart() {
        return MeterIndependentChart.meterChartTypes[0];
    }
}