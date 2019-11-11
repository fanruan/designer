package com.fr.design.data.tabledata.tabledatapane;

import com.fr.base.BaseFormula;
import com.fr.base.StoreProcedureParameter;
import com.fr.design.editor.ValueEditorPane;
import com.fr.design.editor.ValueEditorPaneFactory;
import com.fr.design.editor.editor.CursorEditor;
import com.fr.design.gui.itableeditorpane.ParameterTableModel;
import com.fr.general.ComparatorUtils;


import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.util.Date;
import java.util.TimerTask;


/**
 * Created with IntelliJ IDEA.
 * User: pony
 * Date: 13-5-24
 * Time: 下午1:07
 * To change this template use File | Settings | File Templates.
 */
public class AutoStoreProcedureTableModel extends StoreProcedureTableModel {

    public AutoStoreProcedureTableModel() {
        super();
        Class[] classes = new Class[]{ParameterTableModel.ParameterEditor.class, getColumnClass(1), getColumnClass(2), StoreProcedureParameterValueEditor.class};
        this.setColumnClass(classes);
        this.setDefaultEditor(StoreProcedureParameterValueEditor.class, new StoreProcedureParameterValueEditor());
        this.setDefaultEditor(ParameterTableModel.ParameterEditor.class, new ParameterTableModel().new ParameterEditor());
        this.setDefaultRenderer(StoreProcedureParameterValueEditor.class, new ProcedureParameterValueRenderer());
    }
    @Override
    public boolean isCellEditable(int row, int col) {
        if (ComparatorUtils.equals(getValueAt(row, col -1), "OUT")) {
            return false;
        }
        if (col ==0 || col == 3) {
            return true;
        }
        return false;
    }

    @Override
    public boolean shouldResponseDoubleClickAction () {
        return false;
    }

    private class StoreProcedureParameterValueEditor extends AbstractCellEditor implements TableCellEditor {
        private ValueEditorPane editor;

        public StoreProcedureParameterValueEditor() {

            editor = ValueEditorPaneFactory.createStoreProcedValueEditorPane();
            editor.getMenu().addPopupMenuListener(new PopupMenuListener() {
                public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                    // Do nothing
                }

                public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                    new java.util.Timer().schedule(new TimerTask() {
                        public void run() {
                            int selectedRow = table.getSelectedRow();
                            if (selectedRow != -1) {
                                StoreProcedureParameter para = getList().get(selectedRow);
                                para.setType(getParameterType(getCellEditorValue()));
                                fireTableDataChanged();
                                table.setRowSelectionInterval(selectedRow, selectedRow);
                            }
                        }
                    }, 100);
                }

                @Override
                public void popupMenuCanceled(PopupMenuEvent e) {
                    //do nothing
                }
            });
            this.addCellEditorListener(new CellEditorListener() {

                @Override
                public void editingCanceled(ChangeEvent e) {
                    //do nothing
                }

                @Override
                public void editingStopped(ChangeEvent e) {
                    if (table.getSelectedRow() == -1) {
                        return;
                    }
                    StoreProcedureParameter para = getList().get(table.getSelectedRow());
                    para.setValue(getCellEditorValue());
                    para.setType(getParameterType(getCellEditorValue()));
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

    private int getParameterType (Object value) {
        String type;
        if(value instanceof CursorEditor)
            type= com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Cursor");
        else if(value instanceof String ){
            if(((String) value).length() > 0 && ((String) value).charAt(0) == '=')
                type = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Formula");
            else
                type = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Parameter_String");
        }else if(value instanceof Integer)
            type = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Integer");
        else if(value instanceof Double)
            type = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Double");
        else if(value instanceof Date)
            type = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Date");
        else if(value instanceof Boolean)
            type = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Parameter_Boolean");
        else if(value instanceof BaseFormula)
            type = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Formula");
        else
            type = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Parameter_String");
        return StoreProcedureParameterPane.getInfo4Value(type);

    }

    private class ProcedureParameterValueRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 1L;
        private ValueEditorPane editor;

        public ProcedureParameterValueRenderer() {
            editor = ValueEditorPaneFactory.createStoreProcedValueEditorPane();
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            editor.setCurrentEditor(0);
            editor.populate(value);
            return editor;
        }
    }


}