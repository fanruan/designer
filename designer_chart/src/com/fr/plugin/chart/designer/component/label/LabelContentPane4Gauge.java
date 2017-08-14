package com.fr.plugin.chart.designer.component.label;

import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.general.Inter;
import com.fr.plugin.chart.designer.TableLayout4VanChartHelper;
import com.fr.plugin.chart.designer.component.VanChartLabelContentPane;
import com.fr.plugin.chart.designer.style.VanChartStylePane;

import javax.swing.*;
import java.awt.*;

/**
 * Created by mengao on 2017/8/13.
 */
public class LabelContentPane4Gauge extends VanChartLabelContentPane {

    public LabelContentPane4Gauge(VanChartStylePane parent, JPanel showOnPane) {
        super(parent, showOnPane);
    }

    protected JPanel getContentPane (){
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] column = {p, f};
        double[] row = {p,p,p};
        Component[][] components = new Component[][]{
                new Component[]{null,null},
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_Content")),content},
                new Component[]{null,getCenterPane()},

        };

        return TableLayout4VanChartHelper.createGapTableLayoutPane(components,row,column);
    }
}
