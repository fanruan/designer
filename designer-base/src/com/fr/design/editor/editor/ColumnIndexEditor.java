package com.fr.design.editor.editor;

import com.fr.design.gui.icombobox.IntComboBox;
import com.fr.general.Inter;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;

/**
 * 列序号编辑器：里面是一排连续的整数(0-value)
 *
 * @author zhou
 * @since 2012-6-1下午2:24:04
 */
public class ColumnIndexEditor extends Editor<Object> {
    protected IntComboBox valueColumnIndexComboBox;

    public ColumnIndexEditor() {
        this(0);
    }

    /**
     * 默认名字是“列序号”，也可以通过第二个构造函数改变
     *
     * @param value
     */
    public ColumnIndexEditor(int value) {
        this(value, Inter.getLocText("Datasource-Column_Index"));
    }

    public ColumnIndexEditor(int value, String name) {
        this.setLayout(new BorderLayout(0, 0));
        valueColumnIndexComboBox = new IntComboBox();
        for (int i = 1; i <= value; i++) {
            valueColumnIndexComboBox.addItem(new Integer(i));
        }

        if (value > 0) {
            valueColumnIndexComboBox.setSelectedInt(1);
        }
        this.add(valueColumnIndexComboBox, BorderLayout.CENTER);
        this.setName(name);
        valueColumnIndexComboBox.setBorder(null);

    }

    @Override
    public Integer getValue() {
        return valueColumnIndexComboBox.getSelectedInt();
    }

    @Override
    public void setValue(Object value) {
        valueColumnIndexComboBox.setSelectedInt(value == null ? 0 : Integer.parseInt(value.toString()));
    }

    public String getIconName() {
        return "ds_column_index";
    }

    /**
     * object参数是否是Integer
     *
     * @param object 传进来用于判断的参数
     * @return 返回是否是Index
     */
    public boolean accept(Object object) {
        return object instanceof Integer;
    }

    /**
     * 增加一个ItemListener
     *
     * @param l 用于增加的Listener
     */
    public void addItemListener(ItemListener l) {
        valueColumnIndexComboBox.addItemListener(l);
    }

    /**
     * 增加一个ActionListener
     *
     * @param l 用于增加的Listener
     */
    public void addActionListener(ActionListener l){
        valueColumnIndexComboBox.addActionListener(l);
    }

    /**
     * 重置
     */
    public void reset() {
        valueColumnIndexComboBox.setSelectedIndex(-1);
    }

    /**
     * 清除所有项
     */
    public void clearData() {
        valueColumnIndexComboBox.removeAllItems();
    }


    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        valueColumnIndexComboBox.setEnabled(enabled);
    }

}