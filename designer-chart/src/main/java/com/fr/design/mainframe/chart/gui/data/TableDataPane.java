package com.fr.design.mainframe.chart.gui.data;

import com.fr.base.TableData;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartdata.TableDataDefinition;
import com.fr.chart.chartdata.TopDefinition;
import com.fr.data.impl.NameTableData;
import com.fr.design.ChartTypeInterfaceManager;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.data.tabledata.wrapper.TableDataWrapper;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.i18n.Toolkit;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.table.AbstractTableDataContentPane;
import com.fr.design.utils.gui.UIComponentUtils;
import com.fr.van.chart.map.designer.data.contentpane.table.VanMapTableDataContentPane;

import javax.swing.BorderFactory;
import java.awt.BorderLayout;
import java.awt.Dimension;

public class TableDataPane extends FurtherBasicBeanPane<ChartCollection>{
	private static final long serialVersionUID = 4740461028440155147L;
	private static final int TOP = -5;
	private static final int TABLE_DATA_LABEL_LINE_WRAP_WIDTH = 65;
	private static final int TABLE_DATA_PANE_WIDTH = 246;
	private DatabaseTableDataPane tableDataPane;
	private AbstractTableDataContentPane dataContentPane;

	private ChartDataPane parent;

	protected AbstractTableDataContentPane getDataContentPane() {
		return dataContentPane;
	}

	public TableDataPane(ChartDataPane parent) {
		this.parent = parent;
		initDataPane();
	}

	private void initDataPane() {
		UILabel label = new BoldFontTextLabel(Toolkit.i18nText("Fine-Design_Chart_Table_Data"));
		UIComponentUtils.setLineWrap(label, TABLE_DATA_LABEL_LINE_WRAP_WIDTH);
		UIComponentUtils.setPreferedWidth(label, ChartDataPane.LABEL_WIDTH);

		tableDataPane = new DatabaseTableDataPane(label) {
			@Override
			protected void userEvent() {
				onSelectTableData();
				checkBoxUse();
			}
		};

		tableDataPane.setPreferredSize(new Dimension(TABLE_DATA_PANE_WIDTH , tableDataPane.getPreferredSize().height));
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
			dataContentPane.setTableName(dataWrap.getTableDataName());
		}
	}

	protected AbstractTableDataContentPane getContentPane(Chart chart) {
		return ChartTypeInterfaceManager.getInstance().getTableDataSourcePane(chart.getPlot(), parent);
	}

	/**
	 * 界面标题
	 * @return 返回标题.
	 */
	public String title4PopupWindow() {
		return Toolkit.i18nText("Fine-Design_Chart_TableData");
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
		refreshContentPane(getContentPane(collection.getSelectedChart()));
	}

	protected void refreshContentPane(AbstractTableDataContentPane contentPane) {
		if(dataContentPane != null) {
			remove(dataContentPane);
		}
		dataContentPane = contentPane;
		if(dataContentPane != null) {
			add(dataContentPane, BorderLayout.CENTER);
		}
	}

	public void refreshLevel(int level){
		if(dataContentPane instanceof VanMapTableDataContentPane) {
			((VanMapTableDataContentPane) dataContentPane).setLevel(level);
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
		populateDSName(data);
		if(dataContentPane != null) {
			dataContentPane.populateBean(collection);
		}
	}
	protected void populateDSName(TableDataDefinition dataDefinition){

		TableData tableData = null;
		if(dataDefinition != null) {
			tableData = dataDefinition.getTableData();
		}
		onSelectTableData();
		checkBoxUse();

		tableDataPane.populateBean(tableData);
	}

	/**
	 * 保存界面属性到ChartCollection
	 */
	public void updateBean(ChartCollection collection) {
		if(dataContentPane != null) {
			dataContentPane.updateBean(collection);
		}
		TopDefinition dataDefinition = (TopDefinition)collection.getSelectedChart().getFilterDefinition();
		updateDSName(dataDefinition);
	}

	protected void updateDSName(TopDefinition dataDefinition) {
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
