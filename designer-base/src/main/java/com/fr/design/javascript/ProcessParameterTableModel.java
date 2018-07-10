package com.fr.design.javascript;

import com.fr.base.Parameter;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itableeditorpane.UITableEditAction;
import com.fr.design.gui.itableeditorpane.UITableModelAdapter;
import com.fr.design.gui.ilist.CheckBoxList;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.editor.ValueEditorPane;
import com.fr.design.editor.ValueEditorPaneFactory;
import com.fr.design.editor.editor.Editor;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.stable.ParameterProvider;
import com.fr.stable.project.ProjectConstants;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class ProcessParameterTableModel extends UITableModelAdapter<ParameterProvider> {
	private static final long serialVersionUID = 1L;

	public ProcessParameterTableModel() {
		super(new String[] { Inter.getLocText("Parameter"), Inter.getLocText("Value") });
		this.setColumnClass(new Class[] { JLabel.class, ParameterValueEditor.class });
		this.setDefaultEditor(ParameterValueEditor.class, new ParameterValueEditor());
		this.setDefaultRenderer(ParameterValueEditor.class, new ParameterValueRenderer());
	}

	/**
	 * 单元格是否可编辑
	 * @row 单元格的行号
	 * @col 单元格的列号
	 * @return 是否可编辑
	 */
	public boolean isCellEditable(int row, int col) {
		if (col == 0) {
			return false;
		}
		return true;
	}
	
	protected Editor[] getCorrespondEditors() {
		return ValueEditorPaneFactory.basicEditors();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		ParameterProvider para = this.getList().get(rowIndex);
		switch (columnIndex) {
		case 0:
			return para.getName();
		case 1:
			return para.getValue();
		}
		return null;
	}
	
	@Override
	/**
	 * 可以做的操作（新增参数和删除参数）
	 * @return 可以进行的操作（数组）
	 */
	public UITableEditAction[] createAction() {
		return new UITableEditAction[] { new AddParameterAction(), new DeleteAction()};
	}

	private class ProcessParaPane extends BasicPane {
		private CheckBoxList list;
		
		public ProcessParaPane() {
			this.initComponents();
		}
		
		private void initComponents() {
			this.setLayout(new BorderLayout());
			list = new CheckBoxList(ProcessTransitionAdapter.getParaNamesWithShared(getEditFilePath()));
			this.add(new JScrollPane(list), BorderLayout.CENTER);
		}
		
		public void populator(List<String> list) {
			this.list.setItems(list.size() < 1 ? new String[0] : list.toArray(new String[list.size()]));
		}
		
		public String[] update() {
			Object[] os = this.list.getSelectedValues();
			String[] ss = new String[os.length];
			for (int i = 0, len = os.length; i < len; i++) {
				ss[i] = (String)os[i];
			}
			return ss;
		}
		
		@Override
		protected String title4PopupWindow() {
			return Inter.getLocText("Parameter");
		}
		
		private String getEditFilePath() {
			//wei : 在文件夹下的模板读取不到流程参数
			String path = DesignerContext.getDesignerFrame().getSelectedJTemplate().getEditingFILE().getPath();
			if(path.startsWith(ProjectConstants.REPORTLETS_NAME)) {
				path = path.substring(ProjectConstants.REPORTLETS_NAME.length() + 1);
			}
			return path;
		}
		
	}
	
	/**
	 * 刷新参数
	 * @param names
	 */
	public void refreshParas(String[] names) {
		List<ParameterProvider> nl = new ArrayList<ParameterProvider>();
		for (int i = 0, len = names == null ? 0 :names.length; i < len; i++) {
			nl.add(this.getParaByName(names[i]));
		}
		this.setList(nl);
	}
	
	private ParameterProvider getParaByName(String name) {
		List<ParameterProvider> list = this.getList();
		for (int i = 0, len= list.size(); i < len; i++) {
			if (ComparatorUtils.equals(list.get(i).getName(), name)) {
				return list.get(i);
			}
		}
		return new Parameter(name);
	}
	
	protected class AddParameterAction extends AddTableRowAction {
		private ProcessParaPane pp;
		
		public AddParameterAction() {
			super();
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			super.actionPerformed(e);
			if (pp == null) {
				pp = new ProcessParaPane();
			}
			List<ParameterProvider> list = getList();
			List<String> sist = new ArrayList<String>();
			for (int i = 0, len = list.size(); i < len; i++) {
				sist.add(list.get(i).getName());
			}
			pp.populator(sist);
			pp.showSmallWindow(DesignerContext.getDesignerFrame(), new DialogActionAdapter() {
				@Override
				public void doOk() {
					refreshParas(pp.update());
					fireTableDataChanged();
				}
			}).setVisible(true);
		}
	}

	private class ParameterValueEditor extends AbstractCellEditor implements TableCellEditor {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private ValueEditorPane editor = ValueEditorPaneFactory.createValueEditorPane(ProcessParameterTableModel.this
				.getCorrespondEditors(), null, null);

		public ParameterValueEditor() {
			this.addCellEditorListener(new CellEditorListener() {

				@Override
				public void editingCanceled(ChangeEvent e) {

				}

				@Override
				public void editingStopped(ChangeEvent e) {
					if (table.getSelectedRow() == -1) {
						return;
					}
					ParameterProvider para = getList().get(table.getSelectedRow());
					para.setValue(getCellEditorValue());
					fireTableDataChanged();
				}
			});
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			editor.populate(value == null ? "" : value);
			return editor;
		}

		@Override
		public Object getCellEditorValue() {
			return editor.update();
		}

	}

	private class ParameterValueRenderer extends DefaultTableCellRenderer {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private ValueEditorPane editor = ValueEditorPaneFactory.createValueEditorPane(ProcessParameterTableModel.this
				.getCorrespondEditors(), null, null);
		private UILabel disableLable;

		public ParameterValueRenderer() {
			disableLable = new UILabel(Inter.getLocText("Set-Parameter-Name"));
			disableLable.setHorizontalAlignment(SwingConstants.CENTER);
		}

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			if (table.isCellEditable(row, column)) {
				if (value == null) {
					editor.populate("");
				} else {
					editor.populate(value);
				}
				return editor;
			} else {
				return disableLable;
			}
		}
	}
}