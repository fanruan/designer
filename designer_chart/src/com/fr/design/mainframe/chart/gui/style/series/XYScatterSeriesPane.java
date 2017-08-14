package com.fr.design.mainframe.chart.gui.style.series;

import com.fr.chart.base.AttrColor;
import com.fr.chart.base.AttrLineStyle;
import com.fr.chart.base.AttrMarkerType;
import com.fr.chart.base.DataSeriesCondition;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartattr.XYScatterPlot;
import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.chart.chartglyph.Marker;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.general.ComparatorUtils;
import com.fr.plugin.chart.ConfigHelper;

/**
 * 散点图 属性表 系列界面.
* @author kunsnat E-mail:kunsnat@gmail.com
* @version 创建时间：2013-1-23 上午10:34:22
 */
public class XYScatterSeriesPane extends LineSeriesPane {

	public XYScatterSeriesPane(ChartStylePane parent, Plot plot) {
		super(parent, plot);
	}

	/**
	 * 更新散点图系列界面属性
	 */
	@Override
	public void populateBean(Plot plot) {
		super.populateBean(plot);
		isCurve.setSelected(((XYScatterPlot)plot).isCurve());
	}

	/**
	 * 保存系列界面属性.
	 */
	@Override
	public void updateBean(Plot plot) {
		super.updateBean(plot);
		((XYScatterPlot)plot).setCurve(isCurve.isSelected());
	}
	
	protected void updateAttrCondition(ConditionAttr attrList) {
		DataSeriesCondition attr = attrList.getExisted(AttrLineStyle.class);
		if(attr != null) {
			attrList.remove(attr);
		}
		attrList.addDataSeriesCondition(new AttrLineStyle(lineStyle.getSelectedLineStyle()));

		attr = attrList.getExisted(AttrColor.class);
		if(attr != null) {
			attrList.remove(attr);
		}

		attr = attrList.getExisted(AttrMarkerType.class);
		if(attr != null) {
			attrList.remove(attr);
		}
		
		if(!ComparatorUtils.equals(markerPane.getSelectedMarkder().getMarkerType(), ConfigHelper.NULL_M)){
			attrList.addDataSeriesCondition(new AttrMarkerType(markerPane.getSelectedMarkder().getMarkerType()));
		}
	}
}