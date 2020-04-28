package com.fr.design.actions.server;

import com.fr.config.ServerPreferenceConfig;
import com.fr.design.dialog.BasicPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.present.StyleArrayPane;


import java.awt.*;

public class StyleManagerPane extends BasicPane {
	private StyleArrayPane styleArrayPane;
	
	public StyleManagerPane(){
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		
		styleArrayPane = new StyleArrayPane();
		this.add(styleArrayPane, BorderLayout.CENTER);
	}
	
	@Override
	protected String title4PopupWindow() {
		return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_ServerM_Predefined_Styles");
	}
	
	public void populate(ServerPreferenceConfig configManager) {
		//todo 原来界面上显示的xml路径
//		this.StyleTextField.setText(WorkContext.getCurrent().getPath() + File.separator +
//				ProjectConstants.RESOURCES_NAME +
//				File.separator + configManager.fileName());
		this.styleArrayPane.populate(configManager);
	}
	
	public void update(ServerPreferenceConfig configManager) {
		this.styleArrayPane.update(configManager);
	}

	public boolean isNamePermitted() {
		return styleArrayPane.isNamePermitted();
	}
}
