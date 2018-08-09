package com.fr.design.gui.frpane.tree.layer.config;

import com.fr.design.data.tabledata.wrapper.TableDataWrapper;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.present.dict.TableDataDictPane;
import com.fr.form.ui.tree.LayerDependence;


import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by juhaoyu on 16/9/19.
 */
public class LayerDependenceSettingPane extends JPanel implements ItemListener {

    /**
     * 用户选择数据集的pane,在LayerDataConfigPane中传入
     */
    private TableDataDictPane tableDataDictPane;


    /**
     * 添加依赖按钮
     */
    private UIButton addButton;

    /**
     * 删除依赖按钮
     */
    private UIButton delButton;

    /**
     * 依赖关系编辑Table
     */
    private JTable dependenceTable;

    /**
     * Table的数据模型,两个按钮的操作直接对应于model
     */
    private LayerDepenceTableModel model;

    /**
     * 当前该panel所设置的层级
     */
    private int currentLayerIndex = 1;

    private FieldRenderer fieldRenderer;

    private LayerIndexEditor layerIndexEditor;

    private FiledEditor fieldEditor;


    public LayerDependenceSettingPane(TableDataDictPane tableDictPane) {
        //关联数据集选择,并添加监听
        this.tableDataDictPane = tableDictPane;
        tableDataDictPane.tableDataNameComboBox.addItemListener(this);

        //初始化按钮对象
        addButton = new UIButton(com.fr.design.i18n.Toolkit.i18nText("add"));
        delButton = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Delete"));
        //初始化Table对象,并添加renderer和editor
        model = new LayerDepenceTableModel();
        dependenceTable = new JTable();
        dependenceTable.setModel(model);
        //初始化辅助组件
        fieldEditor = new FiledEditor(tableDataDictPane);
        fieldRenderer = new FieldRenderer(tableDictPane);
        layerIndexEditor = new LayerIndexEditor(currentLayerIndex);
        //添加renderer
        dependenceTable.getColumnModel().getColumn(0).setCellRenderer(new FirstRenderer());
        dependenceTable.getColumnModel().getColumn(1).setCellRenderer(fieldRenderer);
        //添加第一列editor
        dependenceTable.getColumnModel().getColumn(0).setCellEditor(layerIndexEditor);
        //添加第二列editor
        dependenceTable.getColumnModel().getColumn(1).setCellEditor(fieldEditor);
        //添加add按钮监听
        addButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //先要停止编辑,然后再添加
                fieldEditor.stopCellEditing();
                layerIndexEditor.stopCellEditing();
                LayerDependenceSettingPane.this.model.addDependence();
            }
        });
        //添加del按钮监听
        delButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                if (0 == dependenceTable.getSelectedRowCount()) {
                    return;
                }
                //获取视图索引,并根据视图索引获取model索引,删除model指定行
                int selectedRow = dependenceTable.getSelectedRow();
                int selectedRowModelIndex = dependenceTable.convertRowIndexToModel(selectedRow);
                //先要停止编辑,然后再删除
                fieldEditor.stopCellEditing();
                layerIndexEditor.stopCellEditing();
                model.delDependence(selectedRowModelIndex);
            }
        });


        //生成布局
        this.setLayout(new BorderLayout(2, 2));
        //添加按钮panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(2));
        buttonPanel.add(addButton);
        buttonPanel.add(delButton);
        this.add(buttonPanel, BorderLayout.NORTH);
        //添加Table的panel
        JScrollPane tablePanel = new JScrollPane(dependenceTable);
        this.add(tablePanel, BorderLayout.CENTER);


    }


    public void populate(int layerIndex, List<LayerDependence> dependenceList) {

        this.currentLayerIndex = layerIndex;
        //更新Editor
        this.layerIndexEditor.layerChanged(layerIndex);
        this.fieldEditor.layerChanged();

        this.model.clear();
        this.model.addAll(dependenceList);
    }


    public List<LayerDependence> updateLayerDependence() {
        //保存现有编辑
        this.fieldEditor.stopCellEditing();
        this.layerIndexEditor.stopCellEditing();
        return this.model.update();
    }

    /**
     * 当tableDataDictPane变化时,调用该方法
     */


    @Override
    public void itemStateChanged(ItemEvent e) {

        clearDependences();

    }

    /**
     * 清楚当前设置的依赖关系
     */
    private void clearDependences() {

        this.model.clear();

    }

    /**
     * 第一列renderer
     */
    private static final class FirstRenderer extends UILabel implements TableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

            if (value != null) {
                //value是用户选择的字段索引值,从1开始的
                this.setText(String.valueOf(value));
            } else {
                this.setText("");
            }
            if (hasFocus) {
                this.setBorder(UIManager.getBorder("Table.focusCelHighlightBorder"));
            } else {
                this.setBorder(null);
            }
            return this;
        }
    }

    /**
     * 第二列renderer
     * 由于从model中获取的数据是数据集列的索引值,这里要转换为列的名称
     */
    private static final class FieldRenderer extends UILabel implements TableCellRenderer {

        //用于将字段索引转换为字段名;保存改pane,是为了当用户选择其他数据集时,renderer可同步更新
        private TableDataDictPane tableDataDictPane;

        public FieldRenderer(TableDataDictPane tableDataDictPane) {

            this.tableDataDictPane = tableDataDictPane;
        }

        /**
         * 由于数据是从tableDataDictPane中现取的,所以用户选择不同的数据集时,renderer同步更新;
         * 此时只需要在用户选择数据集时,刷新Table即可,不需要对renderer处理
         */
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

            if (value != null) {
                List<String> columnNames = getColumnNameList(this.tableDataDictPane);
                //value是用户选择的字段索引值,从1开始的
                this.setText(columnNames.get(Integer.valueOf(String.valueOf(value))));
            } else {
                this.setText("");
            }
            if (hasFocus) {
                this.setBorder(UIManager.getBorder("Table.focusCelHighlightBorder"));
            } else {
                this.setBorder(null);
            }
            return this;
        }
    }

    /**
     * 第一列editor
     * 该editor于layerIndex关联,当用户选择不同的层时,这个editor要同步更新
     */
    private static final class LayerIndexEditor extends AbstractCellEditor implements TableCellEditor {

        private int currentLayerIndex;

        private UIComboBox layerChoseCombobox = new UIComboBox();

        public LayerIndexEditor(int currentLayerIndex) {

            this.currentLayerIndex = currentLayerIndex;
            for (int i = 1; i < currentLayerIndex; i++) {
                layerChoseCombobox.addItem(i);
            }
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

            if (value != null) {
                layerChoseCombobox.setSelectedIndex(Integer.valueOf(String.valueOf(value)) - 1);
            }
            return layerChoseCombobox;
        }

        @Override
        public Object getCellEditorValue() {

            return layerChoseCombobox.getSelectedItem();
        }

        public void layerChanged(int newLayerIndex) {

            this.currentLayerIndex = newLayerIndex;
            layerChoseCombobox.removeAllItems();
            for (int i = 1; i < currentLayerIndex; i++) {
                layerChoseCombobox.addItem(i);
            }
        }
    }

    /**
     * 第二列editor
     */
    private static final class FiledEditor extends AbstractCellEditor implements TableCellEditor {

        private UIComboBox layerChoseCombobox = new UIComboBox();

        TableDataDictPane tableDataDictPane;

        public FiledEditor(TableDataDictPane tableDataDictPane) {

            this.tableDataDictPane = tableDataDictPane;
            List<String> columnNames = getColumnNameList(this.tableDataDictPane);
            for (String columnName : columnNames) {
                this.layerChoseCombobox.addItem(columnName);
            }

        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

            List<String> columnNames = getColumnNameList(this.tableDataDictPane);
            layerChoseCombobox.removeAllItems();
            for (String columnName : columnNames) {

                layerChoseCombobox.addItem(columnName);
            }
            if (value != null) {
                layerChoseCombobox.setSelectedIndex(Integer.valueOf(String.valueOf(value)));
            }
            return layerChoseCombobox;
        }

        @Override
        public Object getCellEditorValue() {
            //转化为数据集的列索引(从0开始)
            return layerChoseCombobox.getSelectedIndex();
        }

        public void layerChanged() {

            List<String> columnNames = getColumnNameList(this.tableDataDictPane);
            layerChoseCombobox.removeAllItems();
            for (String columnName : columnNames) {

                layerChoseCombobox.addItem(columnName);
            }
        }
    }

    private static List<String> getColumnNameList(TableDataDictPane tableDataDictPane) {

        TableDataWrapper wrapper = tableDataDictPane.tableDataNameComboBox.getSelectedItem();
        if (wrapper == null) {
            return new ArrayList<String>();
        } else {
            return wrapper.calculateColumnNameList();
        }
    }

    private static class LayerDepenceTableModel extends AbstractTableModel {

        private List<LayerDependence> dependences;

        public LayerDepenceTableModel() {

            dependences = new ArrayList<LayerDependence>();
        }

        /**
         * 添加一条数据,该方法会请求Table进行重绘(通过发送时间,告诉JTable数据更新,JTable会自动重绘)
         */
        public void addDependence() {

            dependences.add(new LayerDependence());
            fireTableRowsInserted(dependences.size(), dependences.size());
        }

        /**
         * 删除一条数据
         *
         * @param rowIndex
         */
        public void delDependence(int rowIndex) {

            if (rowIndex < 0 || rowIndex >= dependences.size()) {
                return;
            }
            dependences.remove(rowIndex);
            fireTableRowsDeleted(rowIndex + 1, rowIndex + 1);
        }

        public void addAll(List<LayerDependence> dependenceList) {

            dependences.addAll(dependenceList);
            fireTableRowsInserted(1, dependenceList.size());
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {

            return true;
        }

        @Override
        public Class<?> getColumnClass(int column) {

            return Integer.class;
        }

        @Override
        public String getColumnName(int column) {

            String name;
            if (column == 0) {
                name = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Layer_Index");
            } else {
                name = com.fr.design.i18n.Toolkit.i18nText("FR-Designer_filedChosen");
            }
            return name;
        }

        @Override
        public int getRowCount() {

            return dependences.size();
        }

        @Override
        public int getColumnCount() {

            return 2;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {

            LayerDependence dependence = dependences.get(rowIndex);
            Object obj;
            if (columnIndex == 0) {
                obj = dependence.getLayerIndex();
            } else {
                obj = dependence.getThisColumnIndex();
            }
            return obj;
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

            LayerDependence dependence = dependences.get(rowIndex);
            if (aValue != null) {
                if (columnIndex == 0) {
                    dependence.setLayerIndex((Integer) aValue);
                } else {
                    dependence.setThisColumnIndex((Integer) aValue);
                }
            }

        }

        public void clear() {

            int length = dependences.size();
            dependences.clear();
            fireTableRowsDeleted(1, length);

        }


        public List<LayerDependence> update() {

            return this.dependences;
        }
    }


}


