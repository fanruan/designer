package com.fr.design.mainframe.vcs.ui;

import com.fr.design.editor.editor.DateEditor;
import com.fr.log.FineLoggerFactory;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by XiaXiang on 2019/5/14.
 */
public class VcsDateEditor extends DateEditor {
    private Date tempValue;

    public VcsDateEditor(Date value, boolean format, String name, int dateFormat) {
        super(value, format, name, dateFormat);
        this.tempValue = value;
    }

    @Override
    public Date getValue() {
        if (tempValue == null) {
            return null;
        }
        return super.getValue();
    }

    @Override
    public void setValue(Date value) {
        this.tempValue = value;
        try {
            getUiDatePicker().setSelectedDate(value);
        } catch (ParseException parseException) {
            FineLoggerFactory.getLogger().error(parseException.getMessage(), parseException);
        }
    }
}
