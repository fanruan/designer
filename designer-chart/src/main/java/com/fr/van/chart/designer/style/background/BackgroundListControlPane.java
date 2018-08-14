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
import com.fr.plugin.chart.attr.axis.VanChartCustomIntervalBackground;
import com.fr.plugin.chart.attr.plot.VanChartRectanglePlot;
import com.fr.stable.Nameable;
import com.fr.van.chart.designer.component.VanChartUIListControlPane;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mengao on 2017/8/22.
 * 自定义间隔背景列表面板
 */
public class BackgroundListControlPane extends VanChartUIListControlPane {

    @Override
    public NameableCreator[] createNameableCreators() {
        return new BackgroundNameObjectCreator[]{new BackgroundNameObjectCreator(new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_X_Axis"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Y_Axis")},
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Custom_Interval_Background"), VanChartAlertValue.class, VanChartAlertValuePane.class)};
    }

    @Override
    protected String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Custom_Interval_Background");
    }

    @Override
    public String getAddItemText() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Add_Interval");
    }

    protected ShortCut4JControlPane[] createShortcuts() {
        return new ShortCut4JControlPane[]{
                moveUpItemShortCut(),
                moveDownItemShortCut(),
                removeItemShortCut()
        };
    }

    public void populate(Plot plot) {
        this.plot = plot;
        VanChartRectanglePlot rectanglePlot = (VanChartRectanglePlot) plot;
        List<VanChartAxis> xAxisList = rectanglePlot.getXAxisList();
        List<VanChartAxis> yAxisList = rectanglePlot.getYAxisList();
        String[] axisNames = DefaultAxisHelper.getAllAxisNames(rectanglePlot);

        BackgroundNameObjectCreator[] creators = {new BackgroundNameObjectCreator(getCustomIntervalBackgroundAxisName(axisNames), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Custom_Interval_Background"), VanChartCustomIntervalBackground.class, getIntervalPaneClass())};

        refreshNameableCreator(creators);

        java.util.List<NameObject> nameObjects = new ArrayList<NameObject>();


        for (VanChartAxis axis : xAxisList) {
            List<VanChartCustomIntervalBackground> customIntervalBackgrounds = axis.getCustomIntervalBackgroundArray();
            for (VanChartCustomIntervalBackground background : customIntervalBackgrounds) {
                background.setAxisNamesArray(axisNames);
                background.setAxisName(rectanglePlot.getXAxisName(axis));
                nameObjects.add(new NameObject(background.getCustomIntervalBackgroundSelectName(), background));
            }

        }
        for (VanChartAxis axis : yAxisList) {
            List<VanChartCustomIntervalBackground> customIntervalBackgrounds = axis.getCustomIntervalBackgroundArray();
            for (VanChartCustomIntervalBackground background : customIntervalBackgrounds) {
                background.setAxisNamesArray(axisNames);
                background.setAxisName(rectanglePlot.getYAxisName(axis));
                nameObjects.add(new NameObject(background.getCustomIntervalBackgroundSelectName(), background));
            }
        }
        populate(nameObjects.toArray(new NameObject[nameObjects.size()]));
        doLayout();
    }


    @Override
    protected void update(Plot plot) {
        update(plot, false);
    }

    public void update(Plot plot, boolean isDefaultIntervalBackground) {

        Nameable[] nameables = this.update();

        VanChartRectanglePlot rectanglePlot = (VanChartRectanglePlot) plot;
        List<VanChartAxis> xAxisList = rectanglePlot.getXAxisList();
        List<VanChartAxis> yAxisList = rectanglePlot.getYAxisList();

        for (VanChartAxis axis : xAxisList) {
            List<VanChartCustomIntervalBackground> axisCustomBackground = new ArrayList<VanChartCustomIntervalBackground>();
            if (!isDefaultIntervalBackground) {
                for (int i = 0; i < nameables.length; i++) {
                    VanChartCustomIntervalBackground value = (VanChartCustomIntervalBackground) ((NameObject) nameables[i]).getObject();
                    if (ComparatorUtils.equals(value.getAxisName(), rectanglePlot.getXAxisName(axis))) {
                        value.setCustomIntervalBackgroundSelectName(nameables[i].getName());
                        axisCustomBackground.add(value);
                    }
                }
            }
            axis.setCustomIntervalBackgroundArray(axisCustomBackground);
        }
        for (VanChartAxis axis : yAxisList) {
            List<VanChartCustomIntervalBackground> axisCustomBackground = new ArrayList<VanChartCustomIntervalBackground>();
            if (!isDefaultIntervalBackground) {
                for (int i = 0; i < nameables.length; i++) {
                    VanChartCustomIntervalBackground value = (VanChartCustomIntervalBackground) ((NameObject) nameables[i]).getObject();
                    if (ComparatorUtils.equals(value.getAxisName(), rectanglePlot.getYAxisName(axis))) {
                        value.setCustomIntervalBackgroundSelectName(nameables[i].getName());
                        axisCustomBackground.add(value);
                    }
                }
            }
            axis.setCustomIntervalBackgroundArray(axisCustomBackground);
        }
    }

    protected Class<? extends BasicBeanPane> getIntervalPaneClass() {
        return VanChartCustomIntervalBackgroundPane.class;
    }

    protected String[] getCustomIntervalBackgroundAxisName(String[] axisNames) {
        return axisNames;
    }
}
