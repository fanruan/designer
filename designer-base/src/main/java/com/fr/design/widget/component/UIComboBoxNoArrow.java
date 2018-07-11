package com.fr.design.widget.component;

import com.fr.design.constants.UIConstants;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.icombobox.UIComboBoxEditor;
import com.fr.stable.StringUtils;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by kerry on 2017/9/14.
 */
public class UIComboBoxNoArrow extends UIComboBox {

    public UIComboBoxNoArrow(Object[] items) {
        super(items);
        init();
    }

    public UIComboBoxNoArrow() {

    }

    public void init() {
        this.setEditable(true);
        this.setEditor(new ComboBoxNoArrowEditor(this));
        this.setUI(new ComboBoxNoArrowUI());
    }

    public class ComboBoxNoArrowUI extends BasicComboBoxUI {
        @Override
        protected UIButton createArrowButton() {
            arrowButton = new UIButton(UIConstants.ARROW_DOWN_ICON) {
                @Override
                public boolean shouldResponseChangeListener() {
                    return false;
                }

                @Override
                public Insets getInsets() {
                    return new Insets(0, 0, 0, 0);
                }
            };
            squareButton = false;
            arrowButton.setPreferredSize(new Dimension(0, 0));
            arrowButton.setVisible(false);

            return (UIButton) arrowButton;
        }

    }


    public class ComboBoxNoArrowEditor extends UIComboBoxEditor implements DocumentListener {
        private volatile boolean setting = false;
        private UIComboBoxNoArrow comboBox;
        private Object item;

        public ComboBoxNoArrowEditor(UIComboBoxNoArrow comboBox) {
            super();
            this.comboBox = comboBox;
            textField.getDocument().addDocumentListener(this);
            textField.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    changeHandler();
                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });
        }

        public void setItem(Object item) {
            this.item = item;
            this.setting = true;
            textField.setText((item == null) ? StringUtils.EMPTY : item.toString());
            this.setting = false;
        }

        public Object getItem() {
            return this.item;
        }


        public void insertUpdate(DocumentEvent e) {
            changeHandler();
        }

        public void removeUpdate(DocumentEvent e) {
            changeHandler();
        }

        public void changedUpdate(DocumentEvent e) {
            changeHandler();
        }

        protected void changeHandler() {
            if (setting) {
                return;
            }
            comboBox.setPopupVisible(true);
            this.item = textField.getText();
            this.getEditorComponent().requestFocus();
        }
    }
}

