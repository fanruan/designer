package com.fr.design.mainframe.chart.gui.style.axis;

import com.fr.chart.chartattr.Axis;
import com.fr.chart.chartattr.ValueAxis;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;

/**
 * Created by eason on 14-10-23.
 */
public class ChartSecondValueNoFormulaPane extends ChartValueNoFormulaPane{
    private UICheckBox isAlignZeroValue;

    protected JPanel aliagnZero4Second() {// 添加 0值对齐
        JPanel pane = new JPanel();
        pane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        pane.add(isAlignZeroValue = new UICheckBox(Inter.getLocText("Chart_AxisAlignZeroValueLine"), false));
        return pane;
    }

    public void populateBean(Axis axis) {
        if (axis instanceof ValueAxis) {
            super.populateBean(axis);
            isAlignZeroValue.setSelected(((ValueAxis)axis).isAlignZeroValue());
        }
    }

    public void updateBean(Axis axis) {
        if(axis instanceof ValueAxis) {
            ValueAxis valueAxis = (ValueAxis)axis;
            super.updateBean(valueAxis);
            valueAxis.setAlignZeroValue(isAlignZeroValue.isSelected());
        }
    }

    /**
     * 界面标题 第二值轴
     * @return 第二值轴
     */
    public String title4PopupWindow() {
        return Inter.getLocText(new String[]{"Second", "Chart_F_Radar_Axis"});
    }
}