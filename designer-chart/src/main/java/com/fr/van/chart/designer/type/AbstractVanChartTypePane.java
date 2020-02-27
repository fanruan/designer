package com.fr.van.chart.designer.type;

import com.fr.chart.base.AttrFillStyle;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Legend;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.chart.chartglyph.ConditionCollection;
import com.fr.chart.chartglyph.DataSheet;
import com.fr.design.ChartTypeInterfaceManager;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.MultilineLabel;
import com.fr.design.mainframe.chart.gui.type.AbstractChartTypePane;
import com.fr.design.mainframe.chart.gui.type.ChartImagePane;
import com.fr.design.mainframe.chart.info.ChartInfoCollector;
import com.fr.general.Background;
import com.fr.js.NameJavaScriptGroup;
import com.fr.log.FineLoggerFactory;
import com.fr.plugin.chart.attr.plot.VanChartPlot;
import com.fr.plugin.chart.base.VanChartTools;
import com.fr.plugin.chart.base.VanChartZoom;
import com.fr.plugin.chart.vanchart.VanChart;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

public abstract class AbstractVanChartTypePane extends AbstractChartTypePane<Chart> {
    private static final long serialVersionUID = 7743244512351499265L;
    private UICheckBox largeModelCheckBox;

    private boolean samePlot;

    protected boolean isSamePlot() {
        return samePlot;
    }

    //新图表暂时还没有平面3d，渐变高光等布局。
    @Override
    protected String[] getTypeLayoutPath() {
        return new String[0];
    }

    @Override
    protected String[] getTypeLayoutTipName() {
        return new String[0];
    }

    @Override
    protected String[] getTypeTipName() {
        return ChartTypeInterfaceManager.getInstance().getSubName(getPlotID());
    }

    @Override
    public String title4PopupWindow() {
        return ChartTypeInterfaceManager.getInstance().getName(getPlotID());
    }

    @Override
    protected String getPlotTypeID() {
        return getPlotID();
    }

    protected Component[][] getComponentsWithLargeData(JPanel typePane){
        largeModelCheckBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Open_Large_Data_Model"));
        MultilineLabel prompt = new MultilineLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Large_Data_Model_Prompt"));
        prompt.setForeground(Color.red);
        JPanel largeDataPane = new JPanel(new BorderLayout());
        largeDataPane.add(largeModelCheckBox, BorderLayout.CENTER);
        largeDataPane.add(prompt, BorderLayout.SOUTH);

        return new Component[][]{
                new Component[]{typePane},
                new Component[]{largeDataPane}
        };
    }

    /**
     * 更新界面内容
     */
    public void populateBean(Chart chart) {
        for(ChartImagePane imagePane : typeDemo) {
            imagePane.isPressing = false;
        }
        Plot plot = chart.getPlot();
        typeDemo.get(plot.getDetailType()).isPressing = true;
        checkDemosBackground();
    }

    /**
     * 保存界面属性
     */
    public void updateBean(Chart chart) {
        VanChartPlot oldPlot = chart.getPlot();
        VanChartPlot newPlot = getSelectedClonedPlot();
        checkTypeChange(oldPlot);
        samePlot = accept(chart);
        if(typeChanged && samePlot){
            //同一中图表切换不同类型
            cloneOldPlot2New(oldPlot, newPlot);
            chart.setPlot(newPlot);
            resetChartAttr4SamePlot(chart);
        } else if(!samePlot){
            //不同的图表类型切換
            resetChartAttr(chart, newPlot);
            //切换图表时，数据配置不变,分类个数也不变
            newPlot.setCategoryNum(oldPlot.getCategoryNum());
            //切换类型埋点
            ChartInfoCollector.getInstance().updateChartTypeTime(chart);

        }
    }

    protected void resetChartAttr4SamePlot(Chart chart){
        resetRefreshMoreLabelAttr((VanChart) chart);
    }

