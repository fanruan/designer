package com.fr.design.gui.icombobox;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicComboBoxUI;

import com.fr.design.constants.UIConstants;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.stable.Constants;

/**
 * 
 * @author zhou
 * @since 2012-5-9下午4:33:07
 */
public class TreeComboBoxUI extends BasicComboBoxUI implements MouseListener {

	private boolean isRollover = false;

	public TreeComboBoxUI() {
		super();
	}

	@Override
	protected UIButton createArrowButton() {
		arrowButton = new UIButton(UIConstants.ARROW_DOWN_ICON);
		((UIButton) arrowButton).setRoundBorder(true, Constants.LEFT);
		arrowButton.addMouseListener(this);
		comboBox.addMouseListener(this);
		return (UIButton) arrowButton;
	}

	public void paint(Graphics g, JComponent c) {
		super.paint(g, c);
		Graphics2D g2d = (Graphics2D)g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Color linecolor = null;
		if (comboBox.isPopupVisible()) {
			linecolor = UIConstants.LINE_COLOR;
			arrowButton.setSelected(true);
		} else if (isRollover) {
			linecolor = UIConstants.LIGHT_BLUE;
		} else {
			linecolor = UIConstants.LINE_COLOR;
			arrowButton.setSelected(false);
		}
		g2d.setColor(linecolor);
		if (!comboBox.isPopupVisible()) {
			g2d.drawRoundRect(0, 0, c.getWidth() - arrowButton.getWidth() + 3, c.getHeight() - 1, UIConstants.LARGEARC, UIConstants.LARGEARC);
		} else {
			g2d.drawRoundRect(0, 0, c.getWidth() , c.getHeight() + 3, UIConstants.LARGEARC, UIConstants.LARGEARC	);
			g2d.drawLine(0, c.getHeight()-1, c.getWidth(), c.getHeight()-1);
		}
	}
	


	private void setRollover(boolean isRollover) {
		if (this.isRollover != isRollover) {
			this.isRollover = isRollover;
			comboBox.repaint();
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		setRollover(true);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		setRollover(false);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

}