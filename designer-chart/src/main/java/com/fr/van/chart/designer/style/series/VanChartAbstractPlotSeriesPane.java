package com.fr.van.chart.designer.style.series;

import com.fr.base.chart.chartdata.model.DataProcessor;
import com.fr.base.chart.chartdata.model.LargeDataModel;
import com.fr.base.chart.chartdata.model.NormalDataModel;
import com.fr.chart.base.AttrAlpha;
import com.fr.chart.base.AttrBorder;
import com.fr.chart.base.ChartConstants;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.design.gui.frpane.UINumberDragPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.style.ChartFillStylePane;
import com.fr.design.mainframe.chart.gui.style.series.AbstractPlotSeriesPane;
import com.fr.plugin.chart.VanChartAttrHelper;
import com.fr.plugin.chart.attr.plot.VanChartPlot;
import com.fr.plugin.chart.attr.plot.VanChartRectanglePlot;
import com.fr.plugin.chart.attr.radius.VanChartRadiusPlot;
import com.fr.plugin.chart.base.AttrAreaSeriesFillColorBackground;
import com.fr.plugin.chart.base.AttrLabel;
import com.fr.plugin.chart.base.VanChartAttrLine;
import com.fr.plugin.chart.base.VanChartAttrMarker;
import com.fr.plugin.chart.base.VanChartAttrTrendLine;
import com.fr.van.chart.custom.style.VanChartCustomStylePane;
import com.fr.van.chart.designer.PlotFactory;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;
import com.fr.van.chart.designer.component.VanChartAreaSeriesFillColorPane;
import com.fr.van.chart.designer.component.VanChartBeautyPane;
import com.fr.van.chart.designer.component.VanChartFillStylePane;
import com.fr.van.chart.designer.component.VanChartLineTypePane;
import com.fr.van.chart.designer.component.VanChartMarkerPane;
import com.fr.van.chart.designer.component.VanChartTrendLinePane;
import com.fr.van.chart.designer.component.border.VanChartBorderPane;
import com.fr.van.chart.designer.other.VanChartInteractivePane;
import com.fr.van.chart.pie.RadiusCardLayoutPane;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;

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

    //大数据模式 恢复用注释。下面1行删除。
    private UIButtonGroup<DataProcessor> largeDataModelGroup;//大数据模式

    protected JPanel contentPane;

    public VanChartAbstractPlotSeriesPane(ChartStylePane parent, Plot plot) {
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
    protected JPanel getColorPane() {
        JPanel panel = new JPanel(new BorderLayout());
        stylePane = createStylePane();
        setColorPaneContent(panel);
        JPanel colorPane = TableLayout4VanChartHelper.createExpandablePaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Color"), panel);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 5, 0, 0));
        return panel.getComponentCount() == 0 ? null : colorPane;
    }

    //设置色彩面板内容
    protected void setColorPaneContent(JPanel panel) {
        if (stylePane != null) {
            panel.add(stylePane, BorderLayout.CENTER);
        }
    }

    //趋势线
    protected JPanel createTrendLinePane() {
        trendLinePane = new VanChartTrendLinePane();
        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_TrendLine"), trendLinePane);
    }

    //线
    protected JPanel createLineTypePane() {
        lineTypePane = getLineTypePane();
        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Line"), lineTypePane);
    }

    protected VanChartLineTypePane getLineTypePane() {
        return new VanChartLineTypePane();
    }

    //标记点类型
    protected JPanel createMarkerPane() {
        markerPane = new VanChartMarkerPane();
        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Marker"), markerPane);
    }

    //填充颜色
    protected JPanel createAreaFillColorPane() {
        areaSeriesFillColorPane = new VanChartAreaSeriesFillColorPane();
        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Area"), areaSeriesFillColorPane);
    }

    //边框（默认没有圆角）
    protected JPanel createBorderPane() {
        borderPane = createDiffBorderPane();
        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Border"), borderPane);

    }

    //半径界面
    protected JPanel createRadiusPane() {
        radiusPane = initRadiusPane();
        radiusPaneWithTitle = TableLayout4VanChartHelper.createGapTableLayoutPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Radius_Set"), radiusPane);
        return ((VanChartPlot) plot).isInCustom() ? null : radiusPaneWithTitle;
    }

    //大数据模式 恢复用注释。删除下面4个方法 createLargeDataModelPane checkLarge createLargeDataModelPane createLargeDataModelGroup。
    protected JPanel createLargeDataModelPane() {
        largeDataModelGroup = createLargeDataModelGroup();
        largeDataModelGroup.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                checkLarge();
            }
        });
        JPanel panel = TableLayout4VanChartHelper.createGapTableLayoutPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Large_Model"), largeDataModelGroup);
        return createLargeDataModelPane(panel);
    }

    protected void checkLarge() {
        if (largeModel(plot)) {
            AttrLabel attrLabel = ((VanChartPlot) plot).getAttrLabelFromConditionCollection();
            if (attrLabel == null) {
                attrLabel = ((VanChartPlot) this.plot).getDefaultAttrLabel();
                ConditionAttr defaultAttr = plot.getConditionCollection().getDefaultAttr();
                defaultAttr.addDataSeriesCondition(attrLabel);
            }
            attrLabel.setEnable(false);

            VanChartInteractivePane.resetCustomCondition(plot.getConditionCollection());
        }


        checkCompsEnabledWithLarge(plot);
    }

    protected JPanel createLargeDataModelPane(JPanel jPanel) {
        JPanel panel = TableLayout4VanChartHelper.createExpandablePaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Large_Data"), jPanel);
        return panel;
    }

    protected UIButtonGroup<DataProcessor> createLargeDataModelGroup() {
        String[] strings = new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Open"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Close")};
        DataProcessor[] values = new DataProcessor[]{new LargeDataModel(), new NormalDataModel()};
        return new UIButtonGroup<DataProcessor>(strings, values);
    }

    protected void checkCompsEnabledWithLarge(Plot plot) {
        if (markerPane != null) {
            markerPane.checkLargePlot(largeModel(plot));
        }
    }

    protected void checkLinePane() {
        if (lineTypePane != null) {
            lineTypePane.checkLarge(largeModel(plot));
        }
    }


    protected boolean largeModel(Plot plot) {
        return PlotFactory.largeDataModel(plot);
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
        return TableLayout4VanChartHelper.createGapTableLayoutPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Alpha"), transparent);
    }

    //堆积和坐标轴设置(自定义柱形图等用到)
    protected JPanel createStackedAndAxisPane() {
        stackAndAxisEditPane = new VanChartStackedAndAxisListControlPane();
        stackAndAxisEditExpandablePane = TableLayout4VanChartHelper.createExpandablePaneWithTitle(stackAndAxisEditPane.getPaneTitle(), stackAndAxisEditPane);
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
        if (plot == null) {
            return;
        }

        checkoutMapType(plot);

        super.populateBean(plot);//配色

        if (stylePane != null) {//风格
            stylePane.populateBean(plot.getPlotStyle());
        }

        //大数据模式 恢复用注释。下面3行删除。
        if (largeDataModelGroup != null) {
            largeDataModelGroup.setSelectedItem(plot.getDataProcessor());
        }

        if (stackAndAxisEditPane != null && plot instanceof VanChartRectanglePlot) {//堆积和坐标轴
            VanChartRectanglePlot rectanglePlot = (VanChartRectanglePlot) plot;
            if (rectanglePlot.isCustomChart()) {
                stackAndAxisEditPane.populate(rectanglePlot);
            } else {
                removeStackWholePane();
            }
        }

        if (radiusPane != null && plot instanceof VanChartRadiusPlot) {
            radiusPane.populateBean(plot);
            checkRadiusPane(plot);
        }

        populateCondition(plot.getConditionCollection().getDefaultAttr());

        checkAreaSeriesFillColorPane(plot.getPlotStyle());

        checkCompsEnabledWithLarge(plot);
    }

    /**
     * radius界面是否显示
     *
     * @param plot
     */
    private void checkRadiusPane(Plot plot) {
        radiusPaneWithTitle.setVisible(true);
        if (plot instanceof VanChartPlot) {
            if (((VanChartPlot) plot).isInCustom()) {
                radiusPaneWithTitle.setVisible(false);
            }
        }
    }

    /**
     * 保存 系列界面的属性到Plot
     */
    public void updateBean(Plot plot) {
        if (plot == null) {
            return;
        }

        //更新之前先更新界面的map类型属性
        checkoutMapType(plot);

        super.updateBean(plot);//配色

        if (stylePane != null) {//风格
            plot.setPlotStyle(stylePane.updateBean());
        }

        //大数据模式 恢复用注释。下面3行删除。
        if (largeDataModelGroup != null) {
            plot.setDataProcessor(largeDataModelGroup.getSelectedItem());
        }

        if (stackAndAxisEditPane != null && plot instanceof VanChartRectanglePlot) {//堆积和坐标轴
            VanChartRectanglePlot rectanglePlot = (VanChartRectanglePlot) plot;
            if (rectanglePlot.isCustomChart()) {
                stackAndAxisEditPane.update(rectanglePlot);
            }
        }

        if (radiusPane != null && plot instanceof VanChartRadiusPlot) {
            radiusPane.updateBean(plot);
            checkRadiusPane(plot);
        }

        updateCondition(plot.getConditionCollection().getDefaultAttr());

        checkAreaSeriesFillColorPane(plot.getPlotStyle());
    }

    protected void checkoutMapType(Plot plot) {

    }

    protected void checkAreaSeriesFillColorPane(int plotStyle) {
        if (areaSeriesFillColorPane != null) {
            areaSeriesFillColorPane.checkoutAlpha(plotStyle == ChartConstants.STYLE_NONE);
        }
    }


    protected void populateCondition(ConditionAttr defaultAttr) {
        if (trendLinePane != null) {//趋势线
            VanChartAttrTrendLine attrTrendLine = defaultAttr.getExisted(VanChartAttrTrendLine.class);
            trendLinePane.populate(attrTrendLine);
        }
        if (lineTypePane != null) {//线-线型、控制断开等
            VanChartAttrLine attrLine = defaultAttr.getExisted(VanChartAttrLine.class);
            lineTypePane.populate(attrLine);
        }
        if (markerPane != null) {//标记点
            VanChartAttrMarker attrMarker = defaultAttr.getExisted(VanChartAttrMarker.class);
            markerPane.populate(attrMarker);
        }
        if (areaSeriesFillColorPane != null) {//填充颜色
            AttrAreaSeriesFillColorBackground seriesFillColorBackground = defaultAttr.getExisted(AttrAreaSeriesFillColorBackground.class);
            areaSeriesFillColorPane.populate(seriesFillColorBackground);
        }
        if (borderPane != null) {//边框
            AttrBorder attrBorder = defaultAttr.getExisted(AttrBorder.class);
            if (attrBorder != null) {
                borderPane.populate(attrBorder);
            }
        }
        populateAlpha(defaultAttr);
    }

    protected void populateAlpha(ConditionAttr defaultAttr) {
        if (transparent != null) {//不透明度
            AttrAlpha attrAlpha = defaultAttr.getExisted(AttrAlpha.class);
            if (attrAlpha != null) {
                transparent.populateBean(attrAlpha.getAlpha() * VanChartAttrHelper.PERCENT);
            } else {
                //初始值为100
                transparent.populateBean(VanChartAttrHelper.PERCENT);
            }
        }
    }

    protected void updateCondition(ConditionAttr defaultAttr) {
        if (trendLinePane != null) {
            VanChartAttrTrendLine newTrendLine = trendLinePane.update();
            VanChartAttrTrendLine attrTrendLine = defaultAttr.getExisted(VanChartAttrTrendLine.class);
            defaultAttr.remove(attrTrendLine);
            defaultAttr.addDataSeriesCondition(newTrendLine);
        }
        if (lineTypePane != null) {
            VanChartAttrLine attrLine = defaultAttr.getExisted(VanChartAttrLine.class);
            defaultAttr.remove(attrLine);
            defaultAttr.addDataSeriesCondition(lineTypePane.update());
        }
        if (markerPane != null) {
            VanChartAttrMarker newMarker = markerPane.update();
            VanChartAttrMarker attrMarker = defaultAttr.getExisted(VanChartAttrMarker.class);
            defaultAttr.remove(attrMarker);
            defaultAttr.addDataSeriesCondition(newMarker);
        }
        if (areaSeriesFillColorPane != null) {
            AttrAreaSeriesFillColorBackground newFillColorBackground = areaSeriesFillColorPane.update();
            AttrAreaSeriesFillColorBackground oldFillColorBackground = defaultAttr.getExisted(AttrAreaSeriesFillColorBackground.class);
            if (oldFillColorBackground != null) {
                defaultAttr.remove(oldFillColorBackground);
            }
            defaultAttr.addDataSeriesCondition(newFillColorBackground);
        }
        if (borderPane != null) {
            AttrBorder attrBorder = defaultAttr.getExisted(AttrBorder.class);
            if (attrBorder == null) {
                attrBorder = new AttrBorder();
                defaultAttr.addDataSeriesCondition(attrBorder);
            }
            borderPane.update(attrBorder);
        }
        updateAlpha(defaultAttr);
    }

    protected void updateAlpha(ConditionAttr defaultAttr) {
        if (transparent != null) {
            AttrAlpha attrAlpha = defaultAttr.getExisted(AttrAlpha.class);
            if (attrAlpha == null) {
                attrAlpha = new AttrAlpha();
                defaultAttr.addDataSeriesCondition(attrAlpha);
            }
            attrAlpha.setAlpha((float) (transparent.updateBean() / VanChartAttrHelper.PERCENT));
        }
    }
}