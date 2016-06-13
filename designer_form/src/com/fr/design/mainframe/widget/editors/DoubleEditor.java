package com.fr.design.mainframe.widget.editors;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.NumberFormat;
import java.text.ParseException;

public class DoubleEditor extends FormattedEditor {

    private JFormattedTextField textField;

    public DoubleEditor() {
        super(NumberFormat.getNumberInstance());
        textField = (JFormattedTextField) super.getCustomEditor();
    }

    @Override
    public KeyListener createKeyListener() {
       return new KeyAdapter() {

           public void keyReleased(KeyEvent e) {
               try {
                   textField.commitEdit();
                   return;
               } catch (ParseException e1) {
                   return;
               }
           }
       };
    }

    @Override
    public Object getValue() {
        Object v = super.getValue();
        if (v == null) {
            return new Double(0);
        } else if (v instanceof Number) {
            return new Double(((Number) v).doubleValue());
        }
        return v;
    }
}