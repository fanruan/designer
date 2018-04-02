package com.fr.van.chart.custom;

import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.mainframe.chart.AbstractChartAttrPane;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.type.AbstractChartTypePane;
import com.fr.van.chart.custom.other.VanChartCustomOtherPane;
import com.fr.van.chart.custom.style.VanChartCustomStylePane;
import com.fr.van.chart.designer.other.VanChartOtherPane;
import com.fr.van.chart.designer.style.VanChartStylePane;
import com.fr.van.chart.vanchart.AbstractIndependentVanChartUI;

/**
 * Created by Mitisky on 16/2/16.
 */
public class CustomIndependentVanChartInterface extends AbstractIndependentVanChartUI {
    /**
     * 图表的类型定义界面类型，就是属性表的第一个界面
     *
     * @return 图表的类型定义界面类型
     */
    @Override
    public AbstractChartTypePane getPlotTypePane() {
        return new VanChartCustomPlotPane();
    }

    /**
     * 图标路径
     *
     * @return 图标路径
     */
    @Override
    public String getIconPath() {
        return "com/fr/design/images/form/toolbar/custom.png";
    }


    @Override
    /**
     * 图表的属性界面数组
     * @return 属性界面
     */
    public AbstractChartAttrPane[] getAttrPaneArray(AttributeChangeListener listener){
        VanChartStylePane stylePane = new VanChartCustomStylePane(listener);
        VanChartOtherPane otherPane = new VanChartCustomOtherPane();
        return new AbstractChartAttrPane[]{stylePane, otherPane};
    }

    public ChartDataPane getChartDataPane(AttributeChangeListener listener){
        return new VanChartCustomDataPane(listener);
    }

    public String getPlotTypeTitle4PopupWindow(){
        return VanChartCustomPlotPane.TITLE;
    }
}