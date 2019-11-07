package com.fr.design.mainframe.chart.gui.type;

import com.fr.base.ChartColorMatching;
import com.fr.base.ChartPreStyleConfig;
import com.fr.base.background.ColorBackground;
import com.fr.chart.base.AttrContents;
import com.fr.chart.base.AttrFillStyle;
import com.fr.chart.base.ChartConstants;
import com.fr.chart.base.ChartUtils;
import com.fr.chart.base.DataSeriesCondition;
import com.fr.chart.base.TextAttr;
import com.fr.chart.chartattr.Axis;
import com.fr.chart.chartattr.CategoryPlot;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Legend;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartattr.Title;
import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.design.ChartTypeInterfaceManager;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRFont;
import com.fr.js.NameJavaScriptGroup;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.Constants;

import java.awt.Color;
import java.awt.Font;

/**
 * Created by shine on 2019/7/1.
 */
public abstract class AbstractDeprecatedChartTypePane extends AbstractChartTypePane<Chart> {

    protected void changePlotWithClone(Chart chart, Plot plot) {
        try {
            chart.switchPlot((Plot) plot.clone());
        } catch (CloneNotSupportedException e) {
            FineLoggerFactory.getLogger().error("Error in change plot");
        }
    }


    @Override
    protected String[] getTypeTipName() {
        return ChartTypeInterfaceManager.getInstance().getSubName(getPlotID());
    }

