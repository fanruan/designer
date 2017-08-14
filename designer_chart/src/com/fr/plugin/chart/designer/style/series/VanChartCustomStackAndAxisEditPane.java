package com.fr.plugin.chart.designer.style.series;

import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.chart.chartglyph.ConditionCollection;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.frpane.UICorrelationComboBoxPane;
import com.fr.design.gui.imenutable.UIMenuNameableCreator;
import com.fr.general.Inter;
import com.fr.plugin.chart.VanChartAttrHelper;
import com.fr.plugin.chart.attr.DefaultAxisHelper;
import com.fr.plugin.chart.attr.axis.VanChartAxis;
import com.fr.plugin.chart.attr.plot.VanChartRectanglePlot;
import com.fr.plugin.chart.base.AttrSeriesStackAndAxis;
import com.fr.plugin.chart.column.VanChartCustomStackAndAxisConditionPane;
import com.fr.plugin.chart.designer.TableLayout4VanChartHelper;
import com.fr.plugin.chart.designer.component.VanChartUIMenuNameableCreator;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Fangjie on 2016/4/28.
 */
public class VanChartCustomStackAndAxisEditPane extends BasicBeanPane<VanChartRectanglePlot> {
    //堆积和坐标轴
    protected UICorrelationComboBoxPane stackAndAxisPane;
    protected JPanel stackAndAxisWholePane;
    protected JSeparator jSeparator;

    public VanChartCustomStackAndAxisEditPane() {

        initStackedAndAxisPane();

        jSeparator = new JSeparator();

        initContentPane();
    }

    private void initContentPane() {
        this.setLayout(new BorderLayout(0, 5));
        this.add(stackAndAxisWholePane, BorderLayout.CENTER);
        this.add(jSeparator, BorderLayout.SOUTH);
    }

    //堆积和坐标轴设置(自定义柱形图等用到)
    protected JPanel initStackedAndAxisPane() {
        stackAndAxisPane = new UICorrelationComboBoxPane();
        stackAndAxisWholePane = TableLayout4VanChartHelper.createTableLayoutPaneWithTitle(getPaneTitle(), stackAndAxisPane);
        return stackAndAxisWholePane;
    }
    @Override
    public void populateBean(VanChartRectanglePlot plot) {
        java.util.List<VanChartAxis> xAxisList = plot.getXAxisList();
        java.util.List<VanChartAxis> yAxisList = plot.getYAxisList();
        String[] axisXNames = DefaultAxisHelper.getAxisNames(xAxisList);
        String[] axisYNames = DefaultAxisHelper.getAxisNames(yAxisList);

        java.util.List<UIMenuNameableCreator> menuList = new ArrayList<UIMenuNameableCreator>();
        ConditionAttr demo = new ConditionAttr();
        AttrSeriesStackAndAxis seriesStackAndAxis = new AttrSeriesStackAndAxis();
        seriesStackAndAxis.setXAxisNamesArray(axisXNames);
        seriesStackAndAxis.setYAxisNameArray(axisYNames);
        demo.addDataSeriesCondition(seriesStackAndAxis);
        menuList.add(new VanChartUIMenuNameableCreator(getPaneTitle(), demo, getStackAndAxisPaneClass()));
        stackAndAxisPane.refreshMenuAndAddMenuAction(menuList);

        java.util.List<UIMenuNameableCreator> list = new ArrayList<UIMenuNameableCreator>();

        ConditionCollection stackAndAxisCondition = plot.getStackAndAxisCondition();

        for(int i = 0, len = stackAndAxisCondition.getConditionAttrSize(); i < len; i++){
            ConditionAttr conditionAttr = stackAndAxisCondition.getConditionAttr(i);
            AttrSeriesStackAndAxis stackAndAxis = (AttrSeriesStackAndAxis)conditionAttr.getExisted(AttrSeriesStackAndAxis.class);
            stackAndAxis.setXAxisNamesArray(axisXNames);
            stackAndAxis.setYAxisNameArray(axisYNames);
            list.add(new VanChartUIMenuNameableCreator(conditionAttr.getName(), conditionAttr, getStackAndAxisPaneClass()));
        }

        stackAndAxisPane.populateBean(list);
        stackAndAxisPane.doLayout();
    }

    @Override
    public VanChartRectanglePlot updateBean() {
        return null;
    }

    @Override
    public void updateBean(VanChartRectanglePlot plot){
        ConditionCollection stackAndAxisCondition = plot.getStackAndAxisCondition();
        stackAndAxisCondition.clearConditionAttr();

        java.util.List<UIMenuNameableCreator> list = stackAndAxisPane.updateBean();
        for(UIMenuNameableCreator creator : list){
            ConditionAttr conditionAttr = (ConditionAttr)creator.getObj();
            conditionAttr.setName(creator.getName());
            AttrSeriesStackAndAxis seriesStackAndAxis = (AttrSeriesStackAndAxis)conditionAttr.getExisted(AttrSeriesStackAndAxis.class);
            seriesStackAndAxis.setStackID(creator.getName());
            stackAndAxisCondition.addConditionAttr(conditionAttr);
        }
    }

    @Override
    protected String title4PopupWindow() {
        return null;
    }

    protected Class<? extends BasicBeanPane> getStackAndAxisPaneClass() {
        return VanChartCustomStackAndAxisConditionPane.class;
    }

    protected String getPaneTitle(){
        return Inter.getLocText("Plugin-ChartF_StackAndSeries");
    }
}
