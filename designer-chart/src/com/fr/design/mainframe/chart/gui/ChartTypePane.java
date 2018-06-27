package com.fr.design.mainframe.chart.gui;

import com.fr.base.FRContext;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartattr.SwitchState;
import com.fr.chart.charttypes.ChartTypeManager;
import com.fr.design.ChartTypeInterfaceManager;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.dialog.BasicScrollPane;
import com.fr.design.gui.frpane.UIComboBoxPane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.mainframe.chart.AbstractChartAttrPane;
import com.fr.design.mainframe.chart.ChartEditPane;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.design.mainframe.chart.gui.item.FlexibleComboBox;
import com.fr.design.mainframe.chart.gui.item.ItemEventType;
import com.fr.design.mainframe.chart.gui.type.AbstractChartTypePane;
import com.fr.general.ComparatorUtils;
import com.fr.stable.StringUtils;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

/**
 * 图表 属性表, 类型选择 界面.
* @author kunsnat E-mail:kunsnat@gmail.com
* @version 创建时间：2012-12-26 上午10:56:51
 */
public class ChartTypePane extends AbstractChartAttrPane{
	private ComboBoxPane chartTypeComBox;
	private ChartTypeButtonPane buttonPane;
    private ChartEditPane editPane;
    private ChartCollection editingCollection;
	private PaneState paneState = new PaneState();

	private class PaneState{
		//记录面板所处状态
		private SwitchState paneState = SwitchState.DEFAULT;
		//记录当前面板是谁在使用切换状态
		private String chartID = StringUtils.EMPTY;

		public SwitchState getPaneState() {
			return paneState;
		}

		public void setPaneState(SwitchState paneState) {
			this.paneState = paneState;
		}

		public String getChartID() {
			return chartID;
		}

		public void setChartID(String chartID) {
			this.chartID = chartID;
		}
	}

	@Override
	protected JPanel createContentPane() {
		JPanel content = new JPanel(new BorderLayout());
		
		buttonPane = new ChartTypeButtonPane(this);
		content.add(buttonPane, BorderLayout.NORTH);

		if (editingCollection != null) {
			relayoutChartTypePane(editingCollection);
		}else {
			chartTypeComBox = new ComboBoxPane();
		}

		BasicScrollPane scrollPane = new BasicScrollPane() {
			@Override
			protected JPanel createContentPane() {
				return chartTypeComBox;
			}

			@Override
			protected void layoutContentPane() {

				leftcontentPane = createContentPane();
				this.add(leftcontentPane);
			}

			@Override
			public void populateBean(Object ob) {
			}

			@Override
			protected String title4PopupWindow() {
				return null;
			}
		};
		content.add(scrollPane, BorderLayout.CENTER);
		
		buttonPane.setEditingChartPane(chartTypeComBox);
		
		return content;
	}

	/**
	 * 界面做为按钮时的图片位置. design_base
	 */
	public String getIconPath() {
		return "com/fr/design/images/chart/ChartType.png";
	}

	/**
	 * 界面标题
     * @return 界面标题
	 */
	public String title4PopupWindow() {
		return PaneTitleConstants.CHART_TYPE_TITLE;
	}
	
	class ComboBoxPane extends UIComboBoxPane<Chart>{
		@Override
		protected List<FurtherBasicBeanPane<? extends Chart>> initPaneList() {
			List<FurtherBasicBeanPane<? extends Chart>> paneList = new ArrayList<FurtherBasicBeanPane<? extends Chart>>();
			ChartTypeInterfaceManager.getInstance().addPlotTypePaneList(paneList);
			return paneList;
		}

		@Override
		protected String title4PopupWindow() {
			return null;
		}

