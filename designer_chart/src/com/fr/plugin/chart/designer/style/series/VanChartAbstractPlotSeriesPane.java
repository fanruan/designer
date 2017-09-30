package com.fr.plugin.chart.designer.style.series;

import com.fr.base.chart.chartdata.model.DataProcessor;
import com.fr.base.chart.chartdata.model.LargeDataModel;
import com.fr.base.chart.chartdata.model.NormalDataModel;
import com.fr.chart.base.AttrAlpha;
import com.fr.chart.base.AttrBorder;
import com.fr.chart.base.ChartConstants;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.chart.chartglyph.ConditionCollection;
import com.fr.design.gui.frpane.UINumberDragPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.style.ChartFillStylePane;
import com.fr.design.mainframe.chart.gui.style.series.AbstractPlotSeriesPane;
import com.fr.general.Inter;
import com.fr.plugin.chart.VanChartAttrHelper;
import com.fr.plugin.chart.attr.plot.VanChartPlot;
import com.fr.plugin.chart.attr.plot.VanChartRectanglePlot;
import com.fr.plugin.chart.attr.radius.VanChartRadiusPlot;
import com.fr.plugin.chart.base.AttrAreaSeriesFillColorBackground;
import com.fr.plugin.chart.base.AttrEffect;
import com.fr.plugin.chart.base.AttrLabel;
import com.fr.plugin.chart.base.VanChartAttrLine;
import com.fr.plugin.chart.base.VanChartAttrMarker;
import com.fr.plugin.chart.base.VanChartAttrTrendLine;
import com.fr.plugin.chart.custom.style.VanChartCustomStylePane;
import com.fr.plugin.chart.designer.TableLayout4VanChartHelper;
import com.fr.plugin.chart.designer.component.VanChartAreaSeriesFillColorPane;
import com.fr.plugin.chart.designer.component.VanChartBeautyPane;
import com.fr.plugin.chart.designer.component.VanChartFillStylePane;
import com.fr.plugin.chart.designer.component.VanChartLineTypePane;
import com.fr.plugin.chart.designer.component.VanChartMarkerPane;
import com.fr.plugin.chart.designer.component.VanChartTrendLinePane;
import com.fr.plugin.chart.designer.component.border.VanChartBorderPane;
import com.fr.plugin.chart.map.line.condition.AttrLineEffect;
import com.fr.plugin.chart.pie.RadiusCardLayoutPane;
import com.fr.plugin.chart.scatter.attr.ScatterAttrLabel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * 图表样式-系列抽象界面
 */

public abstract class VanChartAbstractPlotSeriesPane extends AbstractPlotSeriesPane {

    private static final long serialVersionUID = -3909265296019479690L;

    protected VanChartBeautyPane stylePane;//风格

    private VanChartTrendLinePane trendLinePane;//趋势线

    private VanChartLineTypePane lineTypePane;//线

    protected VanChartMarkerPane markerPane;//标记点类型

    private VanChartAreaSeriesFillColorPane areaSeriesFillColorPane;//填充颜色

    private VanChartBorderPane borderPane;//边框

    private UINumberDragPane transparent;//不透明度

    protected VanChartStackedAndAxisListControlPane stackAndAxisEditPane;//堆積和坐標軸
    protected JPanel stackAndAxisEditExpandablePane;//堆積和坐標軸展开面板

    private RadiusCardLayoutPane radiusPane;//半径设置界面
    private JPanel radiusPaneWithTitle;

    private UIButtonGroup<DataProcessor> largeDataModelGroup;//大数据模式

    protected JPanel contentPane;

    public VanChartAbstractPlotSeriesPane(ChartStylePane parent, Plot plot){
        super(parent, plot);
    }

    protected JPanel getContentPane(boolean custom) {
        if (custom) {
            JScrollPane scrollPane = new JScrollPane();
            scrollPane.setViewportView(getContentInPlotType());
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        }
        JPanel panel = new JPanel(new BorderLayout());
        if (fillStylePane != null) {
            panel.add(fillStylePane, BorderLayout.NORTH);
        }
        panel.add(getContentInPlotType(), BorderLayout.CENTER);
        return panel;
    }

    @Override
    /**
     * 返回 填充界面.
     */
    protected ChartFillStylePane getFillStylePane() {
        //如果是自定義組合圖，則不創建填充界面
        return parentPane instanceof VanChartCustomStylePane ? null : new VanChartFillStylePane();
    }

    //风格
    protected VanChartBeautyPane createStylePane() {
        return parentPane instanceof VanChartCustomStylePane ? null : new VanChartBeautyPane();
    }

