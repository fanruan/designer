/*
 * * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.file.export;

import com.fr.base.BaseUtils;
import com.fr.design.mainframe.JWorkBook;
import com.fr.design.menu.KeySetUtils;
import com.fr.file.filter.ChooseFileFilter;
import com.fr.general.Inter;
import com.fr.io.exporter.Exporter;
import com.fr.io.exporter.WordExporter;

/**
 * Export excel.
 */
public class WordExportAction extends AbstractExportAction {
    /**
     * Constructor
     */
	public WordExportAction(JWorkBook jwb) {
		super(jwb);
        this.setMenuKeySet(KeySetUtils.WORD_EXPORT);
        this.setName(getMenuKeySet().getMenuKeySetName()+ "...");
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_file/word.png"));
    }
	
    @Override
	protected Exporter getExporter() {
        return new WordExporter();
    }

    @Override
	protected ChooseFileFilter getChooseFileFilter() {
        return new ChooseFileFilter(new String[]{"doc"}, Inter.getLocText("Export-Word"));
    }

    @Override
	protected String getDefaultExtension() {
        return "doc";
    }
}