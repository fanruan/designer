package com.fr.design.gui.itableeditorpane;

import com.fr.base.Parameter;
import com.fr.design.editor.ValueEditorPane;
import com.fr.design.editor.ValueEditorPaneFactory;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;

import com.fr.stable.ParameterProvider;
import com.fr.stable.StringUtils;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * 参数列表 下拉参数模式.
* @author kunsnat E-mail:kunsnat@gmail.com
* @version 创建时间：2013-1-11 上午10:07:49
 */
public class ParameterTableModel extends UITableModelAdapter<ParameterProvider> {
	
	public static final int NO_CHART_USE = 0;
	public static final int CHART_NORMAL_USE = 1;
    public static final int FORM_NORMAL_USE = -1;

	private static final long serialVersionUID = 1L;
	protected Component component = null; //指定确认对话框的父窗口,bug40340
	public ParameterTableModel() {
		this(NO_CHART_USE);
	}

	public ParameterTableModel(int paraUseType) {
		super(new String[] { com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Parameter"), com.fr.design.i18n.Toolkit.i18nText("Value") });
		this.setColumnClass(new Class[] { ParameterEditor.class, ParameterValueEditor.class });
		this.setDefaultEditor(ParameterValueEditor.class, new ParameterValueEditor(paraUseType));
		this.setDefaultEditor(ParameterEditor.class, new ParameterEditor());
		this.setDefaultRenderer(ParameterValueEditor.class, new ParameterValueRenderer(paraUseType));
	}
	
	public ParameterTableModel(int paraUseType, Component component) {
		super(new String[] { com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Parameter"), com.fr.design.i18n.Toolkit.i18nText("Value") });
		this.setColumnClass(new Class[] { ParameterEditor.class, ParameterValueEditor.class });
		this.setDefaultEditor(ParameterValueEditor.class, new ParameterValueEditor(paraUseType));
		this.setDefaultEditor(ParameterEditor.class, new ParameterEditor());
		this.setDefaultRenderer(ParameterValueEditor.class, new ParameterValueRenderer(paraUseType));
		this.component = component;
	}

	public ParameterTableModel(ValueEditorPane valueEditorPane, ValueEditorPane valueRenderPane, Component component) {
		super(new String[] { com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Parameter"), com.fr.design.i18n.Toolkit.i18nText("Value") });
		this.setColumnClass(new Class[] { ParameterEditor.class, ParameterValueEditor.class });
		this.setDefaultEditor(ParameterValueEditor.class, new ParameterValueEditor(valueEditorPane));
		this.setDefaultEditor(ParameterEditor.class, new ParameterEditor());
		this.setDefaultRenderer(ParameterValueEditor.class, new ParameterValueRenderer(valueRenderPane));
		this.component = component;
	}

    /**
     * 单元格是否可编辑
     * @param row 行
     * @param col 列
     * @return 是否可以编辑
     */
	public boolean isCellEditable(int row, int col) {
		if(col == 1) {
			return this.getList().get(row) != null && StringUtils.isNotEmpty(this.getList().get(row).getName());
		}
		return true;
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

    /**
     * 生成工具栏上的一系列动作按钮
     * @return 返回table上的action数组.
     */
	@Override
	public UITableEditAction[] createAction() {
		return new UITableEditAction[] { new AddParameterAction(), new DeleteAction(), new MoveUpAction(), new MoveDownAction() };
	}
	

    /**
     * 生成上移下移按钮
     * @return UItableEdit数组.
     */
    public UITableEditAction[] createDBTableAction() {
        return new UITableEditAction[] { new MoveUpAction(), new MoveDownAction() };
    }

	private class AddParameterAction extends AddTableRowAction {

		public AddParameterAction() {

		}

		@Override
		public void actionPerformed(ActionEvent e) {
			super.actionPerformed(e);
			addParameter();
		}
	}
	
	private void addParameter() {
		Parameter para = new Parameter();
		addRow(para);
		fireTableDataChanged();
		table.getSelectionModel().setSelectionInterval(table.getRowCount() - 1, table.getRowCount() - 1);
	}

	protected class AddChartParameterAction extends AddTableRowAction {
		public AddChartParameterAction() {
			super();
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			super.actionPerformed(e);
			addParameter();
			// kunsnat: 屏蔽以下部分弹出框 直接添加行针对bug 10061
//			final ChartHotParameterEditPane pane = new ChartHotParameterEditPane();
//			pane.populate(para);
//			BasicDialog dialog = pane.showSmallWindow(SwingUtilities.getWindowAncestor(DesignerContext.getDesignerFrame()),new DialogActionAdapter() {
//
//				@Override
//				public void doOk() {
//					addRow(pane.update());
//					fireTableDataChanged();
//				}
//			});
//			dialog.setVisible(true);
		}
	}

	public class ParameterEditor extends AbstractCellEditor implements TableCellEditor {

		private UITextField textField;

		public ParameterEditor() {
			textField = new UITextField();
			this.addCellEditorListener(new CellEditorListener() {

				@Override
				public void editingCanceled(ChangeEvent e) {

				}

				@Override
				public void editingStopped(ChangeEvent e) {
					if (table.getSelectedRow() == -1){
                        return;
                    }
					ParameterProvider para = getList().get(table.getSelectedRow());
					String value = StringUtils.trimToNull(textField.getText());
					para.setName(value);
					fireTableDataChanged();
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

	private class ParameterValueEditor extends AbstractCellEditor implements TableCellEditor {
		private static final long serialVersionUID = 1L;
		private ValueEditorPane editor;

		public ParameterValueEditor(int paraUseType) {
			this(ValueEditorPaneFactory.createVallueEditorPaneWithUseType(paraUseType));
		}

		public ParameterValueEditor(ValueEditorPane valueEditorPane) {

			editor = valueEditorPane;

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
		private static final long serialVersionUID = 1L;
		private ValueEditorPane editor;
		private UILabel disableLable;

		public ParameterValueRenderer(int paraUseType) {
			this(ValueEditorPaneFactory.createVallueEditorPaneWithUseType(paraUseType));
		}

		public ParameterValueRenderer(ValueEditorPane valueEditorPane) {
			disableLable = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Set_Paramete_Name"));
			disableLable.setForeground(Color.pink);
			disableLable.setHorizontalAlignment(SwingConstants.CENTER);

			editor = valueEditorPane;
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