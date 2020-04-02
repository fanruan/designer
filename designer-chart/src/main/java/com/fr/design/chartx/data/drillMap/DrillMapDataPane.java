package com.fr.design.chartx.data.drillMap;

import com.fr.chartx.data.DrillMapChartDataDefinition;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.chartx.fields.diff.AreaMapCellDataFieldsPane;
import com.fr.design.chartx.fields.diff.AreaMapDataSetFieldsPane;
import com.fr.design.chartx.single.SingleDataPane;
import com.fr.design.gui.frpane.UIComboBoxPane;
import com.fr.design.i18n.Toolkit;
import com.fr.plugin.chart.map.server.ChartGEOJSONHelper;
import com.fr.plugin.chart.vanchart.VanChart;

import java.util.ArrayList;
import java.util.List;
import java.awt.BorderLayout;

/**
 * Created by Mitisky on 16/6/20.
 * 钻取地图数据配置界面中 和钻取层级平级的数据界面
 */
public class DrillMapDataPane extends BasicBeanPane<DrillMapChartDataDefinition> {
    private UIComboBoxPane<DrillMapChartDataDefinition> dataDefinitionType;//数据定义方式:底层数据汇总/各层级分别指定

    private SingleDataPane bottomDataPane;//底层数据汇总方式定义钻取地图数据

    private EachLayerDataDefinitionPane eachLayerDataDefinitionPane;//各层级分别指定

    public DrillMapDataPane(VanChart vanChart) {
        AreaMapDataSetFieldsPane areaMapDataSetFieldsPane = new AreaMapDataSetFieldsPane();
        areaMapDataSetFieldsPane.setChart(vanChart);
        areaMapDataSetFieldsPane.setLevel(ChartGEOJSONHelper.BOTTOM_LEVEL);
        bottomDataPane = new SingleDataPane(areaMapDataSetFieldsPane, new AreaMapCellDataFieldsPane());
        eachLayerDataDefinitionPane = new EachLayerDataDefinitionPane(vanChart);

        dataDefinitionType = new UIComboBoxPane<DrillMapChartDataDefinition>() {
            @Override
            protected List<FurtherBasicBeanPane<? extends DrillMapChartDataDefinition>> initPaneList() {

                List<FurtherBasicBeanPane<? extends DrillMapChartDataDefinition>> paneList = new ArrayList<FurtherBasicBeanPane<? extends DrillMapChartDataDefinition>>();
                paneList.add(new BottomLayerDataDefinitionPane());
                paneList.add(eachLayerDataDefinitionPane);
                return paneList;
            }

            @Override
            protected String title4PopupWindow() {
                return null;
            }
        };

        this.setLayout(new BorderLayout());
        this.add(dataDefinitionType, BorderLayout.CENTER);
    }

    public void fireMapTypeChanged() {
        eachLayerDataDefinitionPane.fireMapTypeChanged();
    }

    @Override
    public void populateBean(DrillMapChartDataDefinition ob) {

        dataDefinitionType.setSelectedIndex(ob.isFromBottomData() ? 0 : 1);

        bottomDataPane.populateBean(ob.getBottomDataDefinition());
        eachLayerDataDefinitionPane.populateBean(ob);
    }

    /**
     * Update.
     */
    @Override
    public DrillMapChartDataDefinition updateBean() {
        return null;
    }

    @Override
    public void updateBean(DrillMapChartDataDefinition drillMapDefinition) {
        if (dataDefinitionType.getSelectedIndex() == 0) {
            drillMapDefinition.setFromBottomData(true);
            drillMapDefinition.setBottomDataDefinition(bottomDataPane.updateBean());
        } else {
            drillMapDefinition.setFromBottomData(false);
            eachLayerDataDefinitionPane.updateBean(drillMapDefinition);
        }
    }

    @Override
    protected String title4PopupWindow() {
        return Toolkit.i18nText("Fine-Design_Chart_Use_Data");
    }

    private class BottomLayerDataDefinitionPane extends FurtherBasicBeanPane<DrillMapChartDataDefinition> {

        private BottomLayerDataDefinitionPane() {
            this.setLayout(new BorderLayout());
            this.add(bottomDataPane, BorderLayout.CENTER);
        }

        @Override
        public boolean accept(Object ob) {
            return false;
        }

        @Override
        public void reset() {

        }

        @Override
        public void populateBean(DrillMapChartDataDefinition ob) {
        }

        @Override
        public DrillMapChartDataDefinition updateBean() {
            return null;
        }

        @Override
        public String title4PopupWindow() {
            return Toolkit.i18nText("Fine-Design_Chart_Bottom_Data_Sum");
        }
    }
}
