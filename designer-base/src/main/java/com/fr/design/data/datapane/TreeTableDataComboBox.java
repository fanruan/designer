package com.fr.design.data.datapane;

import com.fr.data.TableDataSource;
import com.fr.data.impl.RecursionTableData;
import com.fr.design.data.tabledata.wrapper.ServerTableDataWrapper;
import com.fr.design.data.tabledata.wrapper.TableDataWrapper;
import com.fr.design.data.tabledata.wrapper.TemplateTableDataWrapper;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.icombobox.UIComboBoxRenderer;
import com.fr.file.TableDataConfig;

import javax.swing.*;
import java.awt.*;

public class TreeTableDataComboBox extends UIComboBox {
	private static final long serialVersionUID = 1L;
	private java.util.Map<String, TableDataWrapper> res_map = new java.util.LinkedHashMap<String, TableDataWrapper>();
	private TableDataSource source;
	
	public TreeTableDataComboBox(TableDataSource source) {
		this.source = source;
		this.setRenderer(new UIComboBoxRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value instanceof TableDataWrapper) {
					TableDataWrapper tabledatawrappe = (TableDataWrapper) value;
					this.setIcon(tabledatawrappe.getIcon());
					this.setText(tabledatawrappe.getTableDataName());
				}
				return this;
			}
		});
		refresh();
	}

    /**
     * 刷新
     */
	public void refresh() {
		java.util.Iterator<String> nameIt;
		DefaultComboBoxModel model = new DefaultComboBoxModel();
		this.setModel(model);
		if (source != null) {
			// 报表数据集
			nameIt = source.getTableDataNameIterator();
			while (nameIt.hasNext()) {
				String name = nameIt.next();
				if(source.getTableData(name) instanceof RecursionTableData) {
					TemplateTableDataWrapper templateTableDataWrappe = new TemplateTableDataWrapper(source.getTableData(name), name);
					model.addElement(templateTableDataWrappe);
					res_map.put(name, templateTableDataWrappe);
				}
			}
		}

		// 全局数据集
        TableDataConfig mgr = TableDataConfig.getInstance();
		nameIt = mgr.getTableDatas().keySet().iterator();
		while (nameIt.hasNext()) {
			String name = nameIt.next();
			if(mgr.getTableData(name) instanceof RecursionTableData) {
				ServerTableDataWrapper serverTableDataWrappe = new ServerTableDataWrapper(mgr.getTableData(name), name);
				model.addElement(serverTableDataWrappe);
				res_map.put(name, serverTableDataWrappe);
			}
		}
	}
	
	public void setSelectedTableDataByName(String name) {
		TableDataWrapper tableDataWrappe = res_map.get(name);
		this.getModel().setSelectedItem(tableDataWrappe);
	}

	@Override
	public TableDataWrapper getSelectedItem() {
		return (TableDataWrapper) dataModel.getSelectedItem();
	}
	
	public RecursionTableData getSelcetedTableData() {
		String name = ((TableDataWrapper) dataModel.getSelectedItem()).getTableDataName();
		return (RecursionTableData)source.getTableData(name);
	}
}