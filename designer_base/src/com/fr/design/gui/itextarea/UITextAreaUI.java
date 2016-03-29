package com.fr.design.gui.itextarea;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicTextAreaUI;
import javax.swing.text.JTextComponent;

import com.fr.design.constants.UIConstants;
import com.fr.design.utils.gui.GUIPaintUtils;

public class UITextAreaUI extends BasicTextAreaUI {
	protected boolean isRollOver;
	private JComponent textField;

	public UITextAreaUI(final JComponent textField) {
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
		Graphics2D g2d = (Graphics2D)g;
		g2d.clearRect(0, 0, width, height);
		if(isRollOver && textField.isEnabled() && ((UITextArea)textField).isEditable()) {
			Shape shape = new RoundRectangle2D.Double(1, 1, width - 3, height - 3, UIConstants.ARC, UIConstants.ARC);
			GUIPaintUtils.paintBorderShadow(g2d, 3, shape, UIConstants.HOVER_BLUE, Color.white);
		} else {
			g2d.setColor(UIConstants.LINE_COLOR);
			g2d.drawRoundRect(1, 1, width - 2, height - 2, UIConstants.ARC, UIConstants.ARC);
		}
	}

}