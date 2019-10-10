package com.fr.design.gui.itextfield;


import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.Toolkit;

/**
 * Created with IntelliJ IDEA.
 * User: Lenovo
 * Date: 13-3-29
 * Time: 下午12:02
 * To change this template use File | Settings | File Templates.
 */
public class UIIntNumberField extends UINumberField {
    public void setFieldDocument() {
        setDocument(createNumberDocument());
    }

    public class NumberDocument extends PlainDocument {
        public NumberDocument() {
        }

        public void insertString(int offset, String s, AttributeSet a) throws BadLocationException {
            String str = getText(0, getLength());

            if (!s.matches("^[0-9]+$")) {
                Toolkit.getDefaultToolkit().beep();
                return;
            }

            String strNew = str.substring(0, offset) + s + str.substring(offset, getLength());

            if (isOverMaxOrMinValue(strNew) && !isContinueInsertWhenOverMaxOrMinValue()) {
                Toolkit.getDefaultToolkit().beep();
                return;
            }

            setisContentChanged(true);
            super.insertString(offset, s, a);
        }

        /**
         * 是否继续插入输入的字符 - 当超出范围时
         *
         * @return true : 继续插入输入的字符
         */
        public boolean isContinueInsertWhenOverMaxOrMinValue() {
            return false;
        }

        private boolean isOverMaxOrMinValue( String strNew) {
            return  (Double.parseDouble(strNew)<getMinValue() || Double.parseDouble(strNew)>getMaxValue());
        }
    }

    public NumberDocument createNumberDocument() {
        return new NumberDocument();
    }
}