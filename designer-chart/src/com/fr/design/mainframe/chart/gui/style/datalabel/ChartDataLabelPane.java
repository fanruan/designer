package com.fr.design.mainframe.chart.gui.style.datalabel;

import com.fr.chart.base.AttrContents;
import com.fr.chart.base.DataSeriesCondition;
import com.fr.chart.chartattr.*;
import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.style.ChartDataPointLabel4GisPane;
import com.fr.design.mainframe.chart.gui.style.ChartDataPointLabel4MapPane;
import com.fr.design.mainframe.chart.gui.style.ChartDatapointLabelPane;
import com.fr.design.dialog.BasicScrollPane;
import com.fr.general.Inter;
import com.fr.stable.Constants;

import javax.swing.*;
import java.awt.*;

/**
 * 这个面板是从系列那边分出来的
 * @author eason
 *
 */
public class ChartDataLabelPane extends BasicScrollPane<Chart>{
	private ChartDatapointLabelPane labelPane;
	private Chart chart;
	private ChartStylePane parent;
	private static final long serialVersionUID = -5449293740965811991L;

	
	public ChartDataLabelPane(ChartStylePane parent){
		super();
		this.parent = parent;
	}
	
	@Override
	protected JPanel createContentPane() {
		JPanel contentPane = new JPanel(new BorderLayout());
		if(chart == null) {
			return contentPane;
		} 
		labelPane = getLabelPane();
		contentPane.add(labelPane, BorderLayout.NORTH);
		return contentPane;
	}

	@Override
	public void populateBean(Chart chart) {
		this.chart = chart;
		
		if(labelPane == null){
			this.remove(leftcontentPane);
			layoutContentPane();
			parent.initAllListeners();
		}
		
		Plot plot = this.chart.getPlot();
		ConditionAttr attrList = plot.getConditionCollection().getDefaultAttr();
		DataSeriesCondition attr = attrList.getExisted(AttrContents.class);
		if(labelPane != null) {
			labelPane.populate(attr);
		}
	}
	
	public void updateBean(Chart chart) {
		if(chart == null) {
			return;
		}
		ConditionAttr attrList = chart.getPlot().getConditionCollection().getDefaultAttr();
		DataSeriesCondition attr = attrList.getExisted(AttrContents.class);
		if(attr != null) {
			attrList.remove(attr);
		}
		AttrContents attrContents = labelPane.update();
		attrList.addDataSeriesCondition(attrContents);
	}

	@Override
	protected String title4PopupWindow() {
		return PaneTitleConstants.CHART_STYLE_LABEL_TITLE;
	}

    // 代码整理.
	protected ChartDatapointLabelPane getLabelPane() {
        if(chart.getPlot().isMapKindLabel()) {
            return new ChartDataPointLabel4MapPane(parent);
        }else if(chart.getPlot().isGisKindLabel()){
			return new ChartDataPointLabel4GisPane(parent);
		}
        return new ChartDatapointLabelPane(getLabelLocationNameArray(), getLabelLocationValueArray(), chart.getPlot(), parent);
	}
	
	protected String[] getLabelLocationNameArray() {
		Plot plot = chart.getPlot();
		if(plot instanceof BarPlot){
			return new String[] {Inter.getLocText("BarInside"), Inter.getLocText("BarOutSide"), Inter.getLocText("Center")};
		}else if(plot instanceof PiePlot){
            return new String[] {Inter.getLocText("Chart_In_Pie"), Inter.getLocText("Chart_Out_Pie")};
		}else if(plot instanceof RangePlot){
			return new String[]{Inter.getLocText("StyleAlignment-Top"), Inter.getLocText("StyleAlignment-Bottom"), Inter.getLocText("Center")};
		}else if(plot instanceof BubblePlot){
			return new String[] {Inter.getLocText("Chart_Bubble_Inside"), Inter.getLocText("Chart_Bubble_Outside")};
		}else{
			return new String[0];
		}
		
	}

	protected Integer[] getLabelLocationValueArray() {
		Plot plot = chart.getPlot();
		if(plot instanceof BarPlot){
			return new Integer[] {Constants.INSIDE, Constants.OUTSIDE, Constants.CENTER};
		}else if(plot instanceof PiePlot){
            return  new Integer[] {Constants.INSIDE, Constants.OUTSIDE};
		}else if(plot instanceof RangePlot){
			return new Integer[] {Constants.TOP, Constants.BOTTOM, Constants.CENTER};
		}else if(plot instanceof BubblePlot){
			return new Integer[] {Constants.INSIDE, Constants.OUTSIDE};
		}else{
			return new Integer[0];
		}
		
	
	}
}