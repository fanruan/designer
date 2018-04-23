package com.fr.design.widget;

import java.awt.BorderLayout;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.form.ui.UserDefinedWidgetConfig;

public class UserDefinedWidgetConfigPane extends BasicBeanPane<UserDefinedWidgetConfig> {
	private ValueWidgetPane editorDefPane;
	
	public UserDefinedWidgetConfigPane(){
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		editorDefPane = new ValueWidgetPane();
		this.add(editorDefPane, BorderLayout.CENTER);
	}
	
	@Override
	protected String title4PopupWindow() {
		return "custom";
	}

    @Override
	public void populateBean(UserDefinedWidgetConfig obj){
		editorDefPane.populate(obj.toWidget());
	}

    @Override
	public UserDefinedWidgetConfig updateBean(){
		UserDefinedWidgetConfig udsc = new UserDefinedWidgetConfig();
		udsc.setWidget(editorDefPane.update());
		
		return udsc;
	}
}