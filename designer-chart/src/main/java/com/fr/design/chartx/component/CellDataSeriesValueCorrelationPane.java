package com.fr.design.chartx.component;

import com.fr.chartx.data.field.ColumnField;
import com.fr.chartx.data.field.SeriesValueCorrelationDefinition;
import com.fr.chartx.data.field.SeriesValueField;
import com.fr.design.chartx.component.correlation.AbstractCorrelationPane;
import com.fr.design.chartx.component.correlation.FieldEditorComponentWrapper;
import com.fr.design.chartx.component.correlation.TinyFormulaPaneEditorComponent;
import com.fr.design.i18n.Toolkit;
import com.fr.general.GeneralUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shine on 2019/6/4.
 */
public class CellDataSeriesValueCorrelationPane extends AbstractCorrelationPane<SeriesValueCorrelationDefinition> {

    @Override
    protected FieldEditorComponentWrapper[] createFieldEditorComponentWrappers() {
        return new FieldEditorComponentWrapper[]{
                new TinyFormulaPaneEditorComponent(Toolkit.i18nText("Fine-Design_Chart_Series_Name")),
                new TinyFormulaPaneEditorComponent(Toolkit.i18nText("Fine-Design_Chart_Series_Value"))
        };
    }

    @Override
    protected List<Object[]> covertTBeanToTableModelList(SeriesValueCorrelationDefinition seriesValueCorrelationDefinition) {
        List<Object[]> result = new ArrayList<Object[]>();

        List<SeriesValueField> seriesValueFieldList = seriesValueCorrelationDefinition.getSeriesValueFieldList();
        for (SeriesValueField seriesValueField : seriesValueFieldList) {
            Object[] array = new Object[]{seriesValueField.getSeries().getFieldName(), seriesValueField.getValue().getFieldName()};
            result.add(array);
        }

        return result;
    }

    @Override
    protected void setTableModelListToTBean(List<Object[]> tableValues, SeriesValueCorrelationDefinition seriesValueCorrelationDefinition) {
        List<SeriesValueField> seriesValueFieldList = new ArrayList<SeriesValueField>();

        for (Object[] oneLine : tableValues) {
            SeriesValueField seriesValueField = new SeriesValueField();
            ColumnField series = new ColumnField(GeneralUtils.objectToString(oneLine[0]));
            ColumnField value = new ColumnField(GeneralUtils.objectToString(oneLine[1]));
            seriesValueField.setSeries(series);
            seriesValueField.setValue(value);
            seriesValueFieldList.add(seriesValueField);
        }

        seriesValueCorrelationDefinition.setSeriesValueFieldList(seriesValueFieldList);
    }
}
