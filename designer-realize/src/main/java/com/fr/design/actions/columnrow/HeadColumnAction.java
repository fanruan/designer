package com.fr.design.actions.columnrow;


import com.fr.design.mainframe.ElementCasePane;

public class HeadColumnAction extends ColumnRowRepeatAction {

	public HeadColumnAction(ElementCasePane t) {
		super(t);
		
        this.setName(com.fr.design.i18n.Toolkit.i18nText("Set_Column_Title_Start"));
    }
	
	@Override
	protected boolean isColumn() {
		return true;
	}
	
	@Override
	protected boolean isFoot() {
		return false;
	}
}