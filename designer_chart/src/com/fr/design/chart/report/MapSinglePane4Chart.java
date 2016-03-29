package com.fr.design.chart.report;

import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.chartdata.MapSingleLayerTableDefinition;
import com.fr.chart.chartdata.TopDefinition;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.data.tabledata.wrapper.TableDataWrapper;
import com.fr.general.Inter;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 7.1.1
 */
public class MapSinglePane4Chart extends FurtherBasicBeanPane<TopDefinitionProvider> {

   	private MapTableDataSinglePane4Chart tableSinglePane;

   	public MapSinglePane4Chart() {
   		initCom();
   	}

   	private void initCom() {
   		this.setLayout(new BorderLayout());

   		this.add(tableSinglePane = new MapTableDataSinglePane4Chart(), BorderLayout.CENTER);
   	}

    /**
     * 判断准许的情况
     * @param ob  数据集
     * @return 是不是顶层数据
     */
   	public boolean accept(Object ob) {
   		return ob instanceof TopDefinition;
   	}

   	/**
   	 * 重置
   	 */
   	public void reset() {

   	}

    /**
     *界面标题
     * @return 界面标题
     */
   	public String title4PopupWindow() {
   		return Inter.getLocText(new String[]{"SingleLayer", "Chart-Map"});
   	}

   	/**
   	 * 加载单层地图时的 数据来源界面
   	 */
   	public void populateBean(TopDefinitionProvider ob) {
        if(ob instanceof MapSingleLayerTableDefinition) {
   			tableSinglePane.populateBean((MapSingleLayerTableDefinition)ob);
   		}
   	}

   	/**
   	 * 保存下载 单层数据界面
   	 */
   	public TopDefinitionProvider updateBean() {
   		return tableSinglePane.updateBean();
   	}

	/**
     * 出发数据集改变
     * @param tableDataWrapper 数据集
     */
	public void fireTableDataChanged(TableDataWrapper tableDataWrapper) {
		tableSinglePane.setTableDataWrapper(tableDataWrapper);
		tableSinglePane.refreshAreaNameBox();
	}
}