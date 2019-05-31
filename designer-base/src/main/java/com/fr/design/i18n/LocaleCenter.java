package com.fr.design.i18n;

import com.fr.design.i18n.impl.ChinaLocaleAction;
import com.fr.design.i18n.impl.JapanLocaleAction;
import com.fr.design.i18n.impl.KoreaLocaleAction;
import com.fr.design.i18n.impl.TaiWanLocaleAction;
import com.fr.design.i18n.impl.USLocaleAction;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LocaleCenter {

    private Map<Locale, LocaleDifference> actionMap = new HashMap<Locale, LocaleDifference>();

    private LocaleCenter() {
        init();
    }

    private void init() {
        actionMap.put(Locale.CHINA, new ChinaLocaleAction());
        actionMap.put(Locale.US, new USLocaleAction());
        actionMap.put(Locale.TAIWAN, new TaiWanLocaleAction());
        actionMap.put(Locale.JAPAN, new JapanLocaleAction());
        actionMap.put(Locale.KOREA, new KoreaLocaleAction());
    }

    private static class Holder {
        private static final LocaleCenter INSTANCE = new LocaleCenter();
    }

    public static LocaleCenter getInstance() {
        return Holder.INSTANCE;
    }

    public LocaleDifference getLocaleAction(Locale locale) {
        LocaleDifference localeDifference = actionMap.get(locale);
        return localeDifference == null ? actionMap.get(Locale.CHINA) : localeDifference;
    }
}
