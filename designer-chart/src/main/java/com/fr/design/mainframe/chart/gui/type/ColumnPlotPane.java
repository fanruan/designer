package com.fr.design.mainframe.chart.gui.type;

import com.fr.chart.base.ChartConstants;
import com.fr.chart.chartattr.BarPlot;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.charttypes.ColumnIndependentChart;
import com.fr.design.i18n.Toolkit;
import com.fr.log.FineLoggerFactory;


/**
 * 柱形图 属性表 选择类型 布局界面.
* @author kunsnat E-mail:kunsnat@gmail.com
* @version 创建时间：2012-12-25 下午06:57:14
 */
public class ColumnPlotPane extends AbstractBarPane{
	private static final long serialVersionUID = 7070966970039838314L;

    @Override
    protected String[] getTypeIconPath() {
        return new String[]{"/com/fr/design/images/chart/BarPlot/type/0.png",
                "/com/fr/design/images/chart/BarPlot/type/1.png",
                "/com/fr/design/images/chart/BarPlot/type/2.png",
                "/com/fr/design/images/chart/BarPlot/type/3.png",
                "/com/fr/design/images/chart/BarPlot/type/4.png",
                "/com/fr/design/images/chart/BarPlot/type/5.png",
                "/com/fr/design/images/chart/BarPlot/type/6.png",
        };
    }

    @Override
    protected String[] getTypeLayoutPath() {
        return new String[]{"/com/fr/design/images/chart/BarPlot/layout/0.png",
                "/com/fr/design/images/chart/BarPlot/layout/1.png",
                "/com/fr/design/images/chart/BarPlot/layout/2.png",
                "/com/fr/design/images/chart/BarPlot/layout/3.png",};
    }

    @Override
    protected String[] getTypeLayoutTipName() {
        return getNormalLayoutTipName();
    }

	/**
	 * 界面标题
     * @return 界面标题
	 */
	public String title4PopupWindow() {
        return Toolkit.i18nText("Fine-Design_Chart_Type_Column");
    }

    protected Plot getSelectedClonedPlot(){
        Chart[] barChart = ColumnIndependentChart.columnChartTypes;
        BarPlot newPlot = null;
        if(typeDemo.get(COLOMN_CHART).isPressing) {
            newPlot = (BarPlot)barChart[COLOMN_CHART].getPlot();
        }
        else if(typeDemo.get(STACK_COLOMN_CHART).isPressing) {
            newPlot = (BarPlot)barChart[STACK_COLOMN_CHART].getPlot();
        }
        else if(typeDemo.get(PERCENT_STACK_COLOMN_CHART).isPressing) {
            newPlot = (BarPlot)barChart[PERCENT_STACK_COLOMN_CHART].getPlot();
        }
        else if(typeDemo.get(THREE_D_COLOMN_CHART).isPressing) {
            newPlot = (BarPlot)barChart[THREE_D_COLOMN_CHART].getPlot();
        }
        else if(typeDemo.get(THREE_D_COLOMN_HORIZON_DRAW_CHART).isPressing) {
            newPlot = (BarPlot)barChart[THREE_D_COLOMN_HORIZON_DRAW_CHART].getPlot();
        }
        else if(typeDemo.get(THREE_D_STACK_COLOMN_CHART).isPressing) {
            newPlot = (BarPlot)barChart[THREE_D_STACK_COLOMN_CHART].getPlot();
        }
        else if(typeDemo.get(THREE_D_PERCENT_STACK_COLOMN_CHART).isPressing) {
            newPlot = (BarPlot)barChart[THREE_D_PERCENT_STACK_COLOMN_CHART].getPlot();
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
	 * 保存界面属性
	 */
	public void updateBean(Chart chart) {
	    chart.switchPlot(getSelectedClonedPlot());
		super.updateBean(chart);
	}

    @Override
    protected String getPlotTypeID() {
        return ChartConstants.COLUMN_CHART;
    }

    public Chart getDefaultChart() {
        return ColumnIndependentChart.columnChartTypes[0];
    }
}