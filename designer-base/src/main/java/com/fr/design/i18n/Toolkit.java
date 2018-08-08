package com.fr.design.i18n;

import com.fr.general.GeneralContext;
import com.fr.general.log.MessageFormatter;
import com.fr.locale.InterProviderFactory;
import com.fr.locale.LocaleManager;
import com.fr.locale.impl.FineLocaleManager;

/**
 * 设计器国际化类，后面会不再依赖InterProviderFactory
 */
public class Toolkit {

    private static LocaleManager localeManager = FineLocaleManager.create();

    static {
        addResource("com/fr/design/i18n/main");
    }

    public static void addResource(String path) {

        localeManager.addResource(path);
    }

    /**
     * 设计器国际化方法
     *
     * @param key 国际化键
     * @return 国际化值
     */
    public static String i18nText(String key) {
        return localeManager.getLocalBundle(GeneralContext.getLocale()).getText(localeManager, key);
    }

    /**
     * 带格式化内容的国际化方法
     * Toolkit.i18nText("Fine-Design_xxx", 1, 2)，假设Fine-Design_xxx的中文值为：我来计算{}+{}，输出结果为：我来计算1+2
     *
     * @param key  格式化文本
     * @param args 格式化参数
     * @return 国际化值
     */
    public static String i18nText(String key, Object... args) {
        String format = InterProviderFactory.getProvider().getLocText(key);
        MessageFormatter.FormattingTuple tuple = MessageFormatter.arrayFormat(format, args);
        return localeManager.getLocalBundle(GeneralContext.getLocale()).getText(localeManager, tuple.getMessage());
    }

    /**
     * 太鸡儿多了，改不完，先加上慢慢改
     *
     * @deprecated
     */
    public static String i18nTextArray(String[] keys) {
        return InterProviderFactory.getProvider().getLocText(keys);
    }
}
