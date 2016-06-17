package com.fr.design.mainframe.widget.editors;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.ParseException;

public class RefinedDoubleEditor extends DoubleEditor {

	private JFormattedTextField textField;

	public RefinedDoubleEditor() {
        super();
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
               }
           }
       };
    }
}