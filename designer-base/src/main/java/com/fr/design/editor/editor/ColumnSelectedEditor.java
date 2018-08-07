package com.fr.design.editor.editor;

import com.fr.design.data.DesignTableDataManager;
import com.fr.data.SimpleDSColumn;
import com.fr.design.data.datapane.TableDataComboBox;
import com.fr.design.data.tabledata.wrapper.TableDataWrapper;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.layout.FRGUIPaneFactory;

import com.fr.general.data.TableDataColumn;
import com.fr.stable.StringUtils;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 选择数据列编辑器
 *
 * @author zhou
 * @since 2012-3-29下午6:05:08
 */
public class ColumnSelectedEditor extends Editor<SimpleDSColumn> {
	TableDataComboBox tableDataComboBox;
	private UIComboBox columnNameComboBox;
	protected String[] columnNames;

	public ColumnSelectedEditor() {
		this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_DS_Column"));
		this.setLayout(FRGUIPaneFactory.createLeftZeroLayout());
		tableDataComboBox = new TableDataComboBox(DesignTableDataManager.getEditingTableDataSource());
		columnNames = new String[0];
		tableDataComboBox.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
                //这边需要重新初始化columnNames, 否则nameList长度和columnNames长度不同导致出錯。
				if (tableDataComboBox.getSelectedItem() == null) {
					return;
				}
                List<String> nameList = tableDataComboBox.getSelectedItem().calculateColumnNameList();
                columnNames = new String[nameList.size()];
				columnNames = tableDataComboBox.getSelectedItem().calculateColumnNameList().toArray(columnNames);
				columnNameComboBox.removeAllItems();
				for (int i = 0; i < columnNames.length; i++) {
					columnNameComboBox.addItem(columnNames[i]);
				}
				columnNameComboBox.validate();
			}
		});
		columnNameComboBox = new UIComboBox();
		tableDataComboBox.setPreferredSize(new Dimension(82, 20));
		this.add(tableDataComboBox);
		columnNameComboBox.setPreferredSize(new Dimension(82, 20));
		this.add(columnNameComboBox);
	}

	@Override
	public SimpleDSColumn getValue() {
		if (this.tableDataComboBox.getSelectedItem() == null || this.columnNameComboBox.getSelectedItem() == null) {
			return null;
		}
		SimpleDSColumn dsColumn = new SimpleDSColumn();
		TableDataWrapper tableDataWrappe = this.tableDataComboBox.getSelectedItem();
		dsColumn.setDsName(tableDataWrappe.getTableDataName());
		TableDataColumn column;
		String columnExp = (String) this.columnNameComboBox.getSelectedItem();
		if (StringUtils.isNotBlank(columnExp) && (columnExp.length() > 0 && columnExp.charAt(0) == '#') && !columnExp.endsWith("#")) {
			String number = columnExp.substring(1);
			Pattern pattern = Pattern.compile("[^\\d]");
			if (pattern.matcher(number).find()) {
				column = TableDataColumn.createColumn(columnExp);
			} else {
				int serialNumber = Integer.parseInt(columnExp.substring(1));
				column = TableDataColumn.createColumn(serialNumber);
			}
		} else {
			column = TableDataColumn.createColumn(columnExp);
		}
		dsColumn.setColumn(column);
		return dsColumn;
	}

	public String getIconName() {
		return "ds_column";
	}

	@Override
	public boolean accept(Object object) {
		return object instanceof SimpleDSColumn;
	}

	@Override
	public void setValue(SimpleDSColumn value) {
		if (value == null) {
			return;
		} else {
			tableDataComboBox.setSelectedTableDataByName(value.getDsName());
			columnNameComboBox.setSelectedItem(TableDataColumn.getColumnName(value.getColumn()));
		}
	}

	@Override
	public void clearData() {
        tableDataComboBox.setSelectedItem(null);
        columnNameComboBox.setSelectedItem(null);
    }
}
