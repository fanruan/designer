package com.fr.plugin.chart.map.designer.data.component.report;

import com.fr.general.Inter;
import com.fr.plugin.chart.designer.TableLayout4VanChartHelper;
import com.fr.plugin.chart.map.designer.data.component.LongitudeLatitudeAndArea;
import com.fr.plugin.chart.map.data.VanMapReportDefinition;

import javax.swing.*;
import java.awt.*;

/**
 * Created by hufan on 2016/12/22.
 */
public class LineMapAreaPane extends PointMapAreaPane {
    private static final int LEFT_GAP = 15;
    private static final int V_GAP = 10;
    protected AreaPane endAreaPane;

    protected JPanel createContentPane() {
        initAreaPane();
        initEndAreaPane();
        JPanel panel1 = TableLayout4VanChartHelper.createTitlePane(Inter.getLocText("Plugin-ChartF_Start_Point")+":", areaPane, LEFT_GAP);
        JPanel panel2 = TableLayout4VanChartHelper.createTitlePane(Inter.getLocText("Plugin-ChartF_End_Point")+":", endAreaPane, LEFT_GAP);

        JPanel content = new JPanel(new BorderLayout(0, V_GAP));
        content.add(panel1, BorderLayout.NORTH);
        content.add(panel2, BorderLayout.CENTER);
        return content;
    }

    protected void initEndAreaPane() {
        endAreaPane = new AreaPane();
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
