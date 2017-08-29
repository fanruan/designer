package com.fr.plugin.chart.drillmap.designer.other;

import com.fr.chart.base.DrillMapTools;
import com.fr.chart.chartattr.Chart;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.style.ChartTextAttrPane;
import com.fr.general.Inter;
import com.fr.plugin.chart.attr.plot.VanChartPlot;
import com.fr.plugin.chart.designer.TableLayout4VanChartHelper;
import com.fr.plugin.chart.designer.component.background.VanChartBackgroundPaneWithOutImageAndShadow;
import com.fr.plugin.chart.designer.other.VanChartInteractivePaneWithMapZoom;
import com.fr.plugin.chart.drillmap.VanChartDrillMapPlot;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * Created by Mitisky on 16/6/29.
 */
public class VanChartDrillMapInteractivePane extends VanChartInteractivePaneWithMapZoom {
    private UIButtonGroup openOrClose;
    private ChartTextAttrPane textAttrPane;
    private VanChartBackgroundPaneWithOutImageAndShadow backgroundPane;
    private VanChartBackgroundPaneWithOutImageAndShadow selectBackgroundPane;
    private VanChartCatalogHyperLinkPane catalogSuperLink;
    private JPanel drillPane;

    @Override
    protected JPanel getInteractivePane(VanChartPlot plot){
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {p, f};
        double[] rowSize = {p, p, p, p, p, p, p, p, p, p, p};
        Component[][] components = new Component[][]{
                new Component[]{createToolBarPane(new double[]{p, p, p}, columnSize),null},
                new Component[]{createAnimationPane(),null},
                new Component[]{createZoomPane(new double[]{p,p,p}, columnSize, plot),null},
                new Component[]{createDrillToolsPane(), null},
                new Component[]{createAutoRefreshPane(plot),null},
                new Component[]{createHyperlinkPane(),null}
        };

        return TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize);
    }

    private JPanel createDrillToolsPane() {
        openOrClose = new UIButtonGroup(new String[]{Inter.getLocText("Plugin-ChartF_Open"), Inter.getLocText("Plugin-ChartF_Close")});
        textAttrPane = new ChartTextAttrPane(){
            protected Component[][] getComponents(JPanel buttonPane) {
                return new Component[][]{
                        new Component[]{fontNameComboBox, null},
                        new Component[]{buttonPane, null}
                };
            }
        };
        backgroundPane = new VanChartBackgroundPane4DrillMap();
        selectBackgroundPane = new VanChartBackgroundPane4DrillMap();
        catalogSuperLink = new VanChartCatalogHyperLinkPane();

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {f};
        double[] rowSize = {p,p,p,p,p,p};
        Component[][] components = new Component[][]{
                new Component[]{null},
                new Component[]{createTitlePane(Inter.getLocText("Plugin-Chart_Character"), textAttrPane)},
                new Component[]{createTitlePane(Inter.getLocText("Plugin-ChartF_Background"), backgroundPane)},
                new Component[]{createTitlePane(Inter.getLocText("Plugin-ChartF_Select_Color"), selectBackgroundPane)},
                new Component[]{catalogSuperLink}
        };
        drillPane = TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(openOrClose, BorderLayout.NORTH);
        panel.add(drillPane, BorderLayout.CENTER);

        openOrClose.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                checkEnable();
            }
        });

        JPanel panel1 = TableLayout4VanChartHelper.createExpandablePaneWithTitle(Inter.getLocText("Plugin-ChartF_Drill_Dir"), panel);
        panel.setBorder(BorderFactory.createEmptyBorder(10,5,0,0));
        return panel1;
    }

    private JPanel createTitlePane(String title, Component component) {
        return TableLayout4VanChartHelper.createGapTableLayoutPane(title, component);
    }

    private void checkEnable() {
        drillPane.setVisible(openOrClose.getSelectedIndex() == 0);
    }

    @Override
    public void populateBean(Chart chart) {
        super.populateBean(chart);

        if (chart == null || chart.getPlot() == null) {
            return;
        }
        this.chart = chart;
        VanChartPlot plot = (VanChartPlot)chart.getPlot();
        if(plot instanceof VanChartDrillMapPlot){
            DrillMapTools drillMapTools = ((VanChartDrillMapPlot) plot).getDrillMapTools();
            openOrClose.setSelectedIndex(drillMapTools.isEnable() ? 0 : 1);
            textAttrPane.populate(drillMapTools.getTextAttr());
            backgroundPane.populate(drillMapTools.getBackgroundInfo());
            selectBackgroundPane.populate(drillMapTools.getSelectBackgroundInfo());
            catalogSuperLink.populate(plot);
        }

        checkEnable();
    }

    @Override
    public void updateBean(Chart chart) {
        super.updateBean(chart);

        if (chart == null || chart.getPlot() == null) {
            return;
        }

        VanChartPlot plot = (VanChartPlot) chart.getPlot();

        if(plot instanceof VanChartDrillMapPlot){
            DrillMapTools drillMapTools = ((VanChartDrillMapPlot) plot).getDrillMapTools();
            drillMapTools.setEnable(openOrClose.getSelectedIndex() == 0);
            drillMapTools.setTextAttr(textAttrPane.update());
            backgroundPane.update(drillMapTools.getBackgroundInfo());
            selectBackgroundPane.update(drillMapTools.getSelectBackgroundInfo());
            catalogSuperLink.update(plot);
        }
    }

    public class VanChartBackgroundPane4DrillMap extends VanChartBackgroundPaneWithOutImageAndShadow{
        @Override
        protected Component[][] getPaneComponents() {
            return  new Component[][]{
                    new Component[]{null},
                    new Component[]{typeComboBox},
                    new Component[]{centerPane},
                    new Component[]{transparent},
            };
        }
    }
}