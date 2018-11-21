/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.file.export;

import com.fr.base.ExcelUtils;
import com.fr.base.extension.FileExtension;
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
            return new ChooseFileFilter(FileExtension.ZIP, "ZIP");
        } else {
            return new ChooseFileFilter(new String[]{FileExtension.XLSX.getExtension(), FileExtension.XLS.getExtension()},
                    Toolkit.i18nText("Fine-Design_Report_Export_Excel"));
        }
    }

    @Override
    protected String getDefaultExtension() {
        TemplateWorkBook tpl = this.getTemplateWorkBook();
        if (ReportUtils.hasLayerReport4Template(tpl)) {
            return FileExtension.ZIP.getExtension();
        } else {
            return ExcelUtils.checkThirdJarSupportPOI() ? FileExtension.XLSX.getExtension() : FileExtension.XLS.getExtension();
        }
    }
}