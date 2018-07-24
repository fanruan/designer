package com.fr.design.mainframe.chart.gui.data.table;

import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.base.ChartConstants;
import com.fr.chart.chartattr.BubblePlot;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartdata.BubbleTableDefinition;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.ChartDataFilterPane;
import com.fr.general.ComparatorUtils;

import com.fr.stable.ArrayUtils;
import com.fr.stable.StringUtils;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.List;

public class BubblePlotTableDataContentPane extends AbstractTableDataContentPane {

    private UIComboBox seriesName;
    private UIComboBox xCombox;
    private UIComboBox yCombox;
    private UIComboBox bubbleSize;

    private ChartDataFilterPane dataScreeningPane;

    public BubblePlotTableDataContentPane(ChartDataPane parent) {
        seriesName = new UIComboBox();
        xCombox = new UIComboBox();
        yCombox = new UIComboBox();
        bubbleSize = new UIComboBox();
        dataScreeningPane = new ChartDataFilterPane(new BubblePlot(), parent);
        
        seriesName.setPreferredSize(new Dimension(100, 20));
        xCombox.setPreferredSize(new Dimension(100, 20));
        yCombox.setPreferredSize(new Dimension(100, 20));
        bubbleSize.setPreferredSize(new Dimension(100, 20));

        seriesName.addItem(com.fr.design.i18n.Toolkit.i18nText("Chart-Use_None"));

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {f};
        double[] rowSize = { p, p,p,p};

        double[] columnSize_north = {p, f};
        double[] rowSize_north = {p, p, p, p};
        Component[][] components_north = new Component[][]{
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Chart-Series_Name")), seriesName},
                new Component[]{new UILabel("x"), xCombox},
                new Component[]{new UILabel("y"), yCombox},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("FR-Chart_Bubble_Size")), bubbleSize}
        };
        JPanel north = TableLayoutHelper.createTableLayoutPane(components_north,rowSize_north,columnSize_north);
        north.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 1));

        Component[][] components = new Component[][]{
                     new Component[]{north},
                     new Component[]{TableLayout4VanChartHelper.createExpandablePaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("FR-Chart-Data_Filter"),dataScreeningPane), null},
             };


        JPanel panel = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
        dataScreeningPane.setBorder(BorderFactory.createEmptyBorder(10,10,0,15));

        this.setLayout(new BorderLayout());
        this.add(panel, BorderLayout.CENTER);
        
        seriesName.addItemListener(tooltipListener);
        xCombox.addItemListener(tooltipListener);
        yCombox.addItemListener(tooltipListener);
        bubbleSize.addItemListener(tooltipListener);
    }

    /**
     * 检查box是否使用, donothing
     * @param hasUse  是否使用.
     */
    public void checkBoxUse(boolean hasUse) {

    }
    
    protected void refreshBoxListWithSelectTableData(List list) {
    	refreshBoxItems(seriesName, list);
        seriesName.addItem(com.fr.design.i18n.Toolkit.i18nText("Chart-Use_None"));
    	refreshBoxItems(xCombox, list);
    	refreshBoxItems(yCombox, list);
    	refreshBoxItems(bubbleSize, list);
    }

    /**
     * 清空所有的box设置
     */
    public void clearAllBoxList(){
        clearBoxItems(seriesName);
        seriesName.addItem(com.fr.design.i18n.Toolkit.i18nText("Chart-Use_None"));
        clearBoxItems(xCombox);
        clearBoxItems(yCombox);
        clearBoxItems(bubbleSize);
    }

    @Override
    public void populateBean(ChartCollection collection) {
        super.populateBean(collection);
        TopDefinitionProvider top = collection.getSelectedChart().getFilterDefinition();
        if (!(top instanceof BubbleTableDefinition)) {
            return;
        }
        BubbleTableDefinition definition = (BubbleTableDefinition) top;
        
        if(definition.getSeriesName() == null || ComparatorUtils.equals(StringUtils.EMPTY, definition.getSeriesName())) {
            seriesName.setSelectedItem(com.fr.design.i18n.Toolkit.i18nText("Chart-Use_None"));
        } else {
            combineCustomEditValue(seriesName, definition.getSeriesName());
        }

        combineCustomEditValue(xCombox, definition.getBubbleX());
        combineCustomEditValue(yCombox, definition.getBubbleY());
        combineCustomEditValue(bubbleSize, definition.getBubbleSize());
        
        dataScreeningPane.populateBean(collection);
    }

    @Override
    public void updateBean(ChartCollection collection) {
        BubbleTableDefinition definition = new BubbleTableDefinition();
        collection.getSelectedChart().setFilterDefinition(definition);
        
        Object resultName = seriesName.getSelectedItem();
        Object resultX = xCombox.getSelectedItem();
        Object resultY = yCombox.getSelectedItem();
        Object resultSize = bubbleSize.getSelectedItem();

        if(resultName == null || ArrayUtils.contains(ChartConstants.getNoneKeys(), resultName)) {
            definition.setSeriesName(StringUtils.EMPTY);
        } else {
            definition.setSeriesName(resultName.toString());
        }

        if (resultX != null) {
            definition.setBubbleX(resultX.toString());
        }
        if (resultY != null) {
            definition.setBubbleY(resultY.toString());
        }
        if (resultSize != null) {
            definition.setBubbleSize(resultSize.toString());
        }
        
        dataScreeningPane.updateBean(collection);
    }

    /**
     * 重新布局
     */
    public void redoLayoutPane(){
      dataScreeningPane.relayoutPane(this.isNeedSummaryCaculateMethod());
    }
}