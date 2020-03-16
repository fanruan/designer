package com.fr.design.chartx.component;

import com.fr.design.gui.ilable.UILabel;
import com.fr.design.i18n.Toolkit;
import com.fr.general.GeneralUtils;
import com.fr.plugin.chart.map.server.ChartGEOJSONHelper;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * @author Bjorn
 * @version 10.0
 * Created by Bjorn on 2019-11-19
 */
public class MatchAreaTable extends JTable {

    private Set<String> items;

    private MatchResultTable matchResultTable;

    private Map<Object, Integer> areaNameIndex = new HashMap<>();

    private DefaultMutableTreeNode root;

    public MatchAreaTable(Object[][] data, Object[] header) {
        super(data, header);
        this.getTableHeader().setReorderingAllowed(false);
    }

    public void setItems(Set<String> items) {
        this.items = items;
    }

    public Set<String> getItems() {
        return items;
    }

    public void setRoot(DefaultMutableTreeNode root) {
        this.root = root;
    }

    public void setMatchResultTable(MatchResultTable matchResultTable) {
        this.matchResultTable = matchResultTable;
    }

    public void setAreaNameIndex(Map<Object, Integer> areaNameIndex) {
        this.areaNameIndex = areaNameIndex;
    }

    public void setModel(TableModel dataModel) {
        super.setModel(dataModel);

        if (items == null) {
            items = new HashSet<>();
        }
        TableColumnModel columnModel = getColumnModel();
        columnModel.getColumn(0).setCellEditor(new UILabelEditor());
        columnModel.getColumn(1).setCellEditor(new UIComboBoxRenderAndEditor());
        columnModel.getColumn(1).setCellRenderer(new UIComboBoxRenderAndEditor());
    }

    public void reMatch(Object areaName) {
        if (!areaNameIndex.containsKey(areaName)) {
            return;
        }
        int index = areaNameIndex.get(areaName);
        String result = ChartGEOJSONHelper.matchArea(GeneralUtils.objectToString(areaName), items);
        getColumnModel().getColumn(1).getCellEditor().stopCellEditing();
        this.setValueAt(result, index, 1);
    }

    public class UIComboBoxRenderAndEditor extends AbstractCellEditor implements TableCellRenderer, TableCellEditor {

        TableTreeComboBox comboBox;

        public UIComboBoxRenderAndEditor() {
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            initComboBox(value, false);
            return comboBox;
        }

        public Component getTableCellEditorComponent(final JTable table, Object value, boolean isSelected, final int row, int column) {
            initComboBox(value, true);
            comboBox.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    UIComboBoxRenderAndEditor.this.stopCellEditing();
                    Object areaName = MatchAreaTable.this.getValueAt(row, 0);
                    Object result = MatchAreaTable.this.getValueAt(row, 1);
                    if (items.contains(result)) {
                        matchResultTable.dealMatch(areaName, result);
                    }
                }
            });
            return comboBox;
        }

        private void initComboBox(Object value, boolean editor) {
            comboBox = new TableTreeComboBox(new JTree(root));
            comboBox.setEditable(true);

            comboBox.setSelectedItem(value);
            if (!editor && value == null) {
                JTextField textField = (JTextField) (comboBox.getEditor().getEditorComponent());
                textField.setForeground(Color.RED);
                textField.setText(Toolkit.i18nText("Fine-Design_Chart_Prompt_Not_Selected"));
            }
        }

        public Object getCellEditorValue() {
            comboBox.resetText();
            return comboBox.getSelectedItem();
        }
    }

    public static class UILabelEditor extends AbstractCellEditor implements TableCellEditor {

        UILabel uiLabel;

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected,
                                                     int row, int column) {
            uiLabel = new UILabel(GeneralUtils.objectToString(value));
            return uiLabel;
        }

        public Object getCellEditorValue() {
            return uiLabel.getText();
        }
    }
}
