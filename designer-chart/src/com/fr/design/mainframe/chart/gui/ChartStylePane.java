package com.fr.design.mainframe.chart.gui;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.mainframe.chart.AbstractChartAttrPane;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.design.mainframe.chart.gui.style.analysisline.ChartAnalysisLinePane;
import com.fr.design.mainframe.chart.gui.style.area.ChartAreaPane;
import com.fr.design.mainframe.chart.gui.style.axis.ChartAxisPane;
import com.fr.design.mainframe.chart.gui.style.datalabel.ChartDataLabelPane;
import com.fr.design.mainframe.chart.gui.style.datalabel.ChartLabelFontPane;
import com.fr.design.mainframe.chart.gui.style.datasheet.ChartDatasheetPane;
import com.fr.design.mainframe.chart.gui.style.legend.ChartLegendPane;
import com.fr.design.mainframe.chart.gui.style.series.ChartSeriesPane;
import com.fr.design.mainframe.chart.gui.style.title.ChartTitlePane;
import com.fr.design.mainframe.chart.gui.type.ChartTabPane;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ChartStylePane extends AbstractChartAttrPane {
	private static final long serialVersionUID = 8916444369607754491L;
	private TabPane stylePane;
	private Chart chart;
	private AttributeChangeListener listener;
    private BasicPane chartAxisPane;

	protected Chart getChart() {
		return chart;
	}

	public ChartStylePane() {
		super();
	}

	public ChartStylePane(AttributeChangeListener listener) {
		super();
		this.listener = listener;
	}

	//shine: isNeedFormula是图表设计器用的, 所以属性给删除了.
    public ChartStylePane(AttributeChangeListener listener, boolean isNeedFormula){
        super();
        this.listener = listener;
    }

	@Override
	protected JPanel createContentPane() {
		JPanel content = new JPanel(new BorderLayout());
		if (chart == null) {
			return content;
		}
		stylePane = new TabPane();
		content.add(stylePane, BorderLayout.CENTER);
		return content;
	}

	@Override
	public void populate(ChartCollection collection) {
		this.chart = collection.getSelectedChart();
		this.remove(leftContentPane);
		initContentPane();
		this.removeAttributeChangeListener();
		stylePane.populateBean(chart);
		this.addAttributeChangeListener(listener);
		this.initAllListeners();
	}

	@Override
	public void update(ChartCollection collection) {
		stylePane.updateBean(collection.getSelectedChart());
	}

	protected List<BasicPane> getPaneList() {
		List<BasicPane> paneList = new ArrayList<BasicPane>();
		Plot plot = chart.getPlot();

		paneList.add(new ChartTitlePane());

		if(plot.isSupportLegend()){
			paneList.add(new ChartLegendPane());
		} else {
			plot.setLegend(null);
		}

		if(plot.isSupportDataSeriesAttr()) {// 系列属性
			paneList.add(createChartSeriesPane());
		}

		if(plot.isSupportDataLabelAttr()) {// 数据标签
			paneList.add(new ChartDataLabelPane(ChartStylePane.this));
		}

		if(plot.isMeterPlot()){//标签属性
			paneList.add(new ChartLabelFontPane());
		}

		if(plot.isHaveAxis()) {// 然后加载坐标轴界面.
			paneList.add(createAxisPane());
			paneList.add(createAreaPane());
			if(plot.isSupportDataSheet()) {
				paneList.add(createDataSheetPane());
			}
		} else {
			paneList.add(createAreaPane());
		}

		if(plot.needAnalysisLinePane()){//分析线型界面
			paneList.add(new ChartAnalysisLinePane(ChartStylePane.this));
		}
		return paneList;
	}

	protected ChartSeriesPane createChartSeriesPane() {
		return new ChartSeriesPane(ChartStylePane.this);
	}

	private BasicPane createDataSheetPane() {
		ChartDatasheetPane chartDatasheetPane = new ChartDatasheetPane();
		chartDatasheetPane.useWithAxis((ChartAxisPane)chartAxisPane);
		return chartDatasheetPane;
	}

	private BasicPane createAxisPane() {
		return chartAxisPane = new ChartAxisPane(chart.getPlot(),ChartStylePane.this);
	}

	private BasicPane createAreaPane() {
		return new ChartAreaPane(chart.getPlot(),ChartStylePane.this);
	}

	/**
	 * 返回对应的图片路径.
	 */
	public String getIconPath() {
		return "com/fr/design/images/chart/ChartStyle.png";
	}

	/**
	 * 返回界面的标题名称
     * @return  返回名称.
	 */
	public String title4PopupWindow() {
		return PaneTitleConstants.CHART_STYLE_TITLE;
	}

	/**
	 * 设置选中的界面id
	 */
	public void setSelectedByIds(int level, String... id) {
        stylePane.setSelectedByIds(level, id);
    }

	class TabPane extends ChartTabPane {

		@Override
		protected void tabChanged() {
			ChartStylePane.this.removeAttributeChangeListener();
			((BasicBeanPane<Chart>) paneList.get(tabPane.getSelectedIndex())).populateBean(chart);
			ChartStylePane.this.addAttributeChangeListener(listener);
		}

		@Override
		protected List<BasicPane> initPaneList() {
			return ChartStylePane.this.getPaneList();
		}

		@Override
		public void populateBean(Chart chart) {
			if (chart == null || stylePane.getSelectedIndex() == -1) {
				return;
			}
			((BasicBeanPane<Chart>) paneList.get(stylePane.getSelectedIndex())).populateBean(chart);
		}

		@Override
		public Chart updateBean() {
			if (chart == null) {
				return null;
			}
			((BasicBeanPane<Chart>) paneList.get(stylePane.getSelectedIndex())).updateBean(chart);
			return chart;
		}

		@Override
		public void updateBean(Chart ob) {
			((BasicBeanPane<Chart>) paneList.get(stylePane.getSelectedIndex())).updateBean(ob);
		}
	}
}