package com.fr.design.i18n.impl;

import java.util.Locale;

/**
 * 简体中文环境具体的表现动作
 *
 * @author Hades
 * @date 2019/5/30
 */
public class ChinaLocaleAction extends AbstractDefaultLocaleAction {

    @Override
    public String getSplashPath() {
        return "/com/fr/design/images/splash_10.gif";
    }

    @Override
    public Locale getLocale() {
        return Locale.CHINA;
    }
}
