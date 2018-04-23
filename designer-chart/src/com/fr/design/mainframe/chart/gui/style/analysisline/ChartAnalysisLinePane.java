package com.fr.design.mainframe.chart.gui.style.analysisline;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JSeparator;

import com.fr.design.chart.axis.ChartAlertValueInTopBottomPane;
import com.fr.design.chart.axis.ChartAlertValuePane;
import com.fr.chart.base.AttrColor;
import com.fr.chart.base.AttrTrendLine;
import com.fr.chart.base.ConditionTrendLine;
import com.fr.chart.base.LineStyleInfo;
import com.fr.chart.base.TrendLine;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartAlertValue;
import com.fr.chart.chartattr.NumberAxis;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartattr.ValueAxis;
import com.fr.chart.chartattr.XYPlot;
import com.fr.chart.chartglyph.ConditionCollection;
import com.fr.design.chart.series.SeriesCondition.ConditionTrendLinePane;
import com.fr.design.gui.frpane.UICorrelationComboBoxPane;
import com.fr.design.gui.imenutable.UIMenuNameableCreator;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.dialog.BasicScrollPane;
import com.fr.general.Inter;
import com.fr.stable.Constants;

/**
 * 这个面板是把原来值轴那边的警戒线和系列那边的趋势线分离出来
 * @author eason
 *
 */
public class ChartAnalysisLinePane extends BasicScrollPane<Chart>{
	
	private static final long serialVersionUID = 5789092642531951208L;

	private Chart chart;
	private ChartStylePane parent;
	private UICorrelationComboBoxPane trendLinePane;
	private UICorrelationComboBoxPane xAlertPane;
	private UICorrelationComboBoxPane yAlertPane;
	
	private JPanel contentPane;
	
	public ChartAnalysisLinePane(ChartStylePane parent){
		super();
		this.parent = parent;
	}
	protected JPanel createContentPane() {
		if(chart == null) {
			return new JPanel();
		} 
		JPanel trendLine = null;
		JPanel firstAlertPane = null;
		JPanel secondAlertPane = null;
		Plot plot = chart.getPlot();
		if(plot.isSupportTrendLine()){
			this.createTrendLinePane();
			double p = TableLayout.PREFERRED;
			double f = TableLayout.FILL;
			double[] row = {p};
			double[] col = {f};
			trendLine = TableLayoutHelper.createTableLayoutPane4Chart(new String[]{"Chart_TrendLine"}, new Component[][]{new Component[]{trendLinePane}}, row, col);
		}
		//最多有两条坐标轴是值类型的
		if(plot.getAlertLinePaneTitle().length == 2){
			String[] title = plot.getAlertLinePaneTitle();
			firstAlertPane = this.createAlertLinePane(new String[]{title[0], "ChartF-Alert-Line"}, true);
			secondAlertPane = this.createAlertLinePane(new String[]{title[1], "ChartF-Alert-Line"}, false);
		}else if(plot.getyAxis() instanceof ValueAxis){
			firstAlertPane = this.createAlertLinePane(new String[]{"ChartF-Alert-Line"}, true);
		}
		Component[][] component = null;;
		if(trendLine != null){
			if(firstAlertPane != null){
				component = new Component[][]{
						new Component[]{trendLine},
						new Component[]{new JSeparator()},
						new Component[]{firstAlertPane},
						new Component[]{secondAlertPane}
				};
			}else{
				component = new Component[][]{
						new Component[]{firstAlertPane},
						new Component[]{secondAlertPane},
				};
			}
		}else{
			component = new Component[][]{
					new Component[]{firstAlertPane},
					new Component[]{secondAlertPane},
			};
		}
		double p = TableLayout.PREFERRED;
		double f = TableLayout.FILL;
		double[] rowSize = {p,p,p,p};
		double[] colSize = {f};
		return TableLayoutHelper.createTableLayoutPane(component, rowSize, colSize);
	}
	
	
	private JPanel createAlertLinePane(String[] title, boolean first){
		List<UIMenuNameableCreator> list = new ArrayList<UIMenuNameableCreator>();
    	list.add(new UIMenuNameableCreator(Inter.getLocText("ChartF-Alert-Line"), new ChartAlertValue(), ChartAlertValuePane.class));
    	if(first){
    		xAlertPane = new UICorrelationComboBoxPane(list);
    	}else{
    		yAlertPane = new UICorrelationComboBoxPane(list);
    	}
    	
		double p = TableLayout.PREFERRED;
		double f = TableLayout.FILL;
		double[] row = {p};
		double[] col = {f};
		
		return TableLayoutHelper.createTableLayoutPane4Chart(title,new Component[][]{new Component[]{(first ? xAlertPane : yAlertPane)}}, row, col);
	}
	
	
	public void populateBean(Chart ob) {
		this.chart = ob;
		
		Plot plot = chart.getPlot();
		if(plot == null){
			return;
		}
		
		if(contentPane == null){
			this.remove(leftcontentPane);
			layoutContentPane();
			parent.initAllListeners();
		}
		
		//更新趋势线
		if(this.trendLinePane != null && plot.isSupportTrendLine()){
			ConditionCollection collection = plot.getConditionCollection();
			AttrTrendLine trendLineList = (AttrTrendLine)collection.getDefaultAttr().getExisted(AttrTrendLine.class);
			popTrendLine(trendLineList);
		}
		
		//更新警戒线
		if(plot.getAlertLinePaneTitle().length != 0){
			if(plot instanceof XYPlot){
				popuAlert((NumberAxis)plot.getxAxis(), xAlertPane);
				popuAlert((NumberAxis)plot.getyAxis(), yAlertPane);
			}else{
				popuAlert((NumberAxis)plot.getyAxis(), xAlertPane);
				popuAlert((NumberAxis)plot.getSecondAxis(), yAlertPane);
			}
		}else if(xAlertPane != null){
			popuAlert((NumberAxis)plot.getyAxis(), xAlertPane);
		}
	}
	
