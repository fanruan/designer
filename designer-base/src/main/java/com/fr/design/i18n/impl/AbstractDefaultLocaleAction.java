package com.fr.design.i18n.impl;

import com.fr.design.i18n.LocaleDifference;
import com.fr.design.i18n.ActionType;
import com.fr.general.CloudCenter;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractDefaultLocaleAction implements LocaleDifference {

    protected EnumMap<ActionType, String> urls = new EnumMap<ActionType, String>(ActionType.class);

    protected void init() {
        urls.put(ActionType.BBS, CloudCenter.getInstance().acquireUrlByKind("bbs"));
        urls.put(ActionType.VIDEO, CloudCenter.getInstance().acquireUrlByKind("bbs.video"));
        urls.put(ActionType.ACTIVATION_CODE, CloudCenter.getInstance().acquireUrlByKind("frlogin.cn"));
        urls.put(ActionType.HELP_DOCUMENT, CloudCenter.getInstance().acquireUrlByKind("help.zh_CN.10"));
    }

    @Override
    public Map<ActionType, String> getUrls() {
        if (urls.isEmpty()) {
            init();
        }
        return urls;
    }

    @Override
    public void addAction(List list, Object action) {
        // do nothing
    }

    @Override
    public String getSplashPath() {
        return "/com/fr/design/images/splash_10_en.gif";
    }


}
