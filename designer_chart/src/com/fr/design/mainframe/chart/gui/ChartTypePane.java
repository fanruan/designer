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

				if(editPane.isDefaultPane() != isUseDefault || (!isUseDefault && lastSelectIndex != jcb.getSelectedIndex())){
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

		public void reactor(ChartCollection collection){
			//重构前存储所选择的下拉选项
			Object item = jcb.getSelectedItem();
			//重构需要重构下拉框选项和cardNames
			Chart chart = collection.getSelectedChart();
			String chartID = chart.getChartID();
			if (collection.getState() == SwitchState.DEFAULT){
				chartID = StringUtils.EMPTY;
			}
			//第一步就是重构cardNames
			cardNames = ChartTypeInterfaceManager.getInstance().getTitle4PopupWindow(chartID);
			//重构下拉框选项
			FlexibleComboBox fcb = (FlexibleComboBox)jcb;
			fcb.setItemEvenType(ItemEventType.REACTOR);
			fcb.removeAllItems();
			for (int i = 0; i < this.cardNames.length; i++) {
				fcb.addItem(cardNames[i]);
			}
			//重新选择选中的下拉项
			jcb.setSelectedItem(item);
			fcb.setItemEvenType(ItemEventType.DEFAULT);
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


	public void reactorChartTypePane(ChartCollection collection){
		chartTypePane.reactor(collection);
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