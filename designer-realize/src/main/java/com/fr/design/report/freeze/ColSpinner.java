package com.fr.design.report.freeze;

import com.fr.design.gui.ispinner.UISpinner;

import com.fr.design.gui.itextfield.UINumberField;

/**
 * Created with IntelliJ IDEA.
 * User: Lenovo
 * Date: 13-3-29
 * Time: 下午12:35
 * To change this template use File | Settings | File Templates.
 */
public class ColSpinner extends UISpinner {
    public ColSpinner(double minValue, double maxValue, double dierta) {
        super(minValue, maxValue, dierta);
    }

    public ColSpinner(double minValue, double maxValue, double dierta, double defaultValue) {
        super(minValue, maxValue, dierta, defaultValue);
    }
    @Override
    protected UINumberField initNumberField(){
        return new UICapitalLetterField();
    }
}