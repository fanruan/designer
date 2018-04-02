package com.fr.van.chart.designer.component.label;

import com.fr.general.Inter;
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
        return Inter.getLocText("Plugin-ChartF_Content");
    }

    protected JPanel getLabelContentPane(JPanel contentPane) {
        return contentPane;
    }

}
