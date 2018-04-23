/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.file.export;

import com.fr.base.BaseUtils;
import com.fr.design.mainframe.JWorkBook;
import com.fr.design.menu.KeySetUtils;
import com.fr.file.filter.ChooseFileFilter;
import com.fr.general.Inter;
import com.fr.io.exporter.Exporter;
import com.fr.io.exporter.TextExporter;

/**
 * Export Text.
 */
public class TextExportAction extends AbstractExportAction {
    /**
     * Constructor
     */
	public TextExportAction(JWorkBook jwb) {
		super(jwb);
        this.setMenuKeySet(KeySetUtils.TEXT_EXPORT);
        this.setName(getMenuKeySet().getMenuKeySetName()+ "...");
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_file/text.png"));
    }
	
    @Override
	protected Exporter getExporter() {
        return new TextExporter();
    }

    @Override
	protected ChooseFileFilter getChooseFileFilter() {
        return new ChooseFileFilter(new String[]{"txt"}, Inter.getLocText("Export-Text"));
    }

    @Override
	protected String getDefaultExtension() {
        return "txt";
    }
}