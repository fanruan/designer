package com.fr.design.gui.icombobox;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.IllegalComponentStateException;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.ComboBoxUI;
import javax.swing.plaf.basic.ComboPopup;

import com.fr.design.gui.ilist.CheckBoxList;
import com.fr.design.gui.ilist.CheckBoxList.CheckBoxListSelectionChangeListener;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.stable.ArrayUtils;


/**
 * richer:下拉复选框
 */
public class ComboCheckBox extends UIComboBox {

    private static final int VALUE120 = 120;

    private CheckBoxList checkBoxList;

    public ComboCheckBox() {
        this(new Object[0]);
    }

    public ComboCheckBox(Object[] data) {
        this.setData(data);
    }

    public void setData(Object[] data) {
        checkBoxList = new CheckBoxList(data);
        this.updateUI();
    }

    public Dimension getPreferredSize() {
        Dimension dim = super.getPreferredSize();
        dim.width = dim.width < VALUE120 ? VALUE120 : dim.width;
        return dim;
    }

    /**
     * 更新UI
     */
    public void updateUI() {
        ComboBoxUI cui = (ComboBoxUI) UIManager.getUI(this);
        cui = new ComboBoxCheckBoxUI();
        setUI(cui);
    }

    private class ComboBoxCheckBoxUI extends UIBasicComboBoxUI {
        protected ComboPopup createPopup() {
            return new CheckListPopup(comboBox);
        }
    }

    private class CheckListPopup extends JPopupMenu implements ComboPopup {
        protected ComboCheckBox comboBox;
        protected JScrollPane scrollPane;

        protected MouseListener checkListMouseListener;
        protected MouseMotionListener checkListMouseMotionListener;

        public CheckListPopup(JComboBox comboBox) {
            this.comboBox = (ComboCheckBox) comboBox;
            this.setBorder(BorderFactory.createLineBorder(Color.black));
            this.setLayout(FRGUIPaneFactory.createBorderLayout());
            this.setLightWeightPopupEnabled(comboBox.isLightWeightPopupEnabled());
            if (checkBoxList != null) {
                this.scrollPane = new JScrollPane(checkBoxList);
                this.scrollPane.setBorder(null);
                this.add(this.scrollPane, BorderLayout.CENTER);
                checkBoxList.addCheckBoxListSelectionChangeListener(new CheckBoxListSelectionChangeListener() {

                    @Override
                    public void selectionChanged(CheckBoxList target) {
                        Object[] values = checkBoxList.getSelectedValues();
                        CheckListPopup.this.comboBox.getModel().setSelectedItem(values);
                    }
                });
            }
        }

        private int getFittingLocaltion2Show() {
            Dimension size = this.getPreferredSize();
            Component com = comboBox.getParent();
            if (com != null) {
                if (comboBox.getLocation().y + comboBox.getHeight() + size.height > com.getSize().height && comboBox.getLocation().y - size.height > 0) {
                    return -size.height;
                }
            }

            return comboBox.getHeight();
        }

        public void show() {
            this.updatePopup();
            try {
                this.show(comboBox, 0, getFittingLocaltion2Show());
            } catch (IllegalComponentStateException e) {
                // richer:这里有可能会抛出一个异常，可以不用处理
            }
            checkBoxList.requestFocus();
        }

        public void hide() {
            this.setVisible(false);
        }

        protected void togglePopup() {
            if (this.isVisible()) {
                this.hide();
            } else {
                this.show();
            }
        }

        protected void updatePopup() {
            this.setPreferredSize(new Dimension(this.comboBox.getSize().width, checkBoxList.getPreferredSize().height > VALUE120 ? VALUE120 : checkBoxList.getPreferredSize().height + 2));
            Object obj = this.comboBox.getSelectedItem();
            if (obj instanceof Object[]) {
                ListModel model = this.comboBox.checkBoxList.getModel();
                for (int i = 0; i < model.getSize(); i++) {
                    if (ArrayUtils.indexOf((Object[]) obj, model.getElementAt(i)) != -1) {
                        this.comboBox.checkBoxList.setSelected(i, true);
                    }
                }
            }
        }

        public KeyListener getKeyListener() {
            return null;
        }

        public JList getList() {
            return new JList();
        }

        @Override
        public MouseListener getMouseListener() {
            if (checkListMouseListener == null) {
                checkListMouseListener = new InvocationMouseHandler();
            }
            return checkListMouseListener;
        }

        public MouseMotionListener getMouseMotionListener() {
            if (checkListMouseMotionListener == null) {
                checkListMouseMotionListener = new MouseMotionAdapter() {
                };
            }
            return checkListMouseMotionListener;
        }

        public void uninstallingUI() {
            // do nothing
        }

        protected class InvocationMouseHandler extends MouseAdapter {
            public void mousePressed(MouseEvent e) {
                if (!SwingUtilities.isLeftMouseButton(e) || !comboBox.isEnabled()) {
                    return;
                }
                if (comboBox.isEditable()) {
                    Component comp = comboBox.getEditor().getEditorComponent();
                    if ((!(comp instanceof JComponent)) || ((JComponent) comp).isRequestFocusEnabled()) {
                        comp.requestFocus();
                    }
                } else if (comboBox.isRequestFocusEnabled()) {
                    comboBox.requestFocus();
                }
                togglePopup();
            }
        }
    }
}