	private void popuAlert(NumberAxis numberAxis, UICorrelationComboBoxPane editAlertPane) {
		boolean useLeftRight = numberAxis.getPosition() == Constants.LEFT || numberAxis.getPosition() == Constants.RIGHT;
		List<UIMenuNameableCreator> menuList = new ArrayList<UIMenuNameableCreator>();
		if(useLeftRight) {
			menuList.add(new UIMenuNameableCreator(Inter.getLocText("ChartF-Alert-Line"), new ChartAlertValue(), ChartAlertValuePane.class));
		} else {
			menuList.add(new UIMenuNameableCreator(Inter.getLocText("ChartF-Alert-Line"), new ChartAlertValue(), ChartAlertValueInTopBottomPane.class));
		}
		editAlertPane.refreshMenuAndAddMenuAction(menuList);
		
		List<UIMenuNameableCreator> list = new ArrayList<UIMenuNameableCreator>();
		ChartAlertValue[] values = numberAxis.getAlertValues();
		for(int i = 0; i < values.length; i++) {
			ChartAlertValue value = values[i];
			if(useLeftRight) {
				list.add(new UIMenuNameableCreator(value.getAlertPaneSelectName(), value, ChartAlertValuePane.class));
			} else {
				list.add(new UIMenuNameableCreator(value.getAlertPaneSelectName(), value, ChartAlertValueInTopBottomPane.class));
			}
		}
		
		editAlertPane.populateBean(list);
		editAlertPane.doLayout();
	}
	
	
	public void updateBean(Chart chart) {
		if(chart == null || chart.getPlot() == null) {
			return;
		}
		
		Plot plot = chart.getPlot();
		if(plot.isSupportTrendLine()) {
			if(trendLinePane != null) {
				ConditionCollection collection = plot.getConditionCollection();
				
				AttrTrendLine trendLineList = (AttrTrendLine)collection.getDefaultAttr().getExisted(AttrTrendLine.class);
				
				if(trendLineList == null) {
					trendLineList = new AttrTrendLine();
					collection.getDefaultAttr().addDataSeriesCondition(trendLineList);
				}
				upTrendLine(trendLineList);
			}
		}
		
		if(plot.getAlertLinePaneTitle().length != 0){
			if(plot instanceof XYPlot){
				updateAlert((NumberAxis)plot.getxAxis(), xAlertPane);
				updateAlert((NumberAxis)plot.getyAxis(), yAlertPane);
			}else{
				updateAlert((NumberAxis)plot.getyAxis(), xAlertPane);
				updateAlert((NumberAxis)plot.getSecondAxis(), yAlertPane);
			}
		}else if(xAlertPane != null){
			updateAlert((NumberAxis)plot.getyAxis(), xAlertPane);
		}
	}
	
	private void updateAlert(NumberAxis numberAxis, UICorrelationComboBoxPane editAlertPane) {
		if(editAlertPane != null) {
			List<ChartAlertValue> alerts = new ArrayList<ChartAlertValue>();
			
			List<UIMenuNameableCreator> list = editAlertPane.updateBean();
			for(int i = 0; i < list.size(); i++) {
				UIMenuNameableCreator nameC = (UIMenuNameableCreator)list.get(i);
				ChartAlertValue value = (ChartAlertValue)nameC.getObj();
				value.setAlertPaneSelectName(nameC.getName());
				alerts.add(value);
			}
			numberAxis.setAlertValues(alerts.toArray(new ChartAlertValue[alerts.size()]));
		}
	}
	
	
	private void upTrendLine(AttrTrendLine trendLineList) {
		trendLineList.clear();
		List list = trendLinePane.updateBean();
		for (int i = 0; i < list.size(); i++) {
			UIMenuNameableCreator nameObject = (UIMenuNameableCreator)list.get(i);
			
			ConditionTrendLine trendLine = (ConditionTrendLine)nameObject.getObj();
			trendLine.setPaneName(nameObject.getName());
			trendLineList.add(trendLine);
		}
	}
	
	private void popTrendLine(AttrTrendLine trendLineList) {
		if(trendLineList != null && trendLineList.size() > 0) {
			
			List<UIMenuNameableCreator> nameObjectList = new ArrayList<UIMenuNameableCreator>();
			
			for(int i = 0; i < trendLineList.size(); i++) {
				ConditionTrendLine value = trendLineList.get(i);
				nameObjectList.add(new UIMenuNameableCreator(value.getPaneName(), value, ConditionTrendLinePane.class));
			}
			
			if(!nameObjectList.isEmpty()) { 
				trendLinePane.populateBean(nameObjectList);
			}
		}
	}
	
	/**
	 * 添加趋势线界面
	 */
	protected void createTrendLinePane() {
		List<UIMenuNameableCreator> list = new ArrayList<UIMenuNameableCreator>();
		ConditionTrendLine conditionLine = new ConditionTrendLine();
		TrendLine trendLine = new TrendLine();
		LineStyleInfo lineInfo = new LineStyleInfo();
		lineInfo.setAttrLineColor(new AttrColor(Color.gray));
		trendLine.setLineStyleInfo(lineInfo);
		conditionLine.setLine(trendLine);
		list.add(new UIMenuNameableCreator(Inter.getLocText("Chart_TrendLine"), conditionLine, ConditionTrendLinePane.class));
		trendLinePane = new UICorrelationComboBoxPane(list);
		
	}
	
	protected String title4PopupWindow() {
		return PaneTitleConstants.CHART_STYLE_LINE_TITLE;
	}

}