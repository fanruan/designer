package com.fr.design.gui.itextfield;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicTextFieldUI;
import javax.swing.text.JTextComponent;

import com.fr.design.constants.UIConstants;
import com.fr.design.utils.gui.GUIPaintUtils;

public class UITextFieldUI extends BasicTextFieldUI {
	protected boolean isRollOver;
	private JComponent textField;
	
	private Color backgroundColor = Color.WHITE;

	public UITextFieldUI(final JComponent textField) {
		super();
		this.textField = textField;
		this.textField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				isRollOver = true;
				textField.repaint();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				isRollOver = false;
				textField.repaint();
			}
		});
	}

	@Override
	protected void paintBackground(Graphics g) {
		JTextComponent editor = getComponent();
		int width = editor.getWidth();
		int height = editor.getHeight();
		Shape roundShape = new RoundRectangle2D.Double(0, 0, width, height, 0, 0);
		Graphics2D g2d = (Graphics2D)g;
		g2d.clearRect(0, 0, width, height);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.clip(roundShape);
		g2d.setColor(backgroundColor);
		g2d.fillRoundRect(1, 1, width - 2, height - 2, 0, 0);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);
	}
	
	/**
	 * 在出现不允许的数字时, 变换背景.
	 */
	public void setBackgroundColor4NoGiveNumber(Color color) {
		this.backgroundColor = color;
	}

	public void paintBorder(Graphics2D g2d, int width, int height, boolean isRound, int rectDirection) {
		if(isRollOver && textField.isEnabled()) {
            g2d.setColor(UIConstants.TEXT_FILED_BORDER_SELECTED);
            g2d.drawRect(0, 0, width - 1, height - 1);
		} else {
			GUIPaintUtils.drawBorder(g2d, 0, 0, width, height, isRound, rectDirection);
		}
	}

}