package com.fr.van.chart.map.designer.data.component.report;


import com.fr.design.gui.ilable.BoldFontTextLabel;

import java.awt.Component;

/**
 * Created by hufan on 2016/12/22.
 */
public class LineMapLongLatAreaPane extends LineMapAreaPane {

    protected void initEndAreaPane() {
        endAreaPane = new LongLatAreaPane() {
            protected Component[][] getComponent() {
                return new Component[][]{
                        new Component[]{new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_End_Longitude")), longitude},
                        new Component[]{new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_End_Latitude")), latitude},
                        new Component[]{new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_End_Area_Name")), areaName}
                };
            }
        };
    }

    protected void initAreaPane() {
        areaPane = new LongLatAreaPane() {
            protected Component[][] getComponent() {
                return new Component[][]{
                        new Component[]{new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Start_Longitude")), longitude},
                        new Component[]{new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Start_Latitude")), latitude},
                        new Component[]{new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Start_Area_Name")), areaName}
                };
            }
        };
    }
}
