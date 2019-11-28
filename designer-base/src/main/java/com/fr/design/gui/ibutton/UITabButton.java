package com.fr.design.gui.ibutton;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Icon;

import com.fr.design.constants.UIConstants;
import com.fr.design.gui.ilable.UILabel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.fr.design.gui.core.UISelectedComponent;

/**
 * SelectedAble button label
 * 
 * @author zhou
 * @since 2012-5-11下午4:28:24
 */
public class UITabButton extends UILabel implements UISelectedComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final Color TOP = new Color(200, 200, 200);
	public static final Color DOWN = new Color(242, 242, 242);

	protected boolean isSelected = false;

	protected boolean isRollover;

	public UITabButton(Icon image) {
		super(image);
		this.setHorizontalAlignment(SwingConstants.CENTER);
		this.setVerticalAlignment(SwingConstants.CENTER);
		this.addMouseListener(getMouseListener());
		this.setFocusable(true);
	}

	public UITabButton(String text) {
		super(text);
		this.setHorizontalAlignment(SwingConstants.CENTER);
		this.setVerticalAlignment(SwingConstants.CENTER);
		this.addMouseListener(getMouseListener());
	}

	@Override
	public boolean isSelected() {
		return isSelected;
	}

	/**
	 * 能触发事件
	 * 
	 * @param isSelected
	 */

	@Override
	public void setSelected(boolean isSelected) {
		if (this.isSelected != isSelected) {
			this.isSelected = isSelected;
			fireSelectedChanged();
			this.repaint();
		}
	}

	/**
	 * 不触发事件
	 * 
	 * @param isSelected
	 */
	public void setSelectedDoNotFireListener(boolean isSelected) {
		if (this.isSelected != isSelected) {
			this.isSelected = isSelected;
			this.repaint();
		}
	}

	/**
	 * Adds a <code>ChangeListener</code> to the listener list.
	 */
	public void addChangeListener(ChangeListener l) {
		this.listenerList.add(ChangeListener.class, l);
	}

	/**
	 * removes a <code>ChangeListener</code> from the listener list.
	 */
	public void removeChangeListener(ChangeListener l) {
		this.listenerList.remove(ChangeListener.class, l);
	}

	// august: Process the listeners last to first
	protected void fireSelectedChanged() {
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ChangeListener.class) {
				((ChangeListener)listeners[i + 1]).stateChanged(new ChangeEvent(this));
			}
		}
	}

	/**
	 * 
	 * @return
	 */
	protected MouseListener getMouseListener() {
		return new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				if (!isSelected()) {
					isRollover = true;
					repaint();
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				isRollover = false;
				repaint();
			}

			@Override
			public void mousePressed(MouseEvent e) {
				UITabButton.this.requestFocus();
				mousePressedTrigger();
			}
		};
	}

	protected void mousePressedTrigger() {
		setSelected(true);
	}

	@Override
	public void paint(Graphics g) {
		int w = this.getWidth();
		int h = this.getHeight();
		Graphics2D g2d = (Graphics2D)g;
		paintRolloverAndSelected(g2d, w, h);
		paintOther(g2d, w, h);
		super.paint(g);
	}

	protected void paintRolloverAndSelected(Graphics2D g2d, int w, int h) {
		if (isSelected()) {
			Color darkColor = UIConstants.GRDIENT_DARK_GRAY;
			Color lightColor = UIConstants.PRESSED_DARK_GRAY;
			GradientPaint gp = new GradientPaint(1, 1, darkColor, 1, 5, lightColor);
			g2d.setPaint(gp);
			g2d.fillRect(0, 0, w - 1, h);
			this.setForeground(Color.WHITE);
		} else if (!isSelected() && isRollover) {
			Color blue = UIConstants.LIGHT_BLUE;
			g2d.setColor(blue);
			g2d.fillRect(0, 1, w - 1, h / 2);
			GradientPaint gp = new GradientPaint(1, 1, UIConstants.OCEAN_BLUE, 1, h - 1F, blue);
			g2d.setPaint(gp);
			g2d.fillRect(0, h / 2, w - 1, h / 2);
			this.setForeground(UIConstants.FONT_COLOR);
		} else if (!isSelected() && !isRollover) {
			GradientPaint gp = new GradientPaint(1, 1, TOP, 1, h - 1F, DOWN);
			g2d.setPaint(gp);
			g2d.fillRect(0, h / 2, w - 1, h / 2);
			this.setForeground(UIConstants.FONT_COLOR);
		}
	}

	protected void paintOther(Graphics2D g2d, int w, int h) {

	}

}
