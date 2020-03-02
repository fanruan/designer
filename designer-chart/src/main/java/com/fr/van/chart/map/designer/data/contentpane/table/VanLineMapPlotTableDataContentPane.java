package com.fr.van.chart.map.designer.data.contentpane.table;


import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.van.chart.map.designer.data.component.table.AbstractLongLatAreaPane;
import com.fr.van.chart.map.designer.data.component.table.AreaPane;
import com.fr.van.chart.map.designer.data.component.table.LineMapAreaPane;
import com.fr.van.chart.map.designer.data.component.table.LineMapLongLatAreaPane;
import com.fr.van.chart.map.designer.data.component.table.LongLatAreaPane;

import java.awt.Component;


/**
 * Created by hufan on 2016/12/15.
 */
public class VanLineMapPlotTableDataContentPane extends VanPointMapPlotTableDataContentPane {
    public VanLineMapPlotTableDataContentPane(ChartDataPane parent) {
        super(parent);
    }

    protected AbstractLongLatAreaPane createAreaPane(LongLatAreaTableComboPane longLatAreaTableComboPane) {
        return new LineMapAreaPane(longLatAreaTableComboPane) {
            @Override
            protected void initAreaPane(VanPointMapPlotTableDataContentPane.LongLatAreaTableComboPane parentPane) {
                areaPane = new AreaPane(parentPane) {
                    @Override
                    protected Component[][] getComponent () {
                        return new Component[][]{
                                new Component[]{new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Start_Area_Name")), VanLineMapPlotTableDataContentPane.this.createAreaPanel(areaNameCom)}
                        };
                    }
                };
            }
            @Override
            protected void initEndAreaPane(VanPointMapPlotTableDataContentPane.LongLatAreaTableComboPane parentPane) {
                endAreaPane = new AreaPane(parentPane) {
                    @Override
                    protected Component[][] getComponent () {
                        return new Component[][]{
                                new Component[]{new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_End_Area_Name")), VanLineMapPlotTableDataContentPane.this.createAreaPanel(areaNameCom)}
                        };
                    }
                };
            }
        };
    }

    protected AbstractLongLatAreaPane createLongLatAreaPane(LongLatAreaTableComboPane longLatAreaTableComboPane) {
        return new LineMapLongLatAreaPane(longLatAreaTableComboPane){
            @Override
            protected void initAreaPane(VanPointMapPlotTableDataContentPane.LongLatAreaTableComboPane parentPane) {
                areaPane = new LongLatAreaPane(parentPane) {
                    @Override
                    protected Component[][] getComponent () {
                        return new Component[][]{
                                new Component[]{new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Start_Longitude")), longitudeCom},
                                new Component[]{new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Start_Latitude")), latitudeCom},
                                new Component[]{new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Start_Area_Name")), VanLineMapPlotTableDataContentPane.this.createAreaPanel(areaNameCom)}
                        };
                    }
                };
            }

            @Override
            protected void initEndAreaPane(VanPointMapPlotTableDataContentPane.LongLatAreaTableComboPane parentPane) {
                endAreaPane = new LongLatAreaPane(parentPane){
                    @Override
                    protected Component[][] getComponent () {
                        return new Component[][]{
                                new Component[]{new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_End_Longitude")), longitudeCom},
                                new Component[]{new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_End_Latitude")), latitudeCom},
                                new Component[]{new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_End_Area_Name")), VanLineMapPlotTableDataContentPane.this.createAreaPanel(areaNameCom)}
                        };
                    }
                };
            }
        };
    }
}
