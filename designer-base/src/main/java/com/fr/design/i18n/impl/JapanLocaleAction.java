package com.fr.design.i18n.impl;

import com.fr.design.i18n.ActionType;
import com.fr.general.CloudCenter;

import java.util.Locale;

/**
 * 日文环境具体的表现动作
 *
 * @author Hades
 * @date 2019/5/30
 */
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

    @Override
    public Locale getLocale() {
        return Locale.JAPAN;
    }


}
