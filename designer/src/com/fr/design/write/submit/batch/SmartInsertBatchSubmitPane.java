package com.fr.design.write.submit.batch;

import com.fr.cache.list.IntList;
import com.fr.data.ClassSubmitJob;
import com.fr.design.actions.UpdateAction;
import com.fr.design.cell.smartaction.AbstractSmartJTablePaneAction;
import com.fr.design.cell.smartaction.SmartJTablePane;
import com.fr.design.cell.smartaction.SmartJTablePaneAction;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.editor.ValueEditorPaneFactory;
import com.fr.design.gui.controlpane.NameObjectCreator;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UIBasicSpinner;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.mainframe.JWorkBook;
import com.fr.design.selection.SelectionEvent;
import com.fr.design.selection.SelectionListener;
import com.fr.design.write.submit.CustomSubmitJobPane;
import com.fr.design.write.submit.SmartInsertDMLJobPane;
import com.fr.design.write.submit.SubmitJobListPane;
import com.fr.general.Inter;
import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.FloatSelection;
import com.fr.grid.selection.Selection;
import com.fr.stable.ColumnRow;
import com.fr.stable.ColumnRowGroup;
import com.fr.write.DMLConfigJob;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created by loy on 16/8/16.
 */
public class SmartInsertBatchSubmitPane extends BatchSubmitPane {
    private static final Selection NO_SELECTION = new CellSelection(-1, -1, -1, -1);
    private ElementCasePane ePane;
    private static int CELL_GROUP_LIMIT = 6;

    public SmartInsertBatchSubmitPane(ElementCasePane ePane) {
        super(ValueEditorPaneFactory.extendedCellGroupEditors());
        this.ePane = ePane;
    }

    public SmartInsertBatchSubmitPane() {
        super(ValueEditorPaneFactory.extendedCellGroupEditors());
        JTemplate jTemplate = DesignerContext.getDesignerFrame().getSelectedJTemplate();
        this.ePane = ((JWorkBook) jTemplate).getEditingElementCasePane();
    }

    @Override
    protected SubmitJobListPane createSubmitJobListPane() {
        return new SmartInsertSubmitJobListPane();
    }

    class SmartInsertSubmitJobListPane extends SubmitJobListPane {

        public SmartInsertSubmitJobListPane() {
            super(ePane);
        }

        public void hideParentDialog() {
            hideDialog4AddCellAction();
        }

        public void showParentDialog() {
            showDialogAfterAddCellAction();
        }

        @Override
        public NameableCreator[] createNameableCreators() {
            return new NameableCreator[] {
                    new NameObjectCreator(Inter.getLocText(new String[]{"Submit", "Event"}),
                            "/com/fr/web/images/reportlet.png",
                            DMLConfigJob.class,
                            SmartInsertDMLJobPane.class),
                    new NameObjectCreator(Inter.getLocText(new String[]{"Custom", "Event"}),
                            "/com/fr/web/images/reportlet.png",
                            ClassSubmitJob.class,
                            CustomSubmitJobPane.class) };
        }
    }

    @Override
    protected UpdateAction[] getActions() {
        return new UpdateAction[] {
                new BatchSubmitPane.SmartAddFieldsAction(),
                new BatchSubmitPane.AddFieldAction(),
                new SmartAddCellAction(),
                new SmartAddCellGroupAction(),
                new BatchModCellAction(),
                new BatchSubmitPane.RemoveFieldAction()
        };
    }

    public class BatchModCellAction extends UpdateAction {
        public BatchModCellAction() {
            this.setName(Inter.getLocText("RWA-Batch_Modify_Cells"));
        }

