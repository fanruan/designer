package com.fr.design.chart.series.SeriesCondition.impl;

import com.fr.base.chart.BaseChartCollection;
import com.fr.chart.chartattr.Bar2DPlot;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.charttypes.ChartTypeManager;
import com.fr.chart.web.ChartHyperPoplink;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.chart.gui.ChartComponent;
import com.fr.design.editor.ValueEditorPane;
import com.fr.design.editor.ValueEditorPaneFactory;
import com.fr.design.gui.itableeditorpane.ParameterTableModel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.chart.ChartHyperEditPane;
import com.fr.design.module.DesignModuleFactory;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.FRLogger;
import com.fr.general.Inter;

import java.awt.*;

/**
 * 类说明: 图表超链 -- 弹出 悬浮窗. 
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2011-12-28 上午10:41:39
 */
public class ChartHyperPoplinkPane extends BasicBeanPane<ChartHyperPoplink> {
	private static final long serialVersionUID = 2469115951510144738L;
	private UITextField itemNameTextField;
	private ChartHyperEditPane hyperEditPane;
	private ChartComponent chartComponent;
	
	public ChartHyperPoplinkPane() {
		this.setLayout(FRGUIPaneFactory.createM_BorderLayout());

        if(this.needRenamePane()){
            itemNameTextField = new UITextField();
            this.add(GUICoreUtils.createNamedPane(itemNameTextField, Inter.getLocText("FR-Chart-Use_Name") + ":"), BorderLayout.NORTH);
        }

		hyperEditPane = new ChartHyperEditPane(getChartParaType(), getValueEditorPane(), getValueEditorPane());
		this.add(hyperEditPane, BorderLayout.CENTER);
        ChartCollection cc = createChartCollection();
		
		chartComponent = new ChartComponent();
		chartComponent.setPreferredSize(new Dimension(210, 170));// 在单元格弹出时 需要调整保证属性表的大小.
		chartComponent.setSupportEdit(false);
		chartComponent.populate(cc);
		
		this.add(chartComponent, BorderLayout.EAST);
		
		hyperEditPane.populate(cc);
		
		hyperEditPane.useChartComponent(chartComponent);
	}

    private ChartCollection createChartCollection() {
        ChartCollection cc = new ChartCollection();

        Chart chart = ChartTypeManager.getFirstChart();
        if (chart != null){
            try {
                cc.addChart((Chart)chart.clone());
            } catch (CloneNotSupportedException e) {
                FRLogger.getLogger().error(e.getMessage(), e);
            }

        }else {
            cc.addChart(new Chart(new Bar2DPlot()));
        }
        return cc;
    }

    protected int getChartParaType() {
		return ParameterTableModel.CHART_NORMAL_USE;
	}

	protected ValueEditorPane getValueEditorPane() {
		return ValueEditorPaneFactory.createVallueEditorPaneWithUseType(getChartParaType());
	}

    /**
     * 是否需要加载重命名的空间
     * @return 默认需要加载
     */
    protected boolean needRenamePane(){
        return true;
    }
	
	@Override
	protected String title4PopupWindow() {
		return Inter.getLocText("FR-Chart-Pop_Chart");
	}

	@Override
	public void populateBean(ChartHyperPoplink chartHyperlink) {
        if(itemNameTextField != null){
            this.itemNameTextField.setText(chartHyperlink.getItemName());
        }

		BaseChartCollection cc = chartHyperlink.getChartCollection();
		if (cc == null || cc.getChartCount() < 1) {
			cc = createChartCollection();
			chartHyperlink.setChartCollection(cc);
		}
		
		hyperEditPane.populateHyperLink(chartHyperlink);
		chartComponent.populate(cc);
	}

	/**
	 * 超链数组HyperlinkGoup切换时 updateBean.
	 * @return 返回的弹出超链.
	 */
	public ChartHyperPoplink updateBean() {
		ChartHyperPoplink chartLink = new ChartHyperPoplink();
		updateBean(chartLink);
        if(itemNameTextField != null){
            chartLink.setItemName(this.itemNameTextField.getText());
        }
		return chartLink;
	}
	
	/**
	 * 属性表 对应update
	 */
	public void updateBean(ChartHyperPoplink chartHyperlink) {
		hyperEditPane.updateHyperLink(chartHyperlink);
		chartHyperlink.setChartCollection(chartComponent.update());

		DesignModuleFactory.getChartPropertyPane().getChartEditPane().fire();// 响应整个图表保存事件等.
        if(itemNameTextField != null){
            chartHyperlink.setItemName(this.itemNameTextField.getText());
        }
	}

    public static class CHART_NO_RENAME extends ChartHyperPoplinkPane{
        protected boolean needRenamePane(){
            return false;
        }
    }

	public static class CHART_MAP extends ChartHyperPoplinkPane {
		
		protected int getChartParaType() {
			return ParameterTableModel.CHART_MAP_USE;
		}
	}
	
	public static class CHART_GIS extends ChartHyperPoplinkPane {
		
		protected int getChartParaType() {
			return ParameterTableModel.CHART_GIS_USE;
		}
	}
	
	public static class CHART_PIE extends ChartHyperPoplinkPane {
    	@Override
    	protected int getChartParaType() {
    		return ParameterTableModel.CHART_PIE_USE;
    	}
    }

    public static class CHART_XY extends ChartHyperPoplinkPane {
        protected int getChartParaType() {
            return ParameterTableModel.CHART__XY_USE;
        }
    }

    public static class CHART_BUBBLE extends ChartHyperPoplinkPane {
        protected int getChartParaType() {
            return ParameterTableModel.CHART_BUBBLE_USE;
        }
    }

    public static class CHART_STOCK extends  ChartHyperPoplinkPane {
        protected int getChartParaType() {
            return ParameterTableModel.CHART_STOCK_USE;
        }
    }

    public static class CHART_GANTT extends  ChartHyperPoplinkPane {
        protected int getChartParaType() {
            return ParameterTableModel.CHART_GANTT_USE;
        }
    }

    public static class CHART_METER extends  ChartHyperPoplinkPane {
        protected int getChartParaType() {
            return ParameterTableModel.CHART_METER_USE;
        }
    }
}