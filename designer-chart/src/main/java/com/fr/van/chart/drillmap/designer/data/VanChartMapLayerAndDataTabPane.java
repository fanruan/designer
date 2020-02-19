package com.fr.van.chart.drillmap.designer.data;

import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.MultiTabPane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.general.ComparatorUtils;
import com.fr.van.chart.drillmap.designer.data.comp.DrillMapDataPane;
import com.fr.van.chart.drillmap.designer.data.comp.DrillMapLayerPane;
import com.fr.van.chart.map.designer.data.MapDataPaneHelper;

import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mitisky on 16/6/20.
 * 钻取层级和数据界面切换的界面
 */
public class VanChartMapLayerAndDataTabPane extends MultiTabPane<ChartCollection> {

    private DrillMapLayerPane layerPane;
    private DrillMapDataPane dataPane;

    private ChartCollection chartCollection;

    public VanChartMapLayerAndDataTabPane(AttributeChangeListener listener, ChartDataPane parent) {
        cardLayout = new CardLayout();
        layerPane = new DrillMapLayerPane();
        dataPane = new DrillMapDataPane(listener, parent);
        paneList = initPaneList();
        initComponents();
    }

    private void initComponents() {
        super.relayoutWhenListChange();
    }

    protected void tabChanged() {
        if (getSelectedIndex() == 0) {
            return;
        }
        if (chartCollection == null) {
            return;
        }
        if (!ComparatorUtils.equals(MapDataPaneHelper.getDrillMapLayerMapTypeList(chartCollection), dataPane.getCurrentMapTypeList())) {
            dataPane.populateBean(chartCollection);
        }
    }

    /**
     * 当List中的界面变化时, 重新布局
     */
    public void relayoutWhenListChange() {
    }

    @Override
    protected List<BasicPane> initPaneList() {
        List<BasicPane> paneList = new ArrayList<BasicPane>();

        if (layerPane != null) {
            paneList.add(layerPane);
        }
        if (dataPane != null) {
            paneList.add(dataPane);
        }

        return paneList;
    }

    @Override
    public void populateBean(ChartCollection ob) {
        chartCollection = ob;
        layerPane.populateBean(ob);
        dataPane.populateBean(ob);
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
        layerPane.updateBean(ob);
        dataPane.updateBean(ob);
    }

    /**
     * 设置是否关联单元格数据.
     *
     * @param supportCellData
     */
    public void setSupportCellData(boolean supportCellData) {
        dataPane.setSupportCellData(supportCellData);
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
        return null;
    }

    /**
     * 重置
     */
    @Override
    public void reset() {

    }
}
