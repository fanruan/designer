package com.fr.design.chartx.component;

import com.fr.base.BaseUtils;
import com.fr.base.Utils;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.i18n.Toolkit;
import com.fr.general.ComparatorUtils;
import com.fr.plugin.chart.map.data.MapMatchResult;
import com.fr.stable.StringUtils;

import javax.swing.AbstractCellEditor;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Bjorn
 * @version 10.0
 * Created by Bjorn on 2019-11-20
 */
public class MatchResultTable extends JTable {

    private MatchAreaTable matchAreaTable;

    public MatchResultTable(Object[][] data, Object[] header) {
        super(data, header);
        this.getTableHeader().setReorderingAllowed(false);
    }

    public void setMatchAreaTable(MatchAreaTable matchAreaTable) {
        this.matchAreaTable = matchAreaTable;
    }

    public void setModel(TableModel dataModel) {
        super.setModel(dataModel);

        TableColumnModel columnModel = getColumnModel();
        columnModel.getColumn(0).setCellEditor(new MatchAreaTable.UILabelEditor());
        columnModel.getColumn(1).setCellEditor(new MatchAreaTable.UILabelEditor());
        columnModel.getColumn(2).setCellEditor(new UIButtonEditorAndRender());
        columnModel.getColumn(2).setCellRenderer(new UIButtonEditorAndRender());
        columnModel.getColumn(2).setMaxWidth(20);
    }

    public void dealMatch(Object areaName, Object result) {
        int rowCount = this.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            if (ComparatorUtils.equals(this.getValueAt(i, 0), areaName)) {
                getColumnModel().getColumn(1).getCellEditor().stopCellEditing();
                this.setValueAt(result, i, 1);
                return;
            }
        }
        DefaultTableModel model = (DefaultTableModel) this.getModel();
        Vector vector = new Vector();
        vector.add(areaName);
        vector.add(result);
        vector.add("");
        model.addRow(vector);
    }

    public void populateBean(MapMatchResult matchResult) {
        if (matchResult == null) {
            return;
        }
        Map<String, String> customResult = matchResult.getCustomResult();
        if (customResult == null) {
            return;
        }
        DefaultTableModel model = (DefaultTableModel) this.getModel();
        for (Map.Entry<String, String> entry : customResult.entrySet()) {
            Vector vector = new Vector();
            vector.add(entry.getKey());
            vector.add(entry.getValue());
            vector.add("");
            model.addRow(vector);
        }
    }

    public void updateBean(MapMatchResult matchResult) {
        matchResult.setCustomResult(getCustomResult());
    }

    public Map<String, String> getCustomResult() {
        Map<String, String> customResult = new LinkedHashMap<>();
        DefaultTableModel model = (DefaultTableModel) this.getModel();
        for (int i = 0, rowCount = model.getRowCount(); i < rowCount; i++) {
            customResult.put(Utils.objectToString(model.getValueAt(i, 0)), Utils.objectToString(model.getValueAt(i, 1)));
        }
        return customResult;
    }

    public class UIButtonEditorAndRender extends AbstractCellEditor implements TableCellEditor, TableCellRenderer {

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, final int row, int column) {
            UIButton uiButton = new UIButton(BaseUtils.readIcon("com/fr/design/images/toolbarbtn/close.png"));
            uiButton.addMouseListener(new MouseAdapter() {
                boolean mouseEntered = false;

                public void mouseEntered(MouseEvent e) { // 当鼠标进入时候调用.
                    mouseEntered = true;

                }

                public void mouseExited(MouseEvent e) {
                    mouseEntered = false;
                }

                public void mouseReleased(MouseEvent e) {
                    if (mouseEntered) {
                        MatchResultTable.this.getCellEditor().stopCellEditing();
                        int val = JOptionPane.showConfirmDialog(MatchResultTable.this, Toolkit.i18nText("Fine-Design_Basic_Utils_Are_You_Sure_To_Remove_The_Selected_Item") + "?",
                                Toolkit.i18nText("Fine-Design_Basic_Remove"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if (val == JOptionPane.OK_OPTION) {
                            DefaultTableModel model = (DefaultTableModel) MatchResultTable.this.getModel();
                            Object areaName = MatchResultTable.this.getValueAt(row, 0);
                            model.removeRow(row);
                            matchAreaTable.reMatch(areaName);
                        }
                    }
                }
            });
            return uiButton;
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            UIButton button = new UIButton(BaseUtils.readIcon("com/fr/design/images/toolbarbtn/close.png"));
            return button;
        }

        public Object getCellEditorValue() {
            return StringUtils.EMPTY;
        }
    }

}
