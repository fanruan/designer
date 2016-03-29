package com.fr.design.data.tabledata.tabledatapane;

import com.fr.base.FRContext;
import com.fr.data.impl.DecoratedTableData;
import com.fr.design.condition.DSColumnLiteConditionPane;
import com.fr.design.gui.ilist.CheckBoxList;
import com.fr.design.gui.ilist.CheckBoxList.CheckBoxListSelectionChangeListener;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.file.DatasourceManager;
import com.fr.file.DatasourceManagerProvider;
import com.fr.general.Inter;
import com.fr.general.data.DataModel;
import com.fr.general.data.TableDataException;
import com.fr.script.Calculator;

import java.awt.*;

public class DecoratedTableDataPane extends AbstractTableDataPane<DecoratedTableData> implements CheckBoxListSelectionChangeListener {
	private CheckBoxList availableTableDataNameList;
	private DSColumnLiteConditionPane conditionPane;
	
	public DecoratedTableDataPane() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		
		DatasourceManagerProvider mgr = DatasourceManager.getProviderInstance();
		java.util.Iterator serverTableDataNameIterator = mgr.getTableDataNameIterator();
		java.util.List<String> tableDataNameList = new java.util.ArrayList<String>();
		while (serverTableDataNameIterator.hasNext()) {
			tableDataNameList.add((String)serverTableDataNameIterator.next());
		}
		availableTableDataNameList = new CheckBoxList(tableDataNameList.toArray(new String[0]), CheckBoxList.SelectedState.NONE,Inter.getLocText("FR-Designer_Chart_Field_Name"));
		this.add(availableTableDataNameList, BorderLayout.WEST);
		availableTableDataNameList.addCheckBoxListSelectionChangeListener(this);
		
		conditionPane = new DSColumnLiteConditionPane();
		this.add(conditionPane, BorderLayout.CENTER);
	}

	@Override
	public void populateBean(DecoratedTableData ob) {
		String[] relatedTableDataNames = ob.getRelatedTableDataNames();
		availableTableDataNameList.setItems(relatedTableDataNames);
		
		conditionPane.populateBean(ob.getCondition());
	}
	
	private DecoratedTableData updateBeanExcludeFilter() {
		DecoratedTableData td = new DecoratedTableData();
		
		java.util.List<String> targetStringList = new java.util.ArrayList<String>();
		Object[] selectedObz = availableTableDataNameList.getSelectedValues();
		for (Object ob : selectedObz) {
			if (ob instanceof String) {
				targetStringList.add((String)ob);
			}
		}
		td.setRelatedTableDataNames(targetStringList.toArray(new String[0]));
		
		return td;
	}

	@Override
	public DecoratedTableData updateBean() {
		DecoratedTableData td = updateBeanExcludeFilter();
		td.setCondition(conditionPane.updateBean());
		
		return td;
	}

	@Override
	protected String title4PopupWindow() {
		return Inter.getLocText("DS-Relation_TableData");
	}
	
    /**
     * 选中组件改变事件
     *
     * @param taget 目标
     */
	public void selectionChanged(CheckBoxList target) {
		DecoratedTableData updatedBean = updateBeanExcludeFilter();
		DataModel model = updatedBean.createDataModel(Calculator.createCalculator());
		java.util.List<String> columnList = new java.util.ArrayList<String>();
		try {
			for (int i = 0, len = model.getColumnCount(); i < len; i++) {
				columnList.add(model.getColumnName(i));
			}
		} catch (TableDataException e) {
			FRContext.getLogger().error(e.getMessage(), e);
			return;
		}
		
		this.conditionPane.populateColumns(columnList.toArray(new String[0]));
	}
	
}