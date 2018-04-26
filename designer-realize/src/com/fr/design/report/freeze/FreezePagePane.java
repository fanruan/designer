package com.fr.design.report.freeze;

import com.fr.design.gui.ilable.UILabel;
import javax.swing.SwingConstants;

import com.fr.general.Inter;
import com.fr.stable.FT;
import com.fr.stable.StableUtils;

public class FreezePagePane extends FreezeAndRepeatPane {
	private boolean isNumber;

	public FreezePagePane(boolean isNumber) {
		this.isNumber = isNumber;
		start = new UILabel(isNumber ? Inter.getLocText(new String[]{"Frozen", "N.O."})+" 1" : Inter.getLocText(new String[]{"Frozen", "N.O."})+" A", SwingConstants.CENTER);
		end = new UILabel(isNumber ? " 1"+Inter.getLocText("Row") : " A"+Inter.getLocText("Column"), SwingConstants.CENTER);
		super.initComponent();
	}

	@Override
	protected String title4PopupWindow() {
		return "FreezePage";
	}

	@Override
	public void populateBean(FT ob) {
		if (isNumber) {
			((UILabel) end).setText(String.valueOf(ob.getTo() + 1)+Inter.getLocText("Row"));
		} else {
			((UILabel) end).setText(StableUtils.convertIntToABC(ob.getTo() + 1)+Inter.getLocText("Column"));
		}
	}

	@Override
	public FT updateBean() {
//		if (isNumber)
//			return new FT(Integer.parseInt(((UILabel) start).getText()) - 1, Integer.parseInt(((UILabel) end).getText()) - 1);
//		else
//			return new FT(BaseCoreUtils.convertABCToInt(((UILabel) start).getText()) - 1, BaseCoreUtils.convertABCToInt(((UILabel) end).getText()) - 1);
		return null;
	}

	@Override
	public String getLabeshow() {
		return isNumber ? Inter.getLocText("RowTo") : Inter.getLocText("ColumnTo");
	}
}