package com.fr.design.chartx.data.drillMap;

import com.fr.chartx.TwoTuple;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.i18n.Toolkit;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.plugin.chart.drillmap.VanChartDrillMapPlot;
import com.fr.plugin.chart.map.MapMatchResult;
import com.fr.plugin.chart.map.server.CompatibleGeoJSONTreeHelper;
import com.fr.plugin.chart.type.MapType;
import com.fr.plugin.chart.type.ZoomLevel;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;
import com.fr.van.chart.drillmap.designer.data.comp.MapDataTree;
import com.fr.van.chart.map.designer.type.VanChartMapSourceChoosePane;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.awt.BorderLayout;
import java.awt.Component;

/**
 * Created by Mitisky on 16/6/20.
 * 钻取地图数据配置界面--钻取层级界面
 */
public class DrillMapLayerPane extends BasicBeanPane<VanChartDrillMapPlot> {
    private static final String[] CUSTOM_MAP_TYPES = new String[]{MapType.AREA.getLocaleString(), MapType.POINT.getLocaleString()};
    private static final String[] AREA_MAP_TYPES = new String[]{MapType.AREA.getLocaleString()};
    private static final String[] POINT_MAP_TYPES = new String[]{MapType.POINT.getLocaleString()};

    private static final java.util.Map<MapType, String[]> TEMP = new HashMap<MapType, String[]>();

    static {
        TEMP.put(MapType.AREA, AREA_MAP_TYPES);
        TEMP.put(MapType.POINT, POINT_MAP_TYPES);
        TEMP.put(MapType.CUSTOM, CUSTOM_MAP_TYPES);
    }

    private MapDataTree mapDataTree;
    private Component[][] detailComps;


    public DrillMapLayerPane(VanChartDrillMapPlot drillMapPlot) {

        initMapTypeAndZoom(drillMapPlot);

        JPanel contentPane = createContentPane(drillMapPlot);

        this.setLayout(new BorderLayout());
        this.add(contentPane, BorderLayout.CENTER);
    }

    private void initMapTypeAndZoom(VanChartDrillMapPlot drillMapPlot) {

        if (drillMapPlot != null) {

            int depth = getRootAndDepth(drillMapPlot).getSecond();

            java.util.List<ZoomLevel> levelList = drillMapPlot.getLayerLevelList();
            java.util.List<MapType> mapTypeList = drillMapPlot.getLayerMapTypeList();
            List<MapMatchResult> matchResultList = drillMapPlot.getMatchResultList();

            //根据层级初始属性,一切以json那边读到的层级为准
            int levelSize = levelList.size();
            for (int i = levelSize; i < depth; i++) {
                levelList.add(ZoomLevel.AUTO);
            }
            MapType mapType = drillMapPlot.getMapType() == MapType.POINT ? MapType.POINT : MapType.AREA;
            int typeSize = mapTypeList.size();
            for (int j = typeSize; j < depth; j++) {
                mapTypeList.add(mapType);
            }

            int matchSize = matchResultList.size();
            for (int k = matchSize; k < depth; k++) {
                matchResultList.add(new MapMatchResult());
            }
        }
    }

    public static TwoTuple<DefaultMutableTreeNode, Integer> getRootAndDepth(VanChartDrillMapPlot drillMapPlot) {
        int depth = 1;

        DefaultMutableTreeNode root = CompatibleGeoJSONTreeHelper.getNodeByJSONPath(drillMapPlot.getGeoUrl());
        if (root != null) {
            depth = root.getDepth() + 1;//根节点也算一层
        }

        return new TwoTuple<>(root, depth);
    }

