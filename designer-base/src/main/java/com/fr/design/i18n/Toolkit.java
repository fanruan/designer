package com.fr.design.i18n;

import com.fr.general.log.MessageFormatter;
import com.fr.locale.InterProviderFactory;

/**
 * 设计器国际化类，后面会不再依赖InterProviderFactory
 */
public class Toolkit {

    /**
     * 设计器国际化方法
     * @param key 国际化键
     * @return 国际化值
     */
    public static String i18nText(String key) {
        return InterProviderFactory.getProvider().getLocText(key);
    }

    /**
     * 带格式化内容的国际化方法
     * Toolkit.i18nText("Fine-Design_xxx", 1, 2)，假设Fine-Design_xxx的中文值为：我来计算{}+{}，输出结果为：我来计算1+2
     * @param key 格式化文本
     * @param args 格式化参数
     * @return 国际化值
     */
    public static String i18nText(String key, Object... args) {
        String format = InterProviderFactory.getProvider().getLocText(key);
        MessageFormatter.FormattingTuple tuple = MessageFormatter.arrayFormat(format, args);
        return tuple.getMessage();
    }

    /**
     * 太鸡儿多了，改不完，先加上慢慢改
     * @deprecated
     */
    public static String i18nTextArray(String[] keys) {
        return InterProviderFactory.getProvider().getLocText(keys);
    }

    /**
     * 太鸡儿多了，改不完，先加上慢慢改
     * @deprecated
     */
    public static String i18nTextArray(String[] keys, String[] delimiter) {
        return InterProviderFactory.getProvider().getLocText(keys, delimiter);
    }
}
