package com.fr.design.chart;
/**
 * the Pane of the Chart
 */

import com.fr.chart.base.ChartInternationalNameContentBean;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartattr.ChartIcon;
import com.fr.chart.charttypes.ChartTypeManager;
import com.fr.chartx.attr.ChartProvider;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.locale.InterProviderFactory;
import com.fr.log.FineLoggerFactory;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.Color;
import java.awt.Component;

public class ChartTypePane extends ChartCommonWizardPane {
    private static final long serialVersionUID = -1175602484968520546L;

    private ChartInternationalNameContentBean[] typeName = ChartTypeManager.getInstanceWithCheck().getAllChartBaseNames();
    private ChartProvider[][] charts4Icon = null;

    {
        charts4Icon = new ChartProvider[this.typeName.length][];
        for (int i = 0; i < this.typeName.length; i++) {
            ChartProvider[] rowCharts = ChartTypeManager.getInstanceWithCheck().getChartTypes(this.typeName[i].getChartID());
            int rowChartsCount = rowCharts.length;
            charts4Icon[i] = new ChartProvider[rowChartsCount];
            for (int j = 0; j < rowChartsCount; j++) {
                try {
                    charts4Icon[i][j] = (ChartProvider) rowCharts[j].clone();
                    //todo@shinerefactor 老图表也是提供一张图片 这边就不用setTitle(null) 然后实时去画
//                    charts4Icon[i][j].setTitle(null);
//                    if(charts4Icon[i][j].getPlot() != null){
//                        charts4Icon[i][j].getPlot().setLegend(null);
//                    }
                } catch (CloneNotSupportedException e) {
                    FineLoggerFactory.getLogger().error(e.getMessage(), e);
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
            defaultListModel.insertElementAt(InterProviderFactory.getProvider().getLocText(typeName[i].getName()), i);
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
            ChartProvider[] sub_charts = ChartTypePane.this.charts4Icon[main_index];
            ChartTypePane.this.iconListModel.clear();
            for (int i = 0; i < sub_charts.length; i++) {
                String ImagePath = sub_charts[i].demoImagePath();
                String chartName = sub_charts[i].getChartName();
                ChartTypePane.this.iconListModel.addElement(new ChartIcon(ImagePath, chartName));
            }
            iconViewList.setSelectedIndex(0);
        }
    };

    public String getChartName(ChartIcon chartIcon) {
        return chartIcon.getChartName();
    }

    public void populate(ChartProvider chart) {
    }

    public void update(ChartProvider oldChart) {
    }

    public void update(ChartCollection cc) {
        if (cc == null) {
            return;
        }

        ChartProvider chart4Update = cc.getSelectedChartProvider();
        if (chart4Update == null) {
            String plotID = typeName[mainTypeList.getSelectedIndex()].getChartID();
            ChartProvider chart = ChartTypeManager.getInstance().getChartTypes(plotID)[iconViewList.getSelectedIndex()];
            try {
                chart4Update = (ChartProvider) chart.clone();
                cc.addChart(chart4Update);
            } catch (CloneNotSupportedException ex) {
                FineLoggerFactory.getLogger().error(ex.getMessage(), ex);
            }
        }

        update(chart4Update);
    }
}