package com.fr.design.report.freeze;

import com.fr.design.gui.itextfield.UINumberField;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Lenovo
 * Date: 13-3-29
 * Time: 下午12:02
 * To change this template use File | Settings | File Templates.
 */
public class UIIntNumberField extends UINumberField {
    public void setFieldDocument(){
        setDocument(new NumberDocument());
    }

    class NumberDocument extends PlainDocument {
        public NumberDocument() {
        }

        public void insertString(int offset, String s, AttributeSet a) throws BadLocationException {
            String str = getText(0, getLength());

            if (!s.matches("^[0-9]+$")) {
                Toolkit.getDefaultToolkit().beep();
                return;
            }

            String strNew = str.substring(0, offset) + s + str.substring(offset, getLength());

            if (isOverMaxOrMinValue(strNew)) {
                Toolkit.getDefaultToolkit().beep();
                return;
            }

             setisContentChanged(true);
            super.insertString(offset, s, a);
        }

        private boolean isOverMaxOrMinValue( String strNew) {
            return  (Double.parseDouble(strNew)<getMinValue() || Double.parseDouble(strNew)>getMaxValue());
        }
    }
}