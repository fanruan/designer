package com.fr.design.fun.impl;

import com.fr.design.beans.BasicStorePane;
import com.fr.design.fun.ExportAttrTabProvider;
import com.fr.design.report.AbstractExportPane;
import com.fr.stable.StringUtils;
import com.fr.stable.fun.impl.AbstractProvider;
import com.fr.stable.fun.mark.API;

import javax.swing.*;

/**
 * Created by vito on 16/5/5.
 */
@API(level = ExportAttrTabProvider.CURRENT_LEVEL)
public abstract class AbstractExportAttrTabProvider extends AbstractProvider implements ExportAttrTabProvider {
    @Override
    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

    /**
     * @see ExportAttrTabProvider#toServiceComponent()
     */
    @Deprecated
    public AbstractExportPane toExportPane() {
        return null;
    }

    /**
     * @see ExportAttrTabProvider#toServiceComponent()
     */
    @Deprecated
    public JComponent toSwingComponent() {
        return toServiceComponent();
    }

    @Deprecated
    public String title() {
        return StringUtils.EMPTY;
    }

    @Deprecated
    public String tag() {
        return StringUtils.EMPTY;
    }

    @Override
    public BasicStorePane<?> toServiceComponent() {
        return toExportPane();
    }

    @Override
    public String mark4Provider() {
        return this.getClass().getName();
    }
}
