/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.editor.editor;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;

/**
 * CellEditor used to edit String object.
 *
 * @editor zhou
 * @since 2012-3-29下午6:00:43
 */
public class TextEditor extends Editor<String> {

    private UITextField textField; // text field.
    // the old value of text field.
    private String oldValue = StringUtils.EMPTY;

    /**
     * Constructor.
     */
    public TextEditor() {
        this(null);
    }

    public TextEditor(String value) {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        textField = new UITextField();
        textField.setBorder(null);
        this.add(textField, BorderLayout.CENTER);
        this.textField.addKeyListener(textKeyListener);

        this.setValue(value);
        this.setName(Inter.getLocText("Parameter-String"));
    }

    public UITextField getTextField() {
        return this.textField;
    }

    /**
     * Returns the horizontal alignment of the CellEditor. Valid keys are:
     * <ul>
     * <li><code>UITextField.LEFT</code>
     * <li><code>UITextField.CENTER</code>
     * <li><code>UITextField.RIGHT</code>
     * <li><code>UITextField.LEADING</code>
     * <li><code>UITextField.TRAILING</code>
     * </ul>
     */
    public int getHorizontalAlignment() {
        return this.textField.getHorizontalAlignment();
    }

    /**
     * Sets the horizontal alignment of the CellEditor. Valid keys are:
     * <ul>
     * <li><code>UITextField.LEFT</code>
     * <li><code>UITextField.CENTER</code>
     * <li><code>UITextField.RIGHT</code>
     * <li><code>UITextField.LEADING</code>
     * <li><code>UITextField.TRAILING</code>
     * </ul>
     */
    public void setHorizontalAlignment(int horizontalAlignment) {
        this.textField.setHorizontalAlignment(horizontalAlignment);
    }

    /**
     * Return the value of the CellEditor.
     */
    @Override
    public String getValue() {
        return this.textField.getText();
    }

    /**
     * Set the value to the CellEditor.
     */
    @Override
    public void setValue(String value) {
        // populate data to UI
        if (value == null) {
            value = StringUtils.EMPTY;
        }

        oldValue = value.toString();
        this.textField.setText(oldValue);
    }

    /**
     * Sets whether or not this component is enabled.
     */
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        this.textField.setEnabled(enabled);
    }


    /**
     * 请求焦点
     */
    public void requestFocus() {
        this.textField.requestFocus();
    }

    KeyListener textKeyListener = new KeyAdapter() {

        @Override
        public void keyReleased(KeyEvent evt) {
            int code = evt.getKeyCode();
            if (code == KeyEvent.VK_ESCAPE) {
                textField.setText(oldValue);
            }
            if (code == KeyEvent.VK_ENTER) {
                fireEditingStopped();
            } else {
                fireStateChanged();
            }
        }
    };

    /**
     * 被选中时文本输入框请求焦点
     */
    public void selected() {
        this.textField.requestFocus();
    }

    public String getIconName() {
        return "type_string";
    }

    /**
     * 判断object是否是字符类型
     *
     * @param object 需要判断的object
     * @return 是字符类型则返回true
     */
    public boolean accept(Object object) {
        return object instanceof String;
    }
}