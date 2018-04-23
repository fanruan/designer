package com.fr.design.gui.date;

import com.fr.design.gui.itextfield.UINumberField;
import com.fr.general.Inter;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * Created with IntelliJ IDEA.
 * User: 小灰灰
 * Date: 13-9-30
 * Time: 上午11:06
 * To change this template use File | Settings | File Templates.
 */
public class CalendarNumberField extends UINumberField {

    private static final int NUM_TEN = 10;

    public CalendarNumberField( double maxValue) {
        super(2, 0, 0, maxValue);
        this.setBorderPainted(false);
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                setValue(getIntValue());
            }
        });
        this.setFont(new Font(Inter.getLocText("Song_TypeFace"),0,12));
    }

    public void setValue(int value) {
        if (value < 0) {
            value = (int)getMaxValue();
        }
        if (value > getMaxValue()) {
            value = 0;
        }
        this.setText(getValueText(value));
    }

    public int getIntValue () {
        if (this.getText().length() == 0) {
            return 0;
        }
        return Integer.parseInt(getText());
    }

    private String getValueText(int value) {
        String str;
        if (value < NUM_TEN) {
            str = "0" + value;
        } else {
            str = Integer.toString(value);
        }
        return str;
    }



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

    public Dimension getPreferredSize() {
        return new Dimension(24, 11);
    }
    public boolean shouldResponseChangeListener() {
        return false;
    }

    public Insets getInsets() {
        return new Insets(0, 6, 0, 4);
    }

}