    //获取颜色面板
    protected JPanel getColorPane () {
        JPanel panel = new JPanel(new BorderLayout());
        stylePane = createStylePane();
        setColorPaneContent(panel);
        JPanel colorPane = TableLayout4VanChartHelper.createExpandablePaneWithTitle(Inter.getLocText("Plugin-ChartF_Color"), panel);
        panel.setBorder(BorderFactory.createEmptyBorder(10,5,0,0));
        return panel.getComponentCount() == 0 ? null : colorPane;
    }

    //设置色彩面板内容
    protected void setColorPaneContent (JPanel panel) {
        if (stylePane != null) {
            panel.add(stylePane, BorderLayout.CENTER);
        }
    }

    //趋势线
    protected JPanel createTrendLinePane() {
        trendLinePane = new VanChartTrendLinePane();
        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(Inter.getLocText("Chart-Trend_Line"), trendLinePane);
    }

    //线
    protected JPanel createLineTypePane() {
        lineTypePane = getLineTypePane();
        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(Inter.getLocText("Plugin-ChartF_Line"), lineTypePane);
    }

    protected VanChartLineTypePane getLineTypePane() {
        return new VanChartLineTypePane();
    }

    //标记点类型
    protected JPanel createMarkerPane() {
        markerPane = new VanChartMarkerPane();
        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(Inter.getLocText("Plugin-ChartF_Marker"), markerPane);
    }

    //填充颜色
    protected JPanel createAreaFillColorPane() {
        areaSeriesFillColorPane = new VanChartAreaSeriesFillColorPane();
        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(Inter.getLocText("Chart_Area"), areaSeriesFillColorPane);
    }

    //边框（默认没有圆角）
    protected JPanel createBorderPane() {
        borderPane = createDiffBorderPane();
        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(Inter.getLocText("Plugin-ChartF_Border"), borderPane);

    }

    //半径界面
    protected JPanel createRadiusPane() {
        radiusPane = initRadiusPane();
        radiusPaneWithTitle = TableLayout4VanChartHelper.createGapTableLayoutPane(Inter.getLocText("Plugin-ChartF_Radius_Set"), radiusPane);
        return ((VanChartPlot)plot).isInCustom() ? null : radiusPaneWithTitle;
    }

