/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.headerfooter;

import com.fr.page.ReportSettingsProvider;
import com.fr.general.Inter;
import com.fr.stable.unit.UNIT;

/**
 * Edit header(the object ReportHF).
 */
public class EditHeaderPane extends HeaderFooterPane {
	
	@Override
	protected String title4PopupWindow() {
		return Inter.getLocText("HF-Edit_Header");
	}

	public void populate(ReportSettingsProvider reportSettings) {
		super.populate(reportSettings, true);
		
	}
	
	public UNIT updateReportSettings() {
		return super.updateReportSettings();		
	}

}