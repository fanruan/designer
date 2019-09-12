package com.fr.design.write.submit;

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
import com.fr.design.i18n.Toolkit;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.mainframe.JWorkBook;
import com.fr.design.selection.SelectionEvent;
import com.fr.design.selection.SelectionListener;
import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.FloatSelection;
import com.fr.grid.selection.Selection;
import com.fr.report.cell.TemplateCellElement;
import com.fr.stable.ColumnRow;
import com.fr.stable.ColumnRowGroup;
import com.fr.stable.StringUtils;
import com.fr.write.DMLConfigJob;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class SmartInsertDBManipulationPane extends DBManipulationPane {
    private static final Selection NO_SELECTION = new CellSelection(-1, -1, -1, -1);
    private ElementCasePane ePane;
    private static int CELL_GROUP_LIMIT = 6;
    private static int TOP_PADDING = 30;
    private static int LEFT_COLUMN_MAX_WIDTH = 40;

    public SmartInsertDBManipulationPane(ElementCasePane ePane) {
        super(ValueEditorPaneFactory.extendedCellGroupEditors());
        this.ePane = ePane;
    }

    public SmartInsertDBManipulationPane() {
        super(ValueEditorPaneFactory.extendedCellGroupEditors());
        init();
    }

    private void init() {
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
            return new NameableCreator[]{
                    new NameObjectCreator(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Submit_Event"),
                            "/com/fr/web/images/reportlet.png",
                            DMLConfigJob.class,
                            SmartInsertDMLJobPane.class),
                    new NameObjectCreator(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Custom_Event"),
                            "/com/fr/web/images/reportlet.png",
                            ClassSubmitJob.class,
                            CustomSubmitJobPane.class)};
        }
    }

    @Override
    protected UpdateAction[] getActions() {
        return new UpdateAction[]{
                new SmartAddFieldsAction(),
                new AddFieldAction(),
                new SmartAddCellAction(),
                new SmartAddCellGroupAction(),
                new BatchModCellAction(),
                new RemoveFieldAction()
        };
    }

    public class BatchModCellAction extends UpdateAction {
        public BatchModCellAction() {
            this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_RWA_Batch_Modify_Cells"));
        }

        /**
         * 执行事件
         *
         * @param evt 事件对象
         */
        @Override
        public void actionPerformed(ActionEvent evt) {
            BasicPane bPane = new BasicPane() {
                @Override
                protected String title4PopupWindow() {
                    return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_RWA_Batch_Modify_Cells");
                }
            };
            bPane.setLayout(FRGUIPaneFactory.createBorderLayout());
            bPane.setBorder(BorderFactory.createEmptyBorder(TOP_PADDING, 0, 0, 0));
            final UIBasicSpinner columnSpinner = new UIBasicSpinner();
            final UIBasicSpinner rowSpinner = new UIBasicSpinner();
            Component[][] coms = new Component[][]{{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_RWA_Row_Offset")), rowSpinner}, {new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_RWA_Column_Offset")), columnSpinner}};
            double p = TableLayout.PREFERRED;
            double f = TableLayout.FILL;
            bPane.add(TableLayoutHelper.createTableLayoutPane(coms, new double[]{p, p}, new double[]{p, f}), BorderLayout.NORTH);
            BasicDialog dlg = bPane.showSmallWindow(SwingUtilities.getWindowAncestor(SmartInsertDBManipulationPane.this), new DialogActionAdapter() {
                @Override
                public void doOk() {
                    int row_offset = ((Number) rowSpinner.getValue()).intValue();
                    int column_offset = ((Number) columnSpinner.getValue()).intValue();
                    KeyColumnTableModel model = (KeyColumnTableModel) keyColumnValuesTable.getModel();
                    int[] selectedRows = keyColumnValuesTable.getSelectedRows();
                    // 如果一行都没选中,取所有的行
                    if (selectedRows.length == 0) {
                        selectedRows = IntList.range(model.getRowCount());
                    }
                    for (int i = 0; i < selectedRows.length; i++) {
                        int row = selectedRows[i];
                        KeyColumnNameValue kcnv = model.getKeyColumnNameValue(row);
                        if (kcnv.cv.obj instanceof ColumnRow) {
                            ColumnRow or = (ColumnRow) kcnv.cv.obj;
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
            this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_RWA_Smart_Add_Cells"));
        }

        /**
         * 执行事件
         *
         * @param evt 事件s
         */
        @Override
        public void actionPerformed(ActionEvent evt) {
            showCellWindow(false);
        }
    }

    public class SmartAddCellGroupAction extends UpdateAction {
        public SmartAddCellGroupAction() {
            this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_RWA_Smart_Add_Cell_Group"));
        }

        /**
         * 智能添加单元格组
         *
         * @param e 事件s
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            showCellWindow(true);
        }
    }

    /**
     * 切换到单元格窗口，设置属性面板不可编辑
     *
     * @param isCellGroup 判断是否单元格组
     */

    private void showCellWindow(boolean isCellGroup) {
        if (ePane == null) {
            return;
        }

        BasicPane bPane = new SmartJTablePane4DB(keyColumnValuesTable.getTableModel4SmartAddCell(), ePane, isCellGroup);

        // ReportWriteAttrDialog.this.setVisible(false);
        hideDialog4AddCellAction();
        /*
         * 当前的ReportPane不可编辑,不可切换Sheet,加GridSelectionChangeListener
         */
        //必须先设置面板不可编辑才能释放单元格选中
        ePane.setEditable(false);
        ePane.setSelection(NO_SELECTION);
        ePane.getGrid().setNotShowingTableSelectPane(false);

        BasicDialog dlg = bPane.showWindow(SwingUtilities.getWindowAncestor(SmartInsertDBManipulationPane.this));

        dlg.setModal(false);
        dlg.setVisible(true);
    }

    private void showDialogAfterAddCellAction() {
        Container dialog = this;
        if (parentPane != null && parentPane.getContentDBManiPane() instanceof SmartInsertDBManipulationPane && parentPane.getContentDBManiPane() != this) {
            ((SmartInsertDBManipulationPane) parentPane.getContentDBManiPane()).showDialogAfterAddCellAction();
        }
        while (dialog.getParent() != null) {
            dialog = dialog.getParent();
            if (dialog instanceof Dialog) {
                //这边需要另起一个线程设置可见，防止阻塞
                final Container finalDialog = dialog;
                SwingWorker worker = new SwingWorker() {
                    @Override
                    protected Object doInBackground() throws Exception {
                        finalDialog.setVisible(true);
                        return null;
                    }
                };
                worker.execute();
            }
        }
    }

    private void hideDialog4AddCellAction() {
        Container dialog = this;
        if (parentPane != null && parentPane.getContentDBManiPane() instanceof SmartInsertDBManipulationPane && parentPane.getContentDBManiPane() != this) {
            ((SmartInsertDBManipulationPane) parentPane.getContentDBManiPane()).hideDialog4AddCellAction();
        }
        while (dialog.getParent() != null) {
            dialog = dialog.getParent();
            if (dialog instanceof Dialog) {
                // 条件属性中添加的控件的话有两层dialog，需要都隐藏
                dialog.setVisible(false);
            }
        }
    }


    /**
     * 检测是否合法
     *
     * @throws Exception
     */
    @Override
    public void checkValid() throws Exception {
        KeyColumnTableModel model = (KeyColumnTableModel) keyColumnValuesTable.getModel();
        int cnt = model.getRowCount();
        int groupLength = -1;
        for (int i = 0; i < cnt; i++) {
            KeyColumnNameValue kcv = model.getKeyColumnNameValue(i);
            Object val = kcv.cv.obj;
            if (val instanceof ColumnRowGroup) {
                int len = ((ColumnRowGroup) val).getSize();
                if (groupLength < 0) {
                    groupLength = len;
                } else if (len != groupLength) {
                    throw new Exception(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Write_Attributes_Group_Warning"));
                }
            }
        }
    }

    private class SmartJTablePane4DB extends SmartJTablePane {

        // 是否是单元格组
        private boolean isCellGroup = false;

        // 单元格组要记录下之前的选中情况
        private CellSelection oriCellSelection = null;

        private List<String> newAdd = new ArrayList<String>();

        private List<String> oldAdd = new ArrayList<String>();

        public SmartJTablePane4DB(KeyColumnTableModel model, ElementCasePane actionReportPane) {
            this(model, actionReportPane, false);
        }

        public SmartJTablePane4DB(KeyColumnTableModel model, ElementCasePane actionReportPane, boolean isCellGroup) {
            super(model, actionReportPane);
            this.isCellGroup = isCellGroup;
            this.setCellRenderer();
            this.changeGridSelectionChangeListener(isCellGroup ? groupListener : listener);
            this.changeSmartJTablePaneAction(a);
        }

        @Override
        protected String title4PopupWindow() {
            if (isCellGroup) {
                return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_RWA_Smart_Add_Cell_Group");
            } else {
                return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_RWA_Smart_Add_Cells");
            }
        }

        @Override
        public void setCellRenderer() {
            /*
             * set Width
             */
            TableColumn column0 = table.getColumnModel().getColumn(0);
            column0.setMaxWidth(LEFT_COLUMN_MAX_WIDTH);
            /*
             * 设置Column 1的Renderer
             */
            TableColumn column1 = table.getColumnModel().getColumn(1);
            column1.setCellRenderer(new ColumnNameTableCellRenderer());

            /*
             * 设置Column 2的Renderer
             */
            TableColumn column2 = table.getColumnModel().getColumn(2);
//			column2.setCellRenderer(new SelectedColumnValueTableCellRenderer());

            if (isCellGroup) {
                column2.setCellRenderer(new ColumnRowGroupCellRenderer2());
                column2.setCellEditor(new ColumnValueEditor(ValueEditorPaneFactory.cellGroupEditor()));
            } else {
                column2.setCellRenderer(new SelectedColumnValueTableCellRenderer());
            }
        }

        /**
         * 检查是否合法
         *
         * @throws Exception
         */
        @Override
        public void checkValid() throws Exception {
            SmartInsertDBManipulationPane.this.checkValid();
        }

        private SelectionListener listener = new SelectionListener() {

            @Override
            public void selectionChanged(SelectionEvent e) {
                KeyColumnTableModel model = (KeyColumnTableModel) table.getModel();
                if (editingRowIndex < 0 || editingRowIndex >= model.getRowCount()) {
                    return;
                }
                KeyColumnNameValue kcv = model.getKeyColumnNameValue(editingRowIndex);
                ElementCasePane currentReportPane = (ElementCasePane) e.getSource();
                Selection selection = currentReportPane.getSelection();
                if (selection == NO_SELECTION || selection instanceof FloatSelection) {
                    return;
                }
                CellSelection cellselection = (CellSelection) selection;
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
                KeyColumnTableModel model = (KeyColumnTableModel) table.getModel();
                if (editingRowIndex < 0 || editingRowIndex >= model.getRowCount()) {
                    return;
                }
                KeyColumnNameValue kcv = model.getKeyColumnNameValue(editingRowIndex);
                ElementCasePane currentReportPane = (ElementCasePane) e.getSource();
                Selection selection = currentReportPane.getSelection();
                if (selection == NO_SELECTION || selection instanceof FloatSelection) {
                    return;
                }
                CellSelection cellselection = (CellSelection) selection;
                Object oriValue = kcv.cv.obj;
                ColumnRowGroup newValue = getColumnRowGroupValue(oriValue);

                // 要考虑多选的情况 要结合之前的看看 可能是增加 也可能需要减少
                ColumnRowGroup add = new ColumnRowGroup();
                if (oriCellSelection != null && isSameStartPoint(cellselection, oriCellSelection)) {
                    dealDragSelection(add, cellselection, newValue);
                } else if (cellselection.getSelectedType() == CellSelection.CHOOSE_ROW || cellselection.getSelectedType() == CellSelection.CHOOSE_COLUMN) {
                    dealSelectColRow(add, cellselection);
                } else {
                    ColumnRow columnRow = ColumnRow.valueOf(cellselection.getColumn(), cellselection.getRow());
                    String allColumnRow = newValue.toString();
                    if (!allColumnRow.contains(columnRow.toString())) {
                        add.addColumnRow(columnRow);
                    }

                }

                if (add.getSize() > 0) {
                    newValue.addAll(add);
                }

                kcv.cv.obj = newValue;

                model.fireTableDataChanged();

                oriCellSelection = cellselection;
            }

            private void dealDragSelection(ColumnRowGroup add, CellSelection cellselection, ColumnRowGroup newValue) {
                int c = cellselection.getColumn();
                int cs = cellselection.getColumnSpan();
                int r = cellselection.getRow();
                int rs = cellselection.getRowSpan();
                String allColumnRow = newValue.toString();
                newAdd.clear();
                for (int i = 0; i < cs; i++) {
                    for (int j = 0; j < rs; j++) {
                        TemplateCellElement cellElement = ePane.getEditingElementCase().getTemplateCellElement(c + i, r + j );
                        if (cellElement != null && ((i + c) != 0 || (r + j) != 0)) {
                            String value = cellElement.toString();
                            if (!newAdd.contains(value) && !allColumnRow.contains(value)) {
                                add.addColumnRow(ColumnRow.valueOf(value));
                            }
                            newAdd.add(value);
                        }

                        if (cellElement == null) {
                            ColumnRow columnRow = ColumnRow.valueOf(c + i, r + j);
                            if (!allColumnRow.contains(columnRow.toString())) {
                                add.addColumnRow(columnRow);
                            }
                            newAdd.add(columnRow.toString());
                        }
                    }
                }
                int oldSize = oldAdd.size();
                int newSize = newAdd.size();
                if (oldSize > newSize && oldAdd.containsAll(newAdd)) {
                    int diff = oldSize - newSize;
                    newValue.splice(newValue.getSize() - diff, diff);
                }
                oldAdd.clear();
                oldAdd.addAll(newAdd);
            }

            private ColumnRowGroup getColumnRowGroupValue(Object oriValue) {
                ColumnRowGroup newValue = new ColumnRowGroup();
                if (oriValue instanceof ColumnRowGroup) {
                    newValue.addAll((ColumnRowGroup) oriValue);
                } else if (oriValue instanceof ColumnRow) {
                    newValue.addColumnRow((ColumnRow) oriValue);
                }
                return newValue;
            }

            private boolean isSameStartPoint(CellSelection cs1, CellSelection cs2) {
                return cs1.getColumn() == cs2.getColumn() && cs1.getRow() == cs2.getRow();
            }

            private void dealSelectColRow(ColumnRowGroup add, CellSelection se) {
                int c = se.getColumn(), cs = se.getColumnSpan(),
                        r = se.getRow(), rs = se.getRowSpan();
                for (int i = 0; i < cs; i++) {
                    for (int j = 0; j < rs; j++) {
                        add.addColumnRow(ColumnRow.valueOf(c + i, r + j));
                    }
                }
            }
        };

        private SmartJTablePaneAction a = new AbstractSmartJTablePaneAction(this, SmartInsertDBManipulationPane.this) {
            @Override
            public void doOk() {
                // 遗留代码
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
                ((SmartInsertDBManipulationPane) dialog).showDialogAfterAddCellAction();
            }
        };

        /*
         * ColumnValueTableCellRenderer
         */
        private class SelectedColumnValueTableCellRenderer extends DefaultTableCellRenderer {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (value instanceof ColumnValue) {
                    if (((ColumnValue) value).obj != null) {
                        this.setText(((ColumnValue) value).obj.toString());
                    } else {
                        this.setText(StringUtils.EMPTY);
                    }
                }

                if (row == SmartJTablePane4DB.this.editingRowIndex) {
                    this.setBackground(java.awt.Color.cyan);
                } else {
                    this.setBackground(java.awt.Color.WHITE);
                }

                return this;
            }
        }

        private class ColumnRowGroupCellRenderer2 extends DefaultTableCellRenderer {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                String tip = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Double_Click_Edit_OR_Clear");

                if (value instanceof ColumnValue) {
                    Object cv = ((ColumnValue) value).obj;
                    if (cv instanceof ColumnRowGroup && ((ColumnRowGroup) cv).getSize() >= CELL_GROUP_LIMIT) {
                        this.setText("[" +
                                Toolkit.i18nText("Fine-Design_Report_Write_Select_Cell_Count", ((ColumnRowGroup) cv).getSize())
                                + "]");
                        tip = cv.toString() + " " + tip;
                    } else if (cv != null) {
                        this.setText(cv.toString());
                    } else {
                        this.setText(StringUtils.EMPTY);
                    }
                }

                this.setToolTipText(tip);

                if (row == SmartJTablePane4DB.this.editingRowIndex) {
                    this.setBackground(java.awt.Color.cyan);
                } else {
                    this.setBackground(java.awt.Color.WHITE);
                }

                return this;
            }
        }
    }
}
