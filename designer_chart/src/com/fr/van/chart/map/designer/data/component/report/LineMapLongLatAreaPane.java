package com.fr.van.chart.map.designer.data.component.report;


import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.general.Inter;

import java.awt.Component;

/**
 * Created by hufan on 2016/12/22.
 */
public class LineMapLongLatAreaPane extends LineMapAreaPane {

    protected void initEndAreaPane() {
        endAreaPane = new LongLatAreaPane(){
            protected Component[][] getComponent () {
                return new Component[][]{
                        new Component[]{new BoldFontTextLabel(Inter.getLocText(new String[]{"Plugin-ChartF_End", "Plugin-ChartF_Longitude"})), longitude},
                        new Component[]{new BoldFontTextLabel(Inter.getLocText(new String[]{"Plugin-ChartF_End", "Plugin-ChartF_Latitude"})), latitude},
                        new Component[]{new BoldFontTextLabel(Inter.getLocText(new String[]{"Plugin-ChartF_End", "FR-Chart-Area_Name"})), areaName}
                };
            }
        };
    }

    protected void initAreaPane() {
        areaPane = new LongLatAreaPane(){
            protected Component[][] getComponent () {
                return new Component[][]{
                        new Component[]{new BoldFontTextLabel(Inter.getLocText(new String[]{"Plugin-ChartF_Start", "Plugin-ChartF_Longitude"})), longitude},
                        new Component[]{new BoldFontTextLabel(Inter.getLocText(new String[]{"Plugin-ChartF_Start", "Plugin-ChartF_Latitude"})), latitude},
                        new Component[]{new BoldFontTextLabel(Inter.getLocText(new String[]{"Plugin-ChartF_Start", "FR-Chart-Area_Name"})), areaName}
                };
            }
        };
    }
}
