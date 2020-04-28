package com.fr.design.mainframe.chart.gui;

import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartattr.GisMapPlot;
import com.fr.chart.chartattr.MapPlot;
import com.fr.chartx.config.info.constant.ConfigType;
import com.fr.design.chart.report.GisMapDataPane;
import com.fr.design.chart.report.MapDataPane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.mainframe.chart.AbstractChartAttrPane;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.design.mainframe.chart.gui.data.DataContentsPane;
import com.fr.design.mainframe.chart.gui.data.NormalChartDataPane;
import com.fr.design.mainframe.chart.info.ChartInfoCollector;
import com.fr.plugin.chart.attr.plot.VanChartPlot;
import com.fr.plugin.chart.vanchart.VanChart;

import javax.swing.JPanel;
import java.awt.BorderLayout;

public class ChartDataPane extends AbstractChartAttrPane {

	public static final int LABEL_WIDTH = 85;
	public static final int LABEL_HEIGHT = 20;

	protected DataContentsPane contentsPane;
	protected AttributeChangeListener listener;

	private boolean supportCellData = true;

	public ChartDataPane(AttributeChangeListener listener) {
		super();
		this.listener = listener;
	}

	@Override
	protected JPanel createContentPane() {
		contentsPane = new NormalChartDataPane(listener, ChartDataPane.this);
		return contentsPane;
	}


	/**
	 * 界面标题
	 */
	public String getIconPath() {
		return "com/fr/design/images/chart/ChartData.png";
	}

    /**
     * 界面标题
     * @return 界面标题
     */
	public String title4PopupWindow() {
		return PaneTitleConstants.CHART_DATA_TITLE;
	}

	protected void repeatLayout(ChartCollection collection) {
		if(contentsPane != null) {
			this.remove(contentsPane);
		}

		this.setLayout(new BorderLayout(0, 0));
		if (collection == null) {
			throw new IllegalArgumentException("ChartCollection can not be null!");
		}
		if (collection.getChartCount() <= 0) {
			contentsPane = new NormalChartDataPane(listener, ChartDataPane.this);
		} else if (collection.getSelectedChart().getPlot() instanceof MapPlot) {
			contentsPane = new MapDataPane(listener);
		} else if(collection.getSelectedChart().getPlot() instanceof GisMapPlot){
			contentsPane = new GisMapDataPane(listener);
		}else {
			contentsPane = new NormalChartDataPane(listener, ChartDataPane.this);
		}

		if(contentsPane != null) {
			contentsPane.setSupportCellData(supportCellData);
		}
	}

	public boolean isSupportCellData() {
		return supportCellData;
	}

	/**
	 * 设置数据界面是否支持单元格
	 */
	public void setSupportCellData(boolean supportCellData) {
		this.supportCellData = supportCellData;
		if(contentsPane != null) {
			contentsPane.setSupportCellData(supportCellData);
		}
	}

	/**
	 * 更新界面 数据内容
	 */
	public void populate(ChartCollection collection) {
		repeatLayout(collection);

		contentsPane.populate(collection);

		this.add(contentsPane, BorderLayout.CENTER);

        this.validate();
	}

	/**
	 * 保存 数据界面内容
	 */
	public void update(ChartCollection collection) {
		if (contentsPane != null) {
			contentsPane.update(collection);
            updateBuryingPoint(collection);
		}
	}

	protected void updateBuryingPoint(ChartCollection collection){
        VanChart vanchart = collection.getSelectedChartProvider(VanChart.class);
        if (vanchart != null) {
			VanChartPlot plot = vanchart.getPlot();
			if( !plot.isInCustom()) {
				ChartInfoCollector.getInstance().updateChartConfig(vanchart, ConfigType.DATA,
						vanchart.getBuryingPointDataConfig());
			}
        }
    }

	/**
	 * 刷新图表数据界面
	 * @param collection 图表收集器
	 */
	public void refreshChartDataPane(ChartCollection collection){
		this.populate(collection);
	}
}