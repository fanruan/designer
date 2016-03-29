package com.fr.design.mainframe;

import com.fr.chart.base.ChartConstants;
import com.fr.chart.base.MapSvgXMLHelper;
import com.fr.chart.chartattr.*;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.mainframe.chart.ChartDesignEditPane;
import com.fr.general.ComparatorUtils;
import com.fr.stable.Constants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 7.1.1
 * Date: 14/12/1
 * Time: 下午3:15
 */
public abstract class AbstractMapPlotPane4ToolBar extends JPanel{


    protected static final int COM_HEIGHT = 22;
    protected static final int COM_GAP = 14;
    protected static final int COMBOX_WIDTH = 230;

    protected ChartDesigner chartDesigner;
    protected UIComboBox mapTypeComboBox;

    protected ItemListener mapTypeListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            calculateDetailMaps(mapTypeComboBox.getSelectedIndex());
        }
    };


    public AbstractMapPlotPane4ToolBar(ChartDesigner designer){
        this.chartDesigner = designer;
        this.setLayout(new FlowLayout(FlowLayout.LEFT, COM_GAP, 0));
        this.setBorder(new EmptyBorder(2, 0, 2, 0));
        mapTypeComboBox = new UIComboBox(getMapTypes()) {
            public Dimension getPreferredSize() {
                return new Dimension(COMBOX_WIDTH, COM_HEIGHT);
            }
        };
        mapTypeComboBox.addItemListener(mapTypeListener);
        this.add(mapTypeComboBox);
    }


    protected abstract void calculateDetailMaps(int mapType);

    /**
     * 更新地图面板
     * @param mapType 地图名字
     */
    public void populateMapPane(String mapType){
        mapTypeComboBox.removeItemListener(mapTypeListener);
        for (String type : getMapTypes()) {
      		java.util.List list = MapSvgXMLHelper.getInstance().getNamesListWithCateName(type);
      		for (Object name : list) {
      			if(ComparatorUtils.equals(name,mapType)){
                    mapTypeComboBox.setSelectedItem(type);
                    break;
                }
      		}
      	}
        mapTypeComboBox.addItemListener(mapTypeListener);
    }

    public abstract String[] getMapTypes();

    /**
     * 切换图表类型
     */
    public void fireChange(){
        ChartCollection chartCollection = (ChartCollection)chartDesigner.getTarget().getChartCollection();
        Chart chart =chartCollection.getSelectedChart();
        if(chart.getPlot().getPlotStyle() != ChartConstants.STYLE_NONE){
            resetChart(chart);
        }
        chart.switchPlot(getSelectedClonedPlot());

        if(chart.getPlot().getPlotStyle() != ChartConstants.STYLE_NONE){
            resetChart(chart);
        }
        chartDesigner.fireTargetModified();
        ChartDesignEditPane.getInstance().populateSelectedTabPane();
    }

    protected void resetChart(Chart chart){
        chart.setTitle(new Title(chart.getTitle().getTextObject()));
        chart.setBorderStyle(Constants.LINE_NONE);
        chart.setBorderColor(new Color(150, 150, 150));
        chart.setBackground(null);
    }

    protected abstract Plot getSelectedClonedPlot();

    /**
     * 触发更新
     */
    public void fireTargetModified() {
        chartDesigner.fireTargetModified();
   	}
}