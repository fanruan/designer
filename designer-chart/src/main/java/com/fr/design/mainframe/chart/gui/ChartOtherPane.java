package com.fr.design.mainframe.chart.gui;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartattr.Plot;
import com.fr.chartx.config.info.constant.ConfigType;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.dialog.BasicPane;
import com.fr.design.mainframe.chart.AbstractChartAttrPane;
import com.fr.design.mainframe.chart.ChartEditPane;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.design.mainframe.chart.gui.other.ChartConditionAttrPane;
import com.fr.design.mainframe.chart.gui.other.ChartInteractivePane;
import com.fr.design.mainframe.chart.gui.type.ChartTabPane;
import com.fr.design.mainframe.chart.info.ChartInfoCollector;

import javax.swing.JPanel;
import java.util.ArrayList;
import java.util.List;
import java.awt.BorderLayout;

public class ChartOtherPane extends AbstractChartAttrPane {
	private static final long serialVersionUID = -5612046386597783406L;
	protected TabPane otherPane;
	protected boolean hasCondition = false;

	@Override
	protected JPanel createContentPane() {
		JPanel content = new JPanel(new BorderLayout());
		otherPane = new TabPane();
		content.add(otherPane, BorderLayout.CENTER);
		return content;
	}

	@Override
	public void populate(ChartCollection collection) {
		Plot plot = collection.getSelectedChart().getPlot();
        hasCondition = plot.isSupportDataSeriesCondition();

        this.removeAll();
        initAll();
        this.validate();// kunsnat: 重新激活面板. 不然事件 滚动条等不响应.

		otherPane.populateBean(collection.getSelectedChart());
	}

	@Override
	public void update(ChartCollection collection) {
		otherPane.updateBean(collection.getSelectedChart());
	}

	protected BasicBeanPane<Chart> createInteractivePane() {
		return new ChartInteractivePane(ChartOtherPane.this);
	}

	protected BasicBeanPane<Chart> createConditionAttrPane() {
		return new ChartConditionAttrPane();
	}
	/**
	 * 注册 切换按钮的 改变事件, 和超链区分.
     * @param currentChartEditPane  当前图表编辑界面.
	 */
	public void registerChartEditPane(ChartEditPane currentChartEditPane) {
		otherPane.registerChartEditPane(currentChartEditPane);
	}

	@Override
	public String getIconPath() {
		return "com/fr/design/images/chart/InterAttr.png";
	}

	/**
	 * 界面标题
	 * @return 返回标题.
	 */
	public String title4PopupWindow() {
		return PaneTitleConstants.CHART_OTHER_TITLE;
	}
	
	private boolean isHaveCondition() {
		return hasCondition;
	}
	
	/**
	 * 设置选中的界面id
	 */
	public void setSelectedByIds(int level, String... id) {
		otherPane.setSelectedByIds(level, id);
    }

	protected class TabPane extends ChartTabPane {
		protected BasicBeanPane<Chart> interactivePane;
		protected BasicBeanPane<Chart> conditionAttrPane;

		@Override
		protected List<BasicPane> initPaneList() {
			List<BasicPane> paneList = new ArrayList<BasicPane>();
			interactivePane = ChartOtherPane.this.createInteractivePane();

            paneList.add(interactivePane);

			if (ChartOtherPane.this.isHaveCondition()) {
				conditionAttrPane = ChartOtherPane.this.createConditionAttrPane();
				paneList.add(conditionAttrPane);
			}
			return paneList;
		}

		@Override
		public void populateBean(Chart chart) {
            interactivePane.populateBean(chart);
			if (ChartOtherPane.this.isHaveCondition()) {
				conditionAttrPane.populateBean(chart);
			}
		}

		@Override
		public void updateBean(Chart chart) {
			if(chart == null) {
				return;
			}
            interactivePane.updateBean(chart);
			if (ChartOtherPane.this.isHaveCondition()) {
				conditionAttrPane.updateBean(chart);
			}
			//特效埋点
			ChartInfoCollector.getInstance().updateChartConfig(chart, ConfigType.EFFECT, chart.getBuryingPointEffectConfig());
		}
		
		/**
		 * 注册 切换按钮的切换事件.
		 * @param currentChartEditPane 当前编辑的图表编辑界面.
		 */
		public void registerChartEditPane(ChartEditPane currentChartEditPane) {
//			chartSwitchPane.registerChartEditPane(currentChartEditPane);
		}

		@Override
		public Chart updateBean() {
			return null;
		}
	}
}