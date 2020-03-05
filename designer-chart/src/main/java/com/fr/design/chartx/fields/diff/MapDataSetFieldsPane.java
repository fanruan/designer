package com.fr.design.chartx.fields.diff;

import com.fr.base.BaseUtils;
import com.fr.base.Utils;
import com.fr.chartx.TwoTuple;
import com.fr.chartx.data.field.diff.ColumnFieldCollectionWithSeriesValue;
import com.fr.design.chartx.component.MapAreaMatchPane;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionListener;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.plugin.chart.map.data.MapMatchResult;
import com.fr.plugin.chart.map.VanChartMapPlot;
import com.fr.plugin.chart.map.server.ChartGEOJSONHelper;
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
                VanChartMapPlot plot = chart.getPlot();
                if (treeNodeAndItems == null) {
                    treeNodeAndItems = ChartGEOJSONHelper.getTreeNodeAndItems(plot.getGeoUrl(), level);
                }
                final MapAreaMatchPane pane = new MapAreaMatchPane(treeNodeAndItems);

                String nameTable = getTableName();
                final MapMatchResult matchResult = plot.getMatchResult(level);

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
}
