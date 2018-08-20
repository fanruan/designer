package com.fr.van.chart.designer.component.format;


import com.fr.van.chart.designer.style.VanChartStylePane;

import javax.swing.JPanel;

/**
 * Created by Mitisky on 16/5/20.
 * 地图的标签区域名只是改了之前的分类名的名字,读写什么的都一样
 */
public class MapAreaNameFormatPaneWithCheckBox extends CategoryNameFormatPaneWithCheckBox {
    public MapAreaNameFormatPaneWithCheckBox(VanChartStylePane parent, JPanel showOnPane) {
        super(parent, showOnPane);
    }

    @Override
    protected String getCheckBoxText() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Area_Name");
    }
}
