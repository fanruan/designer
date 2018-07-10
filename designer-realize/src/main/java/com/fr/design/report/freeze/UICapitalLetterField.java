package com.fr.design.report.freeze;


import com.fr.design.gui.itextfield.UINumberField;
import com.fr.stable.StableUtils;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Lenovo
 * Date: 13-3-29
 * Time: 下午12:41
 * To change this template use File | Settings | File Templates.
 */

public class UICapitalLetterField extends UINumberField {
    public void setValue(double value) {
        this.setText(StableUtils.convertIntToABC((int)value));
    }

    public double getValue() throws NumberFormatException {
        try {
            if (this.getText().length() == 0) {
                return 0;
            }

            return StableUtils.convertABCToInt(this.getText());
        } catch (NumberFormatException numberFormatException) {
            return UINumberField.ERROR_VALUE;
        }
    }

    public void setFieldDocument(){
        setDocument(new NumberDocument());
    }


    class NumberDocument extends PlainDocument {
            public NumberDocument() {
            }

            public void insertString(int offset, String s, AttributeSet a) throws BadLocationException {

                if(s.matches("^[a-z]+$")) {
                  s = s.toUpperCase();
                }

                if (!s.matches("^[A-Z]+$")) {
                    Toolkit.getDefaultToolkit().beep();
                    return;
                }

                setisContentChanged(true);
                super.insertString(offset, s, a);
            }


    }
}