package com.fr.design.fun.impl;

import com.fr.design.file.HistoryTemplateListCache;
import com.fr.design.fun.ToolbarItemProvider;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.mainframe.JVirtualTemplate;
import com.fr.stable.fun.impl.AbstractProvider;
import com.fr.stable.fun.mark.API;

/**
 * Created by richie on 15/12/1.
 */
@API(level = ToolbarItemProvider.CURRENT_LEVEL)
public abstract class AbstractToolbarItem extends AbstractProvider implements ToolbarItemProvider {

    @Override
    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

    @Override
    public String mark4Provider() {
        return getClass().getName();
    }

    @Override
    public boolean accept(JTemplate jTemplate) {
        if (jTemplate == null) {
            jTemplate = HistoryTemplateListCache.getInstance().getCurrentEditingTemplate();
        }
        return jTemplate == null || jTemplate.isJWorkBook() || jTemplate instanceof JVirtualTemplate;
    }
}