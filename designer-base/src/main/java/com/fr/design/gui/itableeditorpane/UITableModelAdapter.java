package com.fr.design.gui.itableeditorpane;

import com.fr.base.BaseUtils;
import com.fr.design.dialog.FineJOptionPane;
import com.fr.design.mainframe.DesignerContext;
import com.fr.log.FineLoggerFactory;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * 用来处理TableEditorPane的model
 * @editor zhou
 * @since 2012-3-28下午3:07:44
 */
public abstract class UITableModelAdapter<T extends Object> extends AbstractTableModel implements UITableEditorLoader {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3633792011995186187L;
	protected JTable table;

	private Class<?>[] classes;
	private String[] columnNames;

	// list里放的是一行数据
	private List<T> list = new ArrayList<T>();

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected UITableModelAdapter(String[] columnNames) {
		this.columnNames = columnNames;
		table = new JTable(this);
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
//		TableRowSorter rowSorter = new TableRowSorter(this);
//		table.setRowSorter(rowSorter);
	}

    /**
     * 创建表
     * @return   表
     */
	public JTable createTable() {
		return table;
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}

	public T getSelectedValue() {
		if (table.getSelectedRow() >= 0) {
			return list.get(table.getSelectedRow());
		}
		return null;
	}

	public void setColumnClass(Class<?>[] classes) {
		this.classes = classes;
	}

	public void setDefaultEditor(Class<?> columnClass, TableCellEditor editor) {
		table.setDefaultEditor(columnClass, editor);
	}

	public void setDefaultRenderer(Class<?> columnClass, TableCellRenderer renderer) {
		table.setDefaultRenderer(columnClass, renderer);
	}


	public void setList(List<T> list) {
		this.list = list;
	}

	public List<T> getList() {
		return this.list;
	}

    /**
     * 增加行
     * @param obj     行
     */
	public void addRow(T obj) {
		this.list.add(obj);
	}

	public void setSelectedValue(T value) {
		int selectedIndex = table.getSelectedRow();
		if (selectedIndex >= 0) {
			setRowAt(value, selectedIndex);
		}
	}

	public void setRowAt(T value, int rowIndwx) {
		this.list.set(rowIndwx, value);
	}

    /**
     * 去掉行
     * @param rowIndex 行号
     */
	public void removeRow(int rowIndex) {
		if (this.list != null) {
			this.list.remove(rowIndex);

			this.fireTableDataChanged();
		}
	}

    /**
     * 去除所有
     */
	public void clear() {
		this.list.clear();
	}

    /**
     * 通知所有linstener，Table每一行的值可能改变，并检查
     */
	@Override
	public void fireTableDataChanged() {
		super.fireTableDataChanged();
		table.getParent().validate();
		table.repaint();
		for (UITableEditAction action : this.createAction()) {
			action.checkEnabled();
		}
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (classes != null) {
			return classes[columnIndex];
		}
		return super.getColumnClass(columnIndex);
	}

	@Override
	public int getRowCount() {
		return this.list.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames == null ? 0 : columnNames.length;
	}

	@Override
	public abstract Object getValueAt(int rowIndex, int columnIndex);

    /**
     * 停止编辑
     */
	@Override
	public void stopCellEditing() {
		if (table.getCellEditor() != null) {
			try {
				table.getCellEditor().stopCellEditing();
			} catch (Exception ee) {
                FineLoggerFactory.getLogger().error(ee.getMessage(), ee);
			}
		}
	}

    /**
     * 单元格是否可编辑
     * @param row 行
     * @param col 列
     * @return    是则返回true
     */
	@Override
	public abstract boolean isCellEditable(int row, int col);

	protected abstract class AddTableRowAction extends UITableEditAction {
		public AddTableRowAction() {
			this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Insert"));
			this.setSmallIcon(BaseUtils.readIcon("/com/fr/base/images/cell/control/add.png"));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			stopCellEditing();
		}

		public void checkEnabled() {
		}

	}

	protected abstract class EditAction extends UITableEditAction {

		public EditAction() {
			this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Edit"));
			this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/control/edit.png"));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			final int selectedRow = table.getSelectedRow();
			if (selectedRow > table.getRowCount() - 1 || selectedRow < 0) {
				FineJOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_No-Alternatives"));
				return;
			}
			stopCellEditing();

		}

	}

	protected class DeleteAction extends UITableEditAction {

		private Component component = null;
		public DeleteAction() {
			this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Delete"));
			this.setSmallIcon(BaseUtils.readIcon("/com/fr/base/images/cell/control/remove.png"));
		}
		
		public DeleteAction(Component component){
			this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Delete"));
			this.setSmallIcon(BaseUtils.readIcon("/com/fr/base/images/cell/control/remove.png"));
			this.component = component;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			int[] selectedRow = table.getSelectedRows();
			if (ismultiSelected()) {
				JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(),com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Multiple_Select_Warn_Text"));
				return;
			}
			if (table.getCellEditor() != null) {
				try {
					table.getCellEditor().stopCellEditing();
				} catch (Exception ee) {
                    FineLoggerFactory.getLogger().error(ee.getMessage(), ee);
				}
			}
			if (getRowCount() < 1) {
				return;
			}
			
			if(component == null){
				component = DesignerContext.getDesignerFrame();
			}
			int val = FineJOptionPane.showConfirmDialog(component,
					com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Utils_Are_You_Sure_To_Remove_The_Selected_Item") + "?", com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Remove"),
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (val != JOptionPane.OK_OPTION) {
				return;
			}
			for (int i = 0; i < selectedRow.length; i++) {
				if (selectedRow[i] - i < 0) {
					continue;
				}
				removeRow(selectedRow[i] - i);
			}
			fireTableDataChanged();
			int selection = selectedRow[0] > table.getRowCount() ? table.getRowCount() - 1
					: (selectedRow[0] > 1 ? selectedRow[0] - 1 : 0);
			table.getSelectionModel().setSelectionInterval(selection, selection);
		}

        private boolean ismultiSelected(){
            int[] selectedRow = table.getSelectedRows();
            return (selectedRow.length == 1 && (selectedRow[0] > table.getRowCount() - 1 || selectedRow[0] < 0)) || selectedRow.length == 0;
        }
		@Override
		public void checkEnabled() {
			setEnabled(!ismultiSelected());
		}
	}

	protected class MoveUpAction extends UITableEditAction {
		public MoveUpAction() {
			this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Utils_Move_Up"));
			this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/control/up.png"));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			int selectedRow = table.getSelectedRow();
			stopCellEditing();
			if (getList().size() < 2 || selectedRow == 0) {
				return;
			}
			Collections.swap(getList(), selectedRow, selectedRow - 1);
			fireTableDataChanged();
			table.getSelectionModel().setSelectionInterval(selectedRow - 1, selectedRow - 1);
		}

		@Override
		public void checkEnabled() {
		}
	}

	protected class MoveDownAction extends UITableEditAction {

		public MoveDownAction() {
			this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Utils_Move_Down"));
			this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/control/down.png"));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			int selectedRow = table.getSelectedRow();
			stopCellEditing();
			if (getList().size() < 2 || selectedRow == getRowCount() - 1) {
				return;
			}
			Collections.swap(getList(), selectedRow, selectedRow + 1);
			fireTableDataChanged();
			table.getSelectionModel().setSelectionInterval(selectedRow + 1, selectedRow + 1);
		}

		@Override
		public void checkEnabled() {
		}
	}

}
