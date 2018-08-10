/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.headerfooter;

import com.fr.page.ReportSettingsProvider;

import com.fr.stable.unit.UNIT;

/**
 * Edit header(the object ReportHF).
 */
public class EditHeaderPane extends HeaderFooterPane {
	
	@Override
	protected String title4PopupWindow() {
		return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_HF_Edit_Header");
	}

	public void populate(ReportSettingsProvider reportSettings) {
		super.populate(reportSettings, true);
		
	}
	
	public UNIT updateReportSettings() {
		return super.updateReportSettings();		
	}

}
