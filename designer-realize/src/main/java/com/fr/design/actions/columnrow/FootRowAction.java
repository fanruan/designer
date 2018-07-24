package com.fr.design.actions.columnrow;


import com.fr.design.mainframe.ElementCasePane;

public class FootRowAction extends ColumnRowRepeatAction {
	public FootRowAction(ElementCasePane t) {
		super(t);
		
		this.setName(com.fr.design.i18n.Toolkit.i18nText("Set_Row_Title_End"));
//		this.setMnemonic('F');
	}
	
	@Override
	protected boolean isColumn() {
		return false;
	}
	
	@Override
	protected boolean isFoot() {
		return true;
	}
}