package com.fr.design.chart.axis;

import com.fr.design.mainframe.chart.gui.style.axis.ChartAxisUsePane;


/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date   : 11-12-2
 * Time   : 下午2:40
 */
public class AxisStyleObject {
    private String name;
    private ChartAxisUsePane axisStylePane;

    public AxisStyleObject(String name, ChartAxisUsePane axisStylePane) {
        this.name = name;
        this.axisStylePane = axisStylePane;
    }

    public String getName() {
        return name;
    }

    public ChartAxisUsePane getAxisStylePane() {
        return axisStylePane;
    }
}