package com.fr.van.chart.map.designer.data.contentpane.table;

import com.fr.base.BaseUtils;
import com.fr.base.Utils;
import com.fr.chartx.TwoTuple;
import com.fr.design.chartx.component.MapAreaMatchPane;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionListener;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.mainframe.chart.gui.data.table.AbstractTableDataContentPane;
import com.fr.plugin.chart.map.VanChartMapPlot;
import com.fr.plugin.chart.map.data.MapMatchResult;
import com.fr.plugin.chart.map.server.ChartGEOJSONHelper;

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
public abstract class VanMapTableDataContentPane extends AbstractTableDataContentPane {

    private VanChartMapPlot plot;

    //钻取地图有层级，默认-1代表无层级关系
    private int level = ChartGEOJSONHelper.DEFAULT_LEVEL;

    private MapMatchResult matchResult = new MapMatchResult();

    public void setLevel(int level) {
        this.level = level;
    }

    public void setPlot(VanChartMapPlot plot) {
        this.plot = plot;
    }

    public MapMatchResult getMatchResult() {
        return matchResult;
    }

    public void setMatchResult(MapMatchResult matchResult) {
        this.matchResult = matchResult;
    }

    public JPanel createAreaPanel(final UIComboBox areaBox) {
        JPanel areaPanel = new JPanel(new BorderLayout(4, 0));
        areaBox.setPreferredSize(new Dimension(70, 20));
        areaPanel.add(areaBox, BorderLayout.CENTER);
        UIButton uiButton = new UIButton(BaseUtils.readIcon("/com/fr/design/images/buttonicon/config.png"));
        uiButton.addActionListener(new ActionListener() {
            private TwoTuple<DefaultMutableTreeNode, Set<String>> treeNodeAndItems;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (treeNodeAndItems == null) {
                    treeNodeAndItems = ChartGEOJSONHelper.getTreeNodeAndItems(plot.getGeoUrl(), level, plot.getMapType());
                }
                final MapAreaMatchPane pane = new MapAreaMatchPane(treeNodeAndItems);

                String nameTable = getTableName();

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

    @Override
    public void refreshLevel(int level) {
        this.setLevel(level);
    }
}