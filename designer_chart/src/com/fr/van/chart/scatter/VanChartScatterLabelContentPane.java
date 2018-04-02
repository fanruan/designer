package com.fr.van.chart.scatter;

import com.fr.van.chart.designer.component.VanChartHtmlLabelPane;
import com.fr.van.chart.designer.style.VanChartStylePane;

import javax.swing.JPanel;

/**
 * 散点图标签界面
 */
public class  VanChartScatterLabelContentPane extends VanChartScatterTooltipContentPane{
    private static final long serialVersionUID = 5595016643808487922L;

    public VanChartScatterLabelContentPane(VanChartStylePane parent, JPanel showOnPane) {
        super(parent, showOnPane);
    }

    @Override
    protected VanChartHtmlLabelPane createHtmlLabelPane() {
        return new VanChartHtmlLabelPane();
    }

}
