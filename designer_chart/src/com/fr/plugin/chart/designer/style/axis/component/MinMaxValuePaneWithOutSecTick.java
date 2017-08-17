package com.fr.plugin.chart.designer.style.axis.component;

import javax.swing.*;
import java.awt.*;

/**
 * 最大值、最小值、主要刻度单位。没有次要刻度单位
 */
public class MinMaxValuePaneWithOutSecTick extends VanChartMinMaxValuePane {

    private static final long serialVersionUID = -887359523503645758L;

    protected double[] getRowSize(double p) {
        return new double[]{p, p, p};
    }

    protected Component[][] getShowComponents(JPanel minPaneWithCheckBox, JPanel maxPaneWithCheckBox, JPanel mainPaneWithCheckBox, JPanel secPaneWithCheckBox) {
        return new Component[][] {
                {minPaneWithCheckBox},
                {maxPaneWithCheckBox},
                {mainPaneWithCheckBox},
        };
    }

}