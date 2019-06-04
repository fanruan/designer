package com.fr.design.i18n.impl;

import com.fr.design.i18n.Action;
import com.fr.design.i18n.LocaleDifference;
import com.fr.design.i18n.ActionType;
import com.fr.general.CloudCenter;
import com.fr.general.ComparatorUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 一些默认的实现
 *
 * @author Hades
 * @date 2019/5/30
 */
public abstract class AbstractDefaultLocaleAction implements LocaleDifference {

    protected EnumMap<ActionType, String> urls = new EnumMap<ActionType, String>(ActionType.class);
    private Action action = Action.EMPTY_ACTION;

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
    public void doAction(Locale ...locales) {
        List<Locale> localeList = Arrays.asList(locales);
        if (localeList.contains(this.getLocale())) {
            action.todo();
        }
    }

    @Override
    public LocaleDifference buildAction(Action action) {
        this.action = action;
        return this;
    }

    @Override
    public String getSplashPath() {
        return "/com/fr/design/images/splash_10_en.gif";
    }
}
