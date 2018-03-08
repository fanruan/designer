package com.fr.design.report;

import com.fr.config.ServerPreferenceConfig;
import com.fr.design.gui.imenu.UIMenuItem;
import com.fr.design.style.StylePane;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ReportStylePane extends StylePane {

    private static final int Y_OFFSET = 8;

	public ReportStylePane() {
		super();
		getPreviewArea().addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent evt) {
				if (!SwingUtilities.isRightMouseButton(evt)) {
					return;
				}

				JPopupMenu popupMenu = new JPopupMenu();
				UIMenuItem menuItem = new UIMenuItem(Inter.getLocText("FR-Designer_Save_As_Global_Style"));
				popupMenu.add(menuItem);
				menuItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String name = JOptionPane.showInputDialog(getParent(), Inter.getLocText("FR-Designer_Input_The_Name_Of_Gloabel_Style"));
						if (ComparatorUtils.equals(name, "")) {
							return;
						}
						if (ServerPreferenceConfig.getInstance().getStyle(name) == null) {
							ServerPreferenceConfig.getInstance().putStyle(name, ReportStylePane.this.updateBean());
						} else {
							JOptionPane.showMessageDialog(getParent(), Inter.getLocText("FR-Designer_This_Name_Has_Exsit") + "!", Inter.getLocText("FR-Designer_Warning"), JOptionPane.WARNING_MESSAGE);
						}
					}
				});

				GUICoreUtils.showPopupMenu(popupMenu, ReportStylePane.this, evt.getX() - 1, evt.getY() + Y_OFFSET);
			}
		});

	}
}