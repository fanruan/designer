package com.fr.van.chart.designer.style.series;

import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.chart.chartglyph.ConditionCollection;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.design.gui.controlpane.ShortCut4JControlPane;
import com.fr.general.NameObject;
import com.fr.plugin.chart.attr.DefaultAxisHelper;
import com.fr.plugin.chart.attr.plot.VanChartRectanglePlot;
import com.fr.plugin.chart.base.AttrSeriesStackAndAxis;
import com.fr.stable.Nameable;
import com.fr.van.chart.column.VanChartCustomStackAndAxisConditionPane;
import com.fr.van.chart.designer.component.VanChartUIListControlPane;

import java.util.ArrayList;

/**
 * Created by mengao on 2017/9/11.
 * 堆积和坐标轴面板
 */
public class VanChartStackedAndAxisListControlPane extends VanChartUIListControlPane {


    @Override
    public NameableCreator[] createNameableCreators() {
        return new StackedAndAxisNameObjectCreator[]{new StackedAndAxisNameObjectCreator(new AttrSeriesStackAndAxis(), getPaneTitle(), ConditionAttr.class, getStackAndAxisPaneClass())};
    }

    @Override
    protected String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Stack_And_Series");
    }

    protected String getAddItemText() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Add");
    }

    protected ShortCut4JControlPane[] createShortcuts() {
        return new ShortCut4JControlPane[]{
                shortCutFactory.moveUpItemShortCut(),
                shortCutFactory.moveDownItemShortCut(),
                shortCutFactory.removeItemShortCut()
        };
    }

    public void populate(VanChartRectanglePlot plot) {
        setPlot(plot);
        String[] axisXNames = DefaultAxisHelper.getXAxisNames(plot);
        String[] axisYNames = DefaultAxisHelper.getYAxisNames(plot);

        java.util.List<StackedAndAxisNameObjectCreator> menuList = new ArrayList<StackedAndAxisNameObjectCreator>();
        AttrSeriesStackAndAxis seriesStackAndAxis = new AttrSeriesStackAndAxis();
        seriesStackAndAxis.setXAxisNamesArray(axisXNames);
        seriesStackAndAxis.setYAxisNameArray(axisYNames);
        menuList.add(new StackedAndAxisNameObjectCreator(seriesStackAndAxis, getPaneTitle(), ConditionAttr.class, getStackAndAxisPaneClass()));

        refreshNameableCreator(menuList.toArray(new StackedAndAxisNameObjectCreator[menuList.size()]));

        java.util.List<NameObject> nameObjects = new ArrayList<NameObject>();

        ConditionCollection stackAndAxisCondition = plot.getStackAndAxisCondition();

        for (int i = 0, len = stackAndAxisCondition.getConditionAttrSize(); i < len; i++) {
            ConditionAttr conditionAttr = stackAndAxisCondition.getConditionAttr(i);
            AttrSeriesStackAndAxis stackAndAxis = (AttrSeriesStackAndAxis) conditionAttr.getExisted(AttrSeriesStackAndAxis.class);
            stackAndAxis.setXAxisNamesArray(axisXNames);
            stackAndAxis.setYAxisNameArray(axisYNames);
            nameObjects.add(new NameObject(conditionAttr.getName(), conditionAttr));
        }

        populate(nameObjects.toArray(new NameObject[nameObjects.size()]));
        doLayout();
    }

    @Override
    protected void update(Plot plot) {
        VanChartRectanglePlot rectanglePlot = (VanChartRectanglePlot) plot;
        ConditionCollection stackAndAxisCondition = rectanglePlot.getStackAndAxisCondition();
        stackAndAxisCondition.clearConditionAttr();

        Nameable[] nameables = this.update();
        for (Nameable nameable : nameables) {
            ConditionAttr conditionAttr = (ConditionAttr) ((NameObject) nameable).getObject();
            conditionAttr.setName(nameable.getName());
            AttrSeriesStackAndAxis seriesStackAndAxis = (AttrSeriesStackAndAxis) conditionAttr.getExisted(AttrSeriesStackAndAxis.class);
            seriesStackAndAxis.setStackID(nameable.getName());
            stackAndAxisCondition.addConditionAttr(conditionAttr);
        }
    }

    protected Class<? extends BasicBeanPane> getStackAndAxisPaneClass() {
        return VanChartCustomStackAndAxisConditionPane.class;
    }

    public String getPaneTitle() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Stack_And_Series");
    }
}
