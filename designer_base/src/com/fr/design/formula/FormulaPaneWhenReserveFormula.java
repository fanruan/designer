package com.fr.design.formula;

import com.fr.base.Formula;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.general.Inter;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * @author richie
 * @date 2015-04-17
 * @since 8.0
 */
public class FormulaPaneWhenReserveFormula extends FormulaPane {
    private UICheckBox reserveCheckBox4Result;
    private UICheckBox reserveCheckBox4Write;

    public FormulaPaneWhenReserveFormula() {
        super();
    }

    @Override
    protected void extendCheckBoxPane(JPanel checkBoxPane) {
        // peter:添加公式是否兼容Excel的属性
        reserveCheckBox4Result = new UICheckBox(Inter.getLocText("FormulaD-Reserve_when_export-edit_result"));
        reserveCheckBox4Result.setSelected(false);
        reserveCheckBox4Result.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent arg0) {
                if (reserveCheckBox4Result.isSelected()) {
                    reserveCheckBox4Write.setSelected(true);
                    reserveCheckBox4Write.setEnabled(false);
                } else {
                    reserveCheckBox4Write.setEnabled(true);
                }
            }
        });
        reserveCheckBox4Write = new UICheckBox(Inter.getLocText("FormulaD-Reserve_when_bs-write_or_analy"));
        reserveCheckBox4Write.setSelected(false);

        checkBoxPane.add(reserveCheckBox4Result, BorderLayout.CENTER);
        checkBoxPane.add(reserveCheckBox4Write, BorderLayout.SOUTH);
    }

    @Override
    public void populate(Formula formula, VariableResolver variableResolver) {
        super.populate(formula, variableResolver);
        reserveCheckBox4Result.setSelected(formula.isReserveInResult());
        reserveCheckBox4Write.setSelected(formula.isReserveOnWriteOrAnaly());
    }

    @Override
    public Formula update() {
        Formula formula =  super.update();
        if (formula != null) {
            formula.setReserveInResult(this.reserveCheckBox4Result.isSelected());
            formula.setReserveOnWriteOrAnaly(this.reserveCheckBox4Write.isSelected());
        }
        return formula;
    }
}