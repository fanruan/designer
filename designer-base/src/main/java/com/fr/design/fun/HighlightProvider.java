package com.fr.design.fun;

import com.fr.design.condition.ConditionAttrSingleConditionPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.stable.fun.Level;
import com.fr.stable.fun.mark.Mutable;

/**
 * 条件属性界面设计接口
 */
public interface HighlightProvider extends Mutable{

    String MARK_STRING = "HighlightProvider";

    int CURRENT_LEVEL = 1;

    /**
     * 条件属性的实现类
     * @return 实现类
     */
    Class<?> classForHighlightAction();

    /**
     * 条件属性的界面
     * @param conditionAttributesPane 条件界面
     * @return 设置界面
     */
    ConditionAttrSingleConditionPane appearanceForCondition(ConditionAttributesPane conditionAttributesPane);
}