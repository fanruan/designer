package com.fr.van.chart.map.designer.data.component.report;

import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.plugin.chart.map.data.VanMapReportDefinition;
import com.fr.van.chart.map.designer.data.component.LongitudeLatitudeAndArea;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;

/**
 * Created by hufan on 2016/12/22.
 */
public class LineMapAreaPane extends PointMapAreaPane {
    private static final int V_GAP = 10;
    protected AreaPane endAreaPane;

    protected JPanel createContentPane() {
        initAreaPane();
        initEndAreaPane();

        JPanel content = new JPanel(new BorderLayout(0, V_GAP));
        content.add(areaPane, BorderLayout.NORTH);
        content.add(endAreaPane, BorderLayout.CENTER);
        return content;
    }

    protected void initEndAreaPane() {
        endAreaPane = new AreaPane() {
            protected Component[][] getComponent() {
                return new Component[][]{
                        new Component[]{new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_End_Area_Name")), areaName}
                };
            }
        };
    }

    @Override
    protected void initAreaPane() {
        areaPane = new AreaPane() {
            protected Component[][] getComponent() {
                return new Component[][]{
                        new Component[]{new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Start_Area_Name")), areaName}
                };
            }
        };
    }

    @Override
    public void populate(VanMapReportDefinition vanMapReportDefinition) {
        super.populate(vanMapReportDefinition);

        LongitudeLatitudeAndArea endLongLatArea = new LongitudeLatitudeAndArea();
        endLongLatArea.setArea(vanMapReportDefinition.getEndAreaName());
        endLongLatArea.setLongitude(vanMapReportDefinition.getEndLongitude());
        endLongLatArea.setLatitude(vanMapReportDefinition.getEndLatitude());

        endAreaPane.populate(endLongLatArea);
    }

    @Override
    public void update(VanMapReportDefinition vanMapReportDefinition) {
        super.update(vanMapReportDefinition);

        LongitudeLatitudeAndArea endLongLatArea = endAreaPane.update();
        vanMapReportDefinition.setEndAreaName(endLongLatArea.getArea() == null ? null : endLongLatArea.getArea().toString());
        vanMapReportDefinition.setEndLongitude(endLongLatArea.getLongitude() == null ? null : endLongLatArea.getLongitude().toString());
        vanMapReportDefinition.setEndLatitude(endLongLatArea.getLatitude() == null ? null : endLongLatArea.getLatitude().toString());

    }
}
