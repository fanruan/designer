package com.fr.design.gui.itableeditorpane;


import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class UIArrayTableModel extends UITableModelAdapter<Object[]> implements ActionStyle {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int[] array;

	public UIArrayTableModel(String[] s, int[] array) {
		super(s);
		this.array = array;
	}

	public Object getValueAt(int row, int column) {
		Object[] os = this.getList().get(row);
		if(os != null && column < os.length) {
			return os[column];
		}
		return null;
	}

	public void setValueAt(Object value, int row, int column) {
		Object[] os = this.getList().get(row);
		os[column] = value;
		fireTableCellUpdated(row, column);
	}

	public boolean isCellEditable(int row, int col) {
		return true;
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
}