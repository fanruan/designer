package com.fr.design.locale.impl;

import com.fr.general.CloudCenter;
import com.fr.general.GeneralContext;
import com.fr.general.locale.LocaleMark;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author Hades
 * @date 2019/6/24
 */
public class UserInfoMark implements LocaleMark<String> {

    private Map<Locale, String> map = new HashMap<>();
    private static final String CN_LOGIN_HTML = CloudCenter.getInstance().acquireUrlByKind("frlogin.cn");
    private static final String EN_LOGIN_HTML = CloudCenter.getInstance().acquireUrlByKind("frlogin.en");
    private static final String TW_LOGIN_HTML = CloudCenter.getInstance().acquireUrlByKind("frlogin.tw");
    private static final String JP_LOGIN_HTML = CloudCenter.getInstance().acquireUrlByKind("frlogin.jp");
    private static final String KR_LOGIN_HTML = CloudCenter.getInstance().acquireUrlByKind("frlogin.kr");

    public UserInfoMark() {
        map.put(Locale.CHINA, CN_LOGIN_HTML);
        map.put(Locale.KOREA, KR_LOGIN_HTML);
        map.put(Locale.JAPAN, JP_LOGIN_HTML);
        map.put(Locale.US, EN_LOGIN_HTML);
        map.put(Locale.TAIWAN, TW_LOGIN_HTML);
    }

    @Override
    public String getValue() {
        String result = map.get(GeneralContext.getLocale());
        return result == null ? EN_LOGIN_HTML : result;
    }
}
