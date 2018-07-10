package com.fr.design.menu;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.ActionEvent;

import com.fr.design.constants.UIConstants;
import com.fr.design.actions.UpdateAction;
import com.fr.design.gui.imenu.UIMenuItem;

/**
 * 虚线的Separator。
 * @author zhou
 * @since 2012-6-6下午12:34:46
 */
public class DottedSeparator extends UpdateAction {

	private static Color color = new Color(153, 153, 153);

	public DottedSeparator() {

	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}

	@Override
	public UIMenuItem createMenuItem() {
		Object object = this.getValue(UIMenuItem.class.getName());
		UIMenuItem UIMenuItem = null;
		if (object == null && !(object instanceof UIMenuItem)) {
			UIMenuItem = new MenuItem();
			this.putValue(UIMenuItem.class.getName(), UIMenuItem);
			object = UIMenuItem;
		}

		return (UIMenuItem)object;
	}

	private class MenuItem extends UIMenuItem {
		public MenuItem() {
			this.setUI(null);
			this.removeAll();
		}

		public void paint(Graphics g) {
			int w = this.getWidth();
			int h = this.getHeight();
			Graphics2D g2d = (Graphics2D)g;
			g2d.setColor(UIConstants.NORMAL_BACKGROUND);
			g2d.fillRect(0, 0, w, h);
			g2d.setColor(color);
			Stroke bs = new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 2f, new float[] { 3, 1 }, 0);
			g2d.setStroke(bs);
			g2d.drawLine(30, h / 2+1, w-4, h / 2+1);
			this.setForeground(color);
			super.paint(g);
		}

		public Dimension getSize() {
			return new Dimension(super.getSize().width, 8);
		}

		public Dimension getPreferredSize() {
			return new Dimension(super.getPreferredSize().width, 8);
		}
	}

}