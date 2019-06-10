package com.fr.design.chartx.fields.diff;

import com.fr.chartx.data.field.diff.AbstractColumnFieldCollectionWithSeriesValue;
import com.fr.design.chartx.component.SeriesValueFieldComboBoxPane;
import com.fr.design.chartx.fields.AbstractDataSetFieldsPane;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.util.List;

/**
 * Created by shine on 2019/5/16.
 * 带有 自定义系列名（fr表现为 系列名使用字段名） 的字段集合 的一个pane
 */
public abstract class AbstractDataSetFieldsWithSeriesValuePane<T extends AbstractColumnFieldCollectionWithSeriesValue>
        extends AbstractDataSetFieldsPane<T> {

    private SeriesValueFieldComboBoxPane seriesValueFieldComboBoxPane;

    @Override
    protected JPanel createCenterPane() {
        JPanel normalCenter = super.createCenterPane();
        seriesValueFieldComboBoxPane = new SeriesValueFieldComboBoxPane();

        if (normalCenter != null) {
            JPanel panel = new JPanel(new BorderLayout());
            panel.add(normalCenter, BorderLayout.CENTER);
            panel.add(seriesValueFieldComboBoxPane, BorderLayout.SOUTH);
            return panel;
        } else {
            return seriesValueFieldComboBoxPane;
        }
    }

    @Override
    public void checkBoxUse(boolean hasUse) {
        super.checkBoxUse(hasUse);
        seriesValueFieldComboBoxPane.checkBoxUse(hasUse);
    }

    @Override
    public void clearAllBoxList() {
        super.clearAllBoxList();
        seriesValueFieldComboBoxPane.clearAllBoxList();
    }

    @Override
    public void refreshBoxListWithSelectTableData(List columnNameList) {
        super.refreshBoxListWithSelectTableData(columnNameList);
        seriesValueFieldComboBoxPane.refreshBoxListWithSelectTableData(columnNameList);
    }

    protected void populateSeriesValuePane(AbstractColumnFieldCollectionWithSeriesValue fieldCollectionWithSeriesValue) {
        seriesValueFieldComboBoxPane.populateBean(fieldCollectionWithSeriesValue.getSeriesValueCorrelationDefinition());
    }

    protected void updateSeriesValuePane(AbstractColumnFieldCollectionWithSeriesValue fieldCollectionWithSeriesValue) {
        seriesValueFieldComboBoxPane.updateBean(fieldCollectionWithSeriesValue.getSeriesValueCorrelationDefinition());
    }
}
