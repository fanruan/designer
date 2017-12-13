package com.fr.design.gui.imenu;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;

import javax.swing.*;

import com.fr.design.constants.UIConstants;

public class UIPopupMenu extends JPopupMenu{
	private static final float REC = 8f;
	private boolean onlyText = false;
	public UIPopupMenu() {
		super();
		setBackground(UIConstants.NORMAL_BACKGROUND);
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		Shape shape = null;
		shape = new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), REC, REC);
		g2d.setClip(shape);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		super.paintComponent(g2d);
	}

	@Override
	protected void paintBorder(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		int rec = (int) REC;
		g2d.setColor(UIConstants.UIPOPUPMENU_LINE_COLOR);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, rec, rec);
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