package com.fr.design.mainframe.widget.editors;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.Format;
import java.text.ParseException;

import javax.swing.JFormattedTextField;
import javax.swing.JPanel;

import com.fr.design.Exception.ValidationException;
import com.fr.design.layout.FRGUIPaneFactory;


/**
 * @author richer
 * @since 6.5.3
 * 编辑具有特定格式的值
 */
public class FormattedEditor extends AbstractPropertyEditor {

    private JPanel panel;
    private JFormattedTextField textField;
    private Format format;

    /** Creates a new instance of TextEditor */
    public FormattedEditor(Format format) {
        this.format = format;
        panel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        textField = new JFormattedTextField(format);
        panel.add(textField, BorderLayout.CENTER);
        textField.setBorder(null);
        textField.addKeyListener(createKeyListener());
    }

    protected KeyListener createKeyListener() {
        return new KeyAdapter() {

            public void keyReleased(KeyEvent e) {
                try {
                    textField.commitEdit();
                } catch (ParseException e1) {
                    return;
                }
                firePropertyChanged();
            }
        };
    }

    @Override
    public void setValue(Object value) {
        textField.setValue(value);
    }

    @Override
    public Object getValue() {
        return textField.getValue();
    }

    @Override
    public Component getCustomEditor() {
        return textField;
    }

    @Override
    public void validateValue() throws ValidationException {
        try {
            format.parseObject(textField.getText());
        } catch (ParseException ex) {
            throw new ValidationException(com.fr.design.i18n.Toolkit.i18nText("Format-Error") + "!");
        }
    }
}