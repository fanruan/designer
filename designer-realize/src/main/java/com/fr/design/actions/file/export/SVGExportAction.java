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

/**
 * Export SVG
 */
public class SVGExportAction extends AbstractWorkBookExportAction {
    /**
     * Constructor
     */
    public SVGExportAction(JWorkBook jwb) {
        super(jwb);

        this.setMenuKeySet(KeySetUtils.SVG_EXPORT);
        this.setName(getMenuKeySet().getMenuKeySetName() + "...");
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_file/svg.png"));
    }

    @Override
    protected ChooseFileFilter getChooseFileFilter() {
        return new ChooseFileFilter(FileExtension.SVG, Toolkit.i18nText("Fine-Design_Report_Export_SVG"));
    }

    @Override
    protected String getDefaultExtension() {
        return FileExtension.SVG.getExtension();
    }

    @Override
    public DesignExportType exportType() {
        return DesignExportType.SVG;
    }
}