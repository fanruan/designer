package com.fr.design.mainframe.chart.gui;

import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.charttypes.ChartTypeManager;
import com.fr.chartx.attr.ChartProvider;
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
import com.fr.log.FineLoggerFactory;
import com.fr.stable.StringUtils;

import javax.swing.JPanel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

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

	class ComboBoxPane extends UIComboBoxPane<ChartProvider> {
		private Map<String, Map<String, FurtherBasicBeanPane<? extends ChartProvider>>> allChartTypePane;

		@Override
		protected List<FurtherBasicBeanPane<? extends ChartProvider>> initPaneList() {
			List<FurtherBasicBeanPane<? extends ChartProvider>> paneList = new ArrayList<FurtherBasicBeanPane<? extends ChartProvider>>();
			allChartTypePane = new LinkedHashMap<String, Map<String, FurtherBasicBeanPane<? extends ChartProvider>>>();
			ChartTypeInterfaceManager.getInstance().addPlotTypePaneList(paneList, allChartTypePane);
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
		public void updateBean(ChartProvider chart) {

			String lastPlotID = chart.getID();

            try{
				AbstractChartTypePane selectedPane = (AbstractChartTypePane) getSelectedPane();
				ChartProvider newDefaultChart =(ChartProvider) ChartTypeManager.getInstanceWithCheck().getChartTypes(selectedPane.getPlotID())[0].clone();
				if (!ComparatorUtils.equals(chart.getClass(), newDefaultChart.getClass())) {
					//vanChart 和 chart 之间切换
					//不同chart之间切换
                    editingCollection.removeNameObject(editingCollection.getSelectedIndex());
                    editingCollection.addChart(newDefaultChart);
                    chart = newDefaultChart;
                }
            }catch (CloneNotSupportedException e){
                FineLoggerFactory.getLogger().error(e.getMessage(), e);
            }
			//这一步会替换plot
            ((AbstractChartTypePane)getSelectedPane()).updateBean(chart);

			String chartID = chart.getID();

			//chartID改变的话图表类型就算改变了
			if (StringUtils.isNotEmpty(chartID)) {

				boolean isUseDefault = ChartTypeInterfaceManager.getInstance().isUseDefaultPane(chartID);

				if (editPane.isDefaultPane() != isUseDefault || (!isUseDefault && !ComparatorUtils.equals(lastPlotID, chartID))) {
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

		private void addAllCards() {
			Iterator<String> iterator = allChartTypePane.keySet().iterator();

			while (iterator.hasNext()) {
				addOnePriorityCards(iterator.next(), false);
			}
		}

		private void addOnePriorityCards(String priority) {
			addOnePriorityCards(priority, true);
		}

		private void addOnePriorityCards(String priority, boolean ignore) {

			Map<String, FurtherBasicBeanPane<? extends ChartProvider>> map = allChartTypePane.get(priority);

			Iterator<Map.Entry<String, FurtherBasicBeanPane<? extends ChartProvider>>> iterator = map.entrySet().iterator();

			while (iterator.hasNext()) {
				Map.Entry<String, FurtherBasicBeanPane<? extends ChartProvider>> entry = iterator.next();
				String plotID = entry.getKey();
				if (ignore || ChartTypeManager.enabledChart(plotID)) {
					cards.add(entry.getValue());
				}
			}

		}

		private void addOnePlotIDCards(String priority, String plotID) {
			cards.add(allChartTypePane.get(priority).get(plotID));
		}

		//因为饼图（新特性）把（新特性）去掉了，和老的饼图同名，下拉框选项和typePane不能一一对应了
		//处理办法：这边除了重构 下拉项选项和cardNames 还需要把cards重构下（不需要init pane，只需要我需要的拿出来就好了）
		private void relayout(ChartCollection collection){
			//重构需要重构下拉框选项和cardNames
			ChartProvider chart = collection.getSelectedChartProvider();
			String chartID = chart.getID();
			String priority = ChartTypeManager.getInstanceWithCheck().getPriority(chartID);
			boolean enabledChart = ChartTypeManager.enabledChart(chartID);
			String item = ChartTypeInterfaceManager.getInstance().getName(chartID);

			//第一步就是重构cards
			cards.clear();
			if (enabledChart) {
				if (collection.getChartCount() == 1) {
					addAllCards();
				} else {
					addOnePriorityCards(priority);
				}
			} else {
				addOnePlotIDCards(priority, chartID);
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
		public void populateBean(ChartProvider ob) {
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
		public ChartProvider updateBean() {
			return getSelectedPane().updateBean();
		}

		@Override
		public FurtherBasicBeanPane<? extends ChartProvider> getSelectedPane() {
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
		ChartProvider chart = collection.getSelectedChartProvider();
		String plotID = chart.getID();
		boolean isUseDefault = ChartTypeInterfaceManager.getInstance().isUseDefaultPane(plotID);
		if (editPane != null && editPane.isDefaultPane() != isUseDefault || (!isUseDefault && !ComparatorUtils.equals(lastPlotID, plotID))){
			editPane.reLayout(chart);
		}else {
			throw new IllegalArgumentException("editPane can not be null.");
		}
	}


	public void relayoutChartTypePane(ChartCollection collection){
		chartTypeComBox.relayout(collection);
	}

	/**
	 * 更新界面属性 用于展示
	 */
	public void populate(ChartCollection collection) {
		editingCollection = collection;

		ChartProvider chart = collection.getSelectedChartProvider();
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
		ChartProvider chart = collection.getSelectedChartProvider();
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