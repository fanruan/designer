package com.fr.design.chart.report;

import com.fr.chart.chartdata.MapMoreLayerTableDefinition;
import com.fr.chart.chartdata.MapSingleLayerTableDefinition;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.data.tabledata.wrapper.TableDataWrapper;
import com.fr.general.Inter;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 7.1.1
 */
public class MapTableCubeDataPane4Chart extends FurtherBasicBeanPane<MapMoreLayerTableDefinition> {

    private MapMoreTableIndexPane  tablePane;
    private TableDataWrapper tableDataWrapper;

   	public MapTableCubeDataPane4Chart() {
   		this.setLayout(new BorderLayout());
        tablePane = new MapMoreTableIndexPane();
   		this.add(tablePane, BorderLayout.CENTER);
   	}

	/**
	 * 刷新区域名称列表
	 */
   	public void refreshAreaNameBox() {
   		TableDataWrapper tableDataWrappe = tableDataWrapper;
   		if (tableDataWrappe == null) {
   			return;
   		}

		java.util.List<String> columnNameList = tableDataWrappe.calculateColumnNameList();
   		tablePane.initAreaComBox(columnNameList.toArray(new String[columnNameList.size()]));
   	}

	public void setTableDataWrapper(TableDataWrapper wrapper){
		this.tableDataWrapper = wrapper;
	}

   	/**
   	 * 界面接入
        * @param ob  界面
        *            @return  返回接入.
   	 */
   	public boolean accept(Object ob) {
   		return true;
   	}

   	/**
   	 * 重置
   	 */
   	public void reset() {
   	}

   	/**
   	 * 界面弹出标题
        * @return  返回标题.
   	 */
   	public String title4PopupWindow() {
   		return Inter.getLocText("FR-Chart-Table_Data");
   	}

   	@Override
   	public void populateBean(MapMoreLayerTableDefinition tableDefinition) {

   		if (tableDefinition != null) {
               MapSingleLayerTableDefinition[] values = tableDefinition.getNameValues();
               if(values != null && values.length > 0) {
                   tablePane.populateBean(values[0]);
               }
   		}
   	}

   	@Override
   	public MapMoreLayerTableDefinition updateBean() {
   		MapMoreLayerTableDefinition tableDefinition = new MapMoreLayerTableDefinition();

   		TableDataWrapper tableDataWrappe =tableDataWrapper;
   		if (tableDataWrappe != null) {
   			tableDefinition.setTableData(tableDataWrappe.getTableData());

               tableDefinition.clearNameValues();
               tableDefinition.addNameValue(tablePane.updateBean());
   		}

   		return tableDefinition;
   	}

}