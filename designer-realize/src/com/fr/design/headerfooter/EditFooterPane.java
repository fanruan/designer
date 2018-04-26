/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.headerfooter;

import com.fr.general.Inter;
import com.fr.report.stable.ReportSettings;
import com.fr.stable.unit.UNIT;

/**
 * Edit footer(the object ReportHF).
 */
public class EditFooterPane extends HeaderFooterPane {
	
	@Override
	protected String title4PopupWindow() {
		return Inter.getLocText("HF-Edit_Footer");
	}
    
    public void populate(ReportSettings reportSettings) {
		super.populate(reportSettings, false);
		
	}
	
	public UNIT updateReportSettings() {
		return super.updateReportSettings();		
	}
}