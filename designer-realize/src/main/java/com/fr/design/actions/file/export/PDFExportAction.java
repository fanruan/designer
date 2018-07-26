/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.file.export;

import com.fr.base.BaseUtils;
import com.fr.base.extension.FileExtension;
import com.fr.design.mainframe.JWorkBook;
import com.fr.design.menu.KeySetUtils;
import com.fr.file.filter.ChooseFileFilter;

import com.fr.io.exporter.Exporter;
import com.fr.web.core.reserve.PDFExporterFactory;

/**
 * Export pdf
 */
public class PDFExportAction extends AbstractExportAction {
    /**
     * Constructor
     */
	public PDFExportAction(JWorkBook jwb) {
		super(jwb);
        this.setMenuKeySet(KeySetUtils.PDF_EXPORT);
        this.setName(getMenuKeySet().getMenuKeySetName()+"...");
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_file/pdf.png"));
    }

    @Override
	protected Exporter getExporter() {

        return PDFExporterFactory.getPDFExporter();
    }

    @Override
	protected ChooseFileFilter getChooseFileFilter() {
        return new ChooseFileFilter(FileExtension.PDF, com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Export-PDF"));
    }

    @Override
	protected String getDefaultExtension() {
        return FileExtension.PDF.getExtension();
    }

}