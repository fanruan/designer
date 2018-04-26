package com.fr.design.actions.columnrow;

import com.fr.general.Inter;
import com.fr.design.mainframe.ElementCasePane;

public class FootRowAction extends ColumnRowRepeatAction {
	public FootRowAction(ElementCasePane t) {
		super(t);
		
		this.setName(Inter.getLocText("Set_Row_Title_End"));
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