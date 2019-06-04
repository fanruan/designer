package com.fr.design.i18n.impl;

import com.fr.design.i18n.ActionType;
import com.fr.general.CloudCenter;

import java.util.Locale;

/**
 * 英文环境具体的表现动作
 *
 * @author Hades
 * @date 2019/5/30
 */
public class USLocaleAction extends AbstractDefaultLocaleAction {

    @Override
    protected void init() {
        super.init();
        urls.put(ActionType.VIDEO, CloudCenter.getInstance().acquireUrlByKind("bbs.video.en"));
        urls.put(ActionType.HELP_DOCUMENT, CloudCenter.getInstance().acquireUrlByKind("help.en_US.10"));
        urls.put(ActionType.ACTIVATION_CODE, CloudCenter.getInstance().acquireUrlByKind("frlogin.en"));
    }

    @Override
    public Locale getLocale() {
        return Locale.US;
    }
}
