package com.fr.design.report;

import com.fr.config.ServerPreferenceConfig;
import com.fr.design.dialog.FineJOptionPane;
import com.fr.design.gui.imenu.UIMenuItem;
import com.fr.design.style.StylePane;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.ComparatorUtils;


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
				UIMenuItem menuItem = new UIMenuItem(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Save_As_Global_Style"));
				popupMenu.add(menuItem);
				menuItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String name = FineJOptionPane.showInputDialog(getParent(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Input_The_Name_Of_Gloabel_Style"));
						if (ComparatorUtils.equals(name, "")) {
							return;
						}
						if (ServerPreferenceConfig.getInstance().getStyle(name) == null) {
							ServerPreferenceConfig.getInstance().putStyle(name, ReportStylePane.this.updateBean());
						} else {
							FineJOptionPane.showMessageDialog(getParent(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_This_Name_Has_Exsit") + "!", com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Warning"), JOptionPane.WARNING_MESSAGE);
						}
					}
				});

				GUICoreUtils.showPopupMenu(popupMenu, ReportStylePane.this, evt.getX() - 1, evt.getY() + Y_OFFSET);
			}
		});

	}
}
