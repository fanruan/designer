package com.fr.plugin.chart.designer.component.format;

import com.fr.general.Inter;
import com.fr.plugin.chart.designer.style.VanChartStylePane;

import javax.swing.*;

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
        return Inter.getLocText("FR-Chart-Area_Name");
    }
}
