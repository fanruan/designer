/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.poly;

import com.fr.page.ReportSettingsProvider;
import com.fr.poly.creator.PolyElementCasePane;
import com.fr.report.poly.PolyECBlock;
import com.fr.report.stable.ReportSettings;

/**
 * @author richer
 * @since 6.5.4 创建于2011-4-2 聚合报表块的设计界面
 */
public class JPolyBlockPane extends PolyElementCasePane {

	public JPolyBlockPane(PolyECBlock block) {
		super(block);
		
		setHorizontalScrollBarVisible(false);
		setVerticalScrollBarVisible(false);
		setColumnHeaderVisible(false);
		setRowHeaderVisible(false);
		this.setSelection(null);
	}

	@Override
	public ReportSettingsProvider getReportSettings() {
		return new ReportSettings();
	}
}