        /**
         * 执行事件
         * @param evt 事件对象
         */
        public void actionPerformed(ActionEvent evt) {
            BasicPane bPane = new BasicPane() {
                @Override
                protected String title4PopupWindow() {
                    return Inter.getLocText("RWA-Batch_Modify_Cells");
                }
            };
            bPane.setLayout(FRGUIPaneFactory.createBorderLayout());
            bPane.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
            final UIBasicSpinner columnSpinner = new UIBasicSpinner();
            final UIBasicSpinner rowSpinner = new UIBasicSpinner();
            Component[][] coms = new Component[][] { { new UILabel(Inter.getLocText("RWA-Row_Offset")), rowSpinner },{ new UILabel(Inter.getLocText("RWA-Column_Offset")), columnSpinner } };
            double p = TableLayout.PREFERRED;
            double f = TableLayout.FILL;
            bPane.add(TableLayoutHelper.createTableLayoutPane(coms, new double[]{p, p}, new double[]{p, f}), BorderLayout.NORTH);
            BasicDialog dlg = bPane.showSmallWindow(SwingUtilities.getWindowAncestor(SmartInsertBatchSubmitPane.this), new DialogActionAdapter() {
                public void doOk() {
                    int row_offset = ((Number)rowSpinner.getValue()).intValue();
                    int column_offset = ((Number)columnSpinner.getValue()).intValue();
                    BatchSubmitPane.KeyColumnTableModel model = (BatchSubmitPane.KeyColumnTableModel)keyColumnValuesTable.getModel();
                    int[] selectedRows = keyColumnValuesTable.getSelectedRows();
                    // 如果一行都没选中,取所有的行
                    if (selectedRows.length == 0) {
                        selectedRows = IntList.range(model.getRowCount());
                    }
                    for (int i = 0; i < selectedRows.length; i++) {
                        int row = selectedRows[i];
                        BatchSubmitPane.KeyColumnNameValue kcnv = model.getKeyColumnNameValue(row);
                        if (kcnv.cv.obj instanceof ColumnRow) {
                            ColumnRow or = (ColumnRow)kcnv.cv.obj;
                            int n_column = or.getColumn() + column_offset;
                            if (n_column < 0) {
                                n_column = 0;
                            }
                            int n_row = or.getRow() + row_offset;
                            if (n_row < 0) {
                                n_row = 0;
                            }
                            kcnv.cv.obj = ColumnRow.valueOf(n_column, n_row);
                        }
                    }
                    model.fireTableDataChanged();
                    keyColumnValuesTable.validate();
                    for (int i = 0; i < selectedRows.length; i++) {
                        keyColumnValuesTable.addRowSelectionInterval(selectedRows[i], selectedRows[i]);
                    }
                }
            });
            dlg.setVisible(true);
        }
    }

    public class SmartAddCellAction extends UpdateAction {
        public SmartAddCellAction() {
            this.setName(Inter.getLocText("RWA-Smart_Add_Cells"));
        }

        /**
         * 执行事件
         * @param evt 事件s
         */
        public void actionPerformed(ActionEvent evt) {

            // Grid.GridSelectionListener
            if (ePane == null) {
                return;
            }

			/*
			 * 布局
			 */
            BasicPane bPane = new SmartJTablePane4DB(keyColumnValuesTable.getTableModel4SmartAddCell(), ePane);

            // ReportWriteAttrDialog.this.setVisible(false);
            hideDialog4AddCellAction();
			/*
			 * 当前的ReportPane不可编辑,不可切换Sheet,加GridSelectionChangeListener
			 */
            ePane.setSelection(NO_SELECTION);
            ePane.setEditable(false);
            ePane.getGrid().setNotShowingTableSelectPane(false);

            BasicDialog dlg = bPane.showWindow(SwingUtilities.getWindowAncestor(SmartInsertBatchSubmitPane.this));

            dlg.setModal(false);
            dlg.setVisible(true);
        }
    }

    public class SmartAddCellGroupAction extends UpdateAction {
        public SmartAddCellGroupAction() {
            this.setName(Inter.getLocText("RWA-Smart_Add_Cell_Group"));
        }

        /**
         * 智能添加单元格组
         * @param e 事件s
         */
        public void actionPerformed(ActionEvent e) {
            if (ePane == null) {
                return;
            }

            BasicPane bPane = new SmartJTablePane4DB(keyColumnValuesTable.getTableModel4SmartAddCell(), ePane, true);

            // ReportWriteAttrDialog.this.setVisible(false);
            hideDialog4AddCellAction();
			/*
			 * 当前的ReportPane不可编辑,不可切换Sheet,加GridSelectionChangeListener
			 */
            ePane.setSelection(NO_SELECTION);
            ePane.setEditable(false);
            ePane.getGrid().setNotShowingTableSelectPane(false);

            BasicDialog dlg = bPane.showWindow(SwingUtilities.getWindowAncestor(SmartInsertBatchSubmitPane.this));

            dlg.setModal(false);
            dlg.setVisible(true);
        }
    }

