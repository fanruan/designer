package com.fr.van.chart.map.designer.data.component.table;


import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.van.chart.map.designer.data.contentpane.table.VanPointMapPlotTableDataContentPane;

import java.awt.Component;

/**
 * Created by hufan on 2016/12/21.
 */
public class LineMapLongLatAreaPane extends LineMapAreaPane {

    public LineMapLongLatAreaPane(VanPointMapPlotTableDataContentPane.LongLatAreaTableComboPane parentPane) {
        super(parentPane);
    }

    protected void initEndAreaPane(VanPointMapPlotTableDataContentPane.LongLatAreaTableComboPane parentPane) {
        endAreaPane = new LongLatAreaPane(parentPane) {
            protected Component[][] getComponent() {
                return new Component[][]{
                        new Component[]{new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_End_Longitude")), longitudeCom},
                        new Component[]{new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_End_Latitude")), latitudeCom},
                        new Component[]{new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_End_Area_Name")), areaNameCom}
                };
            }
        };
    }

    protected void initAreaPane(VanPointMapPlotTableDataContentPane.LongLatAreaTableComboPane parentPane) {
        areaPane = new LongLatAreaPane(parentPane) {
            protected Component[][] getComponent() {
                return new Component[][]{
                        new Component[]{new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Start_Longitude")), longitudeCom},
                        new Component[]{new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Start_Latitude")), latitudeCom},
                        new Component[]{new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Start_Area_Name")), areaNameCom}
                };
            }
        };
    }
}
