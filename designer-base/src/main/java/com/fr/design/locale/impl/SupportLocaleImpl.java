package com.fr.design.locale.impl;

import com.fr.general.locale.SupportLocale;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * 某些国际化环境支持的操作
 * 需要增加/删除支持的语言 统一在这里修改 无须改动业务代码
 * 后续有新的不同语言下的差异操作 添加新的枚举
 * @author Hades
 * @date 2019/6/24
 */
public enum SupportLocaleImpl implements SupportLocale {

    /**
     * 社区菜单支持的国际化环境
     */
    COMMUNITY {
        @Override
        public Set<Locale> support() {
            Set<Locale> set = new HashSet<Locale>();
            set.add(Locale.CHINA);
            set.add(Locale.TAIWAN);
            return set;
        }
    },

    /**
     * Facebook支持的国际化环境
     */
    FACEBOOK {
        @Override
        public Set<Locale> support() {
            Set<Locale> set = new HashSet<Locale>();
            set.add(Locale.TAIWAN);
            return set;
        }
    }


}
