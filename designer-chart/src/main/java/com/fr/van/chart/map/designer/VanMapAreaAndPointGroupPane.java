package com.fr.van.chart.map.designer;



import javax.swing.JPanel;

/**
 * Created by Mitisky on 16/5/20.
 * 组合地图中各界面都要用到
 * 点地图和区域地图的各种配置
 */
public class VanMapAreaAndPointGroupPane extends VanChartGroupPane {
    private static final String AREA_MAP_STRING = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Region_Map");
    private static final String POINT_MAP_STRING = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_PointMap");
    public VanMapAreaAndPointGroupPane(JPanel areaPane, JPanel pointPane) {
        super(new String[]{AREA_MAP_STRING, POINT_MAP_STRING}, new JPanel[]{areaPane, pointPane});
    }
}
