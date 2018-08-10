package com.fr.design.report.freeze;

import com.fr.design.gui.ilable.UILabel;
import javax.swing.SwingConstants;

import com.fr.stable.FT;
import com.fr.stable.StableUtils;

public class FreezeWriteRowPane extends FreezeAndRepeatPane {

	public FreezeWriteRowPane() {
//		start = new UILabel(com.fr.design.i18n.Toolkit.i18nTextArray(new String[]{"Frozen", "N.O."}) + " 1", SwingConstants.CENTER);
		start = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Frozen_N.O.") + " 1", SwingConstants.CENTER);
		end =  new RowSpinner(1, Integer.MAX_VALUE, 1,1);
		super.initComponent();
		this.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Row")));
	}

	@Override
	protected String title4PopupWindow() {
		return "FreezeRow";
	}

	@Override
	public void populateBean(FT ob) {
//		((UILabel)start).setText(com.fr.design.i18n.Toolkit.i18nTextArray(new String[]{"Frozen", "N.O."}) + String.valueOf(ob.getFrom()));
		((UILabel)start).setText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Frozen_N.O.") + String.valueOf(ob.getFrom()));
		((RowSpinner)end).setValue(ob.getTo() + 1);
	}

	@Override
	public FT updateBean() {
        return new FT(StableUtils.convertABCToInt(((UILabel) start).getText()) - 1, (int)(((RowSpinner) end).getValue() - 1));
	}

	@Override
	public String getLabeshow() {
		return com.fr.design.i18n.Toolkit.i18nText("RowTo");
	}

}