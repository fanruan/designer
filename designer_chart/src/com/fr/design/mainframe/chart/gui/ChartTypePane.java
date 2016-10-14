package com.fr.design.mainframe.chart.gui;

import com.fr.base.FRContext;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.charttypes.ChartTypeManager;
import com.fr.design.ChartTypeInterfaceManager;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.dialog.BasicScrollPane;
import com.fr.design.gui.frpane.UIComboBoxPane;
import com.fr.design.mainframe.chart.gui.item.FlexibleComboBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.mainframe.chart.AbstractChartAttrPane;
import com.fr.design.mainframe.chart.ChartEditPane;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.design.mainframe.chart.gui.item.ItemEvenType;
import com.fr.design.mainframe.chart.gui.type.AbstractChartTypePane;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;
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
	private ComboBoxPane chartTypePane;
	private ChartTypeButtonPane buttonPane;
    private ChartEditPane editPane;
    private ChartCollection editingCollection;
	@Override
	protected JPanel createContentPane() {
		JPanel content = new JPanel(new BorderLayout());
		
		buttonPane = new ChartTypeButtonPane(this);
		content.add(buttonPane, BorderLayout.NORTH);
		
		chartTypePane = new ComboBoxPane();
		BasicScrollPane scrollPane = new BasicScrollPane() {
			@Override
			protected JPanel createContentPane() {
				return chartTypePane;
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
		
		buttonPane.setEditingChartPane(chartTypePane);
		
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
		
		public void updateBean(Chart chart) {
            int lastSelectIndex = editPane.getSelectedChartIndex(chart);
			FlexibleComboBox fcb = (FlexibleComboBox)jcb;

            try{
                Chart newDefaultChart = (Chart)((AbstractChartTypePane)cards.get(fcb.getRelatedSelectedIndex())).getDefaultChart().clone();
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
            ((AbstractChartTypePane) cards.get(fcb.getRelatedSelectedIndex())).updateBean(chart);

			String chartID = chart.getChartID();

			Plot plot = chart.getPlot();

			if(plot != null){
				String plotID = plot.getPlotID();

				//plot改变的话图表类型就算改变了

				chart.setWrapperName(ChartTypeManager.getInstance().getWrapperName(plotID));

				chart.setChartImagePath(ChartTypeManager.getInstance().getChartImagePath(plotID));

				boolean isUseDefault = ChartTypeInterfaceManager.getInstance().isUseDefaultPane(plotID);

				if(editPane.isDefaultPane() != isUseDefault || (!isUseDefault && lastSelectIndex != fcb.getRelatedSelectedIndex())){
					editPane.reLayout(chart);
				}
			}
		}

		@Override
		public void populateBean(Chart ob) {
			for (int i = 0; i < this.cards.size(); i++) {
				FurtherBasicBeanPane pane = cards.get(i);
				if (pane.accept(ob)) {
					pane.populateBean(ob);
					((FlexibleComboBox)jcb).setRelatedSelectedIndex(i);
					return;
				}
			}
		}

		@Override
		protected UIComboBox createComboBox() {
			FlexibleComboBox comboBox = new FlexibleComboBox();
			//初始化分界线
			int partition = 0;
			for (int i = 0; i < this.cards.size(); i++) {
				String name = cards.get(i).title4PopupWindow();
				if (name.contains(Inter.getLocText("Plugin-ChartF_NewChart"))) {
					partition++;
				}
			}
			comboBox.setPartition(partition);
			return comboBox;
		}


		/**
		 * 下拉框重构条件
		 * 1、从None->Default(multiMode)
		 * 2、从None->New(multiMode)
		 * 3、从Default->None(singleMode)
		 * 4、从New->None(singleMode)
		 * @param is2MultiMode
		 * @param isVanChart
		 */
		private void reactorComboBoxItem(boolean is2MultiMode, boolean isVanChart){
			if (jcb == null){
				return;
			}
			//重构下拉选项
			FlexibleComboBox fcb = (FlexibleComboBox)jcb;
			//重构前，保存相对下标
			int relativeIndex = is2MultiMode ? fcb.getSelectedIndex() : fcb.getRelatedSelectedIndex();
			//多图表切换模式
			fcb.setMultiMode(is2MultiMode);
			//设置当前切换是新图表还是老图表
			fcb.setBottom(!isVanChart);
			//重构下拉框选项
			fcb.setItemEvenType(ItemEvenType.REACTOR);
			jcb.removeAllItems();
			for (int i = 0; i < this.cards.size(); i++) {
				String name = cards.get(i).title4PopupWindow();
				if (is2MultiMode && (name.contains(Inter.getLocText("Plugin-ChartF_NewChart")) && isVanChart)) {
					jcb.addItem(cards.get(i).title4PopupWindow());
				} else if (is2MultiMode && (!name.contains(Inter.getLocText("Plugin-ChartF_NewChart")) && !isVanChart)) {
					jcb.addItem(cards.get(i).title4PopupWindow());
				}else if (!is2MultiMode){
					jcb.addItem(cards.get(i).title4PopupWindow());
				}
			}
			//重置选择项
			fcb.setRelatedSelectedIndex(relativeIndex);
			//重构完成
			fcb.setItemEvenType(ItemEvenType.DEFAULT);

		}

		@Override
		protected void addItemChangeEvent() {
			jcb.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					//如果是在进行重构，则不触发状态改变事件
					FlexibleComboBox fcb = (FlexibleComboBox)jcb;
					if (fcb.isReactor()){
						return;
					}
					comboBoxItemStateChanged();
					CardLayout cl = (CardLayout)cardPane.getLayout();
					cl.show(cardPane, cardNames[((FlexibleComboBox)jcb).getRelatedSelectedIndex()]);
				}
			});
		}
	}

	public void fireReactor(ChartCollection collection) {
		switch (collection.getState()){
			case NEW:{
				this.chartTypePane.reactorComboBoxItem(true, true);
				break;
			}
			case DEFAULT:{
				this.chartTypePane.reactorComboBoxItem(true, false);
				break;
			}
			case NONE:{
				this.chartTypePane.reactorComboBoxItem(false, false);
				break;
			}
			default:this.chartTypePane.reactorComboBoxItem(false, false);
		}
	}

	/**
	 * 更新界面属性 用于展示
	 */
	public void populate(ChartCollection collection) {
		Chart chart = collection.getSelectedChart();
		chartTypePane.populateBean(chart);
		
		buttonPane.populateBean(collection);
	}

	/**
	 * 保存界面属性
	 */
	public void update(ChartCollection collection) {
        editingCollection = collection;
		buttonPane.update(collection);// 内部操作时 已经做过处理.
		Chart chart = collection.getSelectedChart();

		chartTypePane.updateBean(chart);
	}

    /**
     * 所有图表的类型界面
     * @return 类型界面
     */
    public FurtherBasicBeanPane[] getPaneList(){
        return chartTypePane.getCards().toArray(new FurtherBasicBeanPane[0]);
    }

    /**
     * 当前选中的图表的index
     * @return 当前选中的图表的index
     */
    public int getSelectedIndex(){
        return chartTypePane.getSelectedIndex();
    }

    /**
     * 返回选中的图表的index
     * @return 选中的图标的序号
     */
    public int getSelectedChartIndex(){
        return chartTypePane.getSelectedIndex();
    }

    /**
     * 设置下编辑的面板
     * @param currentEditPane 设置下编辑的面板
     */
    public void registerChartEditPane(ChartEditPane currentEditPane) {
        this.editPane = currentEditPane;
    }
}