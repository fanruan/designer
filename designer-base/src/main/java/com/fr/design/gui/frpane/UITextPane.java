package com.fr.design.gui.frpane;

import com.fr.common.inputevent.InputEventBaseOnOS;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.StyledDocument;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date: 13-3-27
 * Time: 上午10:50
 */
public class UITextPane extends JTextPane implements UIObserver {

    private UIObserverListener uiObserverListener;

    public UITextPane() {
        super();
        InputEventBaseOnOS.addBasicEditInputMap(this);
        iniListener();
    }

    public UITextPane(StyledDocument doc) {
        super(doc);
        InputEventBaseOnOS.addBasicEditInputMap(this);
        iniListener();
    }

    private void iniListener() {
        if (shouldResponseChangeListener()) {
            this.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    EventQueue.invokeLater(new Runnable() {
                        public void run() {
                            attributeChanged();
                        }
                    });
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    EventQueue.invokeLater(new Runnable() {
                        public void run() {
                            attributeChanged();
                        }
                    });
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    EventQueue.invokeLater(new Runnable() {
                        public void run() {
                            attributeChanged();
                        }
                    });
                }
            });
        }
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