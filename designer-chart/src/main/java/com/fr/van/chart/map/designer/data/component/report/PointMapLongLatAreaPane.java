package com.fr.van.chart.map.designer.data.component.report;

/**
 * Created by hufan on 2016/12/23.
 */
public class PointMapLongLatAreaPane extends PointMapAreaPane {
    public PointMapLongLatAreaPane() {
        super();
    }

    protected void initAreaPane() {
        areaPane = new LongLatAreaPane();
    }
}
