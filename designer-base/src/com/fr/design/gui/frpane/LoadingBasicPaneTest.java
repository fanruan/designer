/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.gui.frpane;

import javax.swing.JPanel;

import com.fr.base.FRContext;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.dialog.BasicDialog;
import com.fr.general.Inter;

/**
 * @author richer
 * @since 6.5.5 创建于2011-6-16
 */
public class LoadingBasicPaneTest {

	public static void main(String[] args) {
		LoadingBasicPane lb = new LoadingBasicPane() {
			protected void initComponents(JPanel container) {
				for (int i = 0; i < 10; i++) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						FRContext.getLogger().error(e.getMessage(), e);
					}
					container.add(new UIButton(i + "adfadwdadawdwad"));
				}
			}
			
			@Override
			protected String title4PopupWindow() {
				return Inter.getLocText("Test") ;
			}
		};
		BasicDialog dlg = lb.showWindow(null);
		dlg.setVisible(true);
	}

}