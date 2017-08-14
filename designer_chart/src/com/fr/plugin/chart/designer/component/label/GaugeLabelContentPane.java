package com.fr.plugin.chart.designer.component.label;

import com.fr.design.gui.ilable.UILabel;
import com.fr.general.Inter;
import com.fr.plugin.chart.designer.TableLayout4VanChartHelper;
import com.fr.plugin.chart.designer.component.VanChartLabelContentPane;
import com.fr.plugin.chart.designer.style.VanChartStylePane;

import javax.swing.*;
import java.awt.*;

/**
 * Created by mengao on 2017/8/13.
 */
public class GaugeLabelContentPane extends VanChartLabelContentPane {

    public GaugeLabelContentPane(VanChartStylePane parent, JPanel showOnPane) {
        super(parent, showOnPane);
    }

    protected JPanel getContentPane(Component[][] components, double[] column, double[] row, UILabel text) {
        text.setText(Inter.getLocText("Plugin-ChartF_Content"));
        return TableLayout4VanChartHelper.createGapTableLayoutPane(components, row, column);
    }
}
