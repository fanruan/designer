package com.fr.design.gui.itableeditorpane;

import com.fr.design.editor.ValueEditorPane;
import com.fr.design.editor.ValueEditorPaneFactory;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;
import java.awt.*;

/**
 * 全部是公式编辑的model
 * Coder: zack
 * Date: 2016/8/31
 * Time: 18:58
 */
public class UIArrayFormulaTableModel extends UIArrayTableModel {
    public UIArrayFormulaTableModel(String[] s, int[] array) {
        super(s, array);
        setDefaultEditors();
    }

    public void setDefaultEditors() {
        for (int i = 0; i < getColumnCount(); i++) {
            setDefaultEditor(Object.class, new FormulaValueEditor());
        }
    }

    private class FormulaValueEditor extends AbstractCellEditor implements TableCellEditor {
        private static final long serialVersionUID = 1L;
        private ValueEditorPane editor;

        public FormulaValueEditor() {

            editor = ValueEditorPaneFactory.createFormulaValueEditorPane();

            this.addCellEditorListener(new CellEditorListener() {

                @Override
                public void editingCanceled(ChangeEvent e) {
                }

                @Override
                public void editingStopped(ChangeEvent e) {
                    if (table.getSelectedRow() == -1 || table.getSelectedColumn() == -1) {
                        return;
                    }
                    Object[] para = getList().get(table.getSelectedRow());
                    para[table.getSelectedColumn()] = getCellEditorValue();
                    fireTableDataChanged();
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            editor.clearComponentsData();
            editor.populate(value == null ? "" : value);
            return editor;
        }

        @Override
        public Object getCellEditorValue() {
            return editor.update();
        }
    }
}
