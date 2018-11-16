/*
 * * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
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
 * Export excel.
 */
public class WordExportAction extends AbstractJWorkBookExportAction {
    /**
     * Constructor
     */
    public WordExportAction(JWorkBook jwb) {
        super(jwb);
        this.setMenuKeySet(KeySetUtils.WORD_EXPORT);
        this.setName(getMenuKeySet().getMenuKeySetName() + "...");
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_file/word.png"));
    }

    @Override
    public DesignExportType exportType() {
        return DesignExportType.WORD;
    }

    @Override
    protected ChooseFileFilter getChooseFileFilter() {
        return new ChooseFileFilter(FileExtension.DOC, Toolkit.i18nText("Fine-Design_Report_Export_Word"));
    }

    @Override
    protected String getDefaultExtension() {
        return FileExtension.DOC.getExtension();
    }
}