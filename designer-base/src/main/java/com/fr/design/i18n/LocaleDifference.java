package com.fr.design.i18n;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 国际化之间有不同表现的动作
 *
 * @author Hades
 * @date 2019/5/30
 */
public interface LocaleDifference {

    /**
     * 返回该国际化所有的url
     * @return url
     */
    Map<ActionType, String> getUrls();


    /**
     * 哪些国际化需要执行该动作
     * @param locales
     */
    void doAction(Locale ...locales);

    /**
     * 构建具体的动作
     * @param action
     * @return
     */
    LocaleDifference buildAction(Action action);

    /**
     * 返回设计器启动画面路径
     * @return 路径
     */
    String getSplashPath();

    /**
     * 对应的国际化
     * @return
     */
    Locale getLocale();

}
