package com.fr.plugin.chart.designer.style.axis.component;

import com.fr.base.BaseFormula;
import com.fr.plugin.chart.range.MinAndMaxValue;

import javax.swing.*;
import java.awt.*;

/**
 * 最大最小值设置，没有主次刻度单位设置
 */
public class MinMaxValuePaneWithOutTick extends VanChartMinMaxValuePane {
    private static final long serialVersionUID = -957414093602086034L;

    protected double[] getRowSize(double p) {
        return new double[]{p, p};
    }

    protected Component[][] getShowComponents(JPanel minPaneWithCheckBox, JPanel maxPaneWithCheckBox, JPanel mainPaneWithCheckBox, JPanel secPaneWithCheckBox) {
        return new Component[][] {
                {minPaneWithCheckBox},
                {maxPaneWithCheckBox},
        };
    }

    public void populate(MinAndMaxValue minAndMaxValue){
        minValueField.setText(String.valueOf(minAndMaxValue.getMinValue()));
        maxValueField.setText(String.valueOf(minAndMaxValue.getMaxValue()));
        minCheckBox.setSelected(minAndMaxValue.isCustomMin());
        maxCheckBox.setSelected(minAndMaxValue.isCustomMax());
        checkBoxUse();
    }

    public void update(MinAndMaxValue minAndMaxValue){
        if(minAndMaxValue == null) {
            return;
        }
        minAndMaxValue.setMinValue(BaseFormula.createFormulaBuilder().build(minValueField.getText()));
        minAndMaxValue.setMaxValue(BaseFormula.createFormulaBuilder().build(maxValueField.getText()));
        minAndMaxValue.setCustomMin(minCheckBox.isSelected());
        minAndMaxValue.setCustomMax(maxCheckBox.isSelected());
    }

}