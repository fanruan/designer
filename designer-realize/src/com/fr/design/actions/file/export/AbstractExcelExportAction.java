/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.file.export;

import com.fr.base.ExcelUtils;
import com.fr.design.mainframe.JWorkBook;
import com.fr.file.filter.ChooseFileFilter;
import com.fr.general.Inter;
import com.fr.main.TemplateWorkBook;

/**
 * Export excel.
 */
public abstract class AbstractExcelExportAction extends AbstractExportAction {
    /**
     * Constructor
     */
	protected AbstractExcelExportAction(JWorkBook jwb) {
		super(jwb);
    }

    @Override
	protected ChooseFileFilter getChooseFileFilter() {
    	TemplateWorkBook tpl = this.getTemplateWorkBook();
    	if (hasLayerReport(tpl)) {
    		return new ChooseFileFilter(new String[]{"zip"}, "ZIP");
    	} else {
    		return new ChooseFileFilter(new String[]{"xls", "xlsx"}, Inter.getLocText("Export-Excel"));
    	}
    }

    @Override
	protected String getDefaultExtension() {
    	TemplateWorkBook tpl = this.getTemplateWorkBook();
    	if (hasLayerReport(tpl)) {
    		return "zip";
    	} else {
    		return ExcelUtils.checkThirdJarSupportPOI() ? "xlsx" : "xls";
    	}
    }
}