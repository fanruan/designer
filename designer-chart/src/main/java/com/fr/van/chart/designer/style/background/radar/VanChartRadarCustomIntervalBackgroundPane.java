package com.fr.van.chart.designer.style.background.radar;

import com.fr.van.chart.designer.style.background.VanChartCustomIntervalBackgroundPane;

import javax.swing.JPanel;
import java.awt.Component;

/**
 * Created by Mitisky on 15/12/28.
 */
public class VanChartRadarCustomIntervalBackgroundPane extends VanChartCustomIntervalBackgroundPane {
    private static final long serialVersionUID = 1579343434752310123L;

    protected Component[][] getPaneComponents(JPanel axisPane, JPanel rangePane, JPanel stylePane) {
        return new Component[][]{
                new Component[]{rangePane,null},
                new Component[]{stylePane,null},
        };
    }
}