package com.fr.design.i18n;

import java.util.List;
import java.util.Map;

/**
 * 国际化之间有不同表现的动作
 */
public interface LocaleDifference {

    /**
     * 返回该国际化所有的url
     * @return url
     */
    Map<ActionType, String> getUrls();


    /**
     * 添加Action
     * @param list 列表
     * @param action 对应action
     */
     void addAction(List list, Object action);

    /**
     * 返回设计器启动画面路径
     * @return 路径
     */
    String getSplashPath();


}
