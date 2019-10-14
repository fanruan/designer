package com.fr.design.gui.itextfield;

import com.fr.base.Utils;
import com.fr.design.gui.ilable.UILabel;
import com.fr.general.ComparatorUtils;
import com.fr.stable.CoreConstants;
import com.fr.stable.StringUtils;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;

/**
 * Number Field.
 */
public class UINumberField extends UITextField {
    public static final double ERROR_VALUE = 0; // peter:错误的值. mata:如果输入负号之类直接走ERROR_VALUE
    public static final int MAX_INTEGERLENGTH = 24;
    public static final int MAX_INTEGERLENGTH_32 = 32;
    public static final int MAX_DECIMALLENGTH = 16;
    /**
     * 整数部分的长度
     */
    private int maxIntegerLength = MAX_INTEGERLENGTH;
    /**
     * 小数部分的长度
     */
    private static final int DEFAULTMAXDECIMALLENTH = 16;
    private static final int TESTMAXVALUE = 100;
    private static final int TESTMINVALUE = -10;
    private int maxDecimalLength = MAX_DECIMALLENGTH;
    private double minValue = -Double.MAX_VALUE;
    private double maxValue = Double.MAX_VALUE;

    private boolean isContentChanged = false;
    private boolean fillNegativeNumber = true;

    public UINumberField() {
        this(MAX_INTEGERLENGTH_32, MAX_DECIMALLENGTH);
    }

    public UINumberField(int columns) {
        this();
        setColumns(columns);
    }

    public UINumberField(int maxIntegerLength, int maxDecimalLength) {
        this(maxIntegerLength, maxDecimalLength, -Double.MAX_VALUE, Double.MAX_VALUE);
    }

    public UINumberField(int maxIntegerLength, int maxDecimalLength, double minValue, double maxValue) {
        this.maxIntegerLength = maxIntegerLength;
        this.maxDecimalLength = maxDecimalLength;
        this.minValue = minValue;
        this.maxValue = maxValue;
        setFieldDocument();
    }

    public void setFieldDocument() {
        setDocument(new NumberDocument());
        initListener();
    }

    public void canFillNegativeNumber(boolean fillNegativeNumber) {
        this.fillNegativeNumber = fillNegativeNumber;
    }

    public int getMaxIntegerLength() {
        return maxIntegerLength;
    }

    public void setMaxIntegerLength(int maxIntegerLength) {
        this.maxIntegerLength = maxIntegerLength;
    }

    public int getMaxDecimalLength() {
        return maxDecimalLength;
    }

    public void setMaxDecimalLength(int maxDecimalLength) {
        this.maxDecimalLength = maxDecimalLength;
    }

