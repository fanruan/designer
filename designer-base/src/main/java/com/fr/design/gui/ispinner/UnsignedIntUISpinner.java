package com.fr.design.gui.ispinner;

import com.fr.design.gui.itextfield.UIIntNumberField;
import com.fr.design.gui.itextfield.UINumberField;

/**
 * Created by IntelliJ IDEA.
 * Author : Hugh.C
 * Date: 19-08-28
 * Time: 下午2:19
 */
public class UnsignedIntUISpinner extends UISpinner {


    public UnsignedIntUISpinner(double minValue, double maxValue, double dierta) {
        super(minValue, maxValue, dierta);
    }

    public UnsignedIntUISpinner(double minValue, double maxValue, double dierta, double defaultValue) {
        super(minValue, maxValue, dierta, defaultValue);
    }

    @Override
    protected UINumberField initNumberField() {
        return new UIIntNumberField() {
            public boolean shouldResponseChangeListener() {
                return false;
            }
        };
    }
}