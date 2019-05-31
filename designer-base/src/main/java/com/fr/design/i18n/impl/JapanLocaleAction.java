package com.fr.design.i18n.impl;

import com.fr.design.i18n.ActionType;
import com.fr.general.CloudCenter;

public class JapanLocaleAction extends AbstractDefaultLocaleAction {

    @Override
    protected void init() {
        super.init();
        urls.put(ActionType.ACTIVATION_CODE, CloudCenter.getInstance().acquireUrlByKind("frlogin.jp"));
    }


    @Override
    public String getSplashPath() {
        return "/com/fr/design/images/splash_10_jp.gif";
    }


}
