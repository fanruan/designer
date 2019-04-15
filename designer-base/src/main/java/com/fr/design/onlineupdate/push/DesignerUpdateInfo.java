package com.fr.design.onlineupdate.push;

import com.fr.general.ComparatorUtils;
import com.fr.json.JSONObject;
import com.fr.stable.StringUtils;

import java.security.InvalidParameterException;

/**
 * Created by plough on 2019/4/8.
 */
class DesignerUpdateInfo {
    private static final String KEY_VERSION = "version";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_BACKGROUND_URL = "background";
    private static final String KEY_MORE_INFO_URL = "more";

    private final String currentVersion;  // 当前版本
    private final String latestVersion;  // 最新版本
    private final String lastIgnoredVersion;  // 最近一次跳过的版本

    private final String pushVersion;  // 推送版本
    private final String pushContent;  // 推送更新内容
    private final String backgroundUrl;  // 推送背景图片 url
    private final String moreInfoUrl;  // 更多新特性

    DesignerUpdateInfo(String currentVersion, String latestVersion, String lastIgnoredVersion, JSONObject pushData) {
        this.currentVersion = currentVersion;
        this.latestVersion = latestVersion;
        this.lastIgnoredVersion = lastIgnoredVersion;

        this.pushVersion = pushData.optString(KEY_VERSION);
        this.pushContent = pushData.optString(KEY_CONTENT);
        this.backgroundUrl = pushData.optString(KEY_BACKGROUND_URL);
        this.moreInfoUrl = pushData.optString(KEY_MORE_INFO_URL);

        // 简单做下参数校验
        if (hasEmptyField()) {
            throw new InvalidParameterException();
        }
    }

    private boolean hasEmptyField() {
        // lastIgnoredVersion 可以为空
        return StringUtils.isEmpty(currentVersion)
                || StringUtils.isEmpty(latestVersion)
                || StringUtils.isEmpty(pushVersion)
                || StringUtils.isEmpty(pushContent)
                || StringUtils.isEmpty(backgroundUrl)
                || StringUtils.isEmpty(moreInfoUrl);
    }

    String getCurrentVersion() {
        return currentVersion;
    }

    String getLatestVersion() {
        return latestVersion;
    }

    String getLastIgnoredVersion() {
        return lastIgnoredVersion;
    }

    String getPushVersion() {
        return pushVersion;
    }

    String getPushContent() {
        return pushContent;
    }

    String getBackgroundUrl() {
        return backgroundUrl;
    }

    String getMoreInfoUrl() {
        return moreInfoUrl;
    }

    boolean hasNewPushVersion() {
        boolean result = ComparatorUtils.compare(pushVersion, currentVersion) > 0
                && ComparatorUtils.compare(pushVersion, latestVersion) <= 0;
        if (StringUtils.isNotEmpty(lastIgnoredVersion)) {
            result = result && ComparatorUtils.compare(pushVersion, lastIgnoredVersion) > 0;
        }

        return result;
    }

}
