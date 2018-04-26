/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.mainframe.widget.editors;

import com.fr.design.data.DesignTableDataManager;
import com.fr.data.TableDataSource;
import com.fr.design.data.datapane.TableDataComboBox;
import com.fr.design.data.tabledata.wrapper.AbstractTableDataWrapper;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.editor.editor.Editor;
import com.fr.form.data.DataTableConfig;
import com.fr.general.Inter;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class DataTableEditor extends Editor<DataTableConfig> {

	private TableDataComboBox tableDataComboBox;
	private boolean isPopulate;

	public DataTableEditor() {
		this.initCompontents();
		this.setName(Inter.getLocText("FieldBinding"));
	}

	private void initCompontents() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		tableDataComboBox = new TableDataComboBox(getTableDataSource());

		tableDataComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				if (!isPopulate && evt.getStateChange() == ItemEvent.SELECTED) {
					DataTableEditor.this.fireStateChanged();
				}
			}
		});
		tableDataComboBox.setPreferredSize(new Dimension(55, 20));
		this.add(tableDataComboBox, BorderLayout.CENTER);
	}
	
	protected TableDataSource getTableDataSource() {
		return DesignTableDataManager.getEditingTableDataSource();
	}


    /**
     * 能接受的数据类型
     * @param object 类型
     * @return 是DataTableConfig类型则返回true
     */
	public boolean accept(Object object) {
		return object instanceof DataTableConfig;
	}

	@Override
	public DataTableConfig getValue() {
		return tableDataComboBox.getSelectedItem() == null ? null : new DataTableConfig(
				((AbstractTableDataWrapper) tableDataComboBox.getSelectedItem()).getTableDataName(),
				((AbstractTableDataWrapper) tableDataComboBox.getSelectedItem()).getTableData());
	}

	@Override
	public void setValue(DataTableConfig value) {
		if (value != null) {
			isPopulate = true;
			tableDataComboBox.setSelectedTableDataByName(value.getTableDataName());
			isPopulate = false;
		}
	}

	/**
	 * 得到图标名
	 *
	 * @return
	 */
	public String getIconName() {
		return "bind_ds_column";
	}

}