package com.fr.design.report.freeze;

import com.fr.design.gui.ilable.UILabel;
import javax.swing.SwingConstants;


import com.fr.stable.FT;
import com.fr.stable.StableUtils;

public class FreezePagePane extends FreezeAndRepeatPane {
	private boolean isNumber;

	public FreezePagePane(boolean isNumber) {
		this.isNumber = isNumber;
		start = new UILabel(isNumber ? com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Frozen_N.O.")+" 1" : com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Frozen_N.O.")+" A", SwingConstants.CENTER);
		end = new UILabel(isNumber ? " 1"+com.fr.design.i18n.Toolkit.i18nText("Row") : " A"+com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Column"), SwingConstants.CENTER);
		super.initComponent();
	}

	@Override
	protected String title4PopupWindow() {
		return "FreezePage";
	}

	@Override
	public void populateBean(FT ob) {
		if (isNumber) {
			((UILabel) end).setText(String.valueOf(ob.getTo() + 1)+com.fr.design.i18n.Toolkit.i18nText("Row"));
		} else {
			((UILabel) end).setText(StableUtils.convertIntToABC(ob.getTo() + 1)+com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Column"));
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
		return isNumber ? com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Row_To") : com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Column_To");
	}
}
