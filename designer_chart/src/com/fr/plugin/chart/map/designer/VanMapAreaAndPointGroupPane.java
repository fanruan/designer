package com.fr.plugin.chart.map.designer;

import com.fr.general.Inter;

import javax.swing.*;

/**
 * Created by Mitisky on 16/5/20.
 * 组合地图中各界面都要用到
 * 点地图和区域地图的各种配置
 */
public class VanMapAreaAndPointGroupPane extends VanChartGroupPane {
    private static final String AREA_MAP_STRING = Inter.getLocText("Plugin-ChartF_AreaMap");
    private static final String POINT_MAP_STRING = Inter.getLocText("Plugin-ChartF_PointMap");
    public VanMapAreaAndPointGroupPane(JPanel areaPane, JPanel pointPane) {
        super(new String[]{AREA_MAP_STRING, POINT_MAP_STRING}, new JPanel[]{areaPane, pointPane});
    }
}