    private JPanel createContentPane(VanChartDrillMapPlot drillMapPlot) {
        if (drillMapPlot == null) {
            return new JPanel();
        }

        DefaultMutableTreeNode root = CompatibleGeoJSONTreeHelper.getNodeByJSONPath(drillMapPlot.getGeoUrl());

        if (mapDataTree == null) {
            mapDataTree = new MapDataTree(root);
            mapDataTree.setRootVisible(true);
        } else {
            mapDataTree.changeRootNode(root);
        }


        JPanel mapDataTreePanel = new JPanel(new BorderLayout());
        mapDataTreePanel.add(mapDataTree);

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {f};
        double[] rowSize = {p, p};
        Component[][] components = new Component[][]{
                new Component[]{createTitlePane(Toolkit.i18nText("Fine-Design_Chart_Layer_Tree"), mapDataTreePanel)},
                new Component[]{createTitlePane(Toolkit.i18nText("Fine-Design_Chart_Layer_Detail"), createLayerDetailPane(drillMapPlot))}
        };

        JPanel contentPane = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(contentPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createTitlePane(String title, JPanel panel) {
        JPanel jPanel = TableLayout4VanChartHelper.createExpandablePaneWithTitle(title, panel);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 5, 0, 0));
        jPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        return jPanel;
    }

    private JPanel createLayerDetailPane(VanChartDrillMapPlot drillMapPlot) {
        int depth = getRootAndDepth(drillMapPlot).getSecond();
        String[] items = TEMP.get(drillMapPlot.getMapType());

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {f, p, p};
        double[] rowSize = new double[depth + 1];
        detailComps = new Component[depth + 1][3];
        rowSize[0] = p;
        detailComps[0] = new Component[]{
                new UILabel(Toolkit.i18nText("Fine-Design_Chart_Descriptor")),
                new UILabel(Toolkit.i18nText("Fine-Design_Chart_Zoom_Layer")),
                new UILabel(Toolkit.i18nText("Fine-Design_Chart_Layer_Map_Type"))
        };
        for (int i = 0; i < depth; i++) {
            rowSize[i + 1] = p;
            int d = i + 1;
            UILabel label = new UILabel(String.format("%s%d%s", Toolkit.i18nText("Fine-Design_Chart_Index_Article"), d, Toolkit.i18nText("Fine-Design_Chart_Index_Layer")));
            UIComboBox level = new UIComboBox(VanChartMapSourceChoosePane.ZOOM_LEVELS);
            level.setEnabled(i != 0);
            UIComboBox type = new UIComboBox(items);
            detailComps[i + 1] = new Component[]{label, level, type};
        }

        return TableLayoutHelper.createGapTableLayoutPane(detailComps, rowSize, columnSize, 10, 6);
    }

    @Override
    public void populateBean(VanChartDrillMapPlot drillMapPlot) {

        if (drillMapPlot != null) {
            java.util.List<ZoomLevel> levelList = drillMapPlot.getLayerLevelList();
            java.util.List<MapType> mapTypeList = drillMapPlot.getLayerMapTypeList();

            for (int i = 0; i < levelList.size(); i++) {
                Component[] components = detailComps[i + 1];
                if (components != null) {
                    UIComboBox level = (UIComboBox) components[1];
                    UIComboBox type = (UIComboBox) components[2];
                    if (level != null) {
                        level.setSelectedItem(levelList.get(i));
                    }
                    if (type != null) {
                        type.setSelectedItem(mapTypeList.get(i).getLocaleString());
                    }
                }
            }
        }
    }

    /**
     * Update.
     */
    @Override
    public VanChartDrillMapPlot updateBean() {
        return null;
    }

    @Override
    public void updateBean(VanChartDrillMapPlot drillMapPlot) {
        if (drillMapPlot != null && detailComps != null) {
            java.util.List<ZoomLevel> levelList = new ArrayList<ZoomLevel>();
            java.util.List<MapType> mapTypeList = new ArrayList<MapType>();
            for (Component[] com : detailComps) {
                if (com[1] instanceof UIComboBox && com[2] instanceof UIComboBox) {
                    UIComboBox level = (UIComboBox) com[1];
                    UIComboBox type = (UIComboBox) com[2];
                    levelList.add((ZoomLevel) level.getSelectedItem());
                    if (type.getSelectedItem() != null) {
                        mapTypeList.add(MapType.parseLocale(type.getSelectedItem().toString()));
                    }
                }
            }
            drillMapPlot.setLayerLevelList(levelList);
            drillMapPlot.setLayerMapTypeList(mapTypeList);
        }
    }

    @Override
    protected String title4PopupWindow() {
        return Toolkit.i18nText("Fine-Design_Chart_Map_Drill_Level");
    }
}
