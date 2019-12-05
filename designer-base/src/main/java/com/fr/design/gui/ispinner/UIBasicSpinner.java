package com.fr.design.gui.ispinner;

import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.ParseException;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date: 13-3-27
 * Time: 下午2:17
 */
public class UIBasicSpinner extends JSpinner implements UIObserver {

    private UIObserverListener uiObserverListener;

    /**
     *
     * @param model
     */
    public UIBasicSpinner(SpinnerModel model) {
        super(model);
        iniListener();
    }

    /**
     *
     */
    public UIBasicSpinner() {
        super();
        iniListener();
    }


    private void iniListener() {
        if (shouldResponseChangeListener()) {
            ((DefaultEditor) this.getEditor()).getTextField().getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
					attributeChanged();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
					attributeChanged();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
					attributeChanged();
                }
            });
        }
        final JFormattedTextField textField = ((JSpinner.DefaultEditor) this.getEditor()).getTextField();
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                try {
                    textField.commitEdit();
                } catch (ParseException ignore) {

                }
            }
        });
    }


    private void attributeChanged() {
        if (uiObserverListener == null) {
            return;
        }
        uiObserverListener.doChange();

    }

    @Override
    /**
     *
     */
    public void registerChangeListener(UIObserverListener listener) {
        uiObserverListener = listener;
    }

    @Override
    /**
     *
     */
    public boolean shouldResponseChangeListener() {
        return true;
    }
}