package com.fr.extended.chart;

import com.fr.base.Utils;
import com.fr.data.util.function.AbstractDataFunction;
import com.fr.design.chartx.component.AbstractCustomFieldComboBoxPane;
import com.fr.design.mainframe.chart.gui.data.table.DataPaneHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shine on 2019/5/16.
 */
public class ExtendedCustomFieldComboBoxPane extends AbstractCustomFieldComboBoxPane<AbstractDataConfig> {
    @Override
    protected AbstractUseFieldValuePane createUseFieldValuePane() {
        return new ExtendedUseFieldValuePane();
    }

    @Override
    protected AbstractCustomFieldNamePane createCustomFieldNamePane() {
        return new ExtendedCustomFieldNamePane();
    }


    @Override
    public void populateBean(AbstractDataConfig ob) {
        if (ob.isCustomName()) {
            populateCustomFieldNamePane(ob);
            jcb.setSelectedIndex(1);
        } else {
            populateUseFieldValuePane(ob);
            jcb.setSelectedIndex(0);
        }
    }

    @Override
    public void updateBean(AbstractDataConfig ob) {
        if (jcb.getSelectedIndex() == 0) {
            ob.setCustomName(false);
            updateUseFieldValuePane(ob);
        } else {
            ob.setCustomName(true);
            updateCustomFieldNamePane(ob);
        }
    }

    private class ExtendedUseFieldValuePane extends AbstractUseFieldValuePane {

        @Override
        public void populateBean(AbstractDataConfig ob) {
            List<ExtendedField> list = ob.getCustomFields();
            if (list.size() == 2) {
                populateSeries(list.get(0).getFieldName());
                populateValue(list.get(1).getFieldName());
                populateFunction((AbstractDataFunction) list.get(1).getDataFunction());
            }
        }

        @Override
        public void updateBean(AbstractDataConfig ob) {
            List<ExtendedField> list = new ArrayList<ExtendedField>();

            list.add(new ExtendedField(updateSeries()));
            ExtendedField field = new ExtendedField(updateValue());
            field.setDataFunction(updateFunction());
            list.add(field);

            ob.setCustomFields(list);
        }


    }

    private class ExtendedCustomFieldNamePane extends AbstractCustomFieldNamePane {

        @Override
        protected List<Object[]> covertTBeanToTableModelList(AbstractDataConfig dataConfig) {
            List<ExtendedField> customFields = dataConfig.getCustomFields();

            List<Object[]> list = new ArrayList<Object[]>();
            for (ExtendedField field : customFields) {
                String[] array = {field.getFieldName(), field.getCustomName(), DataPaneHelper.getFunctionString(field.getDataFunction())};
                list.add(array);
            }
            return list;
        }

        @Override
        protected void setTableModelListToTBean(List<Object[]> tableValues, AbstractDataConfig dataConfig) {
            List<ExtendedField> customFields = new ArrayList<ExtendedField>();
            for (Object[] line : tableValues) {
                ExtendedField field = new ExtendedField(Utils.objectToString(line[0]));
                field.setCustomName(Utils.objectToString(line[1]));
                if (line.length > 2) {
                    field.setDataFunction(DataPaneHelper.getFunctionByName(Utils.objectToString(line[2])));
                }
                customFields.add(field);
            }

            dataConfig.setCustomFields(customFields);
        }

    }

}