    /**
     * 更新界面属性
     */
    public void populateBean(Chart chart) {
        for (ChartImagePane imagePane : typeDemo) {
            imagePane.isPressing = false;
        }
        for (ChartImagePane imagePane : styleList) {
            imagePane.isPressing = false;
        }

        if (styleList != null && !styleList.isEmpty()) {
            int plotStyle = chart.getPlot().getPlotStyle();
            String styleName = chart.getPlot().getPlotFillStyle().getFillStyleName();

            switch (plotStyle) {
                case ChartConstants.STYLE_SHADE:
                    if (ComparatorUtils.equals(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Style_Retro"), styleName)) {
                        styleList.get(STYLE_SHADE).isPressing = true;
                        lastStyleIndex = STYLE_SHADE;
                    }
                    break;
                case ChartConstants.STYLE_TRANSPARENT:
                    if (ComparatorUtils.equals(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Style_Fresh"), styleName)) {
                        styleList.get(STYLE_TRANSPARENT).isPressing = true;
                        lastStyleIndex = STYLE_TRANSPARENT;
                    }
                    break;
                case ChartConstants.STYLE_3D:
                    if (ComparatorUtils.equals(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Style_Bright"), styleName)) {
                        styleList.get(STYLE_PLANE3D).isPressing = true;
                        lastStyleIndex = STYLE_PLANE3D;
                    }
                    break;
                case ChartConstants.STYLE_OUTER:
                    if (ComparatorUtils.equals(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Style_Bright"), styleName)) {
                        styleList.get(STYLE_HIGHLIGHT).isPressing = true;
                        lastStyleIndex = STYLE_HIGHLIGHT;
                    }
                    break;
                default:
                    lastStyleIndex = -1;
                    break;
            }
            stylePane.setVisible(!(chart.getPlot().isSupport3D()));
            this.repaint();
        }
    }

    public void updateBean(Chart chart) {
        Plot oldPlot = chart.getPlot();
        Plot newPlot = this.setSelectedClonedPlotWithCondition(oldPlot);
        checkTypeChange(oldPlot);//判断图表的类型是否发生变化
        if (styleList != null && !styleList.isEmpty()) {
            if (styleList.get(STYLE_SHADE).isPressing && lastStyleIndex != STYLE_SHADE) {
                lastStyleIndex = STYLE_SHADE;
                chart.setPlot(newPlot);
                chart.getPlot().setPlotStyle(ChartConstants.STYLE_SHADE);
                resetChart(chart);
                createCondition4Shade(chart);
                setPlotFillStyle(chart);
            } else if (styleList.get(STYLE_TRANSPARENT).isPressing && lastStyleIndex != STYLE_TRANSPARENT) {
                lastStyleIndex = STYLE_TRANSPARENT;
                chart.setPlot(newPlot);
                chart.getPlot().setPlotStyle(ChartConstants.STYLE_TRANSPARENT);
                resetChart(chart);
                createCondition4Transparent(chart);
                setPlotFillStyle(chart);
            } else if (styleList.get(STYLE_PLANE3D).isPressing && lastStyleIndex != STYLE_PLANE3D) {
                lastStyleIndex = STYLE_PLANE3D;
                chart.setPlot(newPlot);
                chart.getPlot().setPlotStyle(ChartConstants.STYLE_3D);
                resetChart(chart);
                createCondition4Plane3D(chart);
                setPlotFillStyle(chart);
            } else if (styleList.get(STYLE_HIGHLIGHT).isPressing && lastStyleIndex != STYLE_HIGHLIGHT) {
                lastStyleIndex = STYLE_HIGHLIGHT;
                chart.setPlot(newPlot);
                chart.getPlot().setPlotStyle(ChartConstants.STYLE_OUTER);
                resetChart(chart);
                createCondition4HighLight(chart);
                setPlotFillStyle(chart);
            } else if (lastStyleIndex >= STYLE_SHADE && lastStyleIndex <= STYLE_HIGHLIGHT) {
                if (styleList.get(lastStyleIndex).isDoubleClicked || typeChanged) {
                    chart.setPlot(newPlot);
                    resetChart(chart);
                    styleList.get(lastStyleIndex).isPressing = false;
                    checkDemosBackground();
                    lastStyleIndex = -1;
                }
            }

            stylePane.setVisible(!(chart.getPlot().isSupport3D()));
            this.repaint();
        }
    }

    private Plot setSelectedClonedPlotWithCondition(Plot oldPlot) {
        Plot newPlot = getSelectedClonedPlot();
        if (oldPlot != null && ComparatorUtils.equals(newPlot.getClass(), oldPlot.getClass())) {
            if (oldPlot.getHotHyperLink() != null) {
                NameJavaScriptGroup hotHyper = oldPlot.getHotHyperLink();
                try {
                    newPlot.setHotHyperLink((NameJavaScriptGroup) hotHyper.clone());
                } catch (CloneNotSupportedException e) {
                    FineLoggerFactory.getLogger().error("Error in Hyperlink, Please Check it.", e);
                }
            }
            newPlot.setConditionCollection(oldPlot.getConditionCollection());
            newPlot.setSeriesDragEnable(oldPlot.isSeriesDragEnable());
            if (newPlot.isSupportZoomCategoryAxis() && newPlot.getxAxis() != null) {
                newPlot.getxAxis().setZoom(oldPlot.getxAxis().isZoom());
            }
            if (newPlot.isSupportTooltipInInteractivePane()) {
                newPlot.setHotTooltipStyle(oldPlot.getHotTooltipStyle());
            }

            if (newPlot.isSupportAutoRefresh()) {
                newPlot.setAutoRefreshPerSecond(oldPlot.getAutoRefreshPerSecond());
            }

            if (newPlot.isSupportAxisTip()) {
                newPlot.setInteractiveAxisTooltip(oldPlot.isInteractiveAxisTooltip());
            }
        }
        return newPlot;
    }

    //平面3D的默认属性设置
    private void createCondition4Plane3D(Chart chart) {
        if (chart != null) {
            //标题
            Title title = new Title(chart.getTitle().getTextObject());
            chart.setTitle(title);
            title.setTitleVisible(true);
            TextAttr textAttr = title.getTextAttr();
            if (textAttr == null) {
                textAttr = new TextAttr();
                title.setTextAttr(textAttr);
            }
            title.setPosition(Constants.CENTER);
            textAttr.setFRFont(FRFont.getInstance("Microsoft YaHei", Font.PLAIN, 16f, new Color(51, 51, 51)));

            //图例
            Legend legend = new Legend();
            legend.setFRFont(FRFont.getInstance("SimSun", Font.PLAIN, 9f, new Color(128, 128, 128)));
            legend.setPosition(Constants.TOP);
            chart.getPlot().setLegend(legend);

            //分类轴,现在只有柱形图，条形图，面积图
            if (chart.getPlot() instanceof CategoryPlot) {
                CategoryPlot plot = (CategoryPlot) chart.getPlot();
                //分类轴设置
                Axis cateAxis = plot.getxAxis();
                cateAxis.setAxisStyle(Constants.LINE_THICK);
                cateAxis.setAxisColor(new Color(57, 57, 57));
                cateAxis.setTickMarkType(Constants.TICK_MARK_NONE);
                cateAxis.setSecTickMarkType(Constants.TICK_MARK_NONE);
                cateAxis.setShowAxisLabel(true);
                cateAxis.getTextAttr().setFRFont(FRFont.getInstance("Microsoft YaHei", Font.PLAIN, 10f, new Color(57, 57, 57)));

                //值轴设置
                Axis valueAxis = plot.getyAxis();
                valueAxis.setAxisStyle(Constants.LINE_NONE);
                valueAxis.setTickMarkType(Constants.TICK_MARK_NONE);
                valueAxis.setSecTickMarkType(Constants.TICK_MARK_NONE);
                valueAxis.setShowAxisLabel(false);

                //绘图区
                plot.getyAxis().setMainGridStyle(Constants.LINE_THIN);
                plot.getyAxis().setMainGridColor(new Color(192, 192, 192));
                chart.setBorderStyle(Constants.LINE_NONE);

                //数据标签
                ConditionAttr attrList = plot.getConditionCollection().getDefaultAttr();
                DataSeriesCondition attr = attrList.getExisted(AttrContents.class);
                if (attr != null) {
                    attrList.remove(attr);
                }
                AttrContents attrContents = new AttrContents();
                attrContents.setPosition(Constants.OUTSIDE);
                attrContents.setSeriesLabel(ChartConstants.VALUE_PARA);
                attrContents.setTextAttr(new TextAttr(FRFont.getInstance("SimSun", Font.PLAIN, 9f, new Color(51, 51, 51))));
                attrList.addDataSeriesCondition(attrContents);
            }
        }
    }

    //透明风格的默认属性设置
    private void createCondition4Transparent(Chart chart) {
        if (chart != null) {
            //标题
            Title title = new Title(chart.getTitle().getTextObject());
            chart.setTitle(title);
            title.setTitleVisible(true);
            TextAttr textAttr = title.getTextAttr();
            if (textAttr == null) {
                textAttr = new TextAttr();
                title.setTextAttr(textAttr);
            }
            title.setPosition(Constants.LEFT);
            textAttr.setFRFont(FRFont.getInstance("Microsoft YaHei", Font.BOLD, 16f, new Color(192, 192, 192)));

            //图例
            Legend legend = new Legend();
            legend.setFRFont(FRFont.getInstance("SimSun", Font.PLAIN, 9f, new Color(138, 140, 139)));
            legend.setPosition(Constants.RIGHT_TOP);
            chart.getPlot().setLegend(legend);

            Plot plot = chart.getPlot();
            //绘图区
            chart.setBackground(ColorBackground.getInstance(new Color(51, 51, 51)));

            //分类轴,现在只有柱形图，条形图，面积图
            if (plot instanceof CategoryPlot) {
                //边框
                plot.setBorderStyle(Constants.LINE_THIN);
                plot.setBorderColor(new Color(65, 65, 65));

                //分类轴设置
                Axis cateAxis = plot.getxAxis();
                cateAxis.setAxisStyle(Constants.LINE_THICK);
                cateAxis.setAxisColor(new Color(192, 192, 192));
                cateAxis.setTickMarkType(Constants.TICK_MARK_NONE);
                cateAxis.setSecTickMarkType(Constants.TICK_MARK_NONE);
                cateAxis.setShowAxisLabel(true);
                cateAxis.getTextAttr().setFRFont(FRFont.getInstance("Microsoft YaHei", Font.PLAIN, 10f, new Color(150, 150, 150)));

                //值轴
                Axis valueAxis = plot.getyAxis();
                valueAxis.setShowAxisLabel(true);
                valueAxis.setAxisStyle(Constants.LINE_NONE);
                valueAxis.getTextAttr().setFRFont(FRFont.getInstance("SimSun", Font.PLAIN, 10f, new Color(150, 150, 150)));
                valueAxis.setMainGridStyle(Constants.LINE_THIN);
                valueAxis.setMainGridColor(new Color(63, 62, 62));
            }
        }
    }

    //渐变的默认属性设置
    private void createCondition4Shade(Chart chart) {
        if (chart != null) {
            //标题
            Title title = new Title(chart.getTitle().getTextObject());
            chart.setTitle(title);
            title.setTitleVisible(true);
            TextAttr textAttr = title.getTextAttr();
            if (textAttr == null) {
                textAttr = new TextAttr();
                title.setTextAttr(textAttr);
            }
            title.setPosition(Constants.CENTER);
            textAttr.setFRFont(FRFont.getInstance("Microsoft YaHei", Font.BOLD, 16f, new Color(0, 51, 102)));

            //图例
            Legend legend = new Legend();
            legend.setFRFont(FRFont.getInstance("SimSun", Font.PLAIN, 9f, new Color(128, 128, 128)));
            legend.setPosition(Constants.BOTTOM);
            chart.getPlot().setLegend(legend);

            //分类轴,现在只有柱形图，条形图，面积图
            if (chart.getPlot() instanceof CategoryPlot) {
                CategoryPlot plot = (CategoryPlot) chart.getPlot();

                //分类轴设置
                Axis cateAxis = plot.getxAxis();
                cateAxis.setAxisStyle(Constants.LINE_THICK);
                cateAxis.setAxisColor(new Color(73, 100, 117));
                cateAxis.setTickMarkType(Constants.TICK_MARK_NONE);
                cateAxis.setSecTickMarkType(Constants.TICK_MARK_NONE);
                cateAxis.setShowAxisLabel(true);
                cateAxis.getTextAttr().setFRFont(FRFont.getInstance("Microsoft YaHei", Font.PLAIN, 10f, new Color(128, 128, 128)));

                //值轴
                Axis valueAxis = plot.getyAxis();
                valueAxis.setShowAxisLabel(true);
                valueAxis.getTextAttr().setFRFont(FRFont.getInstance("SimSun", Font.PLAIN, 10f, new Color(128, 128, 128)));
                valueAxis.setAxisStyle(Constants.LINE_NONE);

                //绘图区
                plot.getyAxis().setMainGridStyle(Constants.LINE_THIN);
                plot.getyAxis().setMainGridColor(new Color(192, 192, 192));
                plot.setHorizontalIntervalBackgroundColor(new Color(243, 243, 243));
            }
        }
    }

    //高光渐变的默认属性设置
    private void createCondition4HighLight(Chart chart) {
        if (chart != null) {
            //标题
            Title title = new Title(chart.getTitle().getTextObject());
            chart.setTitle(title);
            title.setTitleVisible(true);
            TextAttr textAttr = title.getTextAttr();
            if (textAttr == null) {
                textAttr = new TextAttr();
                title.setTextAttr(textAttr);
            }
            title.setPosition(Constants.LEFT);
            textAttr.setFRFont(FRFont.getInstance("Microsoft YaHei", Font.BOLD, 16f, new Color(51, 51, 51)));

            //图例
            Legend legend = new Legend();
            legend.setFRFont(FRFont.getInstance("SimSun", Font.PLAIN, 9f, new Color(138, 140, 139)));
            legend.setPosition(Constants.RIGHT_TOP);
            chart.getPlot().setLegend(legend);

            //分类轴,现在只有柱形图，条形图，面积图
            if (chart.getPlot() instanceof CategoryPlot) {
                CategoryPlot plot = (CategoryPlot) chart.getPlot();

                //分类轴设置
                Axis cateAxis = plot.getxAxis();
                cateAxis.setAxisStyle(Constants.LINE_THICK);
                cateAxis.setAxisColor(new Color(204, 220, 228));
                cateAxis.setTickMarkType(Constants.TICK_MARK_INSIDE);
                cateAxis.setSecTickMarkType(Constants.TICK_MARK_NONE);
                cateAxis.setShowAxisLabel(true);
                cateAxis.getTextAttr().setFRFont(FRFont.getInstance("Microsoft YaHei", Font.PLAIN, 10f, new Color(138, 140, 139)));

                //值轴
                Axis valueAxis = plot.getyAxis();
                valueAxis.setAxisStyle(Constants.NONE);
                valueAxis.setAxisColor(null);
                valueAxis.setTickMarkType(Constants.TICK_MARK_INSIDE);
                valueAxis.setSecTickMarkType(Constants.TICK_MARK_NONE);
                valueAxis.setShowAxisLabel(true);
                valueAxis.getTextAttr().setFRFont(FRFont.getInstance("SimSun", Font.PLAIN, 10f, new Color(138, 140, 139)));

                //绘图区
                plot.setBorderStyle(Constants.LINE_THIN);
                plot.setBorderColor(new Color(204, 220, 228));
                plot.setBackground(ColorBackground.getInstance(new Color(248, 247, 245)));
                plot.getyAxis().setMainGridStyle(Constants.LINE_THIN);
                plot.getyAxis().setMainGridColor(new Color(192, 192, 192));
            }

        }
    }

    private void setPlotFillStyle(Chart chart) {
        ChartPreStyleConfig manager = ChartPreStyleConfig.getInstance();
        Plot plot = chart.getPlot();
        Object preStyle = null;
        String name = "";
        if (styleList.get(STYLE_SHADE).isPressing) {
            name = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Style_Retro");
            preStyle = manager.getPreStyle(name);
        } else if (styleList.get(STYLE_TRANSPARENT).isPressing) {
            name = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Style_Fresh");
            preStyle = manager.getPreStyle(name);
        } else if (styleList.get(STYLE_PLANE3D).isPressing) {
            name = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Style_Bright");
            preStyle = manager.getPreStyle(name);
        } else if (styleList.get(STYLE_HIGHLIGHT).isPressing) {
            name = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Style_Bright");
            preStyle = manager.getPreStyle(name);
        }
        if (preStyle == null) {
            plot.getPlotFillStyle().setColorStyle(ChartConstants.COLOR_DEFAULT);
        } else {
            AttrFillStyle fillStyle = ChartUtils.chartColorMatching2AttrFillStyle((ChartColorMatching) preStyle);
            fillStyle.setFillStyleName(name);
            plot.setPlotFillStyle(fillStyle);
        }
    }

}
