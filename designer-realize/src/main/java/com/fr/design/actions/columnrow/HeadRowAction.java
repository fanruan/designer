package com.fr.design.actions.columnrow;


import com.fr.design.mainframe.ElementCasePane;

public class HeadRowAction extends ColumnRowRepeatAction {

	public HeadRowAction(ElementCasePane t) {
		super(t);
		
        this.setName(com.fr.design.i18n.Toolkit.i18nText("Set_Row_Title_Start"));
//		this.setMnemonic('H');
    }
	
	@Override
	protected boolean isColumn() {
		return false;
	}
	
	@Override
	protected boolean isFoot() {
		return false;
	}
}