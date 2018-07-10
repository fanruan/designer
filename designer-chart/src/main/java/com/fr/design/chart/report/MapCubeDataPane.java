package com.fr.design.chart.report;

import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.chartdata.MapMoreLayerReportDefinition;
import com.fr.chart.chartdata.MapMoreLayerTableDefinition;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.gui.frpane.UIComboBoxPane;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 钻取数据 
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2012-10-21 下午11:02:02
 */
public class MapCubeDataPane extends UIComboBoxPane<TopDefinitionProvider> {
	
	private MapReportCubeDataPane reportPane;
	private MapTableCubeDataPane tablePane;
	
	protected void initLayout() {
		this.setLayout(new BorderLayout(0, 0));
		JPanel northPane = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		northPane.add(new BoldFontTextLabel(Inter.getLocText("ChartF-Data-Resource") + ":"));
		northPane.add(jcb);
		
		this.add(northPane, BorderLayout.NORTH);
		this.add(cardPane, BorderLayout.CENTER);
	}
	
	@Override
	protected List<FurtherBasicBeanPane<? extends TopDefinitionProvider>> initPaneList() {
		List list = new ArrayList();
		
		list.add(tablePane = new MapTableCubeDataPane());
		list.add(reportPane = new MapReportCubeDataPane());
		
		return list;
	}

	@Override
	protected String title4PopupWindow() {
		return Inter.getLocText("LayerData");
	}
	
	/**
	 * 对数据集或者单元格数据加载界面
	 */
	public void populateBean(TopDefinitionProvider definition) {
		if(definition instanceof MapMoreLayerReportDefinition) {
			this.setSelectedIndex(1);
			MapMoreLayerReportDefinition reportDefinition = (MapMoreLayerReportDefinition)definition;
			reportPane.populateBean(reportDefinition);
		} else if(definition instanceof MapMoreLayerTableDefinition) {
			MapMoreLayerTableDefinition tableDefinition = (MapMoreLayerTableDefinition)definition;
			this.setSelectedIndex(0);
			tablePane.populateBean(tableDefinition);
		}
	}
	
	/**
	 * 根据界面 下载保存数据
	 */
	public TopDefinitionProvider update() {
		if(this.getSelectedIndex() == 0) {
			return tablePane.updateBean();
		} else {
			return reportPane.updateBean();
		}
	}
}