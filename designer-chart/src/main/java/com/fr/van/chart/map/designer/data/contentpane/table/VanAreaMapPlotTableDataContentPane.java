package com.fr.van.chart.map.designer.data.contentpane.table;

import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.plugin.chart.map.VanChartMapPlot;
import com.fr.plugin.chart.map.data.VanMapTableDefinitionProvider;
import com.fr.van.chart.map.designer.data.component.SeriesTypeUseComboxPaneWithOutFilter;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import java.util.List;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Created by Mitisky on 16/5/16.
 */
public class VanAreaMapPlotTableDataContentPane extends VanMapTableDataContentPane {
    private UIComboBox areaNameCom;

    protected SeriesTypeUseComboxPaneWithOutFilter seriesTypeUseComboxPane;

    public VanAreaMapPlotTableDataContentPane(ChartDataPane parent) {
        super(parent);
        this.setLayout(new BorderLayout(0, 4));

        initAreaNameCom();

        JPanel areaNamePane = createAreaNamePane();
        JSeparator jSeparator = new JSeparator();
        areaNamePane.setBorder(BorderFactory.createEmptyBorder(0, 24, 0, 15));
        jSeparator.setPreferredSize(new Dimension(246, 2));

        this.add(areaNamePane, BorderLayout.NORTH);
        this.add(jSeparator, BorderLayout.CENTER);

        seriesTypeUseComboxPane = new SeriesTypeUseComboxPaneWithOutFilter(parent, new VanChartMapPlot());
        this.add(seriesTypeUseComboxPane, BorderLayout.SOUTH);

    }

    protected void initAreaNameCom() {

        areaNameCom = new UIComboBox();

        areaNameCom.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                checkSeriseUse(areaNameCom.getSelectedItem() != null);
                makeToolTipUse(areaNameCom);
            }
        });
    }

    protected JPanel createAreaNamePane() {
        UILabel label = new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Area_Name"));
        label.setPreferredSize(new Dimension(80, 20));
        areaNameCom.setPreferredSize(new Dimension(100, 20));

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {p, f};
        double[] rowSize = {p};

        Component[][] components = new Component[][]{
                new Component[]{label, createAreaPanel(areaNameCom)},
        };

        return TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);

    }

    protected void makeToolTipUse(UIComboBox comBox) {
        if (comBox.getSelectedItem() != null) {
            comBox.setToolTipText(comBox.getSelectedItem().toString());
        } else {
            comBox.setToolTipText(null);
        }
    }

    /**
     * 检查 某些Box是否可用
     *
     * @param hasUse 是否使用.
     */
    public void checkBoxUse(boolean hasUse) {
        checkAreaName(hasUse);
        checkSeriseUse(hasUse);
    }

    protected void checkAreaName(boolean hasUse) {
        areaNameCom.setEnabled(hasUse);
    }

    protected void checkSeriseUse(boolean hasUse) {
        if (seriesTypeUseComboxPane != null) {
            seriesTypeUseComboxPane.checkUseBox(hasUse && isAreaSelectedItem());
        }
    }

    protected boolean isAreaSelectedItem() {
        return areaNameCom.getSelectedItem() != null;
    }

    protected void refreshBoxListWithSelectTableData(java.util.List list) {
        refreshAreaName(list);
        seriesTypeUseComboxPane.refreshBoxListWithSelectTableData(list);
    }

    protected void refreshAreaName(List list) {
        refreshBoxItems(areaNameCom, list);
    }

    /**
     * 清空所有的box设置
     */
    public void clearAllBoxList() {
        clearAreaName();
        seriesTypeUseComboxPane.clearAllBoxList();
    }

    protected void clearAreaName() {
        clearBoxItems(areaNameCom);
    }

    /**
     * 保存界面内容到ChartCollection
     */
    public void updateBean(ChartCollection collection) {
        seriesTypeUseComboxPane.updateBean(collection);
        TopDefinitionProvider topDefinitionProvider = collection.getSelectedChart().getFilterDefinition();
        if (topDefinitionProvider instanceof VanMapTableDefinitionProvider) {
            VanMapTableDefinitionProvider mapTableDefinitionProvider = (VanMapTableDefinitionProvider) topDefinitionProvider;
            mapTableDefinitionProvider.setMatchResult(this.getMatchResult());
            updateDefinition(mapTableDefinitionProvider);
        }
    }

    protected void updateDefinition(VanMapTableDefinitionProvider mapTableDefinitionProvider) {
        Object o = areaNameCom.getSelectedItem();
        mapTableDefinitionProvider.setCategoryName(o == null ? null : o.toString());
    }

    /**
     * 根据ChartCollection 更新界面
     */
    public void populateBean(ChartCollection collection) {
        seriesTypeUseComboxPane.populateBean(collection, this.isNeedSummaryCaculateMethod());
        TopDefinitionProvider topDefinitionProvider = collection.getSelectedChart().getFilterDefinition();
        if (topDefinitionProvider instanceof VanMapTableDefinitionProvider) {
            VanMapTableDefinitionProvider mapTableDefinitionProvider = (VanMapTableDefinitionProvider) topDefinitionProvider;
            this.setMatchResult(mapTableDefinitionProvider.getMatchResult());
            populateDefinition(mapTableDefinitionProvider);
        }
    }

    protected void populateDefinition(VanMapTableDefinitionProvider mapTableDefinitionProvider) {
        if (mapTableDefinitionProvider.getCategoryName() != null) {
            areaNameCom.setSelectedItem(mapTableDefinitionProvider.getCategoryName());
        }
    }

    /**
     * 重新布局整个面板
     */
    public void redoLayoutPane() {
        seriesTypeUseComboxPane.relayoutPane(this.isNeedSummaryCaculateMethod());
    }
}
