package com.fr.design.chartx.fields.diff;

import com.fr.chartx.data.field.diff.ColumnFieldCollectionWithSeriesValue;
import com.fr.design.gui.icombobox.UIComboBox;

/**
 * @author shine
 * @version 10.0
 * Created by shine on 2019/11/28
 * <p>
 * 饼图、多指针仪表盘 在组合图中 没有分类配置项。
 */
public class SeriesValueDataSetFieldsPane extends AbstractDataSetFieldsWithSeriesValuePane<ColumnFieldCollectionWithSeriesValue> {
    @Override
    protected String[] fieldLabels() {
        return new String[0];
    }

    @Override
    protected UIComboBox[] filedComboBoxes() {
        return new UIComboBox[0];
    }

    @Override
    public void populateBean(ColumnFieldCollectionWithSeriesValue ob) {
        populateSeriesValuePane(ob);
    }

    @Override
    public ColumnFieldCollectionWithSeriesValue updateBean() {
        ColumnFieldCollectionWithSeriesValue columnFieldCollectionWithSeriesValue = new ColumnFieldCollectionWithSeriesValue();
        updateSeriesValuePane(columnFieldCollectionWithSeriesValue);
        return columnFieldCollectionWithSeriesValue;
    }
}
