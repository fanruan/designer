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
import com.fr.design.mainframe.chart.AbstractChartAttrPane;
import com.fr.design.mainframe.chart.ChartEditPane;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.design.mainframe.chart.gui.type.AbstractChartTypePane;

import javax.swing.*;
import java.awt.*;
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

	public void reactorChartTypePane(SwitchState state, ChartCollection collection){

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
			if (editingCollection.getState() == SwitchState.DEFAULT) {
				ChartTypeInterfaceManager.getInstance().addPlotTypePaneList(paneList);
			}else {
				Chart chart = editingCollection.getSelectedChart();
				ChartTypeInterfaceManager.getInstance().addPlotTypePaneList(paneList, chart.getChartID());
			}
			return paneList;
		}

		@Override
		protected String title4PopupWindow() {
			return null;
		}
		
		public void updateBean(Chart chart) {
            int lastSelectIndex = editPane.getSelectedChartIndex(chart);

            try{
                Chart newDefaultChart = (Chart)((AbstractChartTypePane)cards.get(jcb.getSelectedIndex())).getDefaultChart().clone();
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
            ((AbstractChartTypePane) cards.get(jcb.getSelectedIndex())).updateBean(chart);

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