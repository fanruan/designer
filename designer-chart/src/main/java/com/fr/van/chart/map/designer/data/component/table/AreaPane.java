package com.fr.van.chart.map.designer.data.component.table;

import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.data.table.AbstractTableDataContentPane;
import com.fr.van.chart.map.designer.data.component.LongitudeLatitudeAndArea;
import com.fr.van.chart.map.designer.data.contentpane.table.VanPointMapPlotTableDataContentPane;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

/**
 * Created by hufan on 2016/12/21.
 */
public class AreaPane extends AbstractTableDataContentPane {
    protected VanPointMapPlotTableDataContentPane.LongLatAreaTableComboPane listener;
    protected UIComboBox areaNameCom;

    public AreaPane(VanPointMapPlotTableDataContentPane.LongLatAreaTableComboPane parentPane) {
        this.listener = parentPane;
        this.setLayout(new BorderLayout());
        JPanel panel = createAreaNamePane();
        this.add(panel, BorderLayout.CENTER);
    }

    @Override
    public void updateBean(ChartCollection ob) {

    }

    public void checkBoxUse(boolean hasUse) {
        areaNameCom.setEnabled(hasUse);
    }


    protected JPanel createAreaNamePane() {
        initAreaNameCom();
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {f, COMPONENT_WIDTH};
        double[] rowSize = {p};
        Component[][] components = getComponent();
        return TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, 12, 6);
    }

    protected Component[][] getComponent() {
        return new Component[][]{
                new Component[]{new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Area_Name")), areaNameCom}
        };
    }

    protected void initAreaNameCom() {

        areaNameCom = new UIComboBox();

        areaNameCom.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                listener.fireCheckSeriesUse(areaNameCom.getSelectedItem() != null);
                makeToolTipUse(areaNameCom);
            }
        });
    }

    protected void makeToolTipUse(UIComboBox comBox) {
        if (comBox.getSelectedItem() != null) {
            comBox.setToolTipText(comBox.getSelectedItem().toString());
        } else {
            comBox.setToolTipText(null);
        }
    }

    @Override
    public void refreshBoxListWithSelectTableData(List list) {
        refreshBoxItems(areaNameCom, list);
    }

    public boolean isSelectedItem() {
        return areaNameCom.getSelectedItem() != null;
    }

    public void populate(LongitudeLatitudeAndArea longLatArea) {
        if (longLatArea.getArea() != null) {
            areaNameCom.setSelectedItem(longLatArea.getArea());
        }
    }

    public LongitudeLatitudeAndArea update() {
        LongitudeLatitudeAndArea longLatArea = new LongitudeLatitudeAndArea();
        longLatArea.setArea(areaNameCom.getSelectedItem());
        return longLatArea;
    }

    @Override
    public void clearAllBoxList() {
        clearBoxItems(areaNameCom);
    }
}
