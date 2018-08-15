package com.fr.van.chart.designer.style.background;

import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.design.gui.controlpane.ShortCut4JControlPane;
import com.fr.general.ComparatorUtils;

import com.fr.general.NameObject;
import com.fr.plugin.chart.attr.DefaultAxisHelper;
import com.fr.plugin.chart.attr.axis.VanChartAlertValue;
import com.fr.plugin.chart.attr.axis.VanChartAxis;
import com.fr.plugin.chart.attr.plot.VanChartRectanglePlot;
import com.fr.stable.Nameable;
import com.fr.van.chart.designer.component.VanChartUIListControlPane;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mengao on 2017/8/22.
 * 自定义警戒线列表面板
 */
public class AlertLineListControlPane extends VanChartUIListControlPane {

    @Override
    public NameableCreator[] createNameableCreators() {
        return new ChartNameObjectCreator[]{new ChartNameObjectCreator(new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_X_Axis"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Y_Axis")},
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Alert_Line"), VanChartAlertValue.class, VanChartAlertValuePane.class)};
    }

    @Override
    protected String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Alert_Line");
    }

    protected String getAddItemText() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Add_Alert_Line");
    }

    protected ShortCut4JControlPane[] createShortcuts() {
        return new ShortCut4JControlPane[]{
                shortCutFactory.moveUpItemShortCut(),
                shortCutFactory.moveDownItemShortCut(),
                shortCutFactory.removeItemShortCut()
        };
    }

    public void populate(Plot plot) {
        this.plot = plot;
        VanChartRectanglePlot rectanglePlot = (VanChartRectanglePlot) plot;
        List<VanChartAxis> xAxisList = rectanglePlot.getXAxisList();
        List<VanChartAxis> yAxisList = rectanglePlot.getYAxisList();
        String[] axisNames = DefaultAxisHelper.getAllAxisNames(rectanglePlot);

        ChartNameObjectCreator[] creators = {new ChartNameObjectCreator(getAlertAxisName(axisNames), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Alert_Line"), VanChartAlertValue.class, getAlertPaneClass())};

        refreshNameableCreator(creators);

        java.util.List<NameObject> nameObjects = new ArrayList<NameObject>();

        for (VanChartAxis axis : xAxisList) {
            List<VanChartAlertValue> values = axis.getAlertValues();
            for (VanChartAlertValue alertValue : values) {
                alertValue.setAxisNamesArray(axisNames);
                alertValue.setAxisName(rectanglePlot.getXAxisName(axis));
                nameObjects.add(new NameObject(alertValue.getAlertPaneSelectName(), alertValue));
            }
        }

        for (VanChartAxis axis : yAxisList) {
            List<VanChartAlertValue> values = axis.getAlertValues();
            for (VanChartAlertValue alertValue : values) {
                alertValue.setAxisNamesArray(axisNames);
                alertValue.setAxisName(rectanglePlot.getYAxisName(axis));
                nameObjects.add(new NameObject(alertValue.getAlertPaneSelectName(), alertValue));
            }
        }

        populate(nameObjects.toArray(new NameObject[nameObjects.size()]));
        doLayout();
    }

    public void update(Plot plot) {

        Nameable[] nameables = this.update();

        VanChartRectanglePlot rectanglePlot = (VanChartRectanglePlot) plot;
        List<VanChartAxis> xAxisList = rectanglePlot.getXAxisList();
        List<VanChartAxis> yAxisList = rectanglePlot.getYAxisList();

        for (VanChartAxis axis : xAxisList) {
            List<VanChartAlertValue> axisAlerts = new ArrayList<VanChartAlertValue>();
            for (int i = 0; i < nameables.length; i++) {
                VanChartAlertValue value = (VanChartAlertValue) ((NameObject) nameables[i]).getObject();
                if (ComparatorUtils.equals(value.getAxisName(), rectanglePlot.getXAxisName(axis))) {
                    value.setAlertPaneSelectName(nameables[i].getName());
                    axisAlerts.add(value);
                }
            }
            axis.setAlertValues(axisAlerts);
        }
        for (VanChartAxis axis : yAxisList) {
            List<VanChartAlertValue> axisAlerts = new ArrayList<VanChartAlertValue>();
            for (int i = 0; i < nameables.length; i++) {
                VanChartAlertValue value = (VanChartAlertValue) ((NameObject) nameables[i]).getObject();
                if (ComparatorUtils.equals(value.getAxisName(), rectanglePlot.getYAxisName(axis))) {
                    value.setAlertPaneSelectName(nameables[i].getName());
                    axisAlerts.add(value);
                }
            }
            axis.setAlertValues(axisAlerts);
        }
    }

    protected Class<? extends BasicBeanPane> getAlertPaneClass() {
        return VanChartAlertValuePane.class;
    }

    protected String[] getAlertAxisName(String[] axisNames) {
        return axisNames;
    }
}
