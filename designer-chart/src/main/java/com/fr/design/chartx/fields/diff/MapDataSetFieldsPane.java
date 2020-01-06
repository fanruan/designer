package com.fr.design.chartx.fields.diff;

import com.fr.base.BaseUtils;
import com.fr.base.Utils;
import com.fr.chart.chartattr.Plot;
import com.fr.chartx.TwoTuple;
import com.fr.chartx.data.ChartDataDefinitionProvider;
import com.fr.chartx.data.DataSetDefinition;
import com.fr.chartx.data.DrillMapChartDataDefinition;
import com.fr.chartx.data.MapChartDataDefinition;
import com.fr.chartx.data.field.diff.ColumnFieldCollectionWithSeriesValue;
import com.fr.data.impl.NameTableData;
import com.fr.design.chartx.component.MapAreaMatchPane;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionListener;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.plugin.chart.drillmap.VanChartDrillMapPlot;
import com.fr.plugin.chart.map.MapMatchResult;
import com.fr.plugin.chart.map.VanChartMapPlot;
import com.fr.plugin.chart.map.server.ChartGEOJSONHelper;
import com.fr.plugin.chart.type.MapType;
import com.fr.plugin.chart.vanchart.VanChart;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.Set;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Bjorn
 * @version 10.0
 * Created by Bjorn on 2019-12-25
 */
public abstract class MapDataSetFieldsPane<T extends ColumnFieldCollectionWithSeriesValue> extends AbstractDataSetFieldsWithSeriesValuePane<T> {

    private VanChart chart;

    //钻取地图有层级，默认-1代表无层级关系
    private int level = ChartGEOJSONHelper.DEFAULT_LEVEL;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setChart(VanChart chart) {
        this.chart = chart;
    }

    public VanChart getChart() {
        return chart;
    }

    public JPanel createAreaPanel(final UIComboBox areaBox) {
        JPanel areaPanel = new JPanel(new BorderLayout(10, 0));
        areaBox.setPreferredSize(new Dimension(91, 20));
        areaPanel.add(areaBox, BorderLayout.WEST);
        UIButton uiButton = new UIButton(BaseUtils.readIcon("/com/fr/design/images/buttonicon/config.png"));
        uiButton.addActionListener(new ActionListener() {
            private TwoTuple<DefaultMutableTreeNode, Set<String>> treeNodeAndItems;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (treeNodeAndItems == null) {
                    treeNodeAndItems = ChartGEOJSONHelper.getTreeNodeAndItems(((VanChartMapPlot) chart.getPlot()).getGeoUrl(), level);
                }
                final MapAreaMatchPane pane = new MapAreaMatchPane(treeNodeAndItems);

                String nameTable = getNameTable(chart, MapDataSetFieldsPane.this.getMapType(), level);
                final MapMatchResult matchResult = getMatchResult(chart, level);

                pane.populateBean(matchResult, nameTable, Utils.objectToString(areaBox.getSelectedItem()));
                BasicDialog dialog = pane.showWindow(new JFrame());
                dialog.addDialogActionListener(new DialogActionListener() {
                    @Override
                    public void doOk() {
                        pane.updateBean(matchResult);
                    }

                    @Override
                    public void doCancel() {

                    }
                });
                dialog.setVisible(true);
            }
        });
        areaPanel.add(uiButton, BorderLayout.EAST);
        return areaPanel;
    }

    public abstract MapType getMapType();

    public String getNameTable(VanChart vanChart, MapType mapType, int level) {
        ChartDataDefinitionProvider chartDataDefinition = vanChart.getChartDataDefinition();
        if (chartDataDefinition == null) {
            return null;
        }
        DataSetDefinition dataSetDefinition;
        if (chartDataDefinition instanceof MapChartDataDefinition) {
            MapChartDataDefinition mapChartDataDefinition = (MapChartDataDefinition) chartDataDefinition;
            switch (mapType) {
                case AREA:
                    dataSetDefinition = (DataSetDefinition) mapChartDataDefinition.getAreaMapDataDefinition();
                    break;
                case POINT:
                    dataSetDefinition = (DataSetDefinition) mapChartDataDefinition.getPointMapDataDefinition();
                    break;
                default:
                    dataSetDefinition = (DataSetDefinition) mapChartDataDefinition.getLineMapDataDefinition();
                    break;
            }
        } else if (chartDataDefinition instanceof DrillMapChartDataDefinition) {
            DrillMapChartDataDefinition drillMapChartDataDefinition = (DrillMapChartDataDefinition) chartDataDefinition;
            if (drillMapChartDataDefinition.isFromBottomData()) {
                dataSetDefinition = (DataSetDefinition) drillMapChartDataDefinition.getBottomDataDefinition();
            } else {
                dataSetDefinition = (DataSetDefinition) drillMapChartDataDefinition.getEachLayerDataDefinitionList().get(level);
            }

        } else {
            dataSetDefinition = (DataSetDefinition) chartDataDefinition;
        }
        if (dataSetDefinition == null) {
            return null;
        }
        NameTableData nameTableData = dataSetDefinition.getNameTableData();
        if (nameTableData == null) {
            return null;
        }
        return nameTableData.getName();
    }

    public MapMatchResult getMatchResult(VanChart vanChart, int level) {
        Plot plot = vanChart.getPlot();
        if (plot == null) {
            return null;
        }
        if (level < 0 && plot instanceof VanChartMapPlot) {
            return ((VanChartMapPlot) plot).getMatchResult();
        } else if (plot instanceof VanChartDrillMapPlot) {
            return ((VanChartDrillMapPlot) plot).getMatchResultList().get(level);
        }
        return null;
    }
}
