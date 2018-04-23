package com.fr.design.formula;

import com.fr.design.fun.impl.AbstractUIFormulaProcessor;

/**
 * @author richie
 * @date 2015-04-17
 * @since 8.0
 */
public class DefaultUIFormulaProcessor extends AbstractUIFormulaProcessor {
    @Override
    public UIFormula appearanceFormula() {
        return new FormulaPane();
    }

    @Override
    public UIFormula appearanceWhenReserveFormula() {
        return new FormulaPaneWhenReserveFormula();
    }
}