    private void showDialogAfterAddCellAction() {
        Container dialog = this;
        if (parentPane != null && parentPane.getContentDBManiPane() instanceof SmartInsertBatchSubmitPane && parentPane.getContentDBManiPane() != this) {
            ((SmartInsertBatchSubmitPane)parentPane.getContentDBManiPane()).showDialogAfterAddCellAction();
        }
        while (dialog.getParent() != null) {
            dialog = dialog.getParent();
            if (dialog instanceof SmartInsertSubmitJobListPane) {
                ((SmartInsertSubmitJobListPane)dialog).showParentDialog();
            } else if (dialog instanceof Dialog) {
                dialog.setVisible(true);
            }
        }
    }

    private void hideDialog4AddCellAction() {
        Container dialog = this;
        if (parentPane != null && parentPane.getContentDBManiPane() instanceof SmartInsertBatchSubmitPane && parentPane.getContentDBManiPane() != this) {
            ((SmartInsertBatchSubmitPane)parentPane.getContentDBManiPane()).hideDialog4AddCellAction();
        }
        while (dialog.getParent() != null) {
            dialog = dialog.getParent();
            if (dialog instanceof SmartInsertSubmitJobListPane) {
                ((SmartInsertSubmitJobListPane)dialog).hideParentDialog();
            } else if (dialog instanceof Dialog) {
                // 条件属性中添加的控件的话有两层dialog，需要都隐藏
                dialog.setVisible(false);
            }
        }
    }

    /**
     * 检测是否合法
     * @throws Exception
     */
    public void checkValid() throws Exception {
        BatchSubmitPane.KeyColumnTableModel model = (BatchSubmitPane.KeyColumnTableModel)keyColumnValuesTable.getModel();
        int cnt = model.getRowCount();
        int groupLength = -1;
        for (int i=0; i<cnt; i++) {
            BatchSubmitPane.KeyColumnNameValue kcv = model.getKeyColumnNameValue(i);
            Object val = kcv.cv.obj;
            if (val instanceof ColumnRowGroup) {
                int len = ((ColumnRowGroup) val).getSize();
                if (groupLength < 0) {
                    groupLength = len;
                } else if (len != groupLength) {
                    throw new Exception(Inter.getLocText("Report-Write_Attributes_Group_Warning"));
                }
            }
        }
    }

    private boolean possibleParentContainer(Container p) {
        return p instanceof Dialog || p instanceof BasicPane ||
                p instanceof JPanel || p instanceof JRootPane || p instanceof JLayeredPane;
    }

    private class SmartJTablePane4DB extends SmartJTablePane {

        // 是否是单元格组
        private boolean isCellGroup = false;

        // 单元格组要记录下之前的选中情况
        private CellSelection oriCellSelection = null;

        public SmartJTablePane4DB(BatchSubmitPane.KeyColumnTableModel model, ElementCasePane actionReportPane) {
            this(model, actionReportPane, false);
        }

        public SmartJTablePane4DB(BatchSubmitPane.KeyColumnTableModel model, ElementCasePane actionReportPane, boolean isCellGroup) {
            super(model, actionReportPane);
            this.isCellGroup = isCellGroup;
            this.setCellRenderer();
            this.changeGridSelectionChangeListener(isCellGroup ? groupListener : listener);
            this.changeSmartJTablePaneAction(a);
        }

        @Override
        protected String title4PopupWindow() {
            if (isCellGroup) {
                return Inter.getLocText("RWA-Smart_Add_Cell_Group");
            } else {
                return Inter.getLocText("RWA-Smart_Add_Cells");
            }
        }

        @Override
        public void setCellRenderer() {
			/*
			 * set Width
			 */
            TableColumn column0 = table.getColumnModel().getColumn(0);
            column0.setMaxWidth(40);
			/*
			 * 设置Column 1的Renderer
			 */
            TableColumn column1 = table.getColumnModel().getColumn(1);
            column1.setCellRenderer(new BatchSubmitPane.ColumnNameTableCellRenderer());

			/*
			 * 设置Column 2的Renderer
			 */
            TableColumn column2 = table.getColumnModel().getColumn(2);
//			column2.setCellRenderer(new SelectedColumnValueTableCellRenderer());

            if (isCellGroup) {
                column2.setCellRenderer(new ColumnRowGroupCellRenderer2());
                column2.setCellEditor(new BatchSubmitPane.ColumnValueEditor(ValueEditorPaneFactory.cellGroupEditor()));
            } else {
                column2.setCellRenderer(new SelectedColumnValueTableCellRenderer());
            }
        }

