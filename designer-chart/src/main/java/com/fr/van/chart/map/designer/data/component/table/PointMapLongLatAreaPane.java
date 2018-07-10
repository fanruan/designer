package com.fr.van.chart.map.designer.data.component.table;

import com.fr.van.chart.map.designer.data.contentpane.table.VanPointMapPlotTableDataContentPane;


/**
 * Created by hufan on 2016/12/23.
 */
public class PointMapLongLatAreaPane extends PointMapAreaPane {

    public PointMapLongLatAreaPane(VanPointMapPlotTableDataContentPane.LongLatAreaTableComboPane parentPane) {
        super(parentPane);
    }

    protected void initAreaPane(VanPointMapPlotTableDataContentPane.LongLatAreaTableComboPane parentPane) {
        areaPane = new LongLatAreaPane(parentPane);
    }
}