    protected JPanel createLargeDataModelPane() {
        largeDataModelGroup = createLargeDataModelGroup();
        largeDataModelGroup.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                checkLarge();
            }
        });
        JPanel panel = TableLayout4VanChartHelper.createGapTableLayoutPane(Inter.getLocText("Plugin-ChartF_Large_Model"), largeDataModelGroup);
        return createLargeDataModelPane(panel);
    }

    protected void checkLarge() {
        if(largeModel()) {
            AttrLabel attrLabel = ((VanChartPlot) plot).getAttrLabelFromConditionCollection();
            if (attrLabel == null) {
                attrLabel = ((VanChartPlot) this.plot).getDefaultAttrLabel();
                ConditionAttr defaultAttr = plot.getConditionCollection().getDefaultAttr();
                defaultAttr.addDataSeriesCondition(attrLabel);
            }
            attrLabel.setEnable(false);

            resetCustomCondition(plot.getConditionCollection());
        }


        checkCompsEnabledWithLarge();
    }

    protected void checkCompsEnabledWithLarge() {
        if(markerPane != null && largeDataModelGroup != null){
            markerPane.checkLargePlot(largeModel());
        }
    }

    protected void checkLinePane() {
        if(lineTypePane != null && largeDataModelGroup != null){
            lineTypePane.checkLarge(largeModel());
        }
    }


    protected boolean largeModel() {
        return largeDataModelGroup != null && largeDataModelGroup.getSelectedIndex() == 0;
    }

    protected void resetCustomCondition(ConditionCollection conditionCollection) {
        for(int i = 0, len = conditionCollection.getConditionAttrSize(); i < len; i++){
            ConditionAttr conditionAttr = conditionCollection.getConditionAttr(i);
            conditionAttr.remove(AttrLabel.class);
            conditionAttr.remove(ScatterAttrLabel.class);
            conditionAttr.remove(AttrEffect.class);
            conditionAttr.remove(AttrLineEffect.class);

            VanChartAttrMarker attrMarker = conditionAttr.getExisted(VanChartAttrMarker.class);
            if(attrMarker != null && !attrMarker.isCommon()){
                conditionAttr.remove(VanChartAttrMarker.class);
            }
        }
    }

    protected JPanel createLargeDataModelPane(JPanel jPanel) {
        JPanel panel = TableLayout4VanChartHelper.createExpandablePaneWithTitle(Inter.getLocText("Plugin-ChartF_Large_Data"), jPanel);
        return panel;
    }

    protected UIButtonGroup<DataProcessor> createLargeDataModelGroup() {
        String[] strings = new String[]{Inter.getLocText("Plugin-ChartF_Open"), Inter.getLocText("Plugin-ChartF_Close")};
        DataProcessor[] values = new DataProcessor[]{new LargeDataModel(), new NormalDataModel()};
        return new UIButtonGroup<DataProcessor>(strings, values);
    }

    protected RadiusCardLayoutPane initRadiusPane() {
        return new RadiusCardLayoutPane();
    }

    protected VanChartBorderPane createDiffBorderPane() {
        return new VanChartBorderPane();
    }


    //不透明度
    protected JPanel createAlphaPane() {
        transparent = new UINumberDragPane(0, 100);
        return TableLayout4VanChartHelper.createGapTableLayoutPane(Inter.getLocText("Plugin-ChartF_Alpha"), transparent);
    }

    //堆积和坐标轴设置(自定义柱形图等用到)
    protected JPanel createStackedAndAxisPane() {
        stackAndAxisEditPane = new VanChartStackedAndAxisListControlPane();
        stackAndAxisEditExpandablePane =  TableLayout4VanChartHelper.createExpandablePaneWithTitle(stackAndAxisEditPane.getPaneTitle(), stackAndAxisEditPane);
        return stackAndAxisEditExpandablePane;
    }

    //界面上删除堆积和坐标轴设置
    protected void removeStackWholePane() {
        contentPane.remove(stackAndAxisEditExpandablePane);
        contentPane.repaint();
    }

    /**
     * 更新Plot的属性到系列界面
     */
    public void populateBean(Plot plot) {
        if(plot == null) {
            return;
        }

        checkoutMapType(plot);

        super.populateBean(plot);//配色

        if(stylePane != null){//风格
            stylePane.populateBean(plot.getPlotStyle());
        }

        if(largeDataModelGroup != null){
            largeDataModelGroup.setSelectedItem(plot.getDataProcessor());
        }

        if(stackAndAxisEditPane != null && plot instanceof VanChartRectanglePlot){//堆积和坐标轴
            VanChartRectanglePlot rectanglePlot = (VanChartRectanglePlot)plot;
            if(rectanglePlot.isCustomChart()){
                stackAndAxisEditPane.populate(rectanglePlot);
            } else {
                removeStackWholePane();
            }
        }

        if(radiusPane != null && plot instanceof VanChartRadiusPlot){
            radiusPane.populateBean(plot);
            checkRadiusPane(plot);
        }

        populateCondition(plot.getConditionCollection().getDefaultAttr());

        checkAreaSeriesFillColorPane(plot.getPlotStyle());

        checkCompsEnabledWithLarge();
    }

    /**
     * radius界面是否显示
     * @param plot
     */
    private void checkRadiusPane(Plot plot) {
        radiusPaneWithTitle.setVisible(true);
        if (plot instanceof VanChartPlot){
            if (((VanChartPlot) plot).isInCustom()){
                radiusPaneWithTitle.setVisible(false);
            }
        }
    }

    /**
     * 保存 系列界面的属性到Plot
     */
    public void updateBean(Plot plot) {
        if(plot == null) {
            return;
        }

        //更新之前先更新界面的map类型属性
        checkoutMapType(plot);

        super.updateBean(plot);//配色

        if(stylePane != null){//风格
            plot.setPlotStyle(stylePane.updateBean());
        }

        if(largeDataModelGroup != null){
            plot.setDataProcessor(largeDataModelGroup.getSelectedItem());
        }

        if(stackAndAxisEditPane != null && plot instanceof VanChartRectanglePlot){//堆积和坐标轴
            VanChartRectanglePlot rectanglePlot = (VanChartRectanglePlot)plot;
            if(rectanglePlot.isCustomChart()){
                stackAndAxisEditPane.update(rectanglePlot);
            }
        }

        if (radiusPane != null && plot instanceof VanChartRadiusPlot){
            radiusPane.updateBean(plot);
            checkRadiusPane(plot);
        }

        updateCondition(plot.getConditionCollection().getDefaultAttr());

        checkAreaSeriesFillColorPane(plot.getPlotStyle());
    }

    protected void checkoutMapType(Plot plot){

    }

    protected void checkAreaSeriesFillColorPane(int plotStyle){
        if (areaSeriesFillColorPane != null) {
            areaSeriesFillColorPane.checkoutAlpha(plotStyle == ChartConstants.STYLE_NONE);
        }
    }


    protected void populateCondition(ConditionAttr defaultAttr){
        if(trendLinePane != null){//趋势线
            VanChartAttrTrendLine attrTrendLine =(VanChartAttrTrendLine)defaultAttr.getExisted(VanChartAttrTrendLine.class);
            trendLinePane.populate(attrTrendLine);
        }
        if(lineTypePane != null){//线-线型、控制断开等
            VanChartAttrLine attrLine = (VanChartAttrLine)defaultAttr.getExisted(VanChartAttrLine.class);
            lineTypePane.populate(attrLine);
        }
        if(markerPane != null){//标记点
            VanChartAttrMarker attrMarker = (VanChartAttrMarker)defaultAttr.getExisted(VanChartAttrMarker.class);
            markerPane.populate(attrMarker);
        }
        if(areaSeriesFillColorPane != null){//填充颜色
            AttrAreaSeriesFillColorBackground seriesFillColorBackground = (AttrAreaSeriesFillColorBackground)defaultAttr.getExisted(AttrAreaSeriesFillColorBackground.class);
            areaSeriesFillColorPane.populate(seriesFillColorBackground);
        }
        if(borderPane != null){//边框
            AttrBorder attrBorder = (AttrBorder)defaultAttr.getExisted(AttrBorder.class);
            if(attrBorder != null){
                borderPane.populate(attrBorder);
            }
        }
        populateAlpha(defaultAttr);
    }

    protected void populateAlpha(ConditionAttr defaultAttr){
        if(transparent != null){//不透明度
            AttrAlpha attrAlpha = (AttrAlpha)defaultAttr.getExisted(AttrAlpha.class);
            if(attrAlpha != null){
                transparent.populateBean(attrAlpha.getAlpha() * VanChartAttrHelper.PERCENT);
            } else {
                //初始值为100
                transparent.populateBean(VanChartAttrHelper.PERCENT);
            }
        }
    }

    protected void updateCondition(ConditionAttr defaultAttr){
        if(trendLinePane != null){
            VanChartAttrTrendLine newTrendLine = trendLinePane.update();
            VanChartAttrTrendLine attrTrendLine =(VanChartAttrTrendLine)defaultAttr.getExisted(VanChartAttrTrendLine.class);
            defaultAttr.remove(attrTrendLine);
            defaultAttr.addDataSeriesCondition(newTrendLine);
        }
        if(lineTypePane != null){
            VanChartAttrLine attrLine = (VanChartAttrLine)defaultAttr.getExisted(VanChartAttrLine.class);
            defaultAttr.remove(attrLine);
            defaultAttr.addDataSeriesCondition(lineTypePane.update());
        }
        if(markerPane != null){
            VanChartAttrMarker newMarker = markerPane.update();
            VanChartAttrMarker attrMarker = (VanChartAttrMarker)defaultAttr.getExisted(VanChartAttrMarker.class);
            defaultAttr.remove(attrMarker);
            defaultAttr.addDataSeriesCondition(newMarker);
        }
        if(areaSeriesFillColorPane != null){
            AttrAreaSeriesFillColorBackground newFillColorBackground = areaSeriesFillColorPane.update();
            AttrAreaSeriesFillColorBackground oldFillColorBackground = defaultAttr.getExisted(AttrAreaSeriesFillColorBackground.class);
            if(oldFillColorBackground != null){
                defaultAttr.remove(oldFillColorBackground);
            }
            defaultAttr.addDataSeriesCondition(newFillColorBackground);
        }
        if(borderPane != null){
            AttrBorder attrBorder = (AttrBorder)defaultAttr.getExisted(AttrBorder.class);
            if(attrBorder == null){
                attrBorder = new AttrBorder();
                defaultAttr.addDataSeriesCondition(attrBorder);
            }
            borderPane.update(attrBorder);
        }
        updateAlpha(defaultAttr);
    }

    protected void updateAlpha(ConditionAttr defaultAttr){
        if(transparent != null){
            AttrAlpha attrAlpha = (AttrAlpha)defaultAttr.getExisted(AttrAlpha.class);
            if(attrAlpha == null){
                attrAlpha = new AttrAlpha();
                defaultAttr.addDataSeriesCondition(attrAlpha);
            }
            attrAlpha.setAlpha((float)(transparent.updateBean()/VanChartAttrHelper.PERCENT));
        }
    }
}