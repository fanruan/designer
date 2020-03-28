package com.fr.van.chart.map.designer.data.component.table;

import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.van.chart.map.designer.data.component.LongitudeLatitudeAndArea;
import com.fr.van.chart.map.designer.data.contentpane.table.VanPointMapPlotTableDataContentPane;

import javax.swing.JPanel;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

/**
 * Created by hufan on 2016/12/21.
 */
public class LongLatAreaPane extends AreaPane {
    protected UIComboBox longitudeCom;
    protected UIComboBox latitudeCom;

    public LongLatAreaPane(VanPointMapPlotTableDataContentPane.LongLatAreaTableComboPane parentPane) {
        super(parentPane);
    }

    @Override
    protected JPanel createAreaNamePane() {
        initAreaNameCom();
        initLongitudeCom();
        initLatitudeCom();

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {f, COMPONENT_WIDTH};
        double[] rowSize = {p, p, p};
        Component[][] components = getComponent();

        return TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, 12, 6);
    }

    protected Component[][] getComponent() {
        return new Component[][]{
                new Component[]{new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Longitude")), longitudeCom},
                new Component[]{new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Latitude")), latitudeCom},
                new Component[]{new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Area_Name")), areaNameCom}
        };
    }

    public void refreshBoxListWithSelectTableData(List list) {
        super.refreshBoxListWithSelectTableData(list);
        refreshBoxItems(longitudeCom, list);
        refreshBoxItems(latitudeCom, list);
    }

    protected void initLatitudeCom() {

        latitudeCom = new UIComboBox();

        latitudeCom.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                listener.fireCheckSeriesUse(latitudeCom.getSelectedItem() != null);
                makeToolTipUse(latitudeCom);
            }
        });
    }

    @Override
    public void populate(LongitudeLatitudeAndArea longLatArea) {
        super.populate(longLatArea);
        if (longLatArea.getLongitude() != null) {
            longitudeCom.setSelectedItem(longLatArea.getLongitude());
        }
        if (longLatArea.getLatitude() != null) {
            latitudeCom.setSelectedItem(longLatArea.getLatitude());
        }
    }

    @Override
    public LongitudeLatitudeAndArea update() {
        LongitudeLatitudeAndArea longitudeLatitudeAndArea = super.update();
        longitudeLatitudeAndArea.setLatitude(latitudeCom.getSelectedItem());
        longitudeLatitudeAndArea.setLongitude(longitudeCom.getSelectedItem());
        return longitudeLatitudeAndArea;
    }

    @Override
    public void checkBoxUse(boolean hasUse) {
        super.checkBoxUse(hasUse);
        longitudeCom.setEnabled(hasUse);
        latitudeCom.setEnabled(hasUse);
    }

    public void clearAllBoxList() {
        super.clearAllBoxList();
        clearBoxItems(longitudeCom);
        clearBoxItems(latitudeCom);
    }

    protected void initLongitudeCom() {

        longitudeCom = new UIComboBox();

        longitudeCom.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                listener.fireCheckSeriesUse(longitudeCom.getSelectedItem() != null);
                makeToolTipUse(longitudeCom);
            }
        });
    }

    @Override
    protected String title4PopupWindow() {
        return null;
    }

    public boolean isSelectedItem() {
        return super.isSelectedItem()
                && longitudeCom.getSelectedItem() != null
                && latitudeCom.getSelectedItem() != null;
    }
}
