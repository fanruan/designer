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
public class VideoMark implements LocaleMark<String> {

    private Map<Locale, String> map = new HashMap<>();
    private static final String VIDEO_EN = CloudCenter.getInstance().acquireUrlByKind("bbs.video.en_US");
    private static final String VIDEO_CN = CloudCenter.getInstance().acquireUrlByKind("bbs.video.zh_CN");
    private static final String VIDEO_TW = CloudCenter.getInstance().acquireUrlByKind("bbs.video.zh_TW");

    public VideoMark() {
        map.put(Locale.CHINA, VIDEO_CN);
        map.put(Locale.KOREA, VIDEO_EN);
        map.put(Locale.JAPAN, VIDEO_EN);
        map.put(Locale.US, VIDEO_EN);
        map.put(Locale.TAIWAN, VIDEO_TW);
    }

    @Override
    public String getValue() {
        String result = map.get(GeneralContext.getLocale());
        return result == null ? VIDEO_EN : result;
    }
}
