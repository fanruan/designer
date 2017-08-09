package com.fr.plugin.chart.designer.style.axis.component;

import com.fr.base.Formula;
import com.fr.plugin.chart.range.MinAndMaxValue;

import java.awt.*;

/**
 * 最大最小值设置，没有主次刻度单位设置
 */
public class MinMaxValuePaneWithOutTick extends VanChartMinMaxValuePane {
    private static final long serialVersionUID = -957414093602086034L;

    @Override
    protected Component[][] getPanelComponents() {
        return 	new Component[][]{
                new Component[]{minCheckBox},
                new Component[]{minValueField},
                new Component[]{maxCheckBox},
                new Component[]{maxValueField},
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
        minAndMaxValue.setMinValue(new Formula(minValueField.getText()));
        minAndMaxValue.setMaxValue(new Formula(maxValueField.getText()));
        minAndMaxValue.setCustomMin(minCheckBox.isSelected());
        minAndMaxValue.setCustomMax(maxCheckBox.isSelected());
    }

    private void checkBoxUse() {
        minValueField.setEnabled(minCheckBox.isSelected());
        maxValueField.setEnabled(maxCheckBox.isSelected());
    }

}