package com.fr.design.chartx;

import com.fr.chart.chartattr.ChartCollection;
import com.fr.chartx.data.AbstractDataDefinition;
import com.fr.chartx.data.CustomChartDataDefinition;
import com.fr.design.chartx.fields.diff.GaugeCellDataFieldsPane;
import com.fr.design.chartx.fields.diff.GaugeDataSetFieldsPane;
import com.fr.design.chartx.fields.diff.MultiCategoryCellDataFieldsPane;
import com.fr.design.chartx.fields.diff.MultiCategoryDataSetFieldsPane;
import com.fr.design.chartx.fields.diff.ScatterCellDataFieldsPane;
import com.fr.design.chartx.fields.diff.ScatterDataSetFieldsPane;
import com.fr.design.chartx.fields.diff.SingleCategoryCellDataFieldsPane;
import com.fr.design.chartx.fields.diff.SingleCategoryDataSetFieldsPane;
import com.fr.design.chartx.single.SingleDataPane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.gui.ibutton.UITabGroup;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.plugin.chart.attr.plot.VanChartPlot;
import com.fr.plugin.chart.custom.CustomPlotFactory;
import com.fr.plugin.chart.custom.VanChartCustomPlot;
import com.fr.plugin.chart.custom.type.CustomPlotType;
import com.fr.plugin.chart.custom.type.CustomStyle;
import com.fr.plugin.chart.vanchart.VanChart;
import com.fr.stable.StringUtils;
import com.fr.van.chart.custom.component.VanChartCustomPlotUITabGroup;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Bjorn
 * @version 10.0
 * Created by Bjorn on 2019-10-23
 */
public class CustomChartDataPane extends ChartDataPane {

    private static final int HGAP = 0;
    private static final int VGAP = 6;

    public CustomChartDataPane(AttributeChangeListener listener) {
        super(listener);
    }

    private VanChartCustomPlot customPlot;

    private CardLayout cardLayout;
    private JPanel centerPane;
    private List<AbstractVanSingleDataPane> paneList;
    private UITabGroup tabPane;

    private String[] nameArray;

    @Override
    protected void initContentPane() {
        if (customPlot == null) {
            return;
        }
        cardLayout = new CardLayout();
        initPaneList();
        relayoutWhenListChange();
    }

    private void initPaneList() {

        paneList = new ArrayList<>();

        List<VanChartPlot> customPlotList = customPlot.getCustomPlotList();

        for (int i = 0; i < customPlotList.size(); i++) {
            //根据不同的plot创建不同的数据配置界面
            final VanChartPlot vanChartPlot = customPlotList.get(i);
            paneList.add(new AbstractVanSingleDataPane(listener) {
                @Override
                protected SingleDataPane createSingleDataPane() {
                    return createSingleDataPaneByPlot(vanChartPlot);
                }
            });
        }
    }

    private SingleDataPane createSingleDataPaneByPlot(VanChartPlot plot) {
        CustomPlotType customType = CustomPlotFactory.getCustomType(plot);
        switch (customType) {
            case RING:
            case SLOT:
            case CUVETTE:
                return new SingleDataPane(new GaugeDataSetFieldsPane(), new GaugeCellDataFieldsPane());
            case SCATTER:
            case BUBBLE:
                return new SingleDataPane(new ScatterDataSetFieldsPane(), new ScatterCellDataFieldsPane());
            default:
                if (StringUtils.equals(CustomStyle.CUSTOM.toString(), plot.getCustomType())){
                    return new SingleDataPane(new SingleCategoryDataSetFieldsPane(), new SingleCategoryCellDataFieldsPane());
                } else {
                    return new SingleDataPane(new MultiCategoryDataSetFieldsPane(), new MultiCategoryCellDataFieldsPane());
                }
        }
    }

    private void relayoutWhenListChange() {
        centerPane = new JPanel(cardLayout) {
            @Override
            public Dimension getPreferredSize() {
                return paneList.get(tabPane.getSelectedIndex()).getPreferredSize();
            }
        };

        //获取tab的标题
        initTabTitle();

        tabPane = new VanChartCustomPlotUITabGroup(nameArray) {
            @Override
            public void tabChanged(int index) {
                dealWithTabChanged(index);
            }
        };
        tabPane.setSelectedIndex(0);
        tabPane.tabChanged(0);
        initLayout();
    }

    private void initTabTitle() {

        if (customPlot == null) {
            return;
        }

        List<VanChartPlot> customPlotList = customPlot.getCustomPlotList();
        nameArray = new String[Math.min(customPlotList.size(), paneList.size())];
        for (int i = 0; i < nameArray.length; i++) {
            JPanel pane = paneList.get(i);
            VanChartPlot vanChartPlot = customPlotList.get(i);
            CustomPlotType plotType = CustomPlotFactory.getCustomType(vanChartPlot);

            nameArray[i] = CustomPlotFactory.getTitle(plotType);
            centerPane.add(pane, nameArray[i]);
        }
    }

    protected void dealWithTabChanged(int index) {
        cardLayout.show(centerPane, nameArray[index]);
    }

    private void initLayout() {
        JPanel tabPanel = new JPanel(new BorderLayout());
        tabPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, getBackground()));
        tabPanel.add(tabPane, BorderLayout.CENTER);
        this.setLayout(new BorderLayout(HGAP, VGAP));
        this.add(tabPanel, BorderLayout.NORTH);
        this.add(centerPane, BorderLayout.CENTER);
    }

    @Override
    public void populate(ChartCollection collection) {
        if (collection == null) {
            return;
        }
        VanChart chart = collection.getSelectedChart(VanChart.class);
        if (chart == null) {
            return;
        }
        customPlot = chart.getPlot();

        this.removeAll();
        initContentPane();

        CustomChartDataDefinition dataSetCollection = (CustomChartDataDefinition) chart.getChartDataDefinition();

        if (dataSetCollection != null) {
            Map<CustomPlotType, AbstractDataDefinition> customDefinitions = dataSetCollection.getCustomDefinitions();
            for (int i = 0; i < paneList.size(); i++) {
                VanChartPlot vanChartPlot = customPlot.getCustomPlotList().get(i);
                AbstractDataDefinition dataDefinition = customDefinitions.get(CustomPlotFactory.getCustomType(vanChartPlot));
                if (dataDefinition != null) {
                    paneList.get(i).populate(dataDefinition);
                }
            }
        }

        this.initAllListeners();
        this.validate();
    }


    @Override
    public void update(ChartCollection collection) {
        if (collection == null) {
            return;
        }
        VanChart chart = collection.getSelectedChart(VanChart.class);
        if (chart == null) {
            return;
        }
        Map<CustomPlotType, AbstractDataDefinition> definitions = new HashMap<>();
        for (int i = 0; i < paneList.size(); i++) {
            definitions.put(CustomPlotFactory.getCustomType(customPlot.getCustomPlotList().get(i)), paneList.get(i).update());
        }
        CustomChartDataDefinition customDefinition = new CustomChartDataDefinition();
        customDefinition.setCustomDefinitions(definitions);
        chart.setChartDataDefinition(customDefinition);
    }
}
