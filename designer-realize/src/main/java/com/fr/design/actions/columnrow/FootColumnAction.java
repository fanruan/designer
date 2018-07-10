package com.fr.design.actions.columnrow;

import com.fr.general.Inter;
import com.fr.design.mainframe.ElementCasePane;

public class FootColumnAction extends ColumnRowRepeatAction {

	public FootColumnAction(ElementCasePane t) {
		super(t);
		
        this.setName(Inter.getLocText("Set_Column_Title_End"));
//		this.setMnemonic('F');
    }
	
	@Override
	protected boolean isColumn() {
		return true;
	}
	
	@Override
	protected boolean isFoot() {
		return true;
	}
}