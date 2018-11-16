package com.fr.design.actions.file.export;

import com.fr.design.actions.JTemplateAction;
import com.fr.design.mainframe.JTemplate;
import com.fr.file.filter.ChooseFileFilter;
import com.fr.io.exporter.DesignExportType;

public abstract class AbstractExportAction<E extends JTemplate<?, ?>> extends JTemplateAction<E> {

    public AbstractExportAction(E t) {
        super(t);
    }

    public abstract String exportScopeName();

    public abstract DesignExportType exportType();

    protected abstract ChooseFileFilter getChooseFileFilter();

}
