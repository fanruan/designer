package com.fr.design.mainframe.chart.gui.data;

import com.fr.base.TableData;
import com.fr.chart.chartattr.*;
import com.fr.chart.chartdata.TableDataDefinition;
import com.fr.chart.chartdata.TopDefinition;
import com.fr.data.impl.NameTableData;
import com.fr.design.ChartTypeInterfaceManager;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.data.tabledata.wrapper.TableDataWrapper;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.table.*;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;

public class TableDataPane extends FurtherBasicBeanPane<ChartCollection>{
	private static final long serialVersionUID = 4740461028440155147L;
	private static final int TOP = -5;
	private DatabaseTableDataPane tableDataPane;
	private AbstractTableDataContentPane dataContentPane;
	
	private ChartDataPane parent;

	public TableDataPane(ChartDataPane parent) {
		this.parent = parent;
		initDataPane();
	}
	
	private void initDataPane() {
        UILabel label = new BoldFontTextLabel(Inter.getLocText("Chart-DS_TableData") + ":", SwingConstants.RIGHT) ;
        label.setPreferredSize(new Dimension(ChartDataPane.LABEL_WIDTH,ChartDataPane.LABEL_HEIGHT));
		tableDataPane = new DatabaseTableDataPane(label) {
			@Override
			protected void userEvent() {
				onSelectTableData();
				checkBoxUse();
			}
		};

		tableDataPane.setBorder(BorderFactory.createMatteBorder(0,6,0, 0, getBackground()));
        tableDataPane.setBorder(BorderFactory.createEmptyBorder(0,1,0,1));
		tableDataPane.setPreferredSize(new Dimension(205 , 20));
        this.setBorder(BorderFactory.createEmptyBorder(TOP,0,0,0));
		this.add(tableDataPane, BorderLayout.NORTH);
	}
	
	/**
	 *  检查box是否可用.
	 */
	public void checkBoxUse() {
		TableDataWrapper dataWrap = tableDataPane.getTableDataWrapper();
		
		if(dataContentPane != null) {
			dataContentPane.checkBoxUse(dataWrap != null);
		}
	}

	private void onSelectTableData() {
		TableDataWrapper dataWrap = tableDataPane.getTableDataWrapper();
		if(dataWrap == null) {
			return;
		}
		if(dataContentPane != null) {
			dataContentPane.onSelectTableData(dataWrap);
		}
	}

	private AbstractTableDataContentPane getContentPane(Plot plot) {
		return ChartTypeInterfaceManager.getInstance().getTableDataSourcePane(plot, parent);
	}

	/**
	 * 界面标题
	 * @return 返回标题.
	 */
	public String title4PopupWindow() {
		return Inter.getLocText(new String[]{"TableData", "Data"});
	}


	/**
	 * 判断界面是否接受
	 * @param ob 需要判断的对象
	 * @return 返回是否接受.
	 */
	public boolean accept(Object ob) {
		return ob instanceof ChartCollection && (((ChartCollection)ob).getSelectedChart().getFilterDefinition() instanceof TableDataDefinition);
	}

	/**
	 * 重置界面
	 */
	public void reset() {

	}

	/**
	 * 根据图表类型 刷新界面
	 * @param collection 图表属性的集合
	 */
	public void refreshContentPane(ChartCollection collection) {
		if(dataContentPane != null) {
			remove(dataContentPane);
		}
		dataContentPane = getContentPane(collection.getSelectedChart().getPlot());
		if(dataContentPane != null) {
			add(dataContentPane, BorderLayout.CENTER);
		}
	}

	/**
	 * 更新界面属性  
	 */
	public void populateBean(ChartCollection collection) {
		if(collection == null) {
			return;
		}
		TableDataDefinition data = (TableDataDefinition)collection.getSelectedChart().getFilterDefinition();
		TableData tableData = null;
		if(data != null) {
			tableData = data.getTableData();
		}
		onSelectTableData();
		checkBoxUse();
		
		tableDataPane.populateBean(tableData);
		if(dataContentPane != null) {
			dataContentPane.populateBean(collection);
		}
	}

	/**
	 * 保存界面属性到ChartCollection
	 */
	public void updateBean(ChartCollection collection) {
		if(dataContentPane != null) {
			dataContentPane.updateBean(collection);
		}
		TopDefinition dataDefinition = (TopDefinition)collection.getSelectedChart().getFilterDefinition();
		if(dataDefinition instanceof  TableDataDefinition) {
			TableDataWrapper tableDataWrapper = tableDataPane.getTableDataWrapper();
			if (dataDefinition != null && tableDataWrapper != null){
				NameTableData nameTableData = new NameTableData(tableDataWrapper.getTableDataName());
				((TableDataDefinition)dataDefinition).setTableData(nameTableData);
			}
		}
	}

	/**
	 * 保存界面属性 新建ChartCollection
	 */
	public ChartCollection updateBean() {
		return null;
	}

}