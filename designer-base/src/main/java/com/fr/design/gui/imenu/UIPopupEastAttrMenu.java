package com.fr.design.gui.imenu;

import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.JPopupMenu;

import com.fr.design.constants.UIConstants;

public class UIPopupEastAttrMenu extends JPopupMenu {

    public UIPopupEastAttrMenu() {
        super();
        setBackground(UIConstants.NORMAL_BACKGROUND);
    }

    @Override
    protected void paintBorder(Graphics g) {
        g.setColor(UIConstants.POP_DIALOG_BORDER);
        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
    }

    @Override
    public Insets getInsets() {
        return new Insets(0, 1, 1, 1);
    }

}