/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.mainframe.chart.gui.other;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartdata.TopDefinition;
import com.fr.design.gui.imenutable.UIMenuNameableCreator;
import com.fr.design.mainframe.chart.gui.ChartOtherPane;
import com.fr.general.Inter;
import com.fr.js.*;
import com.fr.third.org.hsqldb.lib.HashMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date: 14-10-16
 * Time: 下午2:12
 */
public class ChartDesignerInteractivePane extends ChartInteractivePane {
    public ChartDesignerInteractivePane(ChartOtherPane parent) {
        super(parent);
    }

    protected List<UIMenuNameableCreator> refreshList(HashMap map) {
   		List<UIMenuNameableCreator> list = new ArrayList<UIMenuNameableCreator>();

   		list.add(new UIMenuNameableCreator(Inter.getLocText("Hyperlink-Web_link"),
                   new WebHyperlink(), getUseMap(map, WebHyperlink.class)));
   		list.add(new UIMenuNameableCreator("JavaScript", new JavaScriptImpl(), getUseMap(map, JavaScriptImpl.class)));
        list.add(new UIMenuNameableCreator(Inter.getLocText("RelatedChart"),null,null));

   		return list;
   	}

    protected void populateAutoRefresh(Chart chart){
        super.populateAutoRefresh(chart);
        if(chart.getFilterDefinition() != null){
            TopDefinition definition = (TopDefinition)chart.getFilterDefinition();
            isAutoRefresh.setEnabled(definition.isSupportAutoRefresh());
            if(!isAutoRefresh.isEnabled()){
                isAutoRefresh.setSelected(false);
            }
            autoRefreshTime.setEnabled(definition.isSupportAutoRefresh());
        }
    }
}