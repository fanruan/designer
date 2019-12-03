package com.fr.design.fun.impl;

import com.fr.design.file.HistoryTemplateListCache;
import com.fr.design.fun.PreviewProvider;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.mainframe.JVirtualTemplate;
import com.fr.general.ComparatorUtils;
import com.fr.general.web.ParameterConstants;
import com.fr.stable.fun.impl.AbstractProvider;
import com.fr.stable.fun.mark.API;

import java.util.Collections;
import java.util.Map;

/**
 * @author richie
 * @date 2015-05-13
 * @since 8.0
 */
@API(level = PreviewProvider.CURRENT_LEVEL)
public abstract class AbstractPreviewProvider extends AbstractProvider implements PreviewProvider {

    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

    @Override
    public String mark4Provider() {
        return getClass().getName();
    }

    @Override
    public void onClick(JTemplate<?, ?> jt) {
        jt.previewMenuActionPerformed(this);
    }

    @Override
    public Map<String, Object> parametersForPreview() {
        return Collections.emptyMap();
    }

    public boolean equals(Object obj) {
        return obj instanceof AbstractPreviewProvider
                && ComparatorUtils.equals(nameForPopupItem(), ((AbstractPreviewProvider) obj).nameForPopupItem());
    }

    @Override
    public int hashCode() {
        return nameForPopupItem().hashCode();
    }


    @Override
    public String getActionType() {
        return ParameterConstants.VIEWLET;
    }

    @Override
    public boolean accept(JTemplate jTemplate) {
        if (jTemplate == null) {
            jTemplate = HistoryTemplateListCache.getInstance().getCurrentEditingTemplate();
        }
        return jTemplate == null || jTemplate.isJWorkBook() || jTemplate instanceof JVirtualTemplate;
    }
}