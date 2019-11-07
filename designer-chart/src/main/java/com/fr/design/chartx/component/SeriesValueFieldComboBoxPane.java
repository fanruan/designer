package com.fr.design.chartx.component;

import com.fr.chartx.data.field.ColumnField;
import com.fr.chartx.data.field.SeriesValueCorrelationDefinition;
import com.fr.chartx.data.field.SeriesValueField;
import com.fr.data.util.function.AbstractDataFunction;
import com.fr.design.mainframe.chart.gui.data.table.DataPaneHelper;
import com.fr.general.GeneralUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shine on 2019/5/17.
 */
public class SeriesValueFieldComboBoxPane extends AbstractCustomFieldComboBoxPane<SeriesValueCorrelationDefinition> {

    @Override
    protected AbstractUseFieldValuePane createUseFieldValuePane() {
        return new UseFieldValuePane();
    }

    @Override
    protected AbstractCustomFieldNamePane createCustomFieldNamePane() {
        return new CustomFieldNamePane();
    }

    @Override
    public void populateBean(SeriesValueCorrelationDefinition ob) {
        if (ob.isCustomFieldValue()) {
            populateCustomFieldNamePane(ob);
            jcb.setSelectedIndex(1);
        } else {
            populateUseFieldValuePane(ob);
            jcb.setSelectedIndex(0);
        }
    }

    @Override
    public void updateBean(SeriesValueCorrelationDefinition ob) {
        if (jcb.getSelectedIndex() == 0) {
            ob.setCustomFieldValue(false);
            updateUseFieldValuePane(ob);
        } else {
            ob.setCustomFieldValue(true);
            updateCustomFieldNamePane(ob);
        }
    }

    private class UseFieldValuePane extends AbstractUseFieldValuePane {

        @Override
        public void populateBean(SeriesValueCorrelationDefinition ob) {
            List<SeriesValueField> list = ob.getSeriesValueFieldList();
            if (list != null && list.size() > 0) {
                populateSeries(list.get(0).getSeries().getFieldName());
                populateValue(list.get(0).getValue().getFieldName());
                populateFunction((AbstractDataFunction) list.get(0).getValue().getDataFunction());
            }
        }

        @Override
        public void updateBean(SeriesValueCorrelationDefinition ob) {
            List<SeriesValueField> list = new ArrayList<SeriesValueField>();
            SeriesValueField seriesValueField = new SeriesValueField();
            ColumnField series = new ColumnField(updateSeries());
            ColumnField value = new ColumnField(updateValue());
            value.setDataFunction(updateFunction());
            seriesValueField.setValue(value);
            seriesValueField.setSeries(series);
            list.add(seriesValueField);
            ob.setSeriesValueFieldList(list);
        }
    }

    private class CustomFieldNamePane extends AbstractCustomFieldNamePane {
        @Override
        protected List<Object[]> covertTBeanToTableModelList(SeriesValueCorrelationDefinition seriesValueCorrelationDefinition) {
            List<Object[]> list = new ArrayList<Object[]>();
            for (SeriesValueField seriesValueField : seriesValueCorrelationDefinition.getSeriesValueFieldList()) {
                Object[] array = new Object[]{
                        seriesValueField.getValue().getFieldName(),
                        seriesValueField.getSeries().getFieldName(),
                        DataPaneHelper.getFunctionString(seriesValueField.getValue().getDataFunction())
                };
                list.add(array);
            }
            return list;
        }

        @Override
        protected void setTableModelListToTBean(List<Object[]> tableValues, SeriesValueCorrelationDefinition seriesValueCorrelationDefinition) {
            List<SeriesValueField> seriesValueFields = new ArrayList<SeriesValueField>();
            for (Object[] line : tableValues) {
                ColumnField value = new ColumnField(GeneralUtils.objectToString(line[0]));
                ColumnField series = new ColumnField(GeneralUtils.objectToString(line[1]));
                value.setDataFunction(DataPaneHelper.getFunctionByName(GeneralUtils.objectToString(line[2])));
                SeriesValueField seriesValueField = new SeriesValueField();
                seriesValueField.setValue(value);
                seriesValueField.setSeries(series);
                seriesValueFields.add(seriesValueField);
            }
            seriesValueCorrelationDefinition.setSeriesValueFieldList(seriesValueFields);
        }
    }

}
