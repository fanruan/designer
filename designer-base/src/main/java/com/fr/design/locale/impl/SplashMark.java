package com.fr.design.locale.impl;

import com.fr.design.DesignerEnvManager;
import com.fr.general.GeneralContext;
import com.fr.general.locale.LocaleMark;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author Hades
 * @date 2019/6/24
 */
public class SplashMark implements LocaleMark<String> {

    private Map<Locale, String> map = new HashMap<Locale, String>();
    private static final String SPLASH_PATH = "/com/fr/design/images/splash_10.png";
    private static final String SPLASH_EN_PATH = "/com/fr/design/images/splash_10_en.png";

    public SplashMark() {
        map.put(Locale.CHINA, SPLASH_PATH);
        map.put(Locale.KOREA, SPLASH_EN_PATH);
        map.put(Locale.JAPAN, SPLASH_EN_PATH);
        map.put(Locale.US, SPLASH_EN_PATH);
        map.put(Locale.TAIWAN, SPLASH_EN_PATH);
    }

    @Override
    public String getValue() {
        String result = map.get(DesignerEnvManager.getEnvManager().getLanguage());
        return result == null ? SPLASH_EN_PATH : result;
    }
}
