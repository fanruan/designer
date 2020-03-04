package com.fr.design.mainframe.chart.gui.data.table;

import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.data.tabledata.wrapper.TableDataWrapper;
import com.fr.design.gui.icombobox.UIComboBox;

import javax.swing.JSeparator;
import java.util.List;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public abstract class AbstractTableDataContentPane extends BasicBeanPane<ChartCollection>{

	protected static final double COMPONENT_WIDTH = 124;

	private boolean isNeedSummaryCaculateMethod = true;

	public String tableName;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public abstract void updateBean(ChartCollection ob);
	
	/**
	 * 检查box是否 需要使用 比如: 柱形图, 在分类轴未选择时, 不能选择使用 系列名 系列值 "系列名使用"
     * @param hasUse 是否使用
	 */
	public void checkBoxUse(boolean hasUse) {
		
	}
	
	/**
	 * 数据集变更时, 刷新box
     * @param dataWrapper 二维表
	 */
	public void onSelectTableData(TableDataWrapper dataWrapper) {
		List<String> columnNameList = dataWrapper.calculateColumnNameList();
		refreshBoxListWithSelectTableData(columnNameList);
	}

    /**
     * 清空所有的box设置
     */
    public abstract void clearAllBoxList();

	protected abstract void refreshBoxListWithSelectTableData(List columnNameList);
	
	/**
	 * 刷新Box的选项.
	 */
	protected void refreshBoxItems(UIComboBox box, List list) {
		DataPaneHelper.refreshBoxItems(box, list);
	}

    /**
     * 清空box里所有东西
     * @param box 容器
     */
	protected void clearBoxItems(UIComboBox box) {
		DataPaneHelper.clearBoxItems(box);
	}
	
	protected ItemListener tooltipListener = new ItemListener() {

		@Override
		public void itemStateChanged(ItemEvent e) {
			if(e.getSource() instanceof UIComboBox) {
				UIComboBox box = (UIComboBox)e.getSource();
				if(box.getSelectedItem() != null) {
					box.setToolTipText(box.getSelectedItem().toString());
				} else {
					box.setToolTipText(null);
				}
			}
		}
    };
	
	/**
	 *  兼容以前 自定义的数据字段
	 */
	protected void combineCustomEditValue(UIComboBox comBox, String value) {
		if(comBox != null) {
			comBox.setEditable(true);
			comBox.setSelectedItem(value);
			comBox.setEditable(false);
		}
	}

	@Override
	protected String title4PopupWindow() {
		return "";
	}
	
	@Override
	public void populateBean(ChartCollection collection) {
		if(collection == null) {
			return;
		}
	}

    /**
     * 重新layout整个面板
     */
    public void redoLayoutPane(){

    }
	
	@Override
	public ChartCollection updateBean() {
		return null;
	}

    /**
     * 设置是否需要汇总方式
     * @param isNeedSummaryCaculateMethod 是否需要汇总
     */
    public void setNeedSummaryCaculateMethod(boolean isNeedSummaryCaculateMethod){
        this.isNeedSummaryCaculateMethod = isNeedSummaryCaculateMethod;
    }

    /**
     * 返回是否需要汇总方式
     * @return 是否需要汇总方式
     */
    public boolean isNeedSummaryCaculateMethod(){
        return this.isNeedSummaryCaculateMethod;
    }

	protected JSeparator getJSeparator() {
		JSeparator jSeparator = new JSeparator();
		jSeparator.setPreferredSize(new Dimension(246, 2));
		return jSeparator;
	}

	public void refreshLevel(int level) {

	}
}