    public double getMinValue() {
        return minValue;
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    /**
     * Set the value.
     */
    public void setValue(double value) {
        this.setText(Utils.doubleToString(value));
    }

    /**
     * Return the value.
     */
    public double getValue() throws NumberFormatException {
        try {

            if (StringUtils.isEmpty(this.getText())) {
                return 0;
            }

            return Double.parseDouble(this.getText());
        } catch (NumberFormatException numberFormatException) {
            return UINumberField.ERROR_VALUE;
        }
    }

    /**
     * Retusn text value.
     */
    public String getTextValue() {
        return this.getText();
    }

    /**
     * Set property integer.
     *
     * @param integer New value of property integer.
     */
    public void setInteger(boolean integer) {
        if (integer) {
            this.maxDecimalLength = 0;
        } else {
            this.maxDecimalLength = DEFAULTMAXDECIMALLENTH;
        }
    }

    /**
     * Check whether the content changed.
     */
    public boolean isContentChanged() {
        return isContentChanged;
    }

    public void setisContentChanged(boolean isContentChanged) {
        this.isContentChanged = isContentChanged;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(super.getPreferredSize().width, 20);
    }

    class NumberDocument extends PlainDocument {

        public boolean checkString(int offset, String s, String str) {
            return (ComparatorUtils.equals(s, "F")
                    || ComparatorUtils.equals(s, "f")
                    || ComparatorUtils.equals(s, "D")
                    || ComparatorUtils.equals(s, "d")
                    || (ComparatorUtils.equals(str.trim(), "0") && !ComparatorUtils.equals(s.substring(0, 1), ".") && offset != 0)// 第一位是0时,第二位只能为小数点
                    || (ComparatorUtils.equals(s, ".") && maxDecimalLength == 0));

        }

        public void insertString(int offset, String s, AttributeSet a) throws BadLocationException {
            String str = getText(0, getLength());

            // 不能为f,F,d,D
            if (checkString(offset, s, str)) {
                Toolkit.getDefaultToolkit().beep();
                return;
            }

            String strNew = str.substring(0, offset) + s + str.substring(offset, getLength());
            if (notChange(strNew)) {
                return;
            }
            setisContentChanged(true);
            super.insertString(offset, s, a);
        }

        // kunsnat: 这种限制输入 有个不好的地方, 比如删除时: 10.1  最大值限定100, 那么就删除中间的小数点之后变为101, 超出了100.
        // 但是直接限制不能删除中间类似小数点, 那么也可能遇到: 最小值10 , 从100变化到其中的19, 就很难..
        private boolean notChange(String strNew) {
            if (!fillNegativeNumber && strNew.contains("-")) {
                return true;
            }
            boolean noChange = false;
            boolean isMinus = strNew.startsWith("-");
            strNew = strNew.replaceFirst("-", StringUtils.EMPTY); // 控制能输入负数
            String strIntPart;
            String strDecPart = StringUtils.EMPTY;
            int decPos = strNew.indexOf(CoreConstants.DOT);
            if (decPos > -1) {
                strIntPart = strNew.substring(0, decPos);
                strDecPart = strNew.substring(decPos + 1);
            } else {
                strIntPart = strNew;
            }
            if (isOverMaxOrMinValue(strIntPart, strDecPart, strNew)) {
                Toolkit.getDefaultToolkit().beep();
                noChange = true;
            }

            try {
                if (!ComparatorUtils.equals(strNew, StringUtils.EMPTY) && !ComparatorUtils.equals(strNew, "-")) {// 控制能输入负数
                    double d = Double.parseDouble(strNew) * (isMinus ? -1 : 1);
                    if (d < minValue || d > maxValue) {
                        throw new Exception();
                    }
                }
            } catch (Exception e) {
                Toolkit.getDefaultToolkit().beep();
                noChange = true;
            }

            return noChange;
        }

        private boolean isOverMaxOrMinValue(String strIntPart, String strDecPart, String strNew) {
            boolean checkLength = strIntPart.length() > maxIntegerLength
                    || strDecPart.length() > maxDecimalLength;
            return checkLength || (strNew.length() > 1 && ComparatorUtils.equals(strNew.substring(0, 1), "0") && !ComparatorUtils.equals(strNew.substring(1, 2), "."));
        }
    }

    /**
     * 测试程序
     */
    public static void main(String[] args) {
//        JFrame frame = new JFrame("");
//        frame.setSize(400, 320);
//        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
//        frame.setLocation((d.width - frame.getSize().width) / 2, (d.height - frame.getSize().height) / 2);
//        UINumberField tt = new UINumberField();
//        tt.setMinValue(0.0);
//        tt.setMaxValue(100.0);
//        frame.getContentPane().setLayout(new GridLayout(10, 2));
//        frame.getContentPane().add(new UILabel("New JNumberField()"));
//        frame.getContentPane().add(tt);
//        frame.getContentPane().add(new UILabel("New JNumberField(2)"));
//        frame.getContentPane().add(new UILabel("New JNumberField(8,2)"));
//        frame.getContentPane().add(new UINumberField(8, 2));
//        frame.getContentPane().add(new UILabel("New JNumberField(5,2,-10,100)"));
//        frame.getContentPane().add(new UINumberField(5, 2, TESTMINVALUE, TESTMAXVALUE));
//        frame.setVisible(true);
    }
}