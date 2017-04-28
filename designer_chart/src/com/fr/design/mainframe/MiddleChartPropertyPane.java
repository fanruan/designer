/*
 * Copyright(c) 2001-2011, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.mainframe;

import java.awt.BorderLayout;

import javax.swing.Icon;
import javax.swing.JComponent;

import com.fr.base.BaseUtils;
import com.fr.base.chart.BaseChartCollection;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.ChartTypeInterfaceManager;
import com.fr.design.designer.TargetComponent;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.chart.BaseChartPropertyPane;
import com.fr.design.gui.chart.ChartEditPaneProvider;
import com.fr.design.gui.frpane.UITitlePanel;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itabpane.TitleChangeListener;
import com.fr.design.mainframe.chart.ChartEditPane;
import com.fr.general.Inter;
import com.fr.stable.StableUtils;

public abstract class MiddleChartPropertyPane extends BaseChartPropertyPane{

	protected TargetComponentContainer container = new TargetComponentContainer();
	protected UILabel nameLabel;

	protected ChartEditPane chartEditPane;
	protected ChartCollection chartCollection;
	protected String plotID;
	protected BasicPane chartPane;

	public MiddleChartPropertyPane() {
		initComponenet();
	}
	
	protected void initComponenet() {
		this.setLayout(new BorderLayout());
		this.setBorder(null);
		
		createNameLabel();
		this.add(createNorthComponent(), BorderLayout.NORTH);
		chartEditPane =  StableUtils.construct(ChartEditPane.class);
		chartEditPane.setSupportCellData(true);
		this.createMainPane();
	}
	
	protected abstract void createNameLabel();
	
	protected abstract JComponent createNorthComponent();
	
	protected abstract void createMainPane();


	@Override
	public ChartEditPaneProvider getChartEditPane() {
		return chartEditPane;
	}

	public void setSureProperty() {
		chartEditPane.setContainer(container);
		chartEditPane.addTitleChangeListener(titleListener);
		String tabname = chartEditPane.getSelectedTabName();
		nameLabel.setText(Inter.getLocText("Chart-Property_Table") + (tabname != null ? ('-' + chartEditPane.getSelectedTabName()) : ""));
		resetChartEditPane();
	}
	
	protected void resetChartEditPane() {
		remove(chartEditPane);
		if (plotID != null) {
			chartPane = ChartTypeInterfaceManager.getInstance().getChartConfigPane(plotID);
			if (chartPane != null) {
				add(chartPane, BorderLayout.CENTER);
			}
		}
		if (chartPane == null) {
			chartEditPane = StableUtils.construct(ChartEditPane.class);
			add(chartEditPane, BorderLayout.CENTER);
		}
		validate();
		repaint();
		revalidate();
	}
	
	protected TitleChangeListener titleListener = new TitleChangeListener() {
		
		@Override
		public void fireTitleChange(String addName) {
			nameLabel.setText(Inter.getLocText("Chart-Property_Table") + '-' + addName);
		}
	};

    /**
     * 感觉ChartCollection加载图表属性界面.
     * @param collection  收集图表
     * @param ePane  面板
     */
	public void populateChartPropertyPane(ChartCollection collection, TargetComponent<?> ePane) {
		this.container.setEPane(ePane);
		chartEditPane.populate(collection);
	}

    /**
     * 感觉ChartCollection加载图表属性界面.
     * @param collection  收集图表
     * @param ePane  面板
     */
	public void populateChartPropertyPane(BaseChartCollection collection, TargetComponent<?> ePane) {
		if (collection instanceof ChartCollection) {
			chartCollection = (ChartCollection) collection;
			plotID=chartCollection.getSelectedChart().getPlot().getPlotID();
			resetChartEditPane();
			populateChartPropertyPane((ChartCollection)collection, ePane);
		}
	}

//	public void clear() {
//		this.container.setEPane(null);
//		chartEditPane.clear();
//		getParent().remove(this);
//	}

	/**
	 * 返回View的标题.
	 */
	public String getViewTitle() {
		return Inter.getLocText("CellElement-Property_Table");
	}

	/**
	 * 返回View的Icon地址.
	 */
	public Icon getViewIcon() {
		return BaseUtils.readIcon("/com/fr/design/images/m_report/qb.png");
	}

    /**
     *  预定义定位
     * @return    定位
     */
	public Location preferredLocation() {
		return Location.WEST_BELOW;
	}

    /**
     * 创建标题Panel
     * @return  标题panel
     */
	public UITitlePanel createTitlePanel() {
		return new UITitlePanel(this);
	}

	/**
	 * 刷新Dockview
	 */
	public void refreshDockingView() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 设置是否支持单元格数据.
	 */
	public void setSupportCellData(boolean supportCellData) {
		if(chartEditPane != null) {
			chartEditPane.setSupportCellData(supportCellData);
		}
	}
}