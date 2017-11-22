package com.fr.design.gui.itextarea;

import com.fr.common.inputevent.InputEventBaseOnOS;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.stable.Constants;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class UITextArea extends JTextArea implements UIObserver {
    private UIObserverListener uiObserverListener;

    public UITextArea(int i, int j) {
        super(i, j);
        InputEventBaseOnOS.addBasicEditInputMap(this);
        initComponents();
    }

    @Override
    public Insets getInsets() {
        return new Insets(5, 5, 5, 5);
    }

    public UITextArea() {
        super();
        InputEventBaseOnOS.addBasicEditInputMap(this);
        initComponents();
    }

    public UITextArea(String s) {
        super(s);
        InputEventBaseOnOS.addBasicEditInputMap(this);
        initComponents();
    }

    private void initComponents() {
        setLineWrap(true);
        setWrapStyleWord(true);
        initListener();
    }

    private void initListener() {
        if (shouldResponseChangeListener()) {
            getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    EventQueue.invokeLater(new Runnable() {
                        public void run() {
                            attributeChange();
                        }
                    });
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    EventQueue.invokeLater(new Runnable() {
                        public void run() {
                            attributeChange();
                        }
                    });
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    EventQueue.invokeLater(new Runnable() {
                        public void run() {
                            attributeChange();
                        }
                    });
                }
            });
        }
    }

    private void attributeChange() {
        if (uiObserverListener != null) {
            uiObserverListener.doChange();
        }
    }

    @Override
    /**
     *
     */
    public UITextAreaUI getUI() {
        return (UITextAreaUI) ui;
    }

    @Override
    /**
     *
     */
    public void updateUI() {
        this.setUI(new UITextAreaUI(this));
    }

    @Override
    protected void paintBorder(Graphics g) {
        if (getBorder() != null) {
            getUI().paintBorder((Graphics2D) g, getWidth(), getHeight(), true, Constants.NULL);
        }
    }

    @Override
    /**
     *
     */
    public void registerChangeListener(UIObserverListener listener) {
        uiObserverListener = listener;
    }

    /**
     * @return
     */
    public boolean shouldResponseChangeListener() {
        return true;
    }

    /**
     * @param args
     */
    public static void main(String... args) {
//        JFrame jf = new JFrame("test");
//        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        JPanel content = (JPanel) jf.getContentPane();
//        content.setLayout(new BorderLayout());
//        UITextArea bb = new UITextArea("123455weoijweio reiwj kewl jfejkfljds kl jfldk jfk jdskfjkdsfklj dkl jfsdjf");
//        content.add(bb, BorderLayout.CENTER);
//        GUICoreUtils.centerWindow(jf);
//        jf.setSize(400, 400);
//        jf.setVisible(true);
    }
}