package com.fr.design.gui.icombobox;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JComboBox;
import javax.swing.plaf.ComboBoxUI;
import javax.swing.plaf.basic.ComboPopup;


/*
 * 下拉框高度固定
 */
public class FixedHeightComboBox<T> extends FilterComboBox<T> {
    private static final long serialVersionUID = 1L;
    private static final int VALUE120 = 120;

    public FixedHeightComboBox() {
        super();
    }

    public void setUI(ComboBoxUI ui) {
        ui = new UI();
        super.setUI(ui);
    }

    /**
     * 更新UI
     */
    public void updateUI() {
        setUI(new UI());
    }

    @SuppressWarnings("unchecked")
    @Override
    public T getSelectedItem() {
        return (T) dataModel.getSelectedItem();
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension dim = super.getPreferredSize();
        dim.width = VALUE120;
        return dim;
    }

    class UI extends UIBasicComboBoxUI {
        protected ComboPopup createPopup() {
            Popup popup = new Popup(comboBox);
            popup.getAccessibleContext().setAccessibleParent(comboBox);
            return popup;
        }

        public javax.swing.plaf.basic.ComboPopup getPopup() {
            return popup;
        }
    }

    class Popup extends ExtendedComboPopup {
        private static final long serialVersionUID = 1L;

        public Popup(JComboBox combo) {
            super(combo);
        }

        public void show() {
            setListSelection(comboBox.getSelectedIndex());
            Point location = getPopupLocation();
            show(comboBox, location.x, location.y);
        }

        private void setListSelection(int selectedIndex) {
            if (selectedIndex == -1) {
                list.clearSelection();
            } else {
                list.setSelectedIndex(selectedIndex);
                list.ensureIndexIsVisible(selectedIndex);
            }
        }

        private Point getPopupLocation() {
            Dimension popupSize = comboBox.getSize();

            // reduce the width of the scrollpane by the insets so that
            // the popup
            // is the same width as the combo box.
            popupSize.setSize(popupSize.width, 120);
            Rectangle popupBounds = computePopupBounds(0, comboBox.getBounds().height, popupSize.width, popupSize.height);
            Dimension scrollSize = popupBounds.getSize();
            Point popupLocation = popupBounds.getLocation();

            scroller.setMaximumSize(scrollSize);
            scroller.setPreferredSize(scrollSize);
            scroller.setMinimumSize(scrollSize);

            list.revalidate();

            return popupLocation;
        }
    }

}