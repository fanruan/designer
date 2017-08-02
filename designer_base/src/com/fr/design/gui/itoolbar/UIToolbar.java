package com.fr.design.gui.itoolbar;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class UIToolbar extends JToolBar {
    private static final int TOOLBAR_HEIGNT = 26;

    public UIToolbar() {
        this(FlowLayout.LEFT);
    }

    public UIToolbar(int align, UIToolBarUI uiToolBarUI) {
        super();
        setFloatable(false);
        setRollover(true);
        setLayout(new FlowLayout(align, 4, 0));
        setUI(uiToolBarUI);
        setBorderPainted(false);
    }

    public UIToolbar(int align) {
        this(align, new UIToolBarUI());
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension dim = super.getPreferredSize();
        dim.height = TOOLBAR_HEIGNT;
        return dim;
    }

    public void checkComponentsByNames(boolean flag, ArrayList<String> names) {
        for (int i = 0; i < getComponentCount(); i++) {
            Component component = getComponents()[i];
            if (names.contains(component.getName())) {
                component.setEnabled(flag);
            }
        }
    }
}