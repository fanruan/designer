/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.editor.editor;

import com.fr.design.gui.icombobox.UIComboBoxRenderer;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: wikky
 * Date: 14-3-21
 * Time: 上午9:12
 * To change this template use File | Settings | File Templates.
 */
public class XMLANameEditor extends ColumnIndexEditor{
    private String[] XMLANames;

    /**
     * 多维数据集过滤界面的维度和度量值所用Editor
     */
    public XMLANameEditor() {
        this(ArrayUtils.EMPTY_STRING_ARRAY);
    }

    /**
     * 多维数据集过滤界面的维度和度量值所用Editor
     * @param columnNames 度量值
     */
    public XMLANameEditor(String[] columnNames) {
        this(columnNames, Inter.getLocText("Measure"));
    }

    /**
     * 多维数据集过滤界面的维度和度量值所用Editor
     * @param columnNames 维度或度量值
     * @param name 显示名称
     */
    public XMLANameEditor(final String[] columnNames, String name) {
        super(columnNames.length, name);
        this.XMLANames = columnNames;
        valueColumnIndexComboBox.setRenderer(new UIComboBoxRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (value == null) {
                    this.setText("");
                } else {
                    this.setText(columnNames[((Integer) value).intValue() - 1]);
                    this.setToolTipText(columnNames[((Integer) value).intValue() - 1]);
                }

                return this;
            }
        });
    }

    /**
     *给XMLANames赋值
     * @param value 要赋予的值
     */
    @Override
    public void setValue(Object value) {
        for (int i = 0; i < XMLANames.length; i++) {
            if (XMLANames[i].equalsIgnoreCase(String.valueOf(value))) {
                super.setValue(i + 1);
                return;
            }
        }

        super.reset();
    }

    /**
     * 判断参数是否是String
     * @param object 传进来用于判断的参数
     * @return 参数是否是String
     */
    @Override
    public boolean accept(Object object) {
        return object instanceof String;
    }

    /**
     * 获取维度或度量值
     * @return 返回维度或度量值
     */
    public String getColumnName() {
        int index = ((Integer) this.getValue()).intValue() - 1;
        return getColumnNameAtIndex(index);
    }

    /**
     * 获取维度或度量值
     * @param index 所选择的序号
     * @return 返回序号对应的维度或度量值
     */
    public String getColumnNameAtIndex(int index) {
        return index >= 0 && XMLANames.length > index ? XMLANames[index] : StringUtils.EMPTY;
    }

    /**
     * 获取图标名
     * @return
     */
    public String getIconName() {
        return "cube";
    }

}