package com.fr.plugin.chart.designer.style.axis.component;

import com.fr.design.chart.ChartSwingUtils;
import com.fr.design.chart.axis.MinMaxValuePane;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.itextfield.UITextField;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by mengao on 2017/4/4.
 */
public class VanChartMinMaxValuePane extends MinMaxValuePane {

    @Override
    protected double[] getRowSize(double p) {
        return new double[]{p, p, p, p, p, p, p, p};
    }

    @Override
    protected void addComponentListener(Component[][] components) {
        for (int i = 0; i < components.length; i += 2) {
            addListener((UICheckBox) components[i][0]);
            ChartSwingUtils.addListener((UICheckBox) components[i][0], (UITextField) components[i + 1][0]);
        }
    }

    @Override
    protected Component[][] getPanelComponents() {
        return new Component[][]{
                new Component[]{minCheckBox},
                new Component[]{minValueField},
                new Component[]{maxCheckBox},
                new Component[]{maxValueField},
                new Component[]{isCustomMainUnitBox},
                new Component[]{mainUnitField},
                new Component[]{isCustomSecUnitBox},
                new Component[]{secUnitField},
        };
    }

}