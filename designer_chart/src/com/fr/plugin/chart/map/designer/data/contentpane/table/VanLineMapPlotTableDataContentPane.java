package com.fr.plugin.chart.map.designer.data.contentpane.table;


import com.fr.design.mainframe.chart.gui.ChartDataPane;

import com.fr.plugin.chart.map.designer.data.component.table.LineMapAreaPane;
import com.fr.plugin.chart.map.designer.data.component.table.LineMapLongLatAreaPane;
import com.fr.plugin.chart.map.designer.data.component.table.AbstractLongLatAreaPane;


/**
 * Created by hufan on 2016/12/15.
 */
public class VanLineMapPlotTableDataContentPane extends VanPointMapPlotTableDataContentPane{
    public VanLineMapPlotTableDataContentPane(ChartDataPane parent) {
        super(parent);
    }

    protected AbstractLongLatAreaPane createAreaPane(LongLatAreaTableComboPane longLatAreaTableComboPane) {
        return new LineMapAreaPane(longLatAreaTableComboPane);
    }

    protected AbstractLongLatAreaPane createLongLatAreaPane(LongLatAreaTableComboPane longLatAreaTableComboPane) {
        return new LineMapLongLatAreaPane(longLatAreaTableComboPane);
    }
}
