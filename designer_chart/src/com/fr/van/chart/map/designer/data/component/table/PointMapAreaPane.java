package com.fr.van.chart.map.designer.data.component.table;

import com.fr.plugin.chart.map.data.VanMapTableDefinitionProvider;
import com.fr.van.chart.map.designer.data.component.LongitudeLatitudeAndArea;
import com.fr.van.chart.map.designer.data.contentpane.table.VanPointMapPlotTableDataContentPane;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.util.List;

/**
 * Created by hufan on 2016/12/23.
 */
public class PointMapAreaPane extends AbstractLongLatAreaPane {
    protected AreaPane areaPane;

    public PointMapAreaPane(VanPointMapPlotTableDataContentPane.LongLatAreaTableComboPane parentPane) {
        JPanel contentPane = createContentPane(parentPane);
        this.setLayout(new BorderLayout());
        this.add(contentPane, BorderLayout.CENTER);
    }

    protected JPanel createContentPane(VanPointMapPlotTableDataContentPane.LongLatAreaTableComboPane parentPane) {
        initAreaPane(parentPane);
        JPanel content = new JPanel(new BorderLayout());
        content.add(areaPane, BorderLayout.CENTER);
        return content;
    }

    protected void initAreaPane(VanPointMapPlotTableDataContentPane.LongLatAreaTableComboPane parentPane) {
        areaPane = new AreaPane(parentPane);
    }

    public void refreshBoxListWithSelectTableData(List list) {
        areaPane.refreshBoxListWithSelectTableData(list);
    }

    @Override
    public void checkBoxUse(boolean hasUse) {
        areaPane.checkBoxUse(hasUse);
    }

    @Override
    public void clearAllBoxList() {
        areaPane.clearAllBoxList();
    }

    @Override
    public boolean isSelectedItem() {
        return areaPane.isSelectedItem();
    }

    @Override
    public void populate(VanMapTableDefinitionProvider mapTableDefinitionProvider) {
        LongitudeLatitudeAndArea longLatArea = new LongitudeLatitudeAndArea();
        longLatArea.setArea(mapTableDefinitionProvider.getCategoryName());
        longLatArea.setLongitude(mapTableDefinitionProvider.getLongitude());
        longLatArea.setLatitude(mapTableDefinitionProvider.getLatitude());
        areaPane.populate(longLatArea);
    }

    @Override
    public void update(VanMapTableDefinitionProvider mapTableDefinitionProvider) {
        LongitudeLatitudeAndArea longLatArea = areaPane.update();
        mapTableDefinitionProvider.setCategoryName(longLatArea.getArea() == null ? null : longLatArea.getArea().toString());
        mapTableDefinitionProvider.setLongitude(longLatArea.getLongitude() == null ? null : longLatArea.getLongitude().toString());
        mapTableDefinitionProvider.setLatitude(longLatArea.getLatitude() == null ? null : longLatArea.getLatitude().toString());
    }
}
