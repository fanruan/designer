/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.mainframe.chart.gui;

import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartattr.GisMapPlot;
import com.fr.chart.chartattr.MapPlot;
import com.fr.design.chart.report.GisMapDataPane4Chart;
import com.fr.design.chart.report.MapDataPane4Chart;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.mainframe.chart.gui.data.ImportSetChartDataPane;
import com.fr.general.FRLogger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-10-15
 * Time: 下午1:57
 */
public class ChartDesignerDataPane extends ChartDataPane {
    private AttributeChangeListener listener;

    public ChartDesignerDataPane(AttributeChangeListener listener) {
        super(listener);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                FRLogger.getLogger().info("SD");
            }
        });
        this.listener = listener;
    }

    @Override
   	protected JPanel createContentPane() {
   		contentsPane = new ImportSetChartDataPane(listener,ChartDesignerDataPane.this);
   		return contentsPane;
   	}


    protected void repeatLayout(ChartCollection collection) {
   		if(contentsPane != null) {
   			this.remove(contentsPane);
   		}

   		this.setLayout(new BorderLayout(0, 0));
        if (collection.getSelectedChart().getPlot() instanceof MapPlot) {
      		contentsPane = new MapDataPane4Chart(listener,this);
      	}else if(collection.getSelectedChart().getPlot() instanceof GisMapPlot){
            contentsPane = new GisMapDataPane4Chart(listener,this);
        } else{
            contentsPane = new ImportSetChartDataPane(listener,ChartDesignerDataPane.this);
        }
   	}

    /**
     * 主要用于图表设计器
     * @return 是
     */
    public boolean isNeedPresentPaneWhenFilterData(){
        return true;
    }
}