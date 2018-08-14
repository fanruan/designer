package com.fr.van.chart.gantt.designer.data;

import com.fr.base.FRContext;
import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.MultiTabPane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.NormalChartDataPane;
import com.fr.design.mainframe.chart.gui.data.ReportDataPane;
import com.fr.design.mainframe.chart.gui.data.TableDataPane;

import com.fr.plugin.chart.gantt.data.VanGanttDefinition;
import com.fr.van.chart.gantt.designer.data.link.GanttLinkReportDataPane;
import com.fr.van.chart.gantt.designer.data.link.GanttLinkTableDataPane;

import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hufan on 2017/1/10.
 */
public class VanChartGanttDataAndLinkPane extends MultiTabPane<ChartCollection> {
    private NormalChartDataPane dataPane;
    private NormalChartDataPane linkPane;

    private ChartCollection chartCollection;

    public VanChartGanttDataAndLinkPane(AttributeChangeListener listener, ChartDataPane parent) {
        cardLayout = new CardLayout();
        dataPane = new NormalChartDataPane(listener, parent){
            @Override
            public String title4PopupWindow() {
                return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Gantt_Chart");
            }
        };
        linkPane = new NormalChartDataPane(listener, parent){
            @Override
            protected TableDataPane getTableDataPane(ChartDataPane chartDataPane) {
                return new GanttLinkTableDataPane(chartDataPane);
            }

            @Override
            protected ReportDataPane getReportDataPane(ChartDataPane parent) {
                return new GanttLinkReportDataPane(parent);
            }

            @Override
            public String title4PopupWindow() {
                return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Task_Link");
            }
        };

        paneList = initPaneList();
        initComponents();
    }

    private void initComponents(){
        super.relayoutWhenListChange();
    }

    protected void tabChanged() {
        if(getSelectedIndex() == 0){
            return;
        }
        if(chartCollection == null){
            return;
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

        if(dataPane != null) {
            paneList.add(dataPane);
        }
        if(linkPane != null) {
            paneList.add(linkPane);
        }

        return paneList;
    }

    @Override
    public void populateBean(ChartCollection ob) {
        chartCollection = ob;

        ChartCollection dataContentCollection = getDataContentChartCollection(ob);
        ChartCollection taskLinkCollection = getTaskLinkChartCollection(ob);

        linkPane.populateBean(taskLinkCollection);
        dataPane.populateBean(dataContentCollection);

    }

    private ChartCollection getDataContentChartCollection(ChartCollection collection){
        try{
            ChartCollection cloneCollection = (ChartCollection) collection.clone();
            Chart chart = cloneCollection.getSelectedChart();

            TopDefinitionProvider definition = chart.getFilterDefinition();
            if(definition != null && definition instanceof VanGanttDefinition) {
                chart.setFilterDefinition(((VanGanttDefinition) definition).getDataDefinition());
            }

            return cloneCollection;
        } catch (Exception e){
            FRContext.getLogger().error(e.getMessage(), e);
            return collection;
        }
    }

    private ChartCollection getTaskLinkChartCollection(ChartCollection collection){
        try{
            ChartCollection cloneCollection = (ChartCollection) collection.clone();
            Chart chart = cloneCollection.getSelectedChart();

            TopDefinitionProvider definition = chart.getFilterDefinition();
            if(definition != null && definition instanceof VanGanttDefinition) {
                chart.setFilterDefinition(((VanGanttDefinition) definition).getLinkDefinition());
            }

            return cloneCollection;
        } catch (Exception e){
            FRContext.getLogger().error(e.getMessage(), e);
            return collection;
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
        VanGanttDefinition ganttDefinition = new VanGanttDefinition();

        ChartCollection taskLinkCollection = getTaskLinkChartCollection(ob);
        ChartCollection dataContentCollection = getDataContentChartCollection(ob);

        linkPane.update(taskLinkCollection);
        dataPane.update(dataContentCollection);

        ganttDefinition.setDataDefinition(dataContentCollection.getSelectedChart().getFilterDefinition());
        ganttDefinition.setLinkDefinition(taskLinkCollection.getSelectedChart().getFilterDefinition());

        ob.getSelectedChart().setFilterDefinition(ganttDefinition);
    }

    /**
     * 设置是否关联单元格数据.
     *
     * @param surpportCellData
     */
    public void setSupportCellData(boolean surpportCellData) {
        dataPane.setSupportCellData(surpportCellData);
        linkPane.setSupportCellData(surpportCellData);
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
