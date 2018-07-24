package com.fr.van.chart.designer.component.label;


import com.fr.van.chart.designer.component.VanChartLabelContentPane;
import com.fr.van.chart.designer.style.VanChartStylePane;

import javax.swing.JPanel;

/**
 * Created by mengao on 2017/8/13.
 */
public class GaugeLabelContentPane extends VanChartLabelContentPane {

    public GaugeLabelContentPane(VanChartStylePane parent, JPanel showOnPane) {
        super(parent, showOnPane);
    }

    protected String getLabelContentTitle() {
        return com.fr.design.i18n.Toolkit.i18nText("Plugin-ChartF_Content");
    }

    protected JPanel getLabelContentPane(JPanel contentPane) {
        return contentPane;
    }

}
