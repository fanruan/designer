package com.fr.van.chart.drillmap.designer.data.comp;

import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.NormalChartDataPane;

import java.awt.BorderLayout;

/**
 * Created by Mitisky on 16/6/21.
 * 各层级分别指定中 单层区域地图数据配置 以及 底层数据汇总方式的界面
 */
public class SingleLayerDataDefinitionPane extends FurtherBasicBeanPane<ChartCollection> {
    private String title;
    private NormalChartDataPane normalChartDataPane;

    //底层数据汇总
    public SingleLayerDataDefinitionPane(AttributeChangeListener listener, ChartDataPane parent) {
        this(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Bottom_Data_Sum"), listener, parent);
    }

    public SingleLayerDataDefinitionPane(String title, AttributeChangeListener listener, ChartDataPane parent) {
        this.title = title;
        normalChartDataPane = new NormalChartDataPane(listener, parent);

        this.setLayout(new BorderLayout());
        this.add(normalChartDataPane, BorderLayout.CENTER);
    }

    /**
     * 设置是否关联单元格数据.
     *
     * @param supportCellData
     */
    public void setSupportCellData(boolean supportCellData) {
        normalChartDataPane.setSupportCellData(supportCellData);
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
        return title;
    }

    /**
     * 重置
     */
    @Override
    public void reset() {

    }

    /**
     * Populate.
     *
     * @param ob
     */
    @Override
    public void populateBean(ChartCollection ob) {
        normalChartDataPane.populate(ob);

    }

    @Override
    public void updateBean(ChartCollection ob) {
        normalChartDataPane.update(ob);
    }

    /**
     * Update.
     */
    @Override
    public ChartCollection updateBean() {
        return null;
    }
}
