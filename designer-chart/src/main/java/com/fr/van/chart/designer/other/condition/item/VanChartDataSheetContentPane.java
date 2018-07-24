package com.fr.van.chart.designer.other.condition.item;

import com.fr.chart.base.DataSeriesCondition;
import com.fr.chart.chartattr.Plot;
import com.fr.design.condition.ConditionAttributesPane;

import com.fr.plugin.chart.base.AttrDataSheet;
import com.fr.van.chart.designer.style.datasheet.VanchartDataSheetNoCheckPane;

import javax.swing.JPanel;

/**
 * Created by mengao on 2017/5/23.
 */
public class VanChartDataSheetContentPane extends AbstractNormalMultiLineConditionPane {

    private VanchartDataSheetNoCheckPane vanchartDataSheetNoCheckPane;

    public VanChartDataSheetContentPane(ConditionAttributesPane conditionAttributesPane, Plot plot) {
        super(conditionAttributesPane, plot);
    }

    @Override
    protected JPanel initContentPane() {
        this.vanchartDataSheetNoCheckPane = createVanChartDataSheetPane();
        return this.vanchartDataSheetNoCheckPane;
    }


    protected VanchartDataSheetNoCheckPane createVanChartDataSheetPane() {
        return new VanchartDataSheetNoCheckPane();
    }

    @Override
    public String nameForPopupMenuItem() {
        return com.fr.design.i18n.Toolkit.i18nText("Plugin-Chart_Data_Sheet");
    }

    @Override
    protected String getItemLabelString() {
        return com.fr.design.i18n.Toolkit.i18nText("Plugin-Chart_Data_Sheet");
    }

    @Override
    protected String title4PopupWindow() {
        return nameForPopupMenuItem();
    }


    @Override
    public void setDefault() {
        //下面这句话是给各个组件一个默认值
        populate(new AttrDataSheet());
    }

    @Override
    public void populate(DataSeriesCondition condition) {
        if (condition instanceof AttrDataSheet) {
            this.vanchartDataSheetNoCheckPane.populate((AttrDataSheet) condition);
        }
    }

    @Override
    public DataSeriesCondition update() {
        return this.vanchartDataSheetNoCheckPane.update();
    }
}
