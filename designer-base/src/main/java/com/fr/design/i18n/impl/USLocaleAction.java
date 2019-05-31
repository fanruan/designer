package com.fr.design.i18n.impl;

import com.fr.design.i18n.ActionType;
import com.fr.general.CloudCenter;

public class USLocaleAction extends AbstractDefaultLocaleAction {

    @Override
    protected void init() {
        super.init();
        urls.put(ActionType.VIDEO, CloudCenter.getInstance().acquireUrlByKind("bbs.video.en"));
        urls.put(ActionType.HELP_DOCUMENT, CloudCenter.getInstance().acquireUrlByKind("help.en_US.10"));
        urls.put(ActionType.ACTIVATION_CODE, CloudCenter.getInstance().acquireUrlByKind("frlogin.en"));
    }

}
