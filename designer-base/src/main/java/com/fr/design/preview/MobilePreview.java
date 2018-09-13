package com.fr.design.preview;

import com.fr.design.fun.impl.AbstractPreviewProvider;

import java.util.HashMap;
import java.util.Map;

/**
 * @author kerry
 * @date 2018/5/11
 */
public class MobilePreview extends AbstractPreviewProvider {
    private static final int PREVIEW_TYPE = 4;
    private static final String ACTION_TYPE = "path";

    @Override
    public String nameForPopupItem() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Mobile_Preview");
    }

    @Override
    public String iconPathForPopupItem() {
        return "com/fr/design/images/buttonicon/mobile.png";
    }

    @Override
    public String iconPathForLarge() {
        return "com/fr/design/images/buttonicon/mobileb24.png";
    }

    @Override
    public int previewTypeCode() {
        return PREVIEW_TYPE;
    }

    @Override
    public String getActionType() {
        return ACTION_TYPE;
    }

    @Override
    public Map<String, Object> parametersForPreview() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("op", "mobile");
        return map;
    }
}
