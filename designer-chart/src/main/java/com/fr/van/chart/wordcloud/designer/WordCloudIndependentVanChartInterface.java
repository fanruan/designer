package com.fr.van.chart.wordcloud.designer;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.chartx.AbstractVanSingleDataPane;
import com.fr.design.chartx.fields.diff.WordCloudCellDataFieldsPane;
import com.fr.design.chartx.fields.diff.WordCloudDataSetFieldsPane;
import com.fr.design.chartx.single.SingleDataPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.i18n.Toolkit;
import com.fr.design.mainframe.chart.AbstractChartAttrPane;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.type.AbstractChartTypePane;
import com.fr.plugin.chart.vanchart.VanChart;
import com.fr.van.chart.designer.other.VanChartInteractivePaneWithOutSort;
import com.fr.van.chart.designer.other.VanChartOtherPane;
import com.fr.van.chart.designer.other.zoom.ZoomPane;
import com.fr.van.chart.designer.other.zoom.ZoomPaneWithOutMode;
import com.fr.van.chart.designer.style.VanChartStylePane;
import com.fr.van.chart.vanchart.AbstractIndependentVanChartUI;
import com.fr.van.chart.wordcloud.designer.other.VanChartWordCloudConditionPane;
import com.fr.van.chart.wordcloud.designer.style.VanChartWordCloudSeriesPane;
import com.fr.van.chart.wordcloud.designer.type.VanChartWordCloudTypePane;

import java.util.List;

/**
 * Created by Mitisky on 16/11/29.
 */
public class WordCloudIndependentVanChartInterface extends AbstractIndependentVanChartUI{
    /**
     * 图表的类型定义界面类型，就是属性表的第一个界面
     *
     * @return 图表的类型定义界面类型
     */
    @Override
    public AbstractChartTypePane getPlotTypePane() {
        return new VanChartWordCloudTypePane();
    }

    @Override
    public String getName() {
        return Toolkit.i18nText("Fine-Design_Chart_Word_Cloud");
    }

    @Override
    public String[] getDemoImagePath() {
        return new String[]{
                "com/fr/plugin/chart/demo/image/43.png"
        };
    }

    /**
     * 图标路径
     *
     * @return 图标路径
     */
    @Override
    public String getIconPath() {
        return "com/fr/design/images/form/toolbar/wordcloud.png";
    }

    @Override
    public BasicBeanPane<Plot> getPlotSeriesPane(ChartStylePane parent, Plot plot) {
        return new VanChartWordCloudSeriesPane(parent, plot);
    }

    @Override
    public ConditionAttributesPane getPlotConditionPane(Plot plot) {
        return new VanChartWordCloudConditionPane(plot);
    }

    /**
     * 图表的属性界面数组
     * @return 属性界面
     */
    public AbstractChartAttrPane[] getAttrPaneArray(AttributeChangeListener listener){
        VanChartStylePane stylePane = new VanChartStylePane(listener){
            @Override
            protected void createVanChartLabelPane(List<BasicPane> paneList) {
            }
        };
        VanChartOtherPane otherPane = new VanChartOtherPane(){
            @Override
            protected BasicBeanPane<Chart> createInteractivePane() {
                return new VanChartInteractivePaneWithOutSort(){

                    @Override
                    protected ZoomPane createZoomPane() {
                        return new ZoomPaneWithOutMode();
                    }
                };
            }
        };
        return new AbstractChartAttrPane[]{stylePane, otherPane};
    }

    @Override
    public ChartDataPane getChartDataPane(AttributeChangeListener listener) {
        return new AbstractVanSingleDataPane(listener) {
            @Override
            protected SingleDataPane createSingleDataPane(VanChart vanChart) {
                return new SingleDataPane(new WordCloudDataSetFieldsPane(), new WordCloudCellDataFieldsPane());
            }
        };
    }
}
