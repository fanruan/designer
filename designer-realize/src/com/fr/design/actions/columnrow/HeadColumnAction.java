package com.fr.design.actions.columnrow;

import com.fr.general.Inter;
import com.fr.design.mainframe.ElementCasePane;

public class HeadColumnAction extends ColumnRowRepeatAction {

	public HeadColumnAction(ElementCasePane t) {
		super(t);
		
        this.setName(Inter.getLocText("Set_Column_Title_Start"));
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