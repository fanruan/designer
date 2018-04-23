package com.fr.design.gui.imenu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import com.fr.design.constants.UIConstants;

public class UIMenuHighLight extends JPanel{
	
	@Override
	public Dimension getPreferredSize() {
		Dimension dim = super.getPreferredSize();
		dim.height = 0;
		return dim;
	}
	public void paint(Graphics g) {
		g.setColor(UIConstants.LINE_COLOR);
		g.drawLine(0, 0, getWidth(), 0);
		g.setColor(Color.WHITE);
		g.drawLine(0, 1, getWidth(), 1);
	};
}