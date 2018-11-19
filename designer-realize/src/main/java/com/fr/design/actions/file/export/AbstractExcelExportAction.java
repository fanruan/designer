/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.file.export;

import com.fr.base.ExcelUtils;
import com.fr.design.i18n.Toolkit;
import com.fr.design.mainframe.JWorkBook;
import com.fr.file.filter.ChooseFileFilter;
import com.fr.main.TemplateWorkBook;
import com.fr.report.core.ReportUtils;

/**
 * Export excel.
 */
public abstract class AbstractExcelExportAction extends AbstractWorkBookExportAction {

    protected AbstractExcelExportAction(JWorkBook jwb) {
        super(jwb);
    }

    @Override
    protected ChooseFileFilter getChooseFileFilter() {
        TemplateWorkBook tpl = this.getTemplateWorkBook();
        if (ReportUtils.hasLayerReport4Template(tpl)) {
            return new ChooseFileFilter(new String[]{"zip"}, "ZIP");
        } else {
            return new ChooseFileFilter(new String[]{"xls", "xlsx"}, Toolkit.i18nText("Fine-Design_Report_Export_Excel"));
        }
    }

    @Override
    protected String getDefaultExtension() {
        TemplateWorkBook tpl = this.getTemplateWorkBook();
        if (ReportUtils.hasLayerReport4Template(tpl)) {
            return "zip";
        } else {
            return ExcelUtils.checkThirdJarSupportPOI() ? "xlsx" : "xls";
        }
    }
}