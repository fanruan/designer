/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.file.export;

import com.fr.base.BaseUtils;
import com.fr.base.extension.FileExtension;
import com.fr.design.i18n.Toolkit;
import com.fr.design.mainframe.JWorkBook;
import com.fr.design.menu.KeySetUtils;
import com.fr.file.filter.ChooseFileFilter;
import com.fr.io.exporter.DesignExportType;
import com.fr.main.TemplateWorkBook;

/**
 * Export CSV.
 */
public class CSVExportAction extends AbstractJWorkBookExportAction {
    /**
     * Constructor
     */
    public CSVExportAction(JWorkBook jwb) {
        super(jwb);
        this.setMenuKeySet(KeySetUtils.CSV_EXPORT);
        this.setName(getMenuKeySet().getMenuKeySetName() + "...");
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_file/csv.png"));
    }

    @Override
    protected ChooseFileFilter getChooseFileFilter() {
        return new ChooseFileFilter(FileExtension.CSV, Toolkit.i18nText("Fine-Design_Report_Export_CSV"));
    }

    @Override
    protected String getDefaultExtension() {
        TemplateWorkBook tpl = this.getTemplateWorkBook();
        if (hasLayerReport(tpl)) {
            return FileExtension.ZIP.getExtension();
        } else {
            return FileExtension.CSV.getExtension();
        }
    }

    @Override
    public DesignExportType exportType() {
        return DesignExportType.CSV;
    }
}