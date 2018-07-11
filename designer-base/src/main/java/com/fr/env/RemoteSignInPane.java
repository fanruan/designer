package com.fr.env;


import com.fr.design.gui.ilable.UILabel;

import com.fr.design.gui.ipasswordfield.UIPassWordField;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.dialog.BasicPane;
import com.fr.general.Inter;

public class RemoteSignInPane extends BasicPane {
	private UITextField userTextField;
	private UILabel userLabel;
	private UIPassWordField passwordField;
	private UILabel passLabel;
	
	public RemoteSignInPane(){
		this.initComponent();
	}
	
	public void initComponent(){
		this.setLayout(FRGUIPaneFactory.create2ColumnGridLayout());
		
		userLabel = new UILabel(Inter.getLocText("Username")+":");
		userTextField = new UITextField();
		passLabel = new UILabel(Inter.getLocText("Password")+":");
		passwordField = new UIPassWordField();
		this.add(userLabel);
		this.add(userTextField);
		this.add(passLabel);
		this.add(passwordField);
		
		
		
	}
	
	@Override
	protected String title4PopupWindow() {
		return "remote";
	}
	
	public void doClose() {
//    	this.showWindow(null).setVisible(false);
//    	this.showWindow(null).dispose();
    }
}