package com.fr.design.chartx.component;

import com.fr.chartx.data.field.CustomFieldValueColumnFields;

/**
 * Created by shine on 2019/5/17.
 */
public class CustomFieldComboBoxPane extends AbstractCustomFieldComboBoxPane<CustomFieldValueColumnFields> {

    @Override
    protected AbstractUseFieldValuePane createUseFieldValuePane() {
        return new UseFieldValuePane();
    }

    @Override
    protected AbstractCustomFieldNamePane createCustomFieldNamePane() {
        return new CustomFieldNamePane();
    }

    @Override
    public void populateBean(CustomFieldValueColumnFields ob) {
        if (ob.isCustomFieldValue()) {
            populateCustomFieldNamePane(ob);
            jcb.setSelectedIndex(1);
        } else {
            populateUseFieldValuePane(ob);
            jcb.setSelectedIndex(0);
        }
    }

    @Override
    public void updateBean(CustomFieldValueColumnFields ob) {
        if (jcb.getSelectedIndex() == 0) {
            ob.setCustomFieldValue(false);
            updateUseFieldValuePane(ob);
        } else {
            ob.setCustomFieldValue(true);
            updateCustomFieldNamePane(ob);
        }
    }

    private class UseFieldValuePane extends AbstractUseFieldValuePane<CustomFieldValueColumnFields> {

        @Override
        public void populateBean(CustomFieldValueColumnFields ob) {

        }
    }

    private class CustomFieldNamePane extends AbstractCustomFieldNamePane<CustomFieldValueColumnFields> {

        @Override
        public void populateBean(CustomFieldValueColumnFields ob) {

        }
    }

}
