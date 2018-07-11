package com.fr.design.widget.ui.designer;

import com.fr.design.designer.creator.XCreator;
import com.fr.form.ui.NoneWidget;

/**
 * 
 * @author Administrator
 * 用于处理没有控件的情况
 */ 
public class NoneWidgetDefinePane extends AbstractDataModify<NoneWidget> {

	public NoneWidgetDefinePane(XCreator xCreator){
		super(xCreator);
	}

	@Override
	public String title4PopupWindow() {
		return "none";
	}
	
	@Override
	public void populateBean(NoneWidget w) {
	}

	@Override
	public NoneWidget updateBean() {
		return new NoneWidget();
	}
}