        /**
         * 检查是否合法
         * @throws Exception
         */
        public void checkValid() throws Exception {
            SmartInsertBatchSubmitPane.this.checkValid();
        }

        private SelectionListener listener = new SelectionListener() {

            @Override
            public void selectionChanged(SelectionEvent e) {
                BatchSubmitPane.KeyColumnTableModel model = (BatchSubmitPane.KeyColumnTableModel)table.getModel();
                if (editingRowIndex < 0 || editingRowIndex >= model.getRowCount()) {
                    return;
                }
                BatchSubmitPane.KeyColumnNameValue kcv = model.getKeyColumnNameValue(editingRowIndex);
                ElementCasePane currentReportPane = (ElementCasePane)e.getSource();
                Selection selection = currentReportPane.getSelection();
                if (selection == NO_SELECTION || selection instanceof FloatSelection) {
                    return;
                }
                CellSelection cellselection = (CellSelection)selection;
                kcv.cv.obj = ColumnRow.valueOf(cellselection.getColumn(), cellselection.getRow());

                if (editingRowIndex >= model.getRowCount() - 1) {
                    setEditingRowIndex(0);
                } else {
                    setEditingRowIndex(editingRowIndex + 1);
                }

                model.fireTableDataChanged();

            }

        };

        /**
         * 单元格组的点选格子事件
         */
        private SelectionListener groupListener = new SelectionListener() {
            @Override
            public void selectionChanged(SelectionEvent e) {
                BatchSubmitPane.KeyColumnTableModel model = (BatchSubmitPane.KeyColumnTableModel)table.getModel();
                if (editingRowIndex < 0 || editingRowIndex >= model.getRowCount()) {
                    return;
                }
                BatchSubmitPane.KeyColumnNameValue kcv = model.getKeyColumnNameValue(editingRowIndex);
                ElementCasePane currentReportPane = (ElementCasePane)e.getSource();
                Selection selection = currentReportPane.getSelection();
                if (selection == NO_SELECTION || selection instanceof FloatSelection) {
                    return;
                }
                CellSelection cellselection = (CellSelection)selection;
                Object oriValue = kcv.cv.obj;
                ColumnRowGroup newValue = getColumnRowGroupValue(oriValue);

                // 要考虑多选的情况 要结合之前的看看 可能是增加 也可能需要减少
                ColumnRowGroup add = new ColumnRowGroup();
                int removeCount = 0;
                if (oriCellSelection != null && sameStartPoint(cellselection, oriCellSelection)) {
                    removeCount = dealDragSelection(add, cellselection);
                } else if (cellselection.getSelectedType() == CellSelection.CHOOSE_ROW || cellselection.getSelectedType() == CellSelection.CHOOSE_COLUMN) {
                    dealSelectColRow(add, cellselection);
                } else {
                    add.addColumnRow(ColumnRow.valueOf(cellselection.getColumn(), cellselection.getRow()));
                }

                if (add.getSize() > 0) {
                    newValue.addAll(add);
                } else if (removeCount > 0) {
                    newValue.splice(newValue.getSize()-removeCount, removeCount);
                }

                kcv.cv.obj = newValue;

                model.fireTableDataChanged();

                oriCellSelection = cellselection;
            }

            private ColumnRowGroup getColumnRowGroupValue(Object oriValue) {
                ColumnRowGroup newValue = new ColumnRowGroup();
                if (oriValue instanceof ColumnRowGroup) {
                    newValue.addAll((ColumnRowGroup)oriValue);
                } else if (oriValue instanceof ColumnRow) {
                    newValue.addColumnRow((ColumnRow) oriValue);
                }
                return newValue;
            }

            private boolean sameStartPoint(CellSelection cs1, CellSelection cs2) {
                return cs1.getColumn() == cs2.getColumn() && cs1.getRow() == cs2.getRow();
            }

            private int dealDragSelection(ColumnRowGroup add, CellSelection cellselection) {
                int removeCount = 0;
                if (cellselection.getRowSpan() == oriCellSelection.getRowSpan() + 1) {
                    for (int i=0; i<cellselection.getColumnSpan(); i++) {
                        add.addColumnRow(ColumnRow.valueOf(
                                cellselection.getColumn() + i, cellselection.getRow() + cellselection.getRowSpan() -1));
                    }
                } else if (cellselection.getRowSpan() == oriCellSelection.getRowSpan() - 1) {
                    removeCount = cellselection.getColumnSpan();
                } else if (cellselection.getColumnSpan() == oriCellSelection.getColumnSpan() + 1) {
                    for (int i=0; i<cellselection.getRowSpan(); i++) {
                        add.addColumnRow(ColumnRow.valueOf(
                                cellselection.getColumn() + cellselection.getColumnSpan() - 1, cellselection.getRow() + i));
                    }
                } else if (cellselection.getColumnSpan() == oriCellSelection.getColumnSpan() - 1) {
                    removeCount = cellselection.getRowSpan();
                }
                return removeCount;
            }

            private void dealSelectColRow(ColumnRowGroup add, CellSelection se) {
                int c = se.getColumn(), cs = se.getColumnSpan(),
                        r = se.getRow(), rs = se.getRowSpan();
                for (int i=0; i<cs; i++) {
                    for (int j=0; j<rs; j++) {
                        add.addColumnRow(ColumnRow.valueOf(c+i, r+j));
                    }
                }
            }
        };

