package com.fr.plugin.chart.map.designer.data.component.report;



/**
 * Created by hufan on 2016/12/22.
 */
public class LineMapLongLatAreaPane extends LineMapAreaPane {

    protected void initEndAreaPane() {
        endAreaPane = new LongLatAreaPane();
    }

    protected void initAreaPane() {
        areaPane = new LongLatAreaPane();
    }
}
