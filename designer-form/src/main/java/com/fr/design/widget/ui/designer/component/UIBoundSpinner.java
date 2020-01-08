package com.fr.design.widget.ui.designer.component;

import com.fr.design.gui.ispinner.UISpinner;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by kerry on 2017/11/7.
 */
public class UIBoundSpinner extends UISpinner{

    public UIBoundSpinner(double minValue, double maxValue, double dierta) {
        super(minValue, maxValue, dierta);
        initComponents();
    }

    public UIBoundSpinner(double minValue, double maxValue, double dierta, double defaultValue) {
        super(minValue, maxValue, dierta, defaultValue);
        initComponents();
    }

    private void initComponents() {
        setTextField(value);
    }

    @Override
    protected void initTextFiledListeners(){
        this.getTextField().addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                setTextFieldValue(getTextField().getValue());
                setTextField(value);
            }
        });

        this.getTextField().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    setTextFieldValue(getTextField().getValue());
                    setTextField(value);
                }
            }
        });
    }

    @Override
    protected void setTextField(double value){
        this.getTextField().setValue(value);
    }
}
