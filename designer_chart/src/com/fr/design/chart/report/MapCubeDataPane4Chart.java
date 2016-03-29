package com.fr.design.chart.report;

import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.chartdata.MapMoreLayerTableDefinition;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.data.tabledata.wrapper.TableDataWrapper;
import com.fr.design.gui.frpane.UIComboBoxPane;
import com.fr.general.Inter;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 7.1.1
 */
public class MapCubeDataPane4Chart extends UIComboBoxPane<TopDefinitionProvider> {
   	private MapTableCubeDataPane4Chart tablePane;

   	protected void initLayout() {
   		this.setLayout(new BorderLayout(0, 0));
   		this.add(cardPane, BorderLayout.CENTER);
   	}

   	@Override
   	protected List<FurtherBasicBeanPane<? extends TopDefinitionProvider>> initPaneList() {
   		List list = new ArrayList();

   		list.add(tablePane = new MapTableCubeDataPane4Chart());

   		return list;
   	}

   	@Override
   	protected String title4PopupWindow() {
   		return Inter.getLocText("FR-Chart-Map_LayerData");
   	}

   	/**
   	 * 对数据集或者单元格数据加载界面
   	 */
   	public void populateBean(TopDefinitionProvider definition) {
       if(definition instanceof MapMoreLayerTableDefinition) {
   			MapMoreLayerTableDefinition tableDefinition = (MapMoreLayerTableDefinition)definition;
   			this.setSelectedIndex(0);
   			tablePane.populateBean(tableDefinition);
   		}
   	}

   	/**
   	 * 根据界面 下载保存数据
   	 */
   	public TopDefinitionProvider update() {
        return tablePane.updateBean();
   	}

	/**
     * 出发数据集改变
     * @param tableDataWrapper 数据集
     */
	public void fireTableDataChanged(TableDataWrapper tableDataWrapper) {
		tablePane.setTableDataWrapper(tableDataWrapper);
		tablePane.refreshAreaNameBox();
	}

}