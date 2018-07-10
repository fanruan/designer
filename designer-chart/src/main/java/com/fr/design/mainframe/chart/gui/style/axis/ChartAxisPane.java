package com.fr.design.mainframe.chart.gui.style.axis;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.design.chart.ChartPlotFactory;
import com.fr.design.chart.axis.AxisStyleObject;
import com.fr.design.chart.axis.ChartStyleAxisPane;
import com.fr.design.gui.frpane.AbstractAttrNoScrollPane;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.style.ThirdTabPane;
import com.fr.design.mainframe.chart.gui.style.legend.AutoSelectedPane;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.ComparatorUtils;

import java.util.ArrayList;
import java.util.List;

public class ChartAxisPane extends ThirdTabPane<Chart> implements AutoSelectedPane {
	
	private static final long serialVersionUID = 6676169442451992286L;
	private static final int PANE_WIDTH = 220;
	protected ChartAxisUsePane first;
	protected ChartAxisUsePane second;
	protected ChartAxisUsePane third;

	public ChartAxisPane(Plot plot, ChartStylePane parent) {
		super(plot, parent);
	}

	@Override
	protected List<NamePane> initPaneList(Plot plot, AbstractAttrNoScrollPane parent) {
		List<NamePane> paneList = new ArrayList<NamePane>();
		
		ChartStyleAxisPane axisStylePane = ChartPlotFactory.createChartStyleAxisPaneByPlot(plot);
		AxisStyleObject[] objs = axisStylePane.createAxisStyleObjects(plot);
		// 加载 对应的Plot 坐标轴界面 // TODO 之前的, 在右键 删除之后, 可以全部替换.
		for(int i = 0; i < objs.length; i++) {
			ChartAxisUsePane usePane = objs[i].getAxisStylePane();
			
			if(i == 0) {
				first = usePane;
			} else if(i == 1) {
				second = usePane;
			} else if(i == 2) {
				third = usePane;
			}
			paneList.add(new NamePane(objs[i].getName(), usePane));
		}
		
		return paneList;
	}
	
	/**
	 * 根据数据表 检查坐标轴是否可用
	 */
	public void checkUseWithDataSheet(boolean use) {
		if(first instanceof ChartCategoryPane) {
			GUICoreUtils.setEnabled(first, use);
		}
	}
	
	@Override
	protected int getContentPaneWidth() {
		return PANE_WIDTH;
	}

	@Override
	public String title4PopupWindow() {
		return PaneTitleConstants.CHART_STYLE_AXIS_TITLE;
	}

	protected int getOffset4TabPane() {
		return 3;
	}

	@Override
	public void populateBean(Chart chart) {
		if(chart == null) {
			return;
		}
		Plot plot = chart.getPlot();
		if(plot == null ) {
			return;
		}
		if(first != null) {
			if(first instanceof ChartCategoryPane) {
				GUICoreUtils.setEnabled(first, !(plot.isSupportDataSheet() && plot.getDataSheet() != null && plot.getDataSheet().isVisible()));
			}
			
			first.populateBean(plot.getxAxis(), plot);
		}
		if(second != null) {
			second.populateBean(plot.getyAxis(), plot);
		}
		if(third != null) {
			third.populateBean(plot.getSecondAxis(), plot);
		}
	}

	@Override
	public void updateBean(Chart chart) {
		if(chart == null || chart.getPlot() == null) {
			return;
		}
		Plot plot = chart.getPlot();
		
		if(first != null) {
			first.updateBean(plot.getxAxis(), plot);
		}
		if(second != null) {
			second.updateBean(plot.getyAxis(), plot);
		}
		if(third != null) {
			third.updateBean(plot.getSecondAxis(), plot);
		}
	}

	@Override
	public void setSelectedIndex(String id) {
		int idx = 0;
		if (ComparatorUtils.equals(id, Plot.Y_AXIS)) {
			idx = 1;
		} else if (ComparatorUtils.equals(id, Plot.SECOND_AXIS)) {
			idx = 2;
		}
		if (paneList.size() > idx && tabPane != null) {
			tabPane.setSelectedIndex(idx);
		}
	}
}