package com.fr.design.report;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.report.write.ValueVerifier;

import java.awt.*;

/**
 * Created by richie on 16/6/12.
 */
public class BuildInVerifierPane extends BasicBeanPane<ValueVerifier> {
    private ValueVerifierEditPane valueVerifierEditPane;

    public BuildInVerifierPane() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        valueVerifierEditPane = new ValueVerifierEditPane();
        this.add(valueVerifierEditPane, BorderLayout.CENTER);
    }

    @Override
    public void populateBean(ValueVerifier ob) {
        valueVerifierEditPane.populate(ob);
    }

    @Override
    public ValueVerifier updateBean() {
        return valueVerifierEditPane.update();
    }

    @Override
    protected String title4PopupWindow() {
        return "BuiltIn";
    }
}
