package com.fr.plugin.chart.designer.style.axis.component;

import java.awt.*;

/**
 * 最大值、最小值、主要刻度单位。没有次要刻度单位
 */
public class MinMaxValuePaneWithOutSecTick extends VanChartMinMaxValuePane {

    private static final long serialVersionUID = -887359523503645758L;

    protected Component[][] getPanelComponents() {
        return 	new Component[][]{
                new Component[]{minCheckBox},
                new Component[]{minValueField},
                new Component[]{maxCheckBox},
                new Component[]{maxValueField},
                new Component[]{isCustomMainUnitBox},
                new Component[]{mainUnitField},
        };
    }

}