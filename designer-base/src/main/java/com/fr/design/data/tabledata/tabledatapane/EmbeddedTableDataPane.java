package com.fr.design.data.tabledata.tabledatapane;

import com.fr.data.impl.EmbeddedTableData;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.date.UIDatePicker;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.DesignerContext;
import com.fr.general.DateUtils;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.StringUtils;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;


public class EmbeddedTableDataPane extends AbstractTableDataPane<EmbeddedTableData> {
	private JTable dataJTable ;
	private JScrollPane scrollPane;
	private UILabel coordinatelabel;
	private UIButton insertRowButton;
	private UIButton removeRowButton;
	private UIButton columnSetButton;

	/**
	 * Constructor
	 * 
	 */
	public EmbeddedTableDataPane() {
		initComponents();
	}

	// additional methods
	protected void initComponents() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());

		// prepare the data in this way as default if no arguments is passed in
		dataJTable = new JTable(new EmbeddedTableModel(new EmbeddedTableData()));
		scrollPane = new JScrollPane(dataJTable);
		this.add(scrollPane, BorderLayout.CENTER);

		// 显示double,date类;渲染date类
		dataJTable.setDefaultRenderer(Double.class, new DoubleRenderer());
		dataJTable.setDefaultRenderer(Date.class, new DateRenderer());
		dataJTable.setDefaultEditor(Date.class, new DateEditor(new UIDatePicker(UIDatePicker.STYLE_CN_DATE1)));
		tableStructureChanged();

		// 单击即可编辑
		editbysingleclick(dataJTable, String.class);
		editbysingleclick(dataJTable, Date.class);
		editbysingleclick(dataJTable, Double.class);
		editbysingleclick(dataJTable, Integer.class);

		// AUTO RESIZE
		// dataJTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		dataJTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		dataJTable.setRowSelectionAllowed(true);
		dataJTable.setColumnSelectionAllowed(true);


		// peter:控制Panel
		JPanel northPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
		this.add(northPane, BorderLayout.NORTH);
		northPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 3, 0));

		// kel:在左上角加一个JLabel用来显示坐标，方便用户查看。
		coordinatelabel = new UILabel("0/0,0/0");
		coordinatelabel.setHorizontalAlignment(UILabel.CENTER);
		northPane.add(coordinatelabel);

		dataJTable.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				// kel：加上一个鼠标点击的监控，鼠标点击时，返回选中的行坐标和列坐标。
				selectedChanged();
			}
		});
		dataJTable.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				selectedChanged();
			}
		});
        initOtherNorthPaneComponents(northPane);

	}

    private void initOtherNorthPaneComponents(JPanel northPane) {
        // 表结构设置
        columnSetButton = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Tabledata_Embedded_Table_Design"));
        columnSetButton.setMnemonic('C');
        northPane.add(columnSetButton);
        columnSetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {

                final EmbeddedTableModel localDefaultModel = (EmbeddedTableModel) dataJTable.getModel();
                final EmbeddedTableDataDefinedPane columnSetPane = new EmbeddedTableDataDefinedPane();
                columnSetPane.populate(((EmbeddedTableModel) dataJTable.getModel()).getEditableTableData());

                DialogActionAdapter l = new DialogActionAdapter() {
                    public void doOk() {
                        localDefaultModel.setEditableTableData(columnSetPane.update());
                        localDefaultModel.fireTableStructureChanged();
                        localDefaultModel.fireTableDataChanged();
                        tableStructureChanged();
                        selectedChanged();
                    }
                };

                BasicDialog dlg = columnSetPane.showWindow(DesignerContext.getDesignerFrame(), l);
                dlg.setAlwaysOnTop(true);
                dlg.setVisible(true);
            }
        });

        // DataSourceEditor-Insert_Row
        insertRowButton = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Utils_Insert_Row"));
        insertRowButton.setMnemonic('I');
        northPane.add(insertRowButton);
        insertRowButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                insertRow();
            }
        });

        // DataSourceEditor-Remove_Row
        removeRowButton = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Utils_Delete_Row"));
        removeRowButton.setMnemonic('R');
        northPane.add(removeRowButton);
        removeRowButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                removeRow();
            }
        });

    }

	@Override
	protected String title4PopupWindow() {
		return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_DS_Embedded_TableData");
	}



	@Override
	public void checkValid() throws Exception {
		// alex:停止当前的编辑
		try {
			dataJTable.getCellEditor().stopCellEditing();
		} catch (Exception e) {
		}
	}

	private void selectedChanged() {
		int a = dataJTable.getSelectedRow() + 1, b = dataJTable.getSelectedColumn(), c = dataJTable.getRowCount(), d = dataJTable.getColumnCount() - 1;
		if (b < 0) {
			b = 0;
		}
		String coordinate = a + "/" + c + "," + b + "/" + d;
		coordinatelabel.setText(coordinate);
	}


	@Override
	public void populateBean(EmbeddedTableData ob) {
		EmbeddedTableModel localDefaultModel = (EmbeddedTableModel) dataJTable.getModel();
		localDefaultModel.clear();
		// 读取tabeldata
		for (int i = 0; i < ob.getColumnCount(); i++) {
			String columnName = ob.getColumnName(i);
			Class columnClass = ob.getColumnClass(i);
			localDefaultModel.addColumn(columnName, columnClass);
		}
		for (int j = 0; j < ob.getRowCount(); j++) {
			localDefaultModel.addNewRowData(j);
		}
		for (int i = 0; i < ob.getColumnCount(); i++) {
			for (int j = 0; j < ob.getRowCount(); j++) {
				localDefaultModel.setValueAt(ob.getValueAt(j, i), j, i + 1);
			}
		}
		localDefaultModel.fireTableStructureChanged();
		localDefaultModel.fireTableDataChanged();
		tableStructureChanged();
		this.repaint();
	}

	@Override
	public EmbeddedTableData updateBean() {
		if (dataJTable.getCellEditor() != null) {
			dataJTable.getCellEditor().stopCellEditing();
		}
		EmbeddedTableModel localDefaultModel = (EmbeddedTableModel) dataJTable.getModel();
		try {
			return (EmbeddedTableData) localDefaultModel.getEditableTableData().clone();
		} catch (CloneNotSupportedException e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
			return new EmbeddedTableData();
		}
	}

	private void tableStructureChanged() {
		// 行号显示,每次dateJTable的表结构发生变化时都要调用
		TableColumn tableColumn = dataJTable.getColumnModel().getColumn(0);
		tableColumn.setCellRenderer(new CellRenderer());
		tableColumn.setMaxWidth(dataJTable.getColumnCount());
	}

	private class DoubleRenderer extends DefaultTableCellRenderer {
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			setHorizontalAlignment(RIGHT);
			return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		}
	}

	private class DateRenderer extends DefaultTableCellRenderer {
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			Date date2Format = DateUtils.object2Date(value, true);
			return super.getTableCellRendererComponent(table, date2Format == null ? StringUtils.EMPTY : DateUtils.DATEFORMAT1.format(date2Format), isSelected, hasFocus, row,
					column);
		}
	}

	private class DateEditor extends DefaultCellEditor {
		public DateEditor(final UIDatePicker datepicker) {
			super(datepicker);
		}

		public Object getCellEditorValue() {
			SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = null;
			try {
                synchronized (this) {
                    date = simpledateformat.parse(((UIDatePicker) this.editorComponent).getSelectedItem().toString());
                }
			} catch (Exception e) {
				date = new Date();
			}
			return date;
		}

		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			Date date = DateUtils.object2Date(value, false);
			return super.getTableCellEditorComponent(table, date, isSelected, row, column);
		}
	}

	private class CellRenderer extends DefaultTableCellRenderer {
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			if (column == 0) {
				setBackground(new Color(229, 229, 229));
				setHorizontalAlignment(CENTER);
			}
			return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		}
	}

	private void editbysingleclick(JTable table, Class cls) {
		TableCellEditor tableCellEditor = table.getDefaultEditor(cls);
		if (tableCellEditor != null) {
			((DefaultCellEditor) tableCellEditor).setClickCountToStart(1);
		}
	}


	private void insertRow() {
		EmbeddedTableModel localDefaultModel = (EmbeddedTableModel) dataJTable.getModel();

		// alex:停止当前的编辑
		try {
			dataJTable.getCellEditor().stopCellEditing();
		} catch (Exception e) {
		}

		int indexedrow = dataJTable.getSelectedRow();
		int indexedcol = dataJTable.getSelectedColumn();
		if (indexedrow == -1) {
			indexedrow = dataJTable.getRowCount();
		}
		if (indexedcol == -1) {
			indexedcol = 0;
		}

		localDefaultModel.addNewRowData(indexedrow);
		localDefaultModel.fireTableDataChanged();
		// kel:添加一个focus监控，在插入行时获得焦点。（未实现）
		dataJTable.requestFocusInWindow();
		dataJTable.setRowSelectionInterval(indexedrow, indexedrow);
		dataJTable.setColumnSelectionInterval(indexedcol, indexedcol);
		dataJTable.editCellAt(indexedrow, indexedcol);
		selectedChanged();
	}


	private void removeRow() {
		EmbeddedTableModel localDefaultModel = (EmbeddedTableModel) dataJTable.getModel();

		// peter:开始删除行
		int selectedRow = dataJTable.getSelectedRow();

		int selectedCol = dataJTable.getSelectedColumn();
		if (selectedRow == -1) {
			selectedRow = dataJTable.getRowCount() - 1;
		}
		if (selectedCol == -1) {
			selectedCol = 0;
		}

		// alex:停止当前的编辑
		try {
			dataJTable.getCellEditor().stopCellEditing();
		} catch (Exception e) {
		}

		for (int i = 0; i < dataJTable.getSelectedRowCount(); i++) {
			localDefaultModel.removeRow(selectedRow);
		}
		localDefaultModel.fireTableDataChanged();

		// peter:调整Selection
		int rowCount = localDefaultModel.getRowCount();
		if (rowCount > 0) {
			if (selectedRow < rowCount) {
				dataJTable.setRowSelectionInterval(selectedRow, selectedRow);
				dataJTable.setColumnSelectionInterval(selectedCol, selectedCol);
				dataJTable.editCellAt(selectedRow, selectedCol);
			} else {
				dataJTable.setRowSelectionInterval(rowCount - 1, rowCount - 1);
				dataJTable.setColumnSelectionInterval(selectedCol, selectedCol);
				dataJTable.editCellAt(rowCount - 1, selectedCol);
			}
		}
		selectedChanged();
	}
}
