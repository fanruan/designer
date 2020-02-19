package com.fr.van.chart.map.designer.data.component.report;

import com.fr.plugin.chart.map.data.VanMapReportDefinition;
import com.fr.van.chart.map.designer.data.component.LongitudeLatitudeAndArea;

import javax.swing.JPanel;
import java.awt.BorderLayout;

/**
 * Created by hufan on 2016/12/23.
 */
public class PointMapAreaPane extends AbstractLongLatAreaPane {
    protected AreaPane areaPane;

    public PointMapAreaPane() {
        JPanel contentPane = createContentPane();
        this.setLayout(new BorderLayout());
        this.add(contentPane, BorderLayout.CENTER);
    }

    protected JPanel createContentPane() {
        initAreaPane();
        JPanel content = new JPanel(new BorderLayout());
        content.add(areaPane, BorderLayout.CENTER);
        return content;
    }

    protected void initAreaPane() {
        areaPane = new AreaPane();
    }

    @Override
    public void populate(VanMapReportDefinition vanMapReportDefinition) {
        LongitudeLatitudeAndArea longLatArea = new LongitudeLatitudeAndArea();
        longLatArea.setArea(vanMapReportDefinition.getCategoryName());
        longLatArea.setLongitude(vanMapReportDefinition.getLongitude());
        longLatArea.setLatitude(vanMapReportDefinition.getLatitude());
        areaPane.populate(longLatArea);
    }

    @Override
    public void update(VanMapReportDefinition vanMapReportDefinition) {
        LongitudeLatitudeAndArea longLatArea = areaPane.update();
        vanMapReportDefinition.setCategoryName(longLatArea.getArea() == null ? null : longLatArea.getArea().toString());
        vanMapReportDefinition.setLongitude(longLatArea.getLongitude() == null ? null : longLatArea.getLongitude().toString());
        vanMapReportDefinition.setLatitude(longLatArea.getLatitude() == null ? null : longLatArea.getLatitude().toString());
    }
}
