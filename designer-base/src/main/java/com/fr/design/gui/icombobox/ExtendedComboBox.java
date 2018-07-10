package com.fr.design.gui.icombobox;

import com.fr.common.inputevent.InputEventBaseOnOS;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;

import static com.fr.design.gui.syntax.ui.rtextarea.RTADefaultInputMap.DEFAULT_MODIFIER;


public class ExtendedComboBox extends UIComboBox {
    private static final int VALUE120 = 120;

    public ExtendedComboBox() {
    }

    public ExtendedComboBox(ComboBoxModel aModel) {
        super(aModel);
    }

    public ExtendedComboBox(Object[] items) {
        super(items);
    }

    public ExtendedComboBox(Vector<?> items) {
        super(items);
    }

    /**
     * 更新UI
     */
    public void updateUI() {
        setUI(new ExtendedComboBoxUI());
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension dim = super.getPreferredSize();
        dim.width = VALUE120;
        return dim;
    }

    static class ExtendedComboBoxUI extends UIBasicComboBoxUI {
        public static ComponentUI createUI(JComponent c) {
            return new ExtendedComboBoxUI();
        }

        protected ComboPopup createPopup() {
            ExtendedComboPopup popup = new ExtendedComboPopup(comboBox);
            popup.getAccessibleContext().setAccessibleParent(comboBox);
            return popup;
        }

        public ComboPopup getPopup() {
            return popup;
        }
    }

    static class ExtendedComboPopup extends BasicComboPopup {

        public ExtendedComboPopup(JComboBox combo) {
            super(combo);
        }
        protected JList createList() {
            return new JList(comboBox.getModel()) {
                public void processMouseEvent(MouseEvent e) {
                    if (InputEventBaseOnOS.isControlDown(e)) {
                        //   Fix   for   4234053.   Filter   out   the   Control
                        //   Key   from   the   list.
                        //   ie.,   don't   allow   CTRL   key   deselection.
                        e = new MouseEvent((Component) e.getSource(), e.getID(), e.getWhen(), e.getModifiers()
                                ^ DEFAULT_MODIFIER, e.getX(), e.getY(), e.getClickCount(), e.isPopupTrigger());
                    }
                    super.processMouseEvent(e);
                }

                public String getToolTipText(MouseEvent event) {
                    int index = locationToIndex(event.getPoint());
                    if (index != -1) {
                        Object value = getModel().getElementAt(index);
                        ListCellRenderer renderer = getCellRenderer();
                        Component rendererComp = renderer.getListCellRendererComponent(this, value, index, true, false);
                        if (rendererComp.getPreferredSize().width > getVisibleRect().width) {
                            return value == null ? null : value.toString();
                        } else {
                            return null;
                        }
                    }
                    return null;
                }

                public Point getToolTipLocation(MouseEvent event) {
                    int index = locationToIndex(event.getPoint());
                    if (index != -1) {
                        Rectangle cellBounds = getCellBounds(index, index);
                        return new Point(cellBounds.x, cellBounds.y);
                    }
                    return null;
                }
            };
        }
    }

}