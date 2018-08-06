package com.fr.design.chart;
/**
 * the Pane of the Chart
 *
 */

import com.fr.base.FRContext;
import com.fr.chart.base.ChartInternationalNameContentBean;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartattr.ChartIcon;
import com.fr.chart.chartattr.MapPlot;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.charttypes.ChartTypeManager;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;

import com.fr.license.exception.RegistEditionException;
import com.fr.license.function.VT4FR;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

public class ChartTypePane extends ChartCommonWizardPane {
    private static final long serialVersionUID = -1175602484968520546L;

    private ChartInternationalNameContentBean[] typeName = ChartTypeManager.getInstance().getAllChartBaseNames();
    private Chart[][] charts4Icon = null;

    {
        charts4Icon = new Chart[this.typeName.length][];
        for (int i = 0; i < this.typeName.length; i++) {
            Chart[] rowCharts = ChartTypeManager.getInstance().getChartTypes(this.typeName[i].getPlotID());
            int rowChartsCount = rowCharts.length;
            charts4Icon[i] = new Chart[rowChartsCount];
            for (int j = 0; j < rowChartsCount; j++) {
                try {
                    charts4Icon[i][j] = (Chart) rowCharts[j].clone();
                    charts4Icon[i][j].setTitle(null);
                    if(charts4Icon[i][j].getPlot() != null){
                        charts4Icon[i][j].getPlot().setLegend(null);
                    }
                } catch (CloneNotSupportedException e) {
                    FRContext.getLogger().error(e.getMessage(), e);
                }
            }
        }
    }

    private JList mainTypeList = null;
    private JList iconViewList = null;
    private DefaultListModel iconListModel = null;

    public ChartTypePane() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        DefaultListModel defaultListModel = new DefaultListModel();
        mainTypeList = new JList(defaultListModel);

        for (int i = 0; i < typeName.length; i++) {
            defaultListModel.insertElementAt(com.fr.design.i18n.Toolkit.i18nText(typeName[i].getName()), i);
        }
        mainTypeList.addListSelectionListener(listSelectionListener);

        JScrollPane typeScrollPane = new JScrollPane(mainTypeList);
        typeScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        iconViewList = new JList();

        iconListModel = new DefaultListModel();
        iconViewList.setModel(iconListModel);
        iconViewList.setVisibleRowCount(0);
        iconViewList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        iconViewList.setCellRenderer(iconCellRenderer);

        JScrollPane subListPane = new JScrollPane(iconViewList);
        //iconPane.add
        mainTypeList.setSelectedIndex(0);

        JSplitPane spane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, typeScrollPane, subListPane);
        spane.setDividerLocation(120);
        spane.setBorder(BorderFactory.createTitledBorder(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_M_Popup_Chart_Type")));
        this.add(spane);

        iconViewList.setSelectedIndex(0);
    }

    ListCellRenderer iconCellRenderer = new DefaultListCellRenderer() {
        private static final long serialVersionUID = 956888843609479566L;

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            this.setText("");
            if (value instanceof ChartIcon) {
                this.setIcon((ChartIcon) value);
                setHorizontalAlignment(UILabel.CENTER);
                ChartIcon chartIcon = (ChartIcon) value;
                if (isSelected) {
                    // 深蓝色.
                    this.setBackground(new Color(57, 107, 181));
                    this.setBorder(GUICoreUtils.createTitledBorder(getChartName(chartIcon), Color.WHITE));
                } else {
                    this.setBorder(GUICoreUtils.createTitledBorder(getChartName(chartIcon)));
                }

            }
            return this;
        }
    };

    protected ListSelectionListener listSelectionListener = new ListSelectionListener() {
        public void valueChanged(ListSelectionEvent e) {
            int main_index = mainTypeList.getSelectedIndex();
            Chart[] sub_charts = ChartTypePane.this.charts4Icon[main_index];
            ChartTypePane.this.iconListModel.clear();
            for (int i = 0; i < sub_charts.length; i++) {
                ChartTypePane.this.iconListModel.addElement(new ChartIcon(sub_charts[i]));
            }
            iconViewList.setSelectedIndex(0);
        }
    };

    public String getChartName(ChartIcon chartIcon) {
        Chart chart = (Chart)chartIcon.getChart();
        return chart.getChartName();
    }

    public void populate(Chart chart) {
        if (chart == null) {
            return;
        }
        Plot plot = chart.getPlot();

        int mainIndex = 0;
        int subIndex = 0;

        for (int i = 0; i < typeName.length; i++) {
        	Chart [] charts = ChartTypeManager.getInstance().getChartTypes(typeName[i].getPlotID());
            for (int j = 0; j < charts.length; j++) {
                if (charts[j].getPlot().match4GUI(plot)) {
                    mainIndex = i;
                    subIndex = j;
                    // 一旦匹配 立马中断
                    break;
                }
            }
        }

        mainTypeList.setSelectedIndex(mainIndex);
        iconViewList.setSelectedIndex(subIndex);
    }

    public void update(Chart oldChart) {
        String plotID = typeName[mainTypeList.getSelectedIndex()].getPlotID();
        Chart chart = ChartTypeManager.getInstance().getChartTypes(plotID)[iconViewList.getSelectedIndex()];
        if(chart.getPlot() != null){
            if(chart.getPlot() instanceof MapPlot && !supportMap()){
                JOptionPane.showMessageDialog(null, com.fr.design.i18n.Toolkit.i18nText("FR-Chart-Map_Not_Supported"));
                throw new RegistEditionException(VT4FR.ChartMap);
            }

            if (chart.getPlot() != null) {
                try {
                    oldChart.changePlotInNewType((Plot) chart.getPlot().clone());
                } catch (CloneNotSupportedException e) {
                    FRContext.getLogger().error(e.getMessage(), e);
                }
            }
        }
    }

    private boolean supportMap() {
        return VT4FR.ChartMap.isSupport();
    }

    public void update(ChartCollection cc) {
        if (cc == null) {
            return;
        }

        Chart chart4Update = cc.getSelectedChart();
        if (chart4Update == null) {
            String plotID = typeName[mainTypeList.getSelectedIndex()].getPlotID();
            Chart chart = ChartTypeManager.getInstance().getChartTypes(plotID)[iconViewList.getSelectedIndex()];
            try{
                chart4Update = (Chart)chart.clone();
                cc.addChart(chart4Update);
            }catch (CloneNotSupportedException ex){
                FRContext.getLogger().error(ex.getMessage(), ex);
            }
        }

        update(chart4Update);
    }
}