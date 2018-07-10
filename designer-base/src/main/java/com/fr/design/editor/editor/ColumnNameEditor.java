package com.fr.design.editor.editor;

import com.fr.design.gui.icombobox.UIComboBoxRenderer;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;

/**
 * 列编辑器，里面是列名
 *
 * @author zhou
 * @since 2012-6-1下午2:25:16
 */
public class ColumnNameEditor extends ColumnIndexEditor {

    private String[] columnNames;

    public ColumnNameEditor() {
        this(ArrayUtils.EMPTY_STRING_ARRAY);
    }

    public ColumnNameEditor(String[] columnNames) {
        this(columnNames, Inter.getLocText("ColumnName"));
    }

    public ColumnNameEditor(final String[] columnNames, String name) {
        super(columnNames.length, name);
        this.columnNames = columnNames;
        valueColumnIndexComboBox.setRenderer(new UIComboBoxRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (value == null) {
                    this.setText("");
                } else {
                    this.setText(columnNames[((Integer) value).intValue() - 1]);
                }

                return this;
            }
        });
    }

    @Override
    public void setValue(Object value) {
        for (int i = 0; i < columnNames.length; i++) {
            if (columnNames[i].equalsIgnoreCase(String.valueOf(value))) {
                super.setValue(i + 1);
                return;
            }
        }

        super.reset();
    }

    @Override
    public boolean accept(Object object) {
        return object instanceof String;
    }


    public String getColumnName() {
        int index = ((Integer) this.getValue()).intValue() - 1;
        return getColumnNameAtIndex(index);
    }

    public String getColumnNameAtIndex(int index) {
        return index >= 0 && columnNames.length > index ? columnNames[index] : StringUtils.EMPTY;
    }

    public String getIconName() {
        return "ds_column_name";
    }

}