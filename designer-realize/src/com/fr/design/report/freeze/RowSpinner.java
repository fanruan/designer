package com.fr.design.report.freeze;

import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.gui.itextfield.UINumberField;

/**
 * Created with IntelliJ IDEA.
 * User: Lenovo
 * Date: 13-3-29
 * Time: 上午11:13
 * To change this template use File | Settings | File Templates.
 */


public class RowSpinner extends UISpinner {

    public RowSpinner(double minValue, double maxValue, double dierta) {
        super(minValue, maxValue, dierta);
    }

    public RowSpinner(double minValue, double maxValue, double dierta, double defaultValue) {
        super(minValue, maxValue, dierta, defaultValue);
    }

    @Override
    protected UINumberField initNumberField(){
        return new UIIntNumberField();
    }



}