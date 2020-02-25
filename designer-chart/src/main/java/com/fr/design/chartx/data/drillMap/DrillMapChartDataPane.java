package com.fr.design.chartx.data.drillMap;

import com.fr.chartx.data.DrillMapChartDataDefinition;
import com.fr.design.chartx.AbstractChartDataPane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.i18n.Toolkit;
import com.fr.plugin.chart.drillmap.VanChartDrillMapPlot;
import com.fr.van.chart.map.designer.VanChartGroupPane;

import javax.swing.JPanel;

/**
 * @author shine
 * @version 10.0
 * Created by shine on 2019/11/13
 */
public class DrillMapChartDataPane extends AbstractChartDataPane<DrillMapChartDataDefinition> {
    private DrillMapLayerPane layerPane;
    private DrillMapDataPane dataPane;

    public DrillMapChartDataPane(AttributeChangeListener listener) {
        super(listener);
    }

    private VanChartDrillMapPlot getDrillMapPlot() {
        if (getVanChart() != null) {
            return getVanChart().getPlot();
        }
        return null;
    }

    @Override
    protected JPanel createContentPane() {
        VanChartDrillMapPlot drillMapPlot = getDrillMapPlot();
        if (drillMapPlot == null) {
            return new JPanel();
        }

        layerPane = new DrillMapLayerPane(drillMapPlot);
        dataPane = new DrillMapDataPane(getVanChart());
        return new VanChartGroupPane(new String[]{Toolkit.i18nText("Fine-Design_Chart_Map_Drill_Level"), Toolkit.i18nText("Fine-Design_Chart_Use_Data")},
                new JPanel[]{layerPane, dataPane}) {
            @Override
            protected void tabChanged(int index) {
                if (index == 0) {
                    return;
                }
                dataPane.fireMapTypeChanged();
            }
        };
    }


    @Override
    protected void populate(DrillMapChartDataDefinition drillMapChartDataDefinition) {
        if (drillMapChartDataDefinition == null) {
            return;
        }
        VanChartDrillMapPlot drillMapPlot = getDrillMapPlot();

        layerPane.populateBean(drillMapPlot);
        dataPane.populateBean(drillMapChartDataDefinition);
    }

    @Override
    protected DrillMapChartDataDefinition update() {
        VanChartDrillMapPlot drillMapPlot = getDrillMapPlot();

        layerPane.updateBean(drillMapPlot);

        DrillMapChartDataDefinition definition = new DrillMapChartDataDefinition();
        dataPane.updateBean(definition);

        return definition;
    }
}
