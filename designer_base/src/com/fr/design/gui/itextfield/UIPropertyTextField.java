package com.fr.design.gui.itextfield;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.text.Document;


/**
 * Created by ibm on 2017/8/16.
 */
public class UIPropertyTextField extends UITextField{

    public UIPropertyTextField() {
        super();
    }

    public UIPropertyTextField(int columns) {
        super(columns);
    }

    public UIPropertyTextField(String text, int columns) {
        super(text, columns);
    }

    public UIPropertyTextField(String text) {
        super(text);
    }

    public UIPropertyTextField(Document doc, String text, int columns) {
        super(doc, text, columns);
    }

    protected void initListener() {
        if (shouldResponseChangeListener()) {
            addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {
                }

                @Override
                public void focusLost(FocusEvent e) {
                    attributeChange();
                }
            });
        }
    }
}
