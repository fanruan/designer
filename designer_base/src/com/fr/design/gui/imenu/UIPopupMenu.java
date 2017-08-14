package com.fr.design.gui.imenu;

import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.JPopupMenu;

import com.fr.design.constants.UIConstants;

public class UIPopupMenu extends JPopupMenu{
	private boolean onlyText = false;
	public UIPopupMenu() {
		super();
		setBackground(UIConstants.NORMAL_BACKGROUND);
	}

	@Override
	protected void paintBorder(Graphics g) {
		g.setColor(UIConstants.LINE_COLOR);
		g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
	}

	@Override
	public Insets getInsets() {
		if(onlyText) {
			return super.getInsets();
		}
		return new Insets(10, 2, 10, 10);
	}

	public void setOnlyText(boolean onlyText) {
		this.onlyText = onlyText;
	}
}