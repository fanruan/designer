package com.fr.design.actions.file.export;

import com.fr.base.BaseUtils;
import com.fr.base.extension.FileExtension;
import com.fr.design.mainframe.JWorkBook;
import com.fr.design.menu.KeySetUtils;
import com.fr.file.filter.ChooseFileFilter;

import com.fr.io.exporter.EmbeddedTableDataExporter;
import com.fr.io.exporter.Exporter;

import java.util.EnumSet;

/**
 * Export Embedded.
 */
public class EmbeddedExportExportAction extends AbstractExportAction {
    /**
     * Constructor
     */
    public EmbeddedExportExportAction(JWorkBook jwb) {
        super(jwb);
        this.setMenuKeySet(KeySetUtils.EMBEDDED_EXPORT);
        this.setName(getMenuKeySet().getMenuKeySetName() + "...");
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/base/images/oem/logo.png"));
    }

    @Override
    protected Exporter getExporter() {
        return new EmbeddedTableDataExporter();
    }

    @Override
    protected ChooseFileFilter getChooseFileFilter() {
        return new ChooseFileFilter(EnumSet.of(FileExtension.CPTX, FileExtension.CPT),
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Export_Template(Embedded_Data)"));
    }

    @Override
    protected String getDefaultExtension() {
        return getEditingComponent().suffix().substring(1);
    }

}