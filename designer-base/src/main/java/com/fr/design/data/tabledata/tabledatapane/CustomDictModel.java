package com.fr.design.data.tabledata.tabledatapane;

import com.fr.design.gui.itableeditorpane.ActionStyle;
import com.fr.design.gui.itableeditorpane.UIArrayTableModel;
import com.fr.design.gui.itableeditorpane.UITableEditAction;
import com.fr.design.gui.itableeditorpane.UITableModelAdapter;
import com.fr.design.gui.itextfield.UITextField;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class CustomDictModel extends UITableModelAdapter<Object[]> implements ActionStyle {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int[] array;
	private ActionListener actionListener;

	public CustomDictModel(String[] s, int[] array, ActionListener actionListener) {
		super(s);
		this.array = array;
		this.actionListener = actionListener;
		this.setColumnClass(new Class[] { ParameterEditor.class, ParameterEditor.class });
		this.setDefaultEditor(ParameterEditor.class, new ParameterEditor());
	}

	@Override
	public Object getValueAt(int row, int column) {
		Object[] os = this.getList().get(row);
		return os[column];
	}

	@Override
	public void setValueAt(Object value, int row, int column) {
		Object[] os = this.getList().get(row);
		os[column] = value;
	}

	public boolean isCellEditable(int row, int col) {
		return true;
	}

	@Override
	public void fireTableDataChanged() {
		super.fireTableDataChanged();
		if (actionListener != null) {
			actionListener.actionPerformed(null);
		}
	}

	@Override
	public UITableEditAction[] createAction() {
		List<UITableEditAction> list = new ArrayList<UITableEditAction>();
		UITableEditAction[] tableEditAction = new UITableEditAction[array.length];
		for (int i : array) {
			switch (i) {
			case UIArrayTableModel.ADDSTYLE:
				list.add(new AddRow());
				break;
			case UIArrayTableModel.ADDJSFILE:
				break;
			case UIArrayTableModel.ADDCSSFILE:
				break;
			case UIArrayTableModel.DELETESTYLE:
				list.add(new DeleteAction());
				break;
			case UIArrayTableModel.MOVEUPSTYLE:
				list.add(new MoveUpAction());
				break;
			case UIArrayTableModel.MOVEDOWNSTYLE:
				list.add(new MoveDownAction());
				break;

			}
		}
		return list.toArray(tableEditAction);
	}

	public class AddRow extends AddTableRowAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			super.actionPerformed(e);
			addRow(new Object[getColumnCount()]);
			fireTableDataChanged();
		}
	}

	class ParameterEditor extends AbstractCellEditor implements TableCellEditor {

		private static final long serialVersionUID = 1L;
		private UITextField textField;

		public ParameterEditor() {
			textField = new UITextField();
			this.addCellEditorListener(new CellEditorListener() {

				@Override
				public void editingCanceled(ChangeEvent e) {
					//do nothing
				}

				@Override
				public void editingStopped(ChangeEvent e) {
					if (actionListener != null) {
						actionListener.actionPerformed(null);
					}
				}
			});
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			textField.setText((String) value);
			return textField;
		}

		@Override
		public Object getCellEditorValue() {
			return textField.getText();
		}
	}
}