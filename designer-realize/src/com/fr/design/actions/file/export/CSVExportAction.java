/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.file.export;

import com.fr.base.BaseUtils;
import com.fr.design.mainframe.JWorkBook;
import com.fr.design.menu.KeySetUtils;
import com.fr.file.filter.ChooseFileFilter;
import com.fr.general.Inter;
import com.fr.io.exporter.CSVExporter;
import com.fr.io.exporter.Exporter;
import com.fr.io.exporter.LargeDataPageCSVExporter;
import com.fr.main.TemplateWorkBook;

/**
 * Export CSV.
 */
public class CSVExportAction extends AbstractExportAction {
    /**
     * Constructor
     */
	public CSVExportAction(JWorkBook jwb) {
		super(jwb);
        this.setMenuKeySet(KeySetUtils.CSV_EXPORT);
        this.setName(getMenuKeySet().getMenuKeySetName()+ "...");
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_file/csv.png"));
    }
	
    @Override
	protected Exporter getExporter() {
        TemplateWorkBook tpl = this.getTemplateWorkBook();
        if (hasLayerReport(tpl)) {
            return new LargeDataPageCSVExporter();
        } else {
            return new CSVExporter();
        }
    }

    @Override
	protected ChooseFileFilter getChooseFileFilter() {
        return new ChooseFileFilter(new String[]{"csv"}, Inter.getLocText("Export-CSV"));
    }

    @Override
	protected String getDefaultExtension() {
        TemplateWorkBook tpl = this.getTemplateWorkBook();
        if (hasLayerReport(tpl)) {
            return "zip";
        } else {
            return "csv";
        }
    }
}