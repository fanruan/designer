package com.fr.plugin.chart.map.designer.data.component.table;


import com.fr.plugin.chart.map.designer.data.contentpane.table.VanPointMapPlotTableDataContentPane;

/**
 * Created by hufan on 2016/12/21.
 */
public class LineMapLongLatAreaPane extends LineMapAreaPane {

    public LineMapLongLatAreaPane(VanPointMapPlotTableDataContentPane.LongLatAreaTableComboPane parentPane) {
        super(parentPane);
    }

    protected void initEndAreaPane(VanPointMapPlotTableDataContentPane.LongLatAreaTableComboPane parentPane) {
        endAreaPane = new LongLatAreaPane(parentPane);
    }

    protected void initAreaPane(VanPointMapPlotTableDataContentPane.LongLatAreaTableComboPane parentPane) {
        areaPane = new LongLatAreaPane(parentPane);
    }
}
