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
 * Export Text.
 */
public class TextExportAction extends AbstractJWorkBookExportAction {

    public TextExportAction(JWorkBook jwb) {
        super(jwb);
        this.setMenuKeySet(KeySetUtils.TEXT_EXPORT);
        this.setName(getMenuKeySet().getMenuKeySetName() + "...");
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_file/text.png"));
    }

    @Override
    public DesignExportType exportType() {
        return DesignExportType.TEXT;
    }

    @Override
    protected ChooseFileFilter getChooseFileFilter() {
        return new ChooseFileFilter(FileExtension.TXT, Toolkit.i18nText("Fine-Design_Report_Export_Text"));
    }

    @Override
    protected String getDefaultExtension() {
        return FileExtension.TXT.getExtension();
    }
}