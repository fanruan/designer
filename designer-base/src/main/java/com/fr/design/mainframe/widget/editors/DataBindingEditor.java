package com.fr.design.mainframe.widget.editors;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import com.fr.design.data.DesignTableDataManager;
import com.fr.data.TableDataSource;
import com.fr.design.data.datapane.TableDataComboBox;
import com.fr.design.gui.icombobox.UIComboBoxRenderer;
import com.fr.design.gui.icombobox.LazyComboBox;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.editor.editor.Editor;
import com.fr.form.data.DataBinding;


/**
 * DataBindingEditor
 *
 * @editor zhou
 * @since 2012-3-29下午5:26:28
 */
public class DataBindingEditor extends Editor<DataBinding> {
    private final static int HORI_GAP = 1;
    private final static int VER_GAP = 7;

    private TableDataComboBox tableDataComboBox;
    private LazyComboBox columnNameComboBox;
    private ItemListener tableDataComboBoxListener = new ItemListener() {
        public void itemStateChanged(ItemEvent evt) {
            boolean isInit = columnNameComboBox.getSelectedIndex() == -1;
            columnNameComboBox.loadList();
            if (columnNameComboBox.getItemCount() > 0) {
                columnNameComboBox.setSelectedIndex(0);
            }
            if (!isInit && !stopEvent) {
                DataBindingEditor.this.fireStateChanged();
            }
        }
    };

    private ItemListener columnNameComboboxListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (DataBindingEditor.this.getValue() != DataBinding.empty && e.getStateChange() == ItemEvent.SELECTED) {
                DataBindingEditor.this.fireStateChanged();
            }
        }
    };

    private boolean stopEvent = false;

    public DataBindingEditor() {
        this.initCompontents();
        this.setName(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Widget_Field"));
    }


    private void initCompontents() {
        this.setLayout(new BorderLayout(HORI_GAP, VER_GAP));
        tableDataComboBox = new TableDataComboBox(getTableDataSource());
        tableDataComboBox.setPreferredSize(new Dimension(55, 20));
        tableDataComboBox.addItemListener(tableDataComboBoxListener);

        columnNameComboBox = new LazyComboBox() {

            @Override
            public Object[] load() {
                List<String> l = calculateColumnNameList();
                return l.toArray(new String[l.size()]);
            }

        };
        columnNameComboBox.setRenderer(new UIComboBoxRenderer() {

            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                                                          boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setToolTipText(value == null ? null : value.toString());
                return label;
            }
        });
        columnNameComboBox.setEditable(true);
        this.add(tableDataComboBox, BorderLayout.NORTH);
        this.add(columnNameComboBox, BorderLayout.CENTER);
        columnNameComboBox.addItemListener(columnNameComboboxListener);
    }

    protected TableDataSource getTableDataSource() {
        return DesignTableDataManager.getEditingTableDataSource();
    }

    private List<String> calculateColumnNameList() {
        if (this.tableDataComboBox.getSelectedItem() != null) {
            List<String> list = this.tableDataComboBox.getSelectedItem().calculateColumnNameList();
            if (list != null) {
                return list;
            }
        }
        return new ArrayList<String>();
    }

    @Override
    /**
     * get value
     */
    public DataBinding getValue() {
        if (this.tableDataComboBox.getSelectedItem() == null || this.columnNameComboBox.getSelectedItem() == null) {
            return DataBinding.empty;
        }
        return new DataBinding(this.tableDataComboBox.getSelectedItem().getTableDataName(), (String) this.columnNameComboBox.getSelectedItem());
    }

    @Override
    /**
     * set value
     */
    public void setValue(DataBinding value) {
        if (value == null) {
            return;
        }
        stopEvent = true;
        tableDataComboBox.removeItemListener(tableDataComboBoxListener);
        columnNameComboBox.removeItemListener(columnNameComboboxListener);
        tableDataComboBox.setSelectedTableDataByName(value.getDataSourceName());
        columnNameComboBox.setSelectedItem(value.getDataBindingKey());
        tableDataComboBox.addItemListener(tableDataComboBoxListener);
        columnNameComboBox.addItemListener(columnNameComboboxListener);
        stopEvent = false;
    }

    /**
     * 是否是支持的类型
     * @param object  需要被判断的object
     * @return 如果是，返回true
     */
    public boolean accept(Object object) {
        return object instanceof DataBinding;
    }

    public String getIconName() {
        return "bind_ds_column";
    }
}