    protected void resetChartAttr(Chart chart, Plot newPlot){
        chart.setPlot(newPlot);
        if(newPlot.isSupportZoomDirection() && !newPlot.isSupportZoomCategoryAxis()){
            ((VanChart)chart).setVanChartZoom(new VanChartZoom());
        }
        //重置工具栏选项
        ((VanChart)chart).setVanChartTools(createVanChartTools());
        //重置标题选项
        resetTitleAttr(chart);
        //重置监控刷新选项
        resetRefreshMoreLabelAttr((VanChart)chart);
        resetFilterDefinition(chart);

    }

    //默认有标题
    protected void resetTitleAttr(Chart chart){
        VanChartPlot vanChartPlot = (VanChartPlot) chart.getPlot();
        chart.setTitle(vanChartPlot.getDefaultTitle());
    }

    //重置数据配置
    protected void resetFilterDefinition(Chart chart){

    }

    //重置监控刷新面板
    protected void resetRefreshMoreLabelAttr(VanChart chart){
        chart.setRefreshMoreLabel(chart.getDefaultAutoAttrtooltip(chart));
    }

    protected VanChartTools createVanChartTools() {
        return new VanChartTools();
    }

    protected void checkTypeChange(Plot oldPlot){
        for(int i = 0; i < typeDemo.size(); i++){
            if(typeDemo.get(i).isPressing && i != oldPlot.getDetailType()){
                typeChanged = true;
                break;
            }
            typeChanged = false;
        }
    }

    /**
     * 同一个图表， 类型之间切换
     */
    protected void cloneOldPlot2New(Plot oldPlot, Plot newPlot) {
        try {
            if (oldPlot.getLegend() != null) {
                newPlot.setLegend((Legend) oldPlot.getLegend().clone());
            }
            cloneOldConditionCollection(oldPlot, newPlot);

            cloneHotHyperLink(oldPlot, newPlot);

            if (oldPlot.getPlotFillStyle() != null) {
                newPlot.setPlotFillStyle((AttrFillStyle)oldPlot.getPlotFillStyle().clone());
            }
            newPlot.setPlotStyle(oldPlot.getPlotStyle());
            if (oldPlot.getDataSheet() != null) {
                newPlot.setDataSheet((DataSheet)oldPlot.getDataSheet().clone());
            }

            if (oldPlot.getBackground() != null) {
                newPlot.setBackground((Background)oldPlot.getBackground().clone());
            }
            if (oldPlot.getBorderColor() != null) {
                newPlot.setBorderColor(oldPlot.getBorderColor());
            }
            newPlot.setBorderStyle(oldPlot.getBorderStyle());
            newPlot.setRoundRadius(oldPlot.getRoundRadius());
            newPlot.setAlpha(oldPlot.getAlpha());
            newPlot.setShadow(oldPlot.isShadow());

            ((VanChartPlot)newPlot).setCategoryNum( ((VanChartPlot)oldPlot).getCategoryNum());

        } catch (CloneNotSupportedException e) {
            FineLoggerFactory.getLogger().error("Error in change plot");
        }
    }

    protected void cloneHotHyperLink(Plot oldPlot, Plot newPlot) throws CloneNotSupportedException{
        if (oldPlot.getHotHyperLink() != null) {
            newPlot.setHotHyperLink((NameJavaScriptGroup)oldPlot.getHotHyperLink().clone());
        }
    }

    protected void cloneOldDefaultAttrConditionCollection(Plot oldPlot, Plot newPlot) throws CloneNotSupportedException{
        if (oldPlot.getConditionCollection() != null) {
            ConditionCollection newCondition = new ConditionCollection();
            newCondition.setDefaultAttr((ConditionAttr) oldPlot.getConditionCollection().getDefaultAttr().clone());
            newPlot.setConditionCollection(newCondition);
        }
    }

    protected void cloneOldConditionCollection(Plot oldPlot, Plot newPlot) throws CloneNotSupportedException{
        if (oldPlot.getConditionCollection() != null) {
            newPlot.setConditionCollection((ConditionCollection)oldPlot.getConditionCollection().clone());
        }
    }
}