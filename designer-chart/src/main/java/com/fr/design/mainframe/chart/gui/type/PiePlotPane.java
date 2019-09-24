package com.fr.design.mainframe.chart.gui.type;

import com.fr.base.CoreDecimalFormat;
import com.fr.chart.base.AttrBorder;
import com.fr.chart.base.AttrContents;
import com.fr.chart.base.ChartConstants;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Pie3DPlot;
import com.fr.chart.chartattr.PiePlot;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartglyph.ConditionCollection;
import com.fr.chart.charttypes.PieIndependentChart;
import com.fr.design.i18n.Toolkit;
import com.fr.stable.Constants;

import java.text.DecimalFormat;
import java.awt.Color;

/**
 * 饼图 属性表 选择类型 布局界面.
* @author kunsnat E-mail:kunsnat@gmail.com
* @version 创建时间：2012-12-25 下午06:55:33
 */
public class PiePlotPane extends AbstractDeprecatedChartTypePane {
    private static final long serialVersionUID = -601566194238908115L;

	private static final int PIE_CHART = 0;
	private static final int THREE_D_PIE_CHART = 1;

    @Override
    protected String[] getTypeIconPath() {
        return new String[]{"/com/fr/design/images/chart/PiePlot/type/0.png",
                "/com/fr/design/images/chart/PiePlot/type/1.png",
        };
    }

	@Override
    protected String[] getTypeLayoutPath() {
        return new String[]{"/com/fr/design/images/chart/PiePlot/layout/0.png",
                "/com/fr/design/images/chart/PiePlot/layout/1.png",
                "/com/fr/design/images/chart/PiePlot/layout/2.png",
                "/com/fr/design/images/chart/PiePlot/layout/3.png",
        };
    }

	@Override
	protected String[] getTypeLayoutTipName() {
		return getNormalLayoutTipName();
	}


	/**
	 * 返回界面标题
     * @return 界面标题
	 */
	public String title4PopupWindow() {
		return Toolkit.i18nText("Fine-Design_Chart_Type_Pie");
	}

    private void createPieCondition(Plot plot) {
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

	/**
	 * 更新界面内容
	 */
	public void populateBean(Chart chart) {
        super.populateBean(chart);
		for(ChartImagePane imagePane : typeDemo) {
			imagePane.isPressing = false;
		}
		Plot plot = chart.getPlot();
		if(plot instanceof Pie3DPlot) {
			typeDemo.get(THREE_D_PIE_CHART).isPressing = true;
		} else {
			typeDemo.get(PIE_CHART).isPressing = true;
		}

		checkDemosBackground();
	}

    protected Plot getSelectedClonedPlot(){
        Plot newPlot;
        if(typeDemo.get(PIE_CHART).isPressing) {
            newPlot = new PiePlot();
        } else {
            newPlot = new Pie3DPlot();
        }
        createPieCondition(newPlot);
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
		return ChartConstants.PIE_CHART;
	}

    public Chart getDefaultChart() {
        return PieIndependentChart.pieChartTypes[0];
    }
}