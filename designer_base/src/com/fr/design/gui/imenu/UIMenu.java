package com.fr.design.gui.imenu;

import com.fr.design.constants.UIConstants;
import com.fr.stable.StringUtils;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class UIMenu extends JMenu {
	public UIMenu(String name) {
		super(name);
		setName(name);
		setRolloverEnabled(true);
		setBackground(UIConstants.NORMAL_BACKGROUND);
	}

	@Override
	public String getText() {
		if (this.getParent() instanceof JPopupMenu) {
			return StringUtils.BLANK + super.getText();
		}
		return "  " + super.getText();
	}

	public JPopupMenu getPopupMenu() {
		JPopupMenu popupMenu = super.getPopupMenu();
		popupMenu.setBackground(UIConstants.NORMAL_BACKGROUND);
		popupMenu.setBorder(new Border() {

			@Override
			public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
				g.setColor(UIConstants.LINE_COLOR);
				g.drawRect(x, y, width - 1, height - 1);
				if (!(UIMenu.this.getParent() instanceof JPopupMenu)) {
					g.setColor(UIConstants.NORMAL_BACKGROUND);
					g.drawLine(1, 0, UIMenu.this.getWidth() - 2, 0);
				}
			}

			@Override
			public boolean isBorderOpaque() {
				return false;
			}

			@Override
			public Insets getBorderInsets(Component c) {
				return new Insets(5, 2, 10, 10);
			}
		});
		return popupMenu;
	}

	@Override
	public void updateUI() {
		setUI(new UIMenuUI());
	}
}