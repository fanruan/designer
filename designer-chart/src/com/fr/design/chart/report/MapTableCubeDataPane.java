package com.fr.design.chart.report;

import com.fr.chart.chartdata.MapMoreLayerTableDefinition;
import com.fr.chart.chartdata.MapSingleLayerTableDefinition;
import com.fr.data.impl.NameTableData;
import com.fr.design.data.tabledata.wrapper.TableDataWrapper;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.chart.gui.data.DatabaseTableDataPane;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * 地图 多层钻取 数据集定义界面
 *
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2012-10-23 上午10:55:39
 */
public class MapTableCubeDataPane extends FurtherBasicBeanPane<MapMoreLayerTableDefinition> {

	private DatabaseTableDataPane dataFromBox;

    private MapMoreTableIndexPane  tablePane;
	
	public MapTableCubeDataPane() {
		this.setLayout(new BorderLayout());

		JPanel pane = new JPanel();
		this.add(pane, BorderLayout.NORTH);

		pane.setLayout(new FlowLayout(FlowLayout.LEFT));

		UILabel label = new UILabel(Inter.getLocText("Select_Data_Set") + ":", SwingConstants.RIGHT);

		dataFromBox = new DatabaseTableDataPane(label) {
			protected void userEvent() {
				refreshAreaNameBox();
			}
		};
		dataFromBox.setPreferredSize(new Dimension(180, 20));
		pane.add(dataFromBox);

        tablePane = new MapMoreTableIndexPane();
		this.add(tablePane, BorderLayout.CENTER);
	}

	private void refreshAreaNameBox() {// 刷新区域名称列表
		TableDataWrapper tableDataWrappe = dataFromBox.getTableDataWrapper();
		if (tableDataWrappe == null) {
			return;
		}

        List<String> columnNameList = tableDataWrappe.calculateColumnNameList();
		tablePane.initAreaComBox(columnNameList.toArray(new String[columnNameList.size()]));
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
		return Inter.getLocText("DS-TableData");
	}

	@Override
	public void populateBean(MapMoreLayerTableDefinition tableDefinition) {// editingLayerCout

		if (tableDefinition != null) {
			dataFromBox.populateBean(tableDefinition.getTableData());

            MapSingleLayerTableDefinition[] values = tableDefinition.getNameValues();
            if(values != null && values.length > 0) {
                tablePane.populateBean(values[0]);
            }
		}
	}

	@Override
	public MapMoreLayerTableDefinition updateBean() {
		MapMoreLayerTableDefinition tableDefinition = new MapMoreLayerTableDefinition();

		TableDataWrapper tableDataWrappe = dataFromBox.getTableDataWrapper();
		if (tableDataWrappe != null) {
			tableDefinition.setTableData(new NameTableData(tableDataWrappe.getTableDataName()));

            tableDefinition.clearNameValues();
            tableDefinition.addNameValue(tablePane.updateBean());
		}

		return tableDefinition;
	}
}