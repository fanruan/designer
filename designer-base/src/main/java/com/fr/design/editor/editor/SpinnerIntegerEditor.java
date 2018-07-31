package com.fr.design.editor.editor;

import com.fr.base.Utils;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.layout.FRGUIPaneFactory;

import com.fr.stable.StringUtils;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class SpinnerIntegerEditor extends Editor<Integer> {

    private UISpinner spinner;
    private String oldValue = StringUtils.EMPTY;

    public SpinnerIntegerEditor(){
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        spinner = new UISpinner(-Integer.MAX_VALUE, Integer.MAX_VALUE, 1, 0);
        this.add(spinner, BorderLayout.CENTER);
        this.spinner.addKeyListener(textKeyListener);
        this.setName(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Parameter_Integer"));
    }

    @Override
    public Integer getValue() {
        return new Integer((int) this.spinner.getValue());
    }

    @Override
    public void setValue(Integer value) {
        if (value == null) {
            value = new Integer(0);
        }
        this.spinner.setValue(value.intValue());
        oldValue = Utils.objectToString(value);
    }

    @Override
    public boolean accept(Object object) {
        return object != null && object instanceof Integer;
    }

    public String getIconName() {
        return "type_int";
    }

    KeyListener textKeyListener = new KeyAdapter() {

        public void keyReleased(KeyEvent evt) {
            int code = evt.getKeyCode();

            if (code == KeyEvent.VK_ESCAPE) {
                spinner.setValue(Double.parseDouble(oldValue));
            }
            if (code == KeyEvent.VK_ENTER) {
                fireEditingStopped();
            } else {
                fireStateChanged();
            }
        }
    };
}
