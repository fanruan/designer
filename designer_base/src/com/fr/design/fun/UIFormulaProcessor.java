package com.fr.design.fun;

import com.fr.design.formula.UIFormula;
import com.fr.stable.fun.Level;

/**
 * @author richie
 * @date 2015-04-17
 * @since 8.0
 * 公式编辑器界面处理接口
 */
public interface UIFormulaProcessor extends Level{
    String MARK_STRING = "UIFormulaProcessor";

    int CURRENT_LEVEL = 1;


    /**
     * 普通的公式编辑器界面类
     * @return 公式编辑器界面类
     */
    UIFormula appearanceFormula();

    /**
     * 当需要显示“保留公式”项时的公式编辑器界面类
     * @return 公式编辑器界面类
     */
    UIFormula appearanceWhenReserveFormula();
}