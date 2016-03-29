package com.fr.design.gui.itextfield;

import com.fr.design.gui.imenu.UIListPopup;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.ComparatorUtils;
import com.fr.stable.OperatingSystem;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.*;
import java.util.ArrayList;

public class UIAutoCompletionField extends UITextField implements DocumentListener, MouseListener, ListSelectionListener, ActionListener, KeyListener {


    private static final long serialVersionUID = 1L;
    private static int DEFAULT_PREFERRED_HEIGHT = 100;
    private static int PAGE_UP = -5;
    private static int PAGE_DOWN = 5;
    public boolean isOpen = false;
    private int preferredHeight = DEFAULT_PREFERRED_HEIGHT;
    private CompletionFilter filter;
    private UIListPopup popup;


    public void setFilter(CompletionFilter f) {
        filter = f;
    }

    public UIAutoCompletionField() {
        popup = new UIListPopup();
        // getDocument().addDocumentListener(this);
        addMouseListener(this);
        popup.addListSelectionListener(this);
        addActionListener(this);
        addKeyListener(this);
    }

    // 奶奶的 setText和释放UITextField内存时也会触发DocumentListener
    // 只能在setText后加上DocumentListener了

    /**
     *
     */
    public void addDocumentListener() {
        getDocument().addDocumentListener(this);
    }

    /**
     *
     */
    public void removeDocumentListener() {
        getDocument().removeDocumentListener(this);
    }

    /**
     * @param h
     */
    public void setPopupPreferredHeight(int h) {
        preferredHeight = h;
    }

    private boolean isListChange(ArrayList array) {
        if (array.size() != popup.getItemCount()) {
            return true;
        }
        for (int i = 0; i < array.size(); i++) {
            if (!ComparatorUtils.equals(array.get(i), popup.getItem(i))) {
                return true;
            }
        }
        return false;
    }

    private void textChanged() {
        if (OperatingSystem.isMacOS()) {
            return;
        }
        if (!popup.isVisible()) {
            showPopup();
            requestFocus();
        }
        if (filter != null) {
            ArrayList array = filter.filter(getText());
            changeList(array);
        }
    }

    private void showPopup() {
    	popup.setFocusable(false);
        popup.setPopupSize(getWidth(), preferredHeight);
        popup.show(this, 0, getHeight() - 1);
    }

    private void changeList(ArrayList array) {
        if (array.size() == 0) {
            if (popup.isVisible()) {
                popup.setVisible(false);
            }
        } else {
            if (!popup.isVisible()) {
//                showPopup();
            }
        }
        if (isListChange(array) && array.size() != 0) {
            popup.setList(array);
        }
    }

    /**
     * @param e
     */
    public void insertUpdate(DocumentEvent e) {
        textChanged();
    }

    /**
     * @param e
     */
    public void removeUpdate(DocumentEvent e) {
        textChanged();
    }

    /**
     * @param e
     */
    public void changedUpdate(DocumentEvent e) {
        textChanged();
    }

    /**
     * @param e
     */
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() > 1 && !popup.isVisible()) {
            textChanged();
        }
    }

    /**
     * @param e
     */
    public void mousePressed(MouseEvent e) {
    }

    /**
     * @param e
     */
    public void mouseReleased(MouseEvent e) {
    }

    /**
     * @param e
     */
    public void mouseEntered(MouseEvent e) {
    }

    /**
     * @param e
     */
    public void mouseExited(MouseEvent e) {
    }


    /**
     * @param e
     */
    public void valueChanged(ListSelectionEvent e) {
        JList list = (JList) e.getSource();
        String text = list.getSelectedValue().toString();
        setText(text);
        popup.setVisible(false);
    }

    /**
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
        if (popup.isVisible()) {
            Object o = popup.getSelectedValue();
            if (o != null) {
                setText(o.toString());
            }
            popup.setVisible(false);
        }
        this.selectAll();
        this.requestFocus();
    }

    /**
     * @param e
     */
    public void keyTyped(KeyEvent e) {
    }

    /**
     * @param e
     */
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            if (popup.isVisible()) {
                if (!popup.isSelected()) {
                    popup.setSelectedIndex(0);
                } else {
                    popup.setSelectedIndex(popup.getSelectedIndex() + 1);
                }

            }
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            if (popup.isVisible()) {
                if (!popup.isSelected()) {
                    popup.setLastOneSelected();
                } else {
                    popup.setSelectedIndex(popup.getSelectedIndex() - 1);
                }
            }
        } else if (e.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
            if (popup.isVisible()) {
                if (!popup.isSelected()) {
                    popup.setSelectedIndex(0);
                } else {
                    popup.setSelectedIndex(popup.getSelectedIndex() + PAGE_DOWN);
                }
            }
        } else if (e.getKeyCode() == KeyEvent.VK_PAGE_UP) {
            if (popup.isVisible()) {
                if (!popup.isSelected()) {
                    popup.setLastOneSelected();
                } else {
                    popup.setSelectedIndex(popup.getSelectedIndex() + PAGE_UP);
                }

            }
        }
    }

    /**
     * @param e
     */
    public void keyReleased(KeyEvent e) {
    }

    @Override
    /**
     *
     */
    public void setText(String t) {
        if (this.isOpen == true) {
            if (!UIAutoCompletionField.this.isShowing()) {
                return;
            }
        }
        try {
            Document doc = getDocument();
            if (doc instanceof AbstractDocument) {
                ((AbstractDocument) doc).replace(0, doc.getLength(), t, null);
                this.setSelectionStart(0);
                this.setSelectionEnd(this.getText().length());
            } else {
                doc.remove(0, doc.getLength());
                doc.insertString(0, t, null);
                this.setSelectionStart(0);
                this.setSelectionEnd(this.getText().length());
            }
        } catch (BadLocationException e) {
            UIManager.getLookAndFeel().provideErrorFeedback(UIAutoCompletionField.this);
        }

    }
    
}