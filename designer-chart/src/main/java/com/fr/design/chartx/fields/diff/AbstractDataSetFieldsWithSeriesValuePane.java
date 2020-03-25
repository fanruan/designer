package com.fr.design.chartx.fields.diff;

import com.fr.chartx.data.field.diff.ColumnFieldCollectionWithSeriesValue;
import com.fr.design.chartx.component.SeriesValueFieldComboBoxPane;
import com.fr.design.chartx.fields.AbstractDataSetFieldsPane;

import javax.swing.JPanel;
import java.util.List;
import java.awt.BorderLayout;

/**
 * Created by shine on 2019/5/16.
 * 带有 自定义系列名（fr表现为 系列名使用字段名） 的字段集合 的一个pane
 */
public abstract class AbstractDataSetFieldsWithSeriesValuePane<T extends ColumnFieldCollectionWithSeriesValue>
        extends AbstractDataSetFieldsPane<T> {

    private SeriesValueFieldComboBoxPane seriesValueFieldComboBoxPane;

    @Override
    protected JPanel createCenterPane() {
        JPanel normalCenter = super.createCenterPane();
        seriesValueFieldComboBoxPane = new SeriesValueFieldComboBoxPane();

        if (normalCenter != null) {
            JPanel panel = new JPanel(new BorderLayout(0,6));
            panel.add(normalCenter, BorderLayout.NORTH);
            panel.add(seriesValueFieldComboBoxPane, BorderLayout.CENTER);
            return panel;
        } else {
            return seriesValueFieldComboBoxPane;
        }
    }

    public SeriesValueFieldComboBoxPane getSeriesValueFieldComboBoxPane() {
        if (seriesValueFieldComboBoxPane == null) {
            seriesValueFieldComboBoxPane = new SeriesValueFieldComboBoxPane();
        }
        return seriesValueFieldComboBoxPane;
    }

    public void setSeriesValueFieldComboBoxPane(SeriesValueFieldComboBoxPane seriesValueFieldComboBoxPane) {
        this.seriesValueFieldComboBoxPane = seriesValueFieldComboBoxPane;
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

    protected void populateSeriesValuePane(ColumnFieldCollectionWithSeriesValue fieldCollectionWithSeriesValue) {
        seriesValueFieldComboBoxPane.populateBean(fieldCollectionWithSeriesValue.getSeriesValueCorrelationDefinition());
    }

    protected void updateSeriesValuePane(ColumnFieldCollectionWithSeriesValue fieldCollectionWithSeriesValue) {
        seriesValueFieldComboBoxPane.updateBean(fieldCollectionWithSeriesValue.getSeriesValueCorrelationDefinition());
    }
}