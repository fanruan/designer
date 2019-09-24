package com.fr.design.gui.ispinner;

import com.fr.design.gui.itextfield.UIIntNumberField;
import com.fr.design.gui.itextfield.UINumberField;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * Created by IntelliJ IDEA.
 * Author : Hugh.C
 * Date: 19-08-28
 * Time: 下午2:19
 */
public class UnsignedIntUISpinner extends UISpinner {

    private double minValue;
    private double maxValue;

    public UnsignedIntUISpinner(double minValue, double maxValue, double dierta) {
        super(minValue, maxValue, dierta);
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public UnsignedIntUISpinner(double minValue, double maxValue, double dierta, double defaultValue) {
        super(minValue, maxValue, dierta, defaultValue);
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @Override
    protected UINumberField initNumberField() {
        final UIIntNumberField numberField = new UIIntNumberField() {
            public boolean shouldResponseChangeListener() {
                return false;
            }

            public NumberDocument createNumberDocument() {
                return new NumberDocument() {
                    public boolean isContinueInsertWhenOverMaxOrMinValue() {
                        return true;
                    }
                };
            }
        };
        numberField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            /**
             * 失去焦点后再做范围限制、不然最小值为 100 时，输个 1 都不让....
             * @param e
             */
            @Override
            public void focusLost(FocusEvent e) {
                double value = numberField.getValue();
                if (!isOverMaxOrMinValue(value)) {
                    return;
                }
                numberField.setValue(value < minValue ? minValue : maxValue);
            }

            private boolean isOverMaxOrMinValue(double value) {
                return value < minValue || value > maxValue;
            }
        });
        return numberField;
    }
}