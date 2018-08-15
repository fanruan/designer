package com.fr.van.chart.drillmap.designer.data.comp;

import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.dialog.BasicScrollPane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.ComparatorUtils;

import com.fr.plugin.chart.drillmap.DrillMapHelper;
import com.fr.plugin.chart.drillmap.VanChartDrillMapPlot;
import com.fr.plugin.chart.map.server.CompatibleGeoJSONTreeHelper;
import com.fr.plugin.chart.type.MapType;
import com.fr.plugin.chart.type.ZoomLevel;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;
import com.fr.van.chart.map.designer.type.VanChartMapSourceChoosePane;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Mitisky on 16/6/20.
 * 钻取地图数据配置界面--钻取层级界面
 */
public class DrillMapLayerPane extends BasicScrollPane<ChartCollection> {
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

    private int depth;
    private MapType oldMapType;
    private String oldGeoUrl;

    @Override
    protected void layoutContentPane() {
        leftcontentPane = createContentPane();
        leftcontentPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        this.add(leftcontentPane);
    }

    @Override
    protected JPanel createContentPane() {

        if (mapDataTree == null) {
            mapDataTree = new MapDataTree(CompatibleGeoJSONTreeHelper.getRootNodeWithoutPara(oldGeoUrl));
            mapDataTree.setRootVisible(true);
        }

        JPanel mapDataTreePanel = new JPanel(new BorderLayout());
        mapDataTreePanel.add(mapDataTree);

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {f};
        double[] rowSize = {p, p};
        Component[][] components = new Component[][]{
                new Component[]{createTitlePane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Layer_Tree"), mapDataTreePanel)},
                new Component[]{createTitlePane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Layer_Detail"), createLayerDetailPane())}
        };

        JPanel contentPane = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(contentPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createTitlePane (String title, JPanel panel) {
        JPanel jPanel = TableLayout4VanChartHelper.createExpandablePaneWithTitle(title, panel);
        panel.setBorder(BorderFactory.createEmptyBorder(10,5,0,0));
        jPanel.setBorder(BorderFactory.createEmptyBorder(0,5,0,0));
        return jPanel;
    }

    private JPanel createLayerDetailPane() {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {f, p, p};
        double[] rowSize = new double[depth + 1];
        detailComps = new Component[depth + 1][3];
        rowSize[0] = p;
        detailComps[0] = new Component[]{
                new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Descriptor")),
                new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Zoom_Layer")),
                new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Layer_Map_Type"))
        };
        for (int i = 0; i < depth; i++) {
            rowSize[i + 1] = p;
            int d = i + 1;
            UILabel label = new UILabel(String.format("%s%d%s", com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Index_Article"), d, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Index_Layer")));
            UIComboBox level = new UIComboBox(VanChartMapSourceChoosePane.ZOOM_LEVELS);
            level.setEnabled(i != 0);
            UIComboBox type = new UIComboBox(TEMP.get(oldMapType));
            detailComps[i + 1] = new Component[]{label, level, type};
        }

        return TableLayoutHelper.createGapTableLayoutPane(detailComps, rowSize, columnSize, 10, 6);
    }

    /**
     * Populate.
     *
     * @param ob
     */
    @Override
    public void populateBean(ChartCollection ob) {
        VanChartDrillMapPlot drillMapPlot = DrillMapHelper.getDrillMapPlot(ob);

        if (drillMapPlot != null) {
            java.util.List<ZoomLevel> levelList = drillMapPlot.getLayerLevelList();
            java.util.List<MapType> mapTypeList = drillMapPlot.getLayerMapTypeList();

            if (detailComps == null || drillMapPlot.getMapType() != oldMapType || !ComparatorUtils.equals(drillMapPlot.getGeoUrl(), oldGeoUrl)) {
                oldMapType = drillMapPlot.getMapType();
                oldGeoUrl = drillMapPlot.getGeoUrl();

                DefaultMutableTreeNode root = CompatibleGeoJSONTreeHelper.getNodeByJSONPath(oldGeoUrl);
                if (root != null) {
                    mapDataTree.changeRootNode(root);
                    depth = root.getDepth() + 1;//根节点也算一层
                }

                this.remove(leftcontentPane);
                layoutContentPane();
            }

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

            for (int i = 0; i < depth; i++) {
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
    public ChartCollection updateBean() {
        return null;
    }

    @Override
    public void updateBean(ChartCollection ob) {
        VanChartDrillMapPlot drillMapPlot = DrillMapHelper.getDrillMapPlot(ob);
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
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Map_Drill_Level");
    }
}
