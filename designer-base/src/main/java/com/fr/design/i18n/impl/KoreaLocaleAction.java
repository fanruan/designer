package com.fr.design.i18n.impl;

import java.util.Locale;

/**
 * 韩文环境具体的表现动作
 *
 * @author Hades
 * @date 2019/5/30
 */
public class KoreaLocaleAction extends AbstractDefaultLocaleAction {

    @Override
    public Locale getLocale() {
        return Locale.KOREA;
    }
}
