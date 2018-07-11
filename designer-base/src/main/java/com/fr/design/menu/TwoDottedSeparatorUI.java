package com.fr.design.menu;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;
import javax.swing.JSeparator;
import javax.swing.LookAndFeel;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSeparatorUI;


public class TwoDottedSeparatorUI extends BasicSeparatorUI {
	protected static final Dimension VERT_DIMENSION = new Dimension(0, 2);
	protected static final Dimension HORZ_DIMENSION = new Dimension(2, 0);

    /**
     * 创建组件UI
     * @param c 组件
     * @return 组件UI
     */
	public static ComponentUI createUI(JComponent c) {
		return new UISeparatorUI();
	}

	protected void installDefaults(JSeparator s) {
		LookAndFeel.installColors(s, "Separator.background", "Separator.foreground");
	}

	public void paint(Graphics g, JComponent c) {
		drawXpSeparator(g, c);
	}

	protected void drawXpSeparator(Graphics g, JComponent c) {
		Dimension s = c.getSize();
		g.setColor(c.getBackground());
		Graphics2D g2d = (Graphics2D)g;
		BasicStroke dotted = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{1, 1, 1, 1}, 0.0f);
		g2d.setStroke(dotted);
		
		if(((JSeparator)c).getOrientation() == JSeparator.VERTICAL) {
			g2d.drawLine(0, 0, 0, s.height);
			g2d.drawLine(3, 0, 3, s.height);
		}
		else { // HORIZONTAL
			g2d.drawLine(0, 0, s.width, 0);
			g2d.drawLine(0, 3, s.width, 3);
		}
	}

	public Dimension getPreferredSize(JComponent c) {
		if(((JSeparator)c).getOrientation() == JSeparator.VERTICAL) {
			return HORZ_DIMENSION;
		}			
		else {
			return VERT_DIMENSION;
		}			
	}
	
	public Dimension getMinimumSize(JComponent c) {
		return getPreferredSize(c);
	}
}