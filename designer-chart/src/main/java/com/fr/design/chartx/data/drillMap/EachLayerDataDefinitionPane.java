package com.fr.design.chartx.data.drillMap;

import com.fr.chartx.data.AbstractDataDefinition;
import com.fr.chartx.data.DrillMapChartDataDefinition;
import com.fr.design.chartx.fields.diff.AreaMapCellDataFieldsPane;
import com.fr.design.chartx.fields.diff.AreaMapDataSetFieldsPane;
import com.fr.design.chartx.fields.diff.PointMapCellDataFieldsPane;
import com.fr.design.chartx.fields.diff.PointMapDataSetFieldsPane;
import com.fr.design.chartx.single.SingleDataPane;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.MultiTabPane;
import com.fr.design.i18n.Toolkit;
import com.fr.general.ComparatorUtils;
import com.fr.plugin.chart.drillmap.VanChartDrillMapPlot;
import com.fr.plugin.chart.type.MapType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mitisky on 16/6/20.
 * 各层级分别指定的界面
 */
public class EachLayerDataDefinitionPane extends MultiTabPane<DrillMapChartDataDefinition> {

    private List<MapType> oldTypeList;
    private VanChartDrillMapPlot plot;

    public EachLayerDataDefinitionPane(VanChartDrillMapPlot drillMapPlot) {
        this.plot = drillMapPlot;
        initComps();
    }

    private void initComps() {
        paneList = initPaneList(this.plot);
        super.relayoutWhenListChange();
    }

    public void fireMapTypeChanged() {
        if (!ComparatorUtils.equals(plot.getLayerMapTypeList(), oldTypeList)) {
            initComps();
        }
    }

    protected List<BasicPane> initPaneList(VanChartDrillMapPlot drillMapPlot) {
        List<BasicPane> paneList = new ArrayList<BasicPane>();

        oldTypeList = drillMapPlot.getLayerMapTypeList();
        int depth = DrillMapLayerPane.getRootAndDepth(drillMapPlot).getSecond();

        for (int i = 0; i < depth; i++) {
            final String title = String.format("%s%d%s", Toolkit.i18nText("Fine-Design_Chart_Index_Article"), i, Toolkit.i18nText("Fine-Design_Chart_Index_Layer"));
            MapType mapType = oldTypeList.get(i);

            SingleDataPane pane = mapType == MapType.AREA ? new SingleDataPane(new AreaMapDataSetFieldsPane(), new AreaMapCellDataFieldsPane()) {
                @Override
                protected String title4PopupWindow() {
                    return title;
                }
            } : new SingleDataPane(new PointMapDataSetFieldsPane(), new PointMapCellDataFieldsPane()) {
                @Override
                protected String title4PopupWindow() {
                    return title;
                }
            };

            paneList.add(pane);
        }

        return paneList;
    }

    @Override
    public void relayoutWhenListChange() {
    }

    @Override
    protected List<BasicPane> initPaneList() {
        return new ArrayList<BasicPane>();
    }

    @Override
    public void populateBean(DrillMapChartDataDefinition drillMapChartDataDefinition) {
        List<AbstractDataDefinition> eachLayerDataDefinitionList = drillMapChartDataDefinition.getEachLayerDataDefinitionList();

        if (eachLayerDataDefinitionList == null) {
            return;
        }

        for (int i = 0, len = Math.min(paneList.size(), eachLayerDataDefinitionList.size()); i < len; i++) {
            BasicPane basicPane = paneList.get(i);
            if (basicPane instanceof SingleDataPane) {
                ((SingleDataPane) basicPane).populateBean(eachLayerDataDefinitionList.get(i));
            }
        }
    }

    @Override
    public DrillMapChartDataDefinition updateBean() {
        return null;
    }

    @Override
    public void updateBean(DrillMapChartDataDefinition drillMapDefinition) {
        List<AbstractDataDefinition> eachLayerDataDefinitionList = new ArrayList<AbstractDataDefinition>();
        for (BasicPane basicPane : paneList) {
            if (basicPane instanceof SingleDataPane) {
                eachLayerDataDefinitionList.add(((SingleDataPane) basicPane).updateBean());
            }
        }
        drillMapDefinition.setEachLayerDataDefinitionList(eachLayerDataDefinitionList);
    }

    @Override
    public boolean accept(Object ob) {
        return false;
    }

    @Override
    public String title4PopupWindow() {
        return Toolkit.i18nText("Fine-Design_Chart_Each_Layer_Data_Special");
    }

    @Override
    public void reset() {
    }

}