        private SmartJTablePaneAction a = new AbstractSmartJTablePaneAction(this, SmartInsertBatchSubmitPane.this) {
            @Override
            public void doOk() {
            }

            @Override
            public void showDialog() {
                Container container = SmartJTablePane4DB.this;
                while (container.getParent() != null) {
                    container = container.getParent();
                    if (container instanceof JDialog) {
                        container.setVisible(false);
                    }
                }
                updateUpdateCheckBoxEnable();
                ((SmartInsertBatchSubmitPane)dialog).showDialogAfterAddCellAction();
            }
        };

        /*
         * ColumnValueTableCellRenderer
         */
        private class SelectedColumnValueTableCellRenderer extends DefaultTableCellRenderer {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (value instanceof BatchSubmitPane.ColumnValue) {
                    if (((BatchSubmitPane.ColumnValue)value).obj != null) {
                        this.setText(((BatchSubmitPane.ColumnValue)value).obj.toString());
                    } else {
                        this.setText("");
                    }
                }

                if (row == SmartJTablePane4DB.this.editingRowIndex) {
                    this.setBackground(Color.cyan);
                } else {
                    this.setBackground(Color.white);
                }

                return this;
            }
        }

        private class ColumnRowGroupCellRenderer implements TableCellRenderer {

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                JPanel pane = new JPanel();
                UILabel text = new UILabel();

                String tip = Inter.getLocText("FR-Designer_Double_Click_Edit_OR_Clear");

                if (value instanceof BatchSubmitPane.ColumnValue) {
                    Object cv = ((BatchSubmitPane.ColumnValue) value).obj;
                    if (cv instanceof ColumnRowGroup && ((ColumnRowGroup)cv).getSize() >= CELL_GROUP_LIMIT) {
                        text.setText("[" + Inter.getLocText(new String[]{"Has_Selected", "Classifier-Ge", "Cell"},
                                new String[]{((ColumnRowGroup)cv).getSize()+"", ""}) + "]");
                        tip = cv.toString() + " " + tip;
                    } else if (cv != null) {
                        text.setText(cv.toString());
                    } else {
                        text.setText("");
                    }
                }

                if (row == SmartJTablePane4DB.this.editingRowIndex) {
                    pane.setBackground(Color.cyan);
                } else {
                    pane.setBackground(Color.white);
                }

                pane.setToolTipText(tip);
                pane.add(text);

                return pane;
            }
        }

        private class ColumnRowGroupCellRenderer2 extends DefaultTableCellRenderer {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                String tip = Inter.getLocText("FR-Designer_Double_Click_Edit_OR_Clear");

                if (value instanceof BatchSubmitPane.ColumnValue) {
                    Object cv = ((BatchSubmitPane.ColumnValue) value).obj;
                    if (cv instanceof ColumnRowGroup && ((ColumnRowGroup)cv).getSize() >= CELL_GROUP_LIMIT) {
                        this.setText("[" + Inter.getLocText(new String[]{"Has_Selected", "Classifier-Ge", "Cell"},
                                new String[]{((ColumnRowGroup)cv).getSize()+"", ""}) + "]");
                        tip = cv.toString() + " " + tip;
                    } else if (cv != null) {
                        this.setText(cv.toString());
                    } else {
                        this.setText("");
                    }
                }

                this.setToolTipText(tip);

                if (row == SmartJTablePane4DB.this.editingRowIndex) {
                    this.setBackground(Color.cyan);
                } else {
                    this.setBackground(Color.white);
                }

                return this;
            }
        }
    }
}
