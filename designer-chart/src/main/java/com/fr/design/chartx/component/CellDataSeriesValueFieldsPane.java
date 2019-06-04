package com.fr.design.chartx.component;

import com.fr.chartx.data.field.ColumnField;
import com.fr.chartx.data.field.SeriesValueColumnFields;
import com.fr.chartx.data.field.SeriesValueField;
import com.fr.design.i18n.Toolkit;
import com.fr.general.GeneralUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shine on 2019/6/4.
 */
public class CellDataSeriesValueFieldsPane extends AbstractCorrelationPane<SeriesValueColumnFields> {

    @Override
    protected FieldEditorComponentWrapper[] fieldEditorComponentWrappers() {
        return new FieldEditorComponentWrapper[]{
                new TinyFormulaPaneEditorComponent(Toolkit.i18nText("Fine-Design_Chart_Series_Name")),
                new TinyFormulaPaneEditorComponent(Toolkit.i18nText("Fine-Design_Chart_Series_Value"))
        };
    }

    @Override
    public void populateBean(SeriesValueColumnFields ob) {
        List<Object[]> list = new ArrayList<Object[]>();

        List<SeriesValueField> seriesValueFieldList = ob.getSeriesValueFieldList();
        for (SeriesValueField seriesValueField : seriesValueFieldList) {
            Object[] array = new Object[]{seriesValueField.getSeries().getFieldName(), seriesValueField.getValue().getFieldName()};
            list.add(array);
        }

        populate(list);
    }

    @Override
    public void updateBean(SeriesValueColumnFields ob) {
        List<Object[]> list = update();

        List<SeriesValueField> seriesValueFieldList = new ArrayList<SeriesValueField>();

        for (Object[] objects : list) {
            SeriesValueField seriesValueField = new SeriesValueField();
            ColumnField series = new ColumnField(GeneralUtils.objectToString(objects[0]));
            ColumnField value = new ColumnField(GeneralUtils.objectToString(objects[1]));
            seriesValueField.setSeries(series);
            seriesValueField.setValue(value);
            seriesValueFieldList.add(seriesValueField);
        }

        ob.setSeriesValueFieldList(seriesValueFieldList);
    }
}
