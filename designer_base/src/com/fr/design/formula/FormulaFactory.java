package com.fr.design.formula;

import com.fr.design.ExtraDesignClassManager;
import com.fr.design.fun.UIFormulaProcessor;

/**
 * @author richie
 * @date 2015-04-17
 * @since 8.0
 * 公式编辑器管理器
 */
public class FormulaFactory {

    public static UIFormula createFormulaPane() {
        return get().appearanceFormula();
    }

    public static UIFormula createFormulaPaneWhenReserveFormula() {
        return get().appearanceWhenReserveFormula();
    }

    private static UIFormulaProcessor get() {
        UIFormulaProcessor processor = ExtraDesignClassManager.getInstance().getUIFormulaProcessor();
        if (processor == null) {
            processor = new DefaultUIFormulaProcessor();
        }
        return processor;
    }
}