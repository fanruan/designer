package com.fr.design.report.freeze;

import com.fr.general.Inter;
import com.fr.stable.FT;
import com.fr.stable.StringUtils;

public class RepeatColPane extends FreezeAndRepeatPane {

	public RepeatColPane() {
		start = new ColSpinner(1,Integer.MAX_VALUE,1,1);
        end = new ColSpinner(1,Integer.MAX_VALUE,1,1);
		super.initComponent();
	}



	@Override
	protected String title4PopupWindow() {
		return "repeatcolumn";
	}

	@Override
	public void populateBean(FT ob) {
		((ColSpinner)start).setValue((ob.getFrom() + 1));
		((ColSpinner)end).setValue((ob.getTo() + 1));
	}

	@Override
	public FT updateBean() {
		return new FT((int)((ColSpinner) start).getValue()-1 , (int)((ColSpinner)end).getValue()-1 );
	}

	@Override
	public String getLabeshow() {
		return StringUtils.BLANK + Inter.getLocText("ColumnTo") + StringUtils.BLANK;
	}

}