		/**
		 * 不同图表切换分同一个selected的不同图表切换和不同selected的不同图表切换
		 * 如果是切换图表的某个图表发生变化，则collection的选择下标不会变
		 * 如果是切换图表的不同图表之间切换，则collection的选择下标会改变
		 * @param chart
		 */
		public void updateBean(Chart chart) {

			Plot oldPlot = chart.getPlot();
			String lastPlotID = oldPlot == null ? StringUtils.EMPTY : oldPlot.getPlotID();

            try{
                Chart newDefaultChart = (Chart)((AbstractChartTypePane)getSelectedPane()).getDefaultChart().clone();
                if(!chart.accept(newDefaultChart.getClass())){
                    //vanChart 和 chart 之间切换
                    editingCollection.removeNameObject(editingCollection.getSelectedIndex());
                    editingCollection.addChart(newDefaultChart);
                    chart = newDefaultChart;
                }
            }catch (CloneNotSupportedException e){
                FRContext.getLogger().error(e.getMessage(), e);
            }

			//这一步会替换plot
            ((AbstractChartTypePane)getSelectedPane()).updateBean(chart);

			Plot plot = chart.getPlot();

			if(plot != null){
				String plotID = plot.getPlotID();

				//plot改变的话图表类型就算改变了

				chart.setWrapperName(ChartTypeManager.getInstance().getWrapperName(plotID));

				chart.setChartImagePath(ChartTypeManager.getInstance().getChartImagePath(plotID));

				boolean isUseDefault = ChartTypeInterfaceManager.getInstance().isUseDefaultPane(plotID);

				if(editPane.isDefaultPane() != isUseDefault || (!isUseDefault && !ComparatorUtils.equals(lastPlotID, plotID))){
					editPane.reLayout(chart);
				}
			}
		}

		protected UIComboBox createComboBox() {
			return new FlexibleComboBox();
		}

		@Override
		protected void addItemChangeEvent() {
			jcb.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					FlexibleComboBox fcb = (FlexibleComboBox)jcb;
					if (fcb.isReactor()){
						return;
					}
					comboBoxItemStateChanged();
					CardLayout cl = (CardLayout)cardPane.getLayout();
					cl.show(cardPane, cardNames[jcb.getSelectedIndex()]);
				}
			});
		}

		public void relayout(ChartCollection collection){
			//重构需要重构下拉框选项和cardNames
			Chart chart = collection.getSelectedChart();
			String priority = chart.getPriority();
			String plotID = chart.getPlot().getPlotID();
			boolean enabledChart = ChartTypeManager.enabledChart(plotID);
			String item = ChartTypeInterfaceManager.getInstance().getTitle4PopupWindow(priority, plotID);

			//第一步就是重构cards
			cards.clear();
			if (enabledChart) {
				if (collection.getState() == SwitchState.DEFAULT) {
					ChartTypeInterfaceManager.getInstance().addPlotTypePaneList(cards);
				} else {
					ChartTypeInterfaceManager.getInstance().addPlotTypePaneList(priority, cards);
				}
			} else {
				ChartTypeInterfaceManager.getInstance().addPlotTypePaneList(cards, priority, plotID);
			}

			//下拉框重构开始。为了防止重构是触发update
			((FlexibleComboBox)jcb).setItemEvenType(ItemEventType.REACTOR);
			//重构下拉框选项
			cardNames = new String[cards.size()];
			cardPane.removeAll();
			jcb.removeAllItems();
			for (int i = 0; i < this.cards.size(); i++) {
				String name = this.cards.get(i).title4PopupWindow();// Name从各自的pane里面获取
				cardNames[i] = name;
				cardPane.add(this.cards.get(i), cardNames[i]);
				addComboBoxItem(cards, i);
			}
			//重新选择选中的下拉项
			jcb.setSelectedItem(item);
			jcb.setEnabled(enabledChart);
			//下拉框重构结束
			((FlexibleComboBox)jcb).setItemEvenType(ItemEventType.DEFAULT);
			//重新选中
			checkPlotPane();
		}

		private void checkPlotPane() {
			CardLayout cl = (CardLayout)cardPane.getLayout();
			cl.show(cardPane, cardNames[jcb.getSelectedIndex()]);
		}

		@Override
		public void populateBean(Chart ob) {
			for (int i = 0; i < this.cards.size(); i++) {
				FurtherBasicBeanPane pane = cards.get(i);
				if (pane.accept(ob)) {
					pane.populateBean(ob);
					Object item = pane.title4PopupWindow();
					for (int j = 0; j < cardNames.length; j++) {
						if (ComparatorUtils.equals(item, cardNames[j])) {
							jcb.setSelectedIndex(j);
						}
					}
					return;
				}
			}
		}

		@Override
		public Chart updateBean() {
			return getSelectedPane().updateBean();
		}

		@Override
		public FurtherBasicBeanPane<? extends Chart> getSelectedPane(){
			Object item = jcb.getSelectedItem();
			for (int i = 0; i < cards.size(); i++){
				if (ComparatorUtils.equals(item, cards.get(i).title4PopupWindow())){
					return cards.get(i);
				}
			}
			return cards.get(0);
		}

	}


	/**
	 * 面板重构
	 * @param lastPlotID
	 * @param collection
	 */
	public void reLayoutEditPane(String lastPlotID, ChartCollection collection){
		Chart chart = collection.getSelectedChart();
		String plotID = chart.getPlot().getPlotID();
		boolean isUseDefault = ChartTypeInterfaceManager.getInstance().isUseDefaultPane(plotID);
		if (editPane != null && editPane.isDefaultPane() != isUseDefault || (!isUseDefault && !ComparatorUtils.equals(lastPlotID, plotID))){
			editPane.reLayout(chart);
		}
	}


	public void relayoutChartTypePane(ChartCollection collection){
		if (needRelayout(collection)) {
			chartTypeComBox.relayout(collection);
			//设置面板切换状态
			updatePaneState(collection);
		}
	}

	private void updatePaneState(ChartCollection collection) {
		paneState.setChartID(collection.getRepresentChartID());
		paneState.setPaneState(collection.getState());
	}

	// TODO: 2016/11/17 因为现在populate面板时会重新构造面板，所以每次都需要重构
	private boolean needRelayout(ChartCollection collection) {
		/*return paneState.getChartID() != collection.getRepresentChartID() || paneState.getPaneState() != collection.getState();*/
		return true;
	}

	/**
	 * 更新界面属性 用于展示
	 */
	public void populate(ChartCollection collection) {
		editingCollection = collection;

		Chart chart = collection.getSelectedChart();
		this.remove(leftContentPane);
		initContentPane();

		buttonPane.populateBean(collection);
		chartTypeComBox.populateBean(chart);

		buttonPane.setVisible(ChartTypeInterfaceManager.getInstance().needChartChangePane(chart));

		this.initAllListeners();
	}

	/**
	 * 保存界面属性
	 */
	public void update(ChartCollection collection) {
        editingCollection = collection;
		buttonPane.update(collection);// 内部操作时 已经做过处理.
		Chart chart = collection.getSelectedChart();
		chartTypeComBox.updateBean(chart);
	}

    /**
     * 所有图表的类型界面
     * @return 类型界面
     */
    public FurtherBasicBeanPane[] getPaneList(){
        return chartTypeComBox.getCards().toArray(new FurtherBasicBeanPane[0]);
    }

    /**
     * 当前选中的图表的index
     * @return 当前选中的图表的index
     */
    public int getSelectedIndex(){
        return chartTypeComBox.getSelectedIndex();
    }

    /**
     * 返回选中的图表的index
     * @return 选中的图标的序号
     */
    public int getSelectedChartIndex(){
        return chartTypeComBox.getSelectedIndex();
    }

    /**
     * 设置下编辑的面板
     * @param currentEditPane 设置下编辑的面板
     */
    public void registerChartEditPane(ChartEditPane currentEditPane) {
        this.editPane = currentEditPane;
    }
}