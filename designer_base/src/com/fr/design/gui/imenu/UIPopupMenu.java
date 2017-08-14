package com.fr.design.gui.imenu;

import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.JPopupMenu;

import com.fr.design.constants.UIConstants;

public class UIPopupMenu extends JPopupMenu {
    private boolean onlyText = false;
    private boolean rePaint = false;

    public UIPopupMenu() {
        super();
        setBackground(UIConstants.NORMAL_BACKGROUND);
    }

    public UIPopupMenu(boolean rePaint) {
        super();
        this.rePaint = rePaint;
        setBackground(UIConstants.TOOLBARUI_BACKGROUND);
    }

    public boolean getRePaint() {
        return this.rePaint;
    }

    @Override
    protected void paintBorder(Graphics g) {
        if (rePaint) {
            g.setColor(UIConstants.POP_DIALOG_BORDER);
            g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        } else {
            g.setColor(UIConstants.LINE_COLOR);
            g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        }
    }

    @Override
    public Insets getInsets() {
        if (onlyText) {
            return super.getInsets();
        }
        if (rePaint) {
            return new Insets(0, 1, 1, 1);
        } else {
            return new Insets(10, 2, 10, 10);
        }
    }

    public void setOnlyText(boolean onlyText) {
        this.onlyText = onlyText;
    }
}