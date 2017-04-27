package com.fr.design.mainframe.chart.gui.data;

import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartattr.Plot;
import com.fr.design.ChartTypeInterfaceManager;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.dialog.BasicScrollPane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.gui.frpane.UIComboBoxPane;
import com.fr.design.mainframe.chart.gui.ChartDataPane;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by mengao on 2017/4/26.
 */
public class CustomChartDataContentsPane extends DataContentsPane {


    public UIComboBoxPane<ChartCollection> dataPane;
    private TableDataPane tableDataPane;
    private ReportDataPane reportDataPane;
    private BasicBeanPane configPane;
    private AttributeChangeListener listener;
    private Plot plot;

    private ChartDataPane parent;

    public CustomChartDataContentsPane(AttributeChangeListener listener, ChartDataPane parent) {
        this.listener = listener;
        this.parent = parent;
        initAll();
    }


    public CustomChartDataContentsPane(AttributeChangeListener listener, ChartDataPane parent, boolean supportCellData) {
        this.listener = listener;
        this.parent = parent;
        initAll();
        dataPane.justSupportOneSelect(true);
    }

    public CustomChartDataContentsPane(AttributeChangeListener listener, ChartDataPane parent, Plot plot) {
        this.listener = listener;
        this.parent = parent;
        this.plot= plot;
        initAll();
    }


    public CustomChartDataContentsPane(AttributeChangeListener listener, ChartDataPane parent, boolean supportCellData, Plot plot) {
        this.listener = listener;
        this.parent = parent;
        this.plot= plot;
        initAll();
        dataPane.justSupportOneSelect(true);
    }

    public UIComboBoxPane<ChartCollection> getDataPane() {
        return dataPane;
    }

    @Override
    protected JPanel createContentPane() {
        return new BasicScrollPane<ChartCollection>() {

            @Override
            protected JPanel createContentPane() {
                JPanel contentPane = new JPanel(new BorderLayout());
                dataPane = new UIComboBoxPane<ChartCollection>() {
                    protected void initLayout() {
                        this.setLayout(new BorderLayout(LayoutConstants.HGAP_LARGE, 6));
                        JPanel northPane = new JPanel(new BorderLayout(LayoutConstants.HGAP_LARGE, 0));
                        JButton jButton = new JButton();
                        if(plot!=null){
                            configPane = (BasicBeanPane)ChartTypeInterfaceManager.getInstance().getChartConfigPane(plot, parent);
                            northPane.add(configPane, BorderLayout.CENTER);
                        }else {
                            northPane.add(jButton, BorderLayout.CENTER);

                        }
                        this.add(northPane, BorderLayout.NORTH);

                    }

                    @Override
                    protected String title4PopupWindow() {
                        return null;
                    }

                    @Override
                    protected java.util.List<FurtherBasicBeanPane<? extends ChartCollection>> initPaneList() {
                        tableDataPane = getTableDataPane(parent);
                        reportDataPane = getReportDataPane(parent);
                        java.util.List<FurtherBasicBeanPane<? extends ChartCollection>> paneList = new ArrayList<FurtherBasicBeanPane<? extends ChartCollection>>();
                        paneList.add(tableDataPane);
                        paneList.add(reportDataPane);
                        return paneList;
                    }
                };
                contentPane.add(dataPane, BorderLayout.CENTER);
                dataPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
                return contentPane;
            }

            @Override
            protected String title4PopupWindow() {
                return "";
            }

            @Override
            public void populateBean(ChartCollection ob) {

            }
        };

    }

    protected ReportDataPane getReportDataPane(ChartDataPane parent) {
        return new ReportDataPane(parent);
    }

    protected TableDataPane getTableDataPane(ChartDataPane chartDataPane) {
        return new TableDataPane(chartDataPane);
    }

    /**
     * 更新界面 数据内容
     */
    public void populate(ChartCollection collection) {
        Plot plot = collection.getSelectedChart().getPlot();
        dataPane.populateBean(collection);
        configPane.populateBean(plot);
    }

    /**
     * 保存 数据界面内容
     */
    public void update(ChartCollection collection) {
        Plot plot = collection.getSelectedChart().getPlot();
        configPane.updateBean(plot);
        dataPane.updateBean(collection);
        configPane.updateBean();
    }

    /**
     * 是否支持单元格数据
     */
    public void setSupportCellData(boolean supportCellData) {
        dataPane.justSupportOneSelect(supportCellData);
    }

}
