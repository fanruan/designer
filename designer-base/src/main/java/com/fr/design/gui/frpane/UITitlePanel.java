/**
 * 
 */
package com.fr.design.gui.frpane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import com.fr.design.gui.ilable.UILabel;
import javax.swing.JPanel;

import com.fr.design.layout.FRGUIPaneFactory;

/**
 * @author zhou
 * 
 */
public class UITitlePanel extends JPanel {
	public UITitlePanel(JPanel contentPanel) {
		this.setLayout(new BorderLayout());
		this.setBorder(null);
		this.add(contentPanel, BorderLayout.CENTER);
	}

	public UITitlePanel(JPanel contentPanel, String title) {
		JPanel TitleBar = FRGUIPaneFactory.createBorderLayout_S_Pane();
		TitleBar.setBackground(new Color(148, 148, 148));
		TitleBar.setForeground(Color.WHITE);
		TitleBar.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, new Color(85, 85, 85)));

		UILabel titlelabel = new UILabel("  " + title);
		titlelabel.setBackground(new Color(148, 148, 148));
		titlelabel.setForeground(new Color(242, 242, 242));
		TitleBar.setPreferredSize(new Dimension(getWidth(), 20));
		TitleBar.setRequestFocusEnabled(true);
		TitleBar.add(titlelabel, BorderLayout.WEST);
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createEmptyBorder());
		this.add(TitleBar, BorderLayout.NORTH);
		this.add(contentPanel, BorderLayout.CENTER);

	}

}