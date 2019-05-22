package com.fr.design.chartx.fields.diff;

import com.fr.chartx.data.field.diff.AbstractColumnFieldCollectionWithCustomField;
import com.fr.design.chartx.component.CustomFieldComboBoxPane;
import com.fr.design.chartx.fields.AbstractDataSetFieldsPane;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.util.List;

/**
 * Created by shine on 2019/5/16.
 * 带有 自定义系列名（fr表现为 系列名使用字段名） 的字段集合 的一个pane
 */
public abstract class AbstractDataSetFieldsWithCustomFieldPane<T extends AbstractColumnFieldCollectionWithCustomField>
        extends AbstractDataSetFieldsPane<T> {

    private CustomFieldComboBoxPane customFieldComboBoxPane;

    @Override
    protected JPanel createCenterPane() {
        JPanel normalCenter = super.createCenterPane();
        customFieldComboBoxPane = new CustomFieldComboBoxPane();

        if (normalCenter != null) {
            JPanel panel = new JPanel(new BorderLayout());
            panel.add(normalCenter, BorderLayout.CENTER);
            panel.add(customFieldComboBoxPane, BorderLayout.SOUTH);
            return panel;
        } else {
            return customFieldComboBoxPane;
        }
    }

    @Override
    public void checkBoxUse(boolean hasUse) {
        super.checkBoxUse(hasUse);
        customFieldComboBoxPane.checkBoxUse(hasUse);
    }

    @Override
    public void clearAllBoxList() {
        super.clearAllBoxList();
        customFieldComboBoxPane.clearAllBoxList();
    }

    @Override
    public void refreshBoxListWithSelectTableData(List columnNameList) {
        super.refreshBoxListWithSelectTableData(columnNameList);
        customFieldComboBoxPane.refreshBoxListWithSelectTableData(columnNameList);
    }

    protected void populateCustomPane(AbstractColumnFieldCollectionWithCustomField t) {
        customFieldComboBoxPane.populateBean(t.getCustomFieldValueColumnFields());
    }

    protected void updateCustomPane(AbstractColumnFieldCollectionWithCustomField t) {
        customFieldComboBoxPane.updateBean(t.getCustomFieldValueColumnFields());
    }
}
