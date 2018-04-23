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
import com.fr.io.exporter.SVGExporter;

/**
 * Export SVG
 */
public class SVGExportAction extends AbstractExportAction {
    /**
     * Constructor
     */
	public SVGExportAction(JWorkBook jwb) {
		super(jwb);
		
        this.setMenuKeySet(KeySetUtils.SVG_EXPORT);
        this.setName(getMenuKeySet().getMenuKeySetName()+"...");
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_file/svg.png"));
    }
	
    @Override
	protected Exporter getExporter() {
        return new SVGExporter();
    }

    @Override
	protected ChooseFileFilter getChooseFileFilter() {
        return new ChooseFileFilter(new String[]{"svg"}, Inter.getLocText("Export-SVG"));
    }

    @Override
	protected String getDefaultExtension() {
        return "svg";
    }
}