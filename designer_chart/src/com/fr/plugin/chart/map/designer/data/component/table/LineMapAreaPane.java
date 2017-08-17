package com.fr.plugin.chart.map.designer.data.component.table;

import com.fr.general.Inter;
import com.fr.plugin.chart.designer.TableLayout4VanChartHelper;
import com.fr.plugin.chart.map.designer.data.component.LongitudeLatitudeAndArea;
import com.fr.plugin.chart.map.data.VanMapTableDefinitionProvider;
import com.fr.plugin.chart.map.designer.data.contentpane.table.VanPointMapPlotTableDataContentPane;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Created by hufan on 2016/12/21.
 */
public class LineMapAreaPane extends PointMapAreaPane {
    private static final int LEFT_GAP = 15;
    private static final int V_GAP = 10;
    protected AreaPane endAreaPane;
    public LineMapAreaPane(VanPointMapPlotTableDataContentPane.LongLatAreaTableComboPane parentPane) {
        super(parentPane);
    }

    protected JPanel createContentPane(VanPointMapPlotTableDataContentPane.LongLatAreaTableComboPane parentPane) {
        initAreaPane(parentPane);
        initEndAreaPane(parentPane);

        JPanel startPane = TableLayout4VanChartHelper.createTitlePane(Inter.getLocText("Plugin-ChartF_Start_Point")+":", areaPane, LEFT_GAP);
        JPanel endPane = TableLayout4VanChartHelper.createTitlePane(Inter.getLocText("Plugin-ChartF_End_Point")+":", endAreaPane, LEFT_GAP);

        JPanel content = new JPanel(new BorderLayout(0, V_GAP));
        content.add(startPane, BorderLayout.NORTH);
        content.add(endPane, BorderLayout.CENTER);
        return content;
    }

    protected void initEndAreaPane(VanPointMapPlotTableDataContentPane.LongLatAreaTableComboPane parentPane) {
        endAreaPane = new AreaPane(parentPane);
    }

    @Override
    public void refreshBoxListWithSelectTableData(List list) {
        super.refreshBoxListWithSelectTableData(list);
        endAreaPane.refreshBoxListWithSelectTableData(list);
    }

    @Override
    public boolean isSelectedItem() {
        return super.isSelectedItem()
                && endAreaPane.isSelectedItem();
    }

    @Override
    public void populate(VanMapTableDefinitionProvider mapTableDefinitionProvider) {
        super.populate(mapTableDefinitionProvider);

        LongitudeLatitudeAndArea endLongLatArea = new LongitudeLatitudeAndArea();
        endLongLatArea.setArea(mapTableDefinitionProvider.getEndAreaName());
        endLongLatArea.setLongitude(mapTableDefinitionProvider.getEndLongitude());
        endLongLatArea.setLatitude(mapTableDefinitionProvider.getEndLatitude());

        endAreaPane.populate(endLongLatArea);
    }

    @Override
    public void update(VanMapTableDefinitionProvider mapTableDefinitionProvider) {
        super.update(mapTableDefinitionProvider);

        LongitudeLatitudeAndArea endLongLatArea = endAreaPane.update();
        mapTableDefinitionProvider.setEndAreaName(endLongLatArea.getArea() == null ? null : endLongLatArea.getArea().toString());
        mapTableDefinitionProvider.setEndLongitude(endLongLatArea.getLongitude() == null ? null : endLongLatArea.getLongitude().toString());
        mapTableDefinitionProvider.setEndLatitude(endLongLatArea.getLatitude() == null ? null : endLongLatArea.getLatitude().toString());
    }

    @Override
    public void checkBoxUse(boolean hasUse) {
        super.checkBoxUse(hasUse);
        endAreaPane.checkBoxUse(hasUse);
    }

    @Override
    public void clearAllBoxList() {
        super.clearAllBoxList();
        endAreaPane.clearAllBoxList();
    }
}
