package com.fr.design.menu;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;

import com.fr.design.constants.UIConstants;
import com.fr.design.gui.ilable.UILabel;

import com.fr.base.GraphHelper;
import com.fr.design.actions.UpdateAction;
import com.fr.design.gui.imenu.UIMenuItem;

/**
 * 
 * @author zhou
 * @since 2012-6-6下午12:02:58
 */
public class NameSeparator extends UpdateAction {
	private String text;

	public NameSeparator(String text) {
		this.text = text;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}

	@Override
	public UIMenuItem createMenuItem() {
		Object object = this.getValue(UIMenuItem.class.getName());
		UIMenuItem UIMenuItem = null;
		if (object == null && !(object instanceof UIMenuItem)) {
			UIMenuItem = new MenuItem(text);
			this.putValue(UIMenuItem.class.getName(), UIMenuItem);
			object = UIMenuItem;
		}

		return (UIMenuItem)object;
	}

	private class MenuItem extends UIMenuItem {
		public MenuItem(String text) {
			this.setUI(null);
			this.removeAll();
			this.setLayout(new BorderLayout());
			this.add(new CustomerLable(text, UILabel.LEFT), BorderLayout.CENTER);
		}

		public void paint(Graphics g) {
			int w = this.getWidth();
			int h = this.getHeight();
			Graphics2D g2d = (Graphics2D)g;
			g2d.setColor(UIConstants.NORMAL_BACKGROUND);
			g2d.fillRect(0, 0, w, h);
			this.setForeground(UIConstants.FONT_COLOR);
			super.paint(g);
		}

		public Dimension getSize() {
			return new Dimension(super.getSize().width, 20);
		}

		public Dimension getPreferredSize() {
			return new Dimension(super.getPreferredSize().width, 20);
		}
	}

	private class CustomerLable extends UILabel {

		public CustomerLable(String text, int left) {
			super(text, left);
		}

		public void paint(Graphics g) {
			int w = this.getWidth();
			int h = this.getHeight();
			Graphics2D g2d = (Graphics2D)g;
			g2d.setColor(UIConstants.FONT_COLOR);
			FontMetrics fm = GraphHelper.getFontMetrics(this.getFont());
			int strwidth = 0;
			String str = this.getText();
			for (int i = 0; i < str.length(); i++) {
				strwidth = strwidth + fm.charWidth(str.charAt(i));
			}
			g2d.drawLine(strwidth + 4, h / 2+2, w, h / 2+2);
			this.setForeground(UIConstants.FONT_COLOR);
			super.paint(g);
		}
	}

}