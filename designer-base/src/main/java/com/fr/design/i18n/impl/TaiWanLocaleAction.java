package com.fr.design.i18n.impl;

import com.fr.design.i18n.ActionType;
import com.fr.general.CloudCenter;

import java.util.List;
import java.util.Locale;

/**
 * 繁体中文具体的表现动作
 *
 * @author Hades
 * @date 2019/5/30
 */
public class TaiWanLocaleAction extends AbstractDefaultLocaleAction {

    @Override
    protected void init() {
        super.init();
        urls.put(ActionType.VIDEO, CloudCenter.getInstance().acquireUrlByKind("bbs.video.zh_TW"));
        urls.put(ActionType.ACTIVATION_CODE, CloudCenter.getInstance().acquireUrlByKind("frlogin.tw"));
    }

    @Override
    public Locale getLocale() {
        return Locale.TAIWAN;
    }
}
