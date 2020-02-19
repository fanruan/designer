package com.fr.van.chart.drillmap.designer.data.comp;

import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.MultiTabPane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.general.ComparatorUtils;
import com.fr.plugin.chart.drillmap.DrillMapHelper;
import com.fr.plugin.chart.drillmap.VanChartDrillMapPlot;
import com.fr.plugin.chart.drillmap.data.DrillMapDefinition;
import com.fr.plugin.chart.map.server.CompatibleGeoJSONTreeHelper;
import com.fr.plugin.chart.type.MapType;
import com.fr.van.chart.map.designer.data.MapDataPaneHelper;

import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mitisky on 16/6/20.
 * 各层级分别指定的界面
 */
public class EachLayerDataDefinitionPane extends MultiTabPane<ChartCollection> {
    private AttributeChangeListener listener;
    private ChartDataPane parent;
    private int depth;
    private String oldGeoUrl;
    private List<MapType> oldMapList;

    public EachLayerDataDefinitionPane(AttributeChangeListener listener, ChartDataPane parent) {
        this.listener = listener;
        this.parent = parent;
        cardLayout = new CardLayout();
    }

    private void initComponents() {
        super.relayoutWhenListChange();
    }

    /**
     * 当List中的界面变化时, 重新布局
     */
    public void relayoutWhenListChange() {
    }

    public List<MapType> getCurrentMapTypeList() {
        return oldMapList;
    }

    @Override
    protected List<BasicPane> initPaneList() {
        List<BasicPane> paneList = new ArrayList<BasicPane>();

        for (int i = 1; i < depth + 1; i++) {
            String tile = String.format("%s%d%s", com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Index_Article"), i, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Index_Layer"));

            SingleLayerDataDefinitionPane pane = new SingleLayerDataDefinitionPane(tile, this.listener, this.parent);
            pane.setSupportCellData(parent.isSupportCellData());
            paneList.add(pane);
        }

        return paneList;
    }

    @Override
    public void populateBean(ChartCollection ob) {
        VanChartDrillMapPlot drillMapPlot = DrillMapHelper.getDrillMapPlot(ob);
        if (drillMapPlot == null) {
            return;
        }
        if (!ComparatorUtils.equals(oldGeoUrl, drillMapPlot.getGeoUrl())) {
            oldGeoUrl = drillMapPlot.getGeoUrl();
            DefaultMutableTreeNode root = CompatibleGeoJSONTreeHelper.getNodeByJSONPath(oldGeoUrl);
            if (root == null) {
                return;
            }

            depth = root.getDepth() + 1;//根节点也算一层
            paneList = initPaneList();
            this.removeAll();
            initComponents();
        }

        oldMapList = drillMapPlot.getLayerMapTypeList();
        populatePaneList(ob);
    }

    private void populatePaneList(ChartCollection chartCollection) {
        for (int i = 0, len = paneList.size(); i < len; i++) {
            BasicPane basicPane = paneList.get(i);
            MapType mapType = oldMapList.get(i);
            if (basicPane instanceof SingleLayerDataDefinitionPane) {
                ChartCollection clone = MapDataPaneHelper.getLayerChartCollection(chartCollection, i, mapType);
                ((SingleLayerDataDefinitionPane) basicPane).populateBean(clone);
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
        DrillMapDefinition drillMapDefinition = MapDataPaneHelper.getDrillMapDefinition(ob);
        List<TopDefinitionProvider> eachLayerDataDefinitionList = new ArrayList<TopDefinitionProvider>();
        for (BasicPane basicPane : paneList) {
            if (basicPane instanceof SingleLayerDataDefinitionPane) {
                ChartCollection temp = new ChartCollection(new Chart());
                ((SingleLayerDataDefinitionPane) basicPane).updateBean(temp);
                eachLayerDataDefinitionList.add(temp.getSelectedChart().getFilterDefinition());
            }
        }
        drillMapDefinition.setEachLayerDataDefinitionList(eachLayerDataDefinitionList);
    }

    /**
     * 是否是指定类型
     *
     * @param ob 对象
     * @return 是否是指定类型
     */
    @Override
    public boolean accept(Object ob) {
        return false;
    }

    /**
     * title应该是一个属性，不只是对话框的标题时用到，与其他组件结合时，也会用得到
     *
     * @return 绥化狂标题
     */
    @Override
    public String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Each_Layer_Data_Special");
    }

    /**
     * 重置
     */
    @Override
    public void reset() {

    }


    /**
     * 设置是否关联单元格数据.
     *
     * @param supportCellData
     */
    public void setSupportCellData(boolean supportCellData) {
        if (paneList == null) {
            return;
        }
        for (BasicPane basicPane : paneList) {
            if (basicPane instanceof SingleLayerDataDefinitionPane) {
                ((SingleLayerDataDefinitionPane) basicPane).setSupportCellData(supportCellData);
            }
        }
    }
}
