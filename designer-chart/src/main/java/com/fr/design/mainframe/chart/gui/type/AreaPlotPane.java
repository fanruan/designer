package com.fr.design.mainframe.chart.gui.type;

import com.fr.chart.base.AttrAlpha;
import com.fr.chart.base.ChartConstants;
import com.fr.chart.chartattr.Area3DPlot;
import com.fr.chart.chartattr.AreaPlot;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartglyph.ConditionCollection;
import com.fr.chart.charttypes.AreaIndependentChart;
import com.fr.design.i18n.Toolkit;


/**
 * 面积图 属性表 选择类型 布局 界面.
* @author kunsnat E-mail:kunsnat@gmail.com
* @version 创建时间：2012-12-25 下午06:57:36
 */
public class AreaPlotPane extends AbstractDeprecatedChartTypePane {

	private static final int STACK_AREA_CHART = 0;
	private static final int PERCENT_AREA_LINE_CHART = 1;
	private static final int STACK_3D_AREA_CHART = 2;
	private static final int PERCENT_3D_AREA_LINE_CHART = 3;

	@Override
	protected String[] getTypeIconPath() {
        return new String[]{"/com/fr/design/images/chart/AreaPlot/type/0.png",
                "/com/fr/design/images/chart/AreaPlot/type/1.png",
                "/com/fr/design/images/chart/AreaPlot/type/2.png",
                "/com/fr/design/images/chart/AreaPlot/type/3.png",
		};
	}

	@Override
	protected String[] getTypeTipName() {
		String area = Toolkit.i18nText("Fine-Design_Chart_Type_Area");
		String stack = Toolkit.i18nText("Fine-Design_Chart_Stacked");
		String percent = Toolkit.i18nText("Fine-Design_Chart_Use_Percent");
		String td = Toolkit.i18nText("Fine-Design_Chart_3D");
		return new String[]{
				stack + area,
				percent + stack + area,
				td + stack + area,
				td + percent + stack + area
		};
	}

	@Override
	protected String getPlotTypeID() {
		return ChartConstants.AREA_CHART;
	}

	protected String[] getTypeLayoutPath() {
		return new String[]{"/com/fr/design/images/chart/AreaPlot/layout/0.png",
                "/com/fr/design/images/chart/AreaPlot/layout/1.png",
                "/com/fr/design/images/chart/AreaPlot/layout/2.png",
                "/com/fr/design/images/chart/AreaPlot/layout/3.png",
        };
	}

	@Override
	protected String[] getTypeLayoutTipName() {
		return getNormalLayoutTipName();
	}

	/**
	 * 更新界面 内容
	 */
	public void populateBean(Chart chart) {
		super.populateBean(chart);
		Plot plot = chart.getPlot();
		if(plot instanceof AreaPlot) {
			AreaPlot area = (AreaPlot)plot;
			if(area.isStacked()) {
				if(area.getyAxis().isPercentage()) {
					typeDemo.get(PERCENT_AREA_LINE_CHART).isPressing = true;
				} else {
					typeDemo.get(STACK_AREA_CHART).isPressing = true;
				}
			}
		} else if(plot instanceof Area3DPlot) {
			Area3DPlot threeDPlot = (Area3DPlot)plot;
			if(threeDPlot.isStacked()) {
				if(threeDPlot.getyAxis().isPercentage()) {
					typeDemo.get(PERCENT_3D_AREA_LINE_CHART).isPressing = true;
				} else {
					typeDemo.get(STACK_3D_AREA_CHART).isPressing = true;
				}
			}
		}

		checkDemosBackground();
	}

    protected Plot getSelectedClonedPlot(){
        Plot plot = null;
        if(typeDemo.get(STACK_AREA_CHART).isPressing) {
            plot = new AreaPlot();
            ((AreaPlot)plot).setStacked(true);
        }
        else if(typeDemo.get(PERCENT_AREA_LINE_CHART).isPressing) {
            plot = new AreaPlot();
            ((AreaPlot)plot).setStacked(true);
            ((AreaPlot)plot).getyAxis().setPercentage(true);
        }
        else if(typeDemo.get(STACK_3D_AREA_CHART).isPressing) {
            plot = new Area3DPlot();
            ((Area3DPlot)plot).setStacked(true);
        }
        else if(typeDemo.get(PERCENT_3D_AREA_LINE_CHART).isPressing) {
            plot = new Area3DPlot();
            ((Area3DPlot)plot).setStacked(true);
            ((Area3DPlot)plot).getyAxis().setPercentage(true);
        }
        if(plot != null) {
			createAreaCondition(plot);
		}
        return plot;
    }

	/**
	 * 保存界面属性
	 */
	public void updateBean(Chart chart) {
		chart.switchPlot(getSelectedClonedPlot());
		super.updateBean(chart);
	}

	private void createAreaCondition(Plot plot) {
		ConditionCollection collection = plot.getConditionCollection();
		AttrAlpha alpha = (AttrAlpha) collection.getDefaultAttr().getExisted(AttrAlpha.class);
		if (alpha == null) {
			alpha = new AttrAlpha();
			collection.getDefaultAttr().addDataSeriesCondition(alpha);
		}
		alpha.setAlpha(0.7f);
	}

	/**
	 * 界面标题
     * @return  界面标题
	 */
	public String title4PopupWindow() {
		return Toolkit.i18nText("Fine-Design_Chart_Type_Area");
	}

    public Chart getDefaultChart() {
        return AreaIndependentChart.areaChartTypes[0];
    }
}