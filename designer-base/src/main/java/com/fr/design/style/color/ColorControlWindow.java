package com.fr.design.style.color;

import com.fr.design.gui.ipoppane.PopupHider;
import com.fr.design.layout.FRGUIPaneFactory;

import javax.swing.JPopupMenu;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.awt.Color;

public abstract class ColorControlWindow extends JPopupMenu {
    private static final long serialVersionUID = 4317136753151221742L;
    private PopupHider popupHider;
    private ColorSelectionPopupPane selectionPopupPane;

    protected abstract void colorChanged();

    /**
     * Constructor.
     */
    public ColorControlWindow(PopupHider popupHider) {
        this(false, popupHider);
    }

    /**
     * Constructor.
     */
    public ColorControlWindow(boolean isSupportTransparent, PopupHider popupHider) {
        this.initComponents(isSupportTransparent);
        this.popupHider = popupHider;
    }

    public Color getColor() {
        if (selectionPopupPane == null) {
            return null;
        }
        return selectionPopupPane.getColor();
    }

    /**
     * Init components.
     */
    private void initComponents(boolean isSupportTransparent) {
        setLightWeightPopupEnabled(JPopupMenu.getDefaultLightWeightPopupEnabled());

        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        setBorderPainted(false);
        setOpaque(false);
        setDoubleBuffered(true);
        setFocusable(false);
        selectionPopupPane = new ColorSelectionPopupPane(isSupportTransparent);
        this.add(selectionPopupPane, BorderLayout.CENTER);
        this.pack();
    }

    class ColorSelectionPopupPane extends NewColorSelectPane {
        private static final long serialVersionUID = 7822856562329146354L;

        public ColorSelectionPopupPane(boolean isSupportTransparent) {
            super(isSupportTransparent);
            this.addChangeListener(new ChangeListener() {

                @Override
                public void stateChanged(ChangeEvent e) {
                    colorChanged();
                }
            });
        }

        @Override
        protected void doTransparent() {
            popupHider.hidePopupMenu();
            super.doTransparent();
        }

        @Override
        public void customButtonPressed() {
            popupHider.hidePopupMenu();
            super.customButtonPressed();
        }

    }

}