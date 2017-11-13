package com.fr.design.mainframe.chart.gui.other;

import com.fr.base.CoreDecimalFormat;
import com.fr.base.Style;
import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.base.AttrContents;
import com.fr.chart.base.ChartConstants;
import com.fr.chart.base.TimeSwitchAttr;
import com.fr.chart.chartattr.*;
import com.fr.chart.chartdata.GisMapReportDefinition;
import com.fr.chart.chartdata.GisMapTableDefinition;
import com.fr.chart.web.ChartHyperPoplink;
import com.fr.chart.web.ChartHyperRelateCellLink;
import com.fr.chart.web.ChartHyperRelateFloatLink;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.chart.javascript.ChartEmailPane;
import com.fr.design.chart.series.SeriesCondition.impl.ChartHyperPoplinkPane;
import com.fr.design.chart.series.SeriesCondition.impl.ChartHyperRelateCellLinkPane;
import com.fr.design.chart.series.SeriesCondition.impl.ChartHyperRelateFloatLinkPane;
import com.fr.design.chart.series.SeriesCondition.impl.FormHyperlinkPane;
import com.fr.design.dialog.BasicScrollPane;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.fun.HyperlinkProvider;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.design.gui.frpane.UIBubbleFloatPane;
import com.fr.design.gui.frpane.UICorrelationComboBoxPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.imenutable.UIMenuNameableCreator;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.gui.style.FormatPane;
import com.fr.design.hyperlink.ReportletHyperlinkPane;
import com.fr.design.hyperlink.WebHyperlinkPane;
import com.fr.design.javascript.JavaScriptImplPane;
import com.fr.design.javascript.ParameterJavaScriptPane;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartOtherPane;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import com.fr.js.*;
import com.fr.stable.Constants;
import com.fr.stable.StringUtils;
import com.fr.stable.bridge.StableFactory;
import com.fr.third.org.hsqldb.lib.HashMap;
import com.fr.third.org.hsqldb.lib.Iterator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ChartInteractivePane extends BasicScrollPane<Chart> implements UIObserver{
    private static final long serialVersionUID = 3477409806918835992L;
    private static HashMap normalMap = new HashMap();
    private static HashMap gisMap = new HashMap();
    private static HashMap mapMap = new HashMap();
    private static HashMap pieMap = new HashMap();

    private static HashMap xyMap = new HashMap();
    private static HashMap bubbleMap = new HashMap();
    private static HashMap stockMap = new HashMap();
    private static HashMap ganttMap = new HashMap();
    private static HashMap meterMap = new HashMap();

    private static final int TIME_SWITCH_GAP = 40;

    private UICheckBox isChartAnimation;// 动态
    private UICheckBox isSeriesDragEnable; //系列拖拽

    private UICheckBox isAxisZoom;// 缩放

    private UICheckBox isDatapointValue;// 数据点提示
    private UIButton dataPointValueFormat;

    private UICheckBox isDatapointPercent;
    private UIButton dataPointPercentFormat;

    private UILabel tooltipStyleLabel;
    private UIComboBox tooltipStyle;

    private UILabel tooltipShowTypeLabel;
    private UIComboBox tooltipShowType;

    private UICheckBox isAddressTittle;
    private UICheckBox isAddress;
    private UICheckBox isAddressName;

    private UICheckBox isAxisShowToolTip;// 坐标轴提示

    protected UICheckBox isAutoRefresh;// 自动刷新
    protected UISpinner autoRefreshTime;

    private UICorrelationComboBoxPane superLink;//  超链

    private FormatPane valueFormatPane;
    private FormatPane percentFormatPane;
    private Format valueFormat;
    private Format percentFormat;

    private JPanel tooltipPane;
    private JPanel axisShowPane;
    private JPanel autoRefreshPane;
    private JPanel superlinkPane;

    private ChartOtherPane parent;

    private UICheckBox timeSwitch;

    private JPanel timeSwitchContainer;
    private TimeSwitchPane timeSwitchPane;

    private static final int SIZEX = 258;
    private static final int SIZEY = 209;
    private static final int DET = 20;
    public ChartInteractivePane(ChartOtherPane parent) {
        super();
        this.parent = parent;
    }

    /**
     * 界面标题.
     * @return 返回标题.
     */
    public String title4PopupWindow() {
        return Inter.getLocText("Chart-Interactive_Tab");
    }

    @Override
    protected JPanel createContentPane() {
        isChartAnimation = new UICheckBox(Inter.getLocText("Chart-Animation_JSShow"));
        isSeriesDragEnable = new UICheckBox(Inter.getLocText("Chart-Series_Drag"));

        isDatapointValue = new UICheckBox(Inter.getLocText("Chart-Use_Value"));
        dataPointValueFormat = new UIButton(Inter.getLocText("Chart-Use_Format"));
        isDatapointPercent = new UICheckBox(Inter.getLocText("Chart-Value_Percent"));
        dataPointPercentFormat = new UIButton(Inter.getLocText("Chart-Use_Format"));
        tooltipStyle = new UIComboBox(new String []{Inter.getLocText("Chart-White_Black"), Inter.getLocText("Chart-Black_White")});
        tooltipStyleLabel = new UILabel(Inter.getLocText("Chart-Style_Name"));
        tooltipShowType = new UIComboBox(new String []{Inter.getLocText("Chart-Series_SingleData"), Inter.getLocText("Chart-Series_AllData")});
        tooltipShowTypeLabel = new UILabel(Inter.getLocText("Chart-Use_Show"));

        isAddressTittle = new UICheckBox(Inter.getLocText("Chart-Area_Title"));
        isAddress = new UICheckBox(Inter.getLocText("Chart-Gis_Address"));
        isAddressName = new UICheckBox(Inter.getLocText("Chart-Address_Name"));

        isAxisShowToolTip = new UICheckBox(Inter.getLocText("Chart-Interactive_AxisTooltip"));
        isAxisZoom = new UICheckBox(Inter.getLocText("Chart-Use_Zoom"));
        isAutoRefresh = new UICheckBox(Inter.getLocText(new String[]{"Chart-Use_Auto", "Chart-Use_Refresh"}));
        autoRefreshTime = new UISpinner(1, Integer.MAX_VALUE, 1);
        superLink = new UICorrelationComboBoxPane();

        isAutoRefresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkAutoRefresh();
            }
        });
        timeSwitch = new UICheckBox(Inter.getLocText("FR-Chart-Interactive_timeSwitch"));
        timeSwitchPane = new TimeSwitchPane();
        initFormatListener();
        return initPaneWithListener();
    }


    private void initFormatListener() {
        initValueFormatListener();
        initPercentFormatListener();
        isAxisZoom.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                timeSwitch.setEnabled(isAxisZoom.isSelected());
                if(!isAxisZoom.isSelected()){
                    timeSwitch.setSelected(false);
                }
            }
        });

        timeSwitch.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                relayoutTimeSwitchPane();
            }
        });
    }

    private void initValueFormatListener() {
        dataPointValueFormat.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (valueFormatPane == null) {
                    valueFormatPane = new FormatPane();
                }
                Point comPoint = dataPointValueFormat.getLocationOnScreen();
                Point arrowPoint = new Point(comPoint.x - DET, comPoint.y + dataPointValueFormat.getHeight());
                UIBubbleFloatPane<Style> pane = new UIBubbleFloatPane<Style>(Constants.RIGHT, arrowPoint, valueFormatPane, SIZEX, SIZEY) {

                    @Override
                    public void updateContentPane() {
                        valueFormat = valueFormatPane.update();
                        parent.attributeChanged();
                    }
                };
                pane.show(ChartInteractivePane.this, Style.getInstance(valueFormat));
            }
        });
    }

    private void initPercentFormatListener() {
        dataPointPercentFormat.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (percentFormatPane == null) {
                    percentFormatPane = new FormatPane();
                }

                DecimalFormat defaultFormat = new CoreDecimalFormat(new DecimalFormat(), "#.##%");
                percentFormatPane.populateBean(defaultFormat);

                Point comPoint = dataPointPercentFormat.getLocationOnScreen();
                Point arrowPoint = new Point(comPoint.x - DET, comPoint.y + dataPointPercentFormat.getHeight());
                UIBubbleFloatPane<Style> pane = new UIBubbleFloatPane<Style>(Constants.RIGHT, arrowPoint, percentFormatPane, SIZEX, SIZEY) {

                    @Override
                    public void updateContentPane() {
                        percentFormat = percentFormatPane.update();
                        parent.attributeChanged();
                    }
                };

                pane.show(ChartInteractivePane.this, Style.getInstance(percentFormat));
                super.mouseReleased(e);

                percentFormatPane.justUsePercentFormat();
            }
        });
    }

    private void checkAutoRefresh() {
        GUICoreUtils.setEnabled(autoRefreshTime, isAutoRefresh.isSelected());
    }

    /**
     * 反正后面还有relayout,这边init一下就好了 保证所有的init 加入界面 并且加载入事件.
     * @return
     */
    private JPanel initPaneWithListener() {
        initDataPointToolTipPane();
        initAxisShowPane();
        initAutoRefreshPane();
        initSuperlinkPane();
        initTimeSwitchPane();


        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = new double[]{f};
        double[] rowSize = new double[]{p, p, p, p, p, p, p};
        Component[][] components = new Component[][]{
                new Component[]{isChartAnimation,},
                new Component[]{isSeriesDragEnable,},
                new Component[]{tooltipPane,},
                new Component[]{axisShowPane,},
                new Component[]{isAxisZoom,},
                new Component[]{autoRefreshPane},
                new Component[]{superlinkPane}
        };
        //初始化界面时  加载事件
        return TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
    }

    private void initTimeSwitchPane(){
        timeSwitchContainer = new JPanel(new BorderLayout());
        timeSwitchContainer.add(timeSwitch, BorderLayout.CENTER);
    }



    /**
     * 全部初始化, 对所有的界面 都加入, 然后会加载事件响应. 后续再relayout.
     */
    private void initDataPointToolTipPane() {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = new double[]{p, f};
        double[] rowSize = new double[]{p, p, p, p, p, p, p};
        Component[][] components = new Component[][]{
                new Component[]{isDatapointValue, dataPointValueFormat},
                new Component[]{isDatapointPercent, dataPointPercentFormat},
                new Component[]{isAddress, null},
                new Component[]{isAddressName,null},
                new Component[]{isAddressTittle, null},
                new Component[]{tooltipShowTypeLabel, tooltipShowType},
                new Component[]{tooltipStyleLabel, tooltipStyle}
        };
        tooltipPane = TableLayoutHelper.createTableLayoutPane4Chart(new String[]{"ChartData-Tooltip"},components, rowSize, columnSize);
    }

    private void relayoutDataPointToolTipPane(Plot plot) {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = new double[]{p, f};
        double[] rowSize = new double[]{p, p};

        if(plot.isShowAllDataPointLabel()) {
            isDatapointPercent.setText(Inter.getLocText("Chart-Value_Conversion"));
        }
        if(plot.isSupportAddress4Gis()) {
            UIButton tmpButton = new UIButton(); //用来调整对齐
            tmpButton.setVisible(false);
            rowSize = new double[]{p, p, p, p, p};
            Component[][] components = new Component[][]{
                    new Component[]{isAddress, null},
                    new Component[]{isAddressName,null},
                    new Component[]{isAddressTittle, tmpButton},
                    new Component[]{isDatapointValue, dataPointValueFormat},
            };
            tooltipPane = TableLayoutHelper.createTableLayoutPane4Chart(new String[]{"ChartData-Tooltip"},components, rowSize, columnSize);
        } else if(plot.isSupportValuePercent()) {
            Component[][] components;
            if (plot.isSupportTooltipSeriesType()) {
                rowSize = new double[]{p, p, p, p};
                components = new Component[][]{
                        getTooltipShowTypeComponent(),
                        new Component[]{isDatapointValue, dataPointValueFormat},
                        new Component[]{isDatapointPercent, dataPointPercentFormat},
                        getTooltipStyleComponent()
                };
            } else {
                rowSize = new double[]{p, p, p};
                components = new Component[][]{
                        new Component[]{isDatapointValue, dataPointValueFormat},
                        new Component[]{isDatapointPercent, dataPointPercentFormat},
                        getTooltipStyleComponent()
                };
            }
            tooltipPane = TableLayoutHelper.createTableLayoutPane4Chart(new String[]{"ChartData-Tooltip"},components, rowSize, columnSize);
        } else {
            Component[][] components = new Component[][]{
                    new Component[]{isDatapointValue, dataPointValueFormat},
                    getTooltipStyleComponent()
            };
            tooltipPane = TableLayoutHelper.createTableLayoutPane4Chart(new String[]{"ChartData-Tooltip"},components, rowSize, columnSize);
        }
    }

    private Component[] getTooltipShowTypeComponent() {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        Component[][] newComponents = new Component[][]{
                new Component[]{tooltipShowTypeLabel, tooltipShowType}
        };
        double[] newColumnSize = new double[]{f, p};
        double []newRowSize = new double[]{p};
        return new Component[] {TableLayoutHelper.createTableLayoutPane(newComponents, newRowSize, newColumnSize), null};
    }

    private Component[] getTooltipStyleComponent() {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        Component[][] newComponents = new Component[][]{
                new Component[]{tooltipStyleLabel, tooltipStyle}
        };
        double[] newColumnSize = new double[]{f, p};
        double []newRowSize = new double[]{p};
        return new Component[] {TableLayoutHelper.createTableLayoutPane(newComponents, newRowSize, newColumnSize), null};
    }

    private void initAxisShowPane() {
        double p = TableLayout.PREFERRED;
        double[] columnSize = new double[]{p};
        double[] rowSize = new double[]{p};
        Component[][] components = new Component[][]{
                new Component[]{isAxisShowToolTip},
        };
        axisShowPane = TableLayoutHelper.createTableLayoutPane4Chart(new String[]{"ChartF-Axis", "Chart-Interactive"},components, rowSize, columnSize);
    }

    private void initAutoRefreshPane() {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = new double[]{p, f};
        double[] rowSize = new double[]{p, p, p,p};

        Component[][] components = new Component[][]{
                new Component[]{isAutoRefresh,null},
                new Component[]{GUICoreUtils.createFlowPane(new Component[]{
                        new UILabel(Inter.getLocText("Chart-Time_Interval")),
                        autoRefreshTime,
                        new UILabel(Inter.getLocText("Chart-Time_Seconds"))
                }, 1)},
                new Component[]{new UILabel("<html><font size='2' face='Microsoft Yahei' color='red'>" + Inter.getLocText("FR-Chart-AutoRefresh_NotSupportIMGAndReportData") + "</font></html>"), null},
        };
        autoRefreshPane = TableLayoutHelper.createTableLayoutPane4Chart(new String[]{"Data-Check"},components, rowSize, columnSize);
    }

    private void initSuperlinkPane() {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = new double[]{p, f};
        double[] rowSize = new double[]{p};
        Component[][] components = new Component[][]{
                new Component[]{superLink, null},

        };
        superlinkPane = TableLayoutHelper.createTableLayoutPane4Chart(new String[]{"Hyperlink"},components, rowSize, columnSize);
    }

    private void relayoutWithPlot(Plot plot) {
        this.removeAll();
        double p = TableLayout.PREFERRED;
        double[] columnSize = new double[]{TableLayout.FILL};
        double[] rowSize = new double[]{p, p, p};

        Component[][] components = new Component[][]{
                getChartAnimatePane(plot, rowSize, columnSize),
                getChartScalePane(plot, rowSize, columnSize),
                getDataTooltipPane(plot, rowSize, columnSize),
                getAxisTipPane(plot, rowSize, columnSize),
                getAutoRefreshPane(plot, rowSize, columnSize),
                getHotHyperlinPane(plot)
        };

        double[] row = new double[]{p, p, p, p, p, p};
        reloaPane(TableLayoutHelper.createTableLayoutPane(components, row, columnSize));
    }


    private Component[] getChartAnimatePane(Plot plot, double[] row, double[] col) {
        if(plot.isSupportAnimate() && plot.isSupportSeriesDrag()) {
            return new Component[]{TableLayoutHelper.createTableLayoutPane(
                    new Component[][]{
                            new Component[]{isChartAnimation},
                            new Component[]{isSeriesDragEnable},
                            new Component[]{new JSeparator()}
                    }, row, col)
            };
        }else if(plot.isSupportAnimate() && !plot.isSupportSeriesDrag()){
            return new Component[]{TableLayoutHelper.createTableLayoutPane(
                    new Component[][]{
                            new Component[]{isChartAnimation},
                            new Component[]{new JSeparator()}
                    }, row, col)
            };
        }
        return  new Component[]{null};
    }

    private void relayoutTimeSwitchPane(){
        timeSwitchContainer.removeAll();
        timeSwitchContainer.add(timeSwitch, BorderLayout.CENTER);
        if(timeSwitch.isSelected()){
            double p = TableLayout.PREFERRED;
            double f = TableLayout.FILL;
            double[] columnSize = new double[]{TIME_SWITCH_GAP,f};
            double[] rowSize = new double[]{p};
            Component[][] components = new Component[][]{
                    new Component[]{null, timeSwitchPane},
            };
            JPanel panel= TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
            timeSwitchContainer.add(panel, BorderLayout.SOUTH);
        }
        timeSwitchContainer.revalidate();
    }

    private Component[] getChartScalePane(Plot plot, double[] row, double[] col) {
        boolean isNeedTimeSwitch = plot.getxAxis()!=null && plot.getxAxis().isDate();
        if(plot.isSupportZoomCategoryAxis() && !isNeedTimeSwitch) {
            return new Component[]{TableLayoutHelper.createTableLayoutPane(
                    new Component[][]{
                            new Component[]{isAxisZoom},
                            new Component[]{new JSeparator()}
                    }, row, col)
            };
        }else if(plot.isSupportZoomCategoryAxis() && isNeedTimeSwitch){
            return new Component[]{TableLayoutHelper.createTableLayoutPane(
                    new Component[][]{
                            new Component[]{isAxisZoom},
                            new Component[]{timeSwitchContainer},
                            new Component[]{new JSeparator()}
                    }, row, col)
            };
        }
        return  new Component[]{null};
    }

    private Component[] getDataTooltipPane(Plot plot, double[] row, double[] col) {
        relayoutDataPointToolTipPane(plot);
        if(plot.isSupportTooltipInInteractivePane()) {
            return new Component[]{TableLayoutHelper.createTableLayoutPane(new Component[][]{
                    new Component[]{tooltipPane}, new Component[]{new JSeparator()}}, row, col)};
        }
        return  new Component[]{null};
    }

    private Component[] getAxisTipPane(Plot plot, double[] row, double[] col) {
        if(plot.isSupportAxisTip()) {
            return new Component[]{TableLayoutHelper.createTableLayoutPane(new Component[][]{
                    new Component[]{axisShowPane}, new Component[]{new JSeparator()}}, row, col)};

        }
        return  new Component[]{null};
    }

    private Component[] getAutoRefreshPane(Plot plot, double[] row, double[] col) {
        if(plot.isSupportAutoRefresh()) {
            return new Component[]{TableLayoutHelper.createTableLayoutPane(
                    new Component[][]{
                            new Component[]{autoRefreshPane},
                            new Component[]{new JSeparator()}
                    }, row, col)
            };
        }

        return new Component[]{null};
    }

    private Component[] getHotHyperlinPane(Plot plot) {
        return new Component[]{superlinkPane};
    }

    @Override
    public void populateBean(Chart chart) {
        if (chart == null || chart.getPlot() == null) {
            return;
        }

        Plot plot = chart.getPlot();
        relayoutWithGis(chart, plot);
        relayoutWithPlot(plot);

        populateChartAnimate(chart, plot);
        populateChartScale(plot);
        populateDataTooltip(plot);
        populateAxisTip(plot);
        populateAutoRefresh(chart);
        populateHyperlink(plot);

        checkAutoRefresh();
    }

    private void relayoutWithGis(Chart chart, Plot plot) {
        if(plot.isSupportAddress4Gis()) {
            TopDefinitionProvider definition = chart.getFilterDefinition();
            boolean addressType = true;
            if(definition instanceof GisMapTableDefinition){
                addressType = ((GisMapTableDefinition)definition).isAddress();
            }else if(definition instanceof GisMapReportDefinition){
                addressType = ((GisMapReportDefinition)definition).isAddress();
            }
            if(addressType){
                this.isAddress.setText(Inter.getLocText("Chart-Use_Address"));
            }else{
                this.isAddress.setText(Inter.getLocText("Chart-Use_LatLng"));
            }
        }
    }

    private void populateChartAnimate(Chart chart, Plot plot) {
        if(plot.isSupportAnimate()) {
            isChartAnimation.setSelected(chart.isJSDraw());
        }

        if(plot.isSupportSeriesDrag()){
            isSeriesDragEnable.setSelected(plot.isSeriesDragEnable());
        }
    }

    private void populateChartScale(Plot plot) {
        if(plot.isSupportZoomCategoryAxis()) {
            isAxisZoom.setSelected(plot.getxAxis() != null && plot.getxAxis().isZoom());
        }

        timeSwitch.setSelected(false);
        timeSwitch.setEnabled(false);
        //只有坐标轴为时间坐标轴,并且勾选了图表缩放的时候，才支持时间切换
        if(!plot.isSupportZoomCategoryAxis() || !isAxisZoom.isSelected()){
            return;
        }

        if(plot.getxAxis() ==null && !plot.getxAxis().isDate()){
            return;
        }

        timeSwitch.setEnabled(true);
        ArrayList<TimeSwitchAttr> timeMap=plot.getxAxis().getTimeSwitchMap();
        timeSwitch.setSelected(timeMap != null && !timeMap.isEmpty());

        if(timeSwitch.isSelected()){
            timeSwitchPane.populate(plot);
        }
    }

    private void populateDataTooltip(Plot plot) {
        if(plot.isSupportTooltipInInteractivePane()) {
            AttrContents contents = plot.getHotTooltipStyle();
            if (contents == null) {
                return;
            }
            String dataLabel = contents.getSeriesLabel();
            if (dataLabel == null) {
                return;
            }
            valueFormat = contents.getFormat();
            isDatapointValue.setSelected(dataLabel.contains(ChartConstants.VALUE_PARA));
            if (contents.isWhiteBackground()) {
                tooltipStyle.setSelectedIndex(0);
            } else {
                tooltipStyle.setSelectedIndex(1);
            }
            if(plot.isSupportValuePercent()) {
                percentFormat = contents.getPercentFormat();
                isDatapointPercent.setSelected(dataLabel.contains(ChartConstants.PERCENT_PARA));
            }

            if(plot.isSupportAddress4Gis()) {
                isAddressTittle.setSelected(dataLabel.contains(ChartConstants.AREA_TITTLE_PARA));
                isAddress.setSelected(dataLabel.contains(ChartConstants.ADDRESS_PARA));
                isAddressName.setSelected(dataLabel.contains(ChartConstants.ADDRESS_NAME_PARA));
            }

            if (plot.isSupportTooltipSeriesType()) {
                if (contents.isShowMutiSeries()) {
                    tooltipShowType.setSelectedIndex(1);
                } else {
                    tooltipShowType.setSelectedIndex(0);
                }
            }
        }
    }

    private void populateAxisTip(Plot plot) {
        if(plot.isSupportAxisTip()) {
            isAxisShowToolTip.setSelected(plot.isInteractiveAxisTooltip());
        }
    }

    protected void populateAutoRefresh(Chart chart) {
        Plot plot = chart.getPlot();
        if(plot.isSupportAutoRefresh()) {
            if (plot.getAutoRefreshPerSecond() < 1) {
                isAutoRefresh.setSelected(false);
                autoRefreshTime.setValue(2);
            } else {
                isAutoRefresh.setSelected(true);
                autoRefreshTime.setValue(plot.getAutoRefreshPerSecond());
            }
        }
    }

    private void populateHyperlink(Plot plot) {
        HashMap paneMap = renewMapWithPlot(plot);

        List<UIMenuNameableCreator> list = refreshList(paneMap);
        superLink.refreshMenuAndAddMenuAction(list);

        List<UIMenuNameableCreator> hyperList = new ArrayList<UIMenuNameableCreator>();
        NameJavaScriptGroup nameGroup = plot.getHotHyperLink();
        for(int i = 0; nameGroup != null &&  i < nameGroup.size(); i++) {
            NameJavaScript javaScript = nameGroup.getNameHyperlink(i);
            if(javaScript != null && javaScript.getJavaScript() != null) {
                JavaScript script = javaScript.getJavaScript();
                hyperList.add(new UIMenuNameableCreator(javaScript.getName(), script, getUseMap(paneMap, script.getClass())));
            }
        }

        superLink.populateBean(hyperList);
        superLink.doLayout();
    }

    @Override
    public void updateBean(Chart chart) {
        if (chart == null || chart.getPlot() == null) {
            return;
        }

        Plot plot = chart.getPlot();

        updateChartAnimate(chart, plot);
        updateChartScale(plot);
        updateDataTooltip(plot);
        updateAxisTip(plot);
        updateAutoRefresh(plot);
        updateHyperlink(plot);
    }

    private void updateChartAnimate(Chart chart, Plot plot) {
        if(plot.isSupportAnimate()) {
            chart.setJSDraw(isChartAnimation.isSelected());
        }

        if(plot.isSupportSeriesDrag()){
            plot.setSeriesDragEnable(isSeriesDragEnable.isSelected());
        }
    }

    private void updateChartScale(Plot plot) {
        if(plot.isSupportZoomCategoryAxis() && plot.getxAxis() != null) {
            plot.getxAxis().setZoom(isAxisZoom.isSelected());
        }
        if(plot.getxAxis() == null){
            return;
        }
        boolean isNeedTimeSwitch = plot.getxAxis()!=null && plot.getxAxis().isDate();
        boolean isClear =  !isNeedTimeSwitch || !timeSwitch.isSelected();
        if(isClear && plot.getxAxis().getTimeSwitchMap() != null){
            plot.getxAxis().getTimeSwitchMap().clear();
            return;
        }
        if(plot.getxAxis().isDate() && timeSwitch.isSelected()){
            timeSwitchPane.update(plot);
        }
    }

    private void updateDataTooltip(Plot plot) {
        if(plot.isSupportTooltipInInteractivePane()) {
            AttrContents seriesAttrContents = plot.getHotTooltipStyle();
            if (seriesAttrContents == null) {
                seriesAttrContents = new AttrContents();
            }
            String contents = plot.isSupportAddress4Gis() ? getGisTooltipContent() : getTooltipContent(plot);
            seriesAttrContents.setSeriesLabel(contents);

            if(tooltipStyle != null){
                boolean isWhiteBackground = tooltipStyle.getSelectedIndex() == 0;
                seriesAttrContents.setWhiteBackground(isWhiteBackground);
            }

            if(tooltipShowType != null){
                boolean isShowMutiSeries = plot.isSupportTooltipSeriesType() && tooltipShowType.getSelectedIndex() == 1;
                seriesAttrContents.setShowMutiSeries(isShowMutiSeries);
            }

            seriesAttrContents.setFormat(valueFormat);
            if(plot.isSupportValuePercent()) {
                if (percentFormat != null) {
                    seriesAttrContents.setPercentFormat(percentFormat);
                }
            }
        }
    }

    private String getTooltipContent(Plot plot) {
        String contents = StringUtils.EMPTY;
        contents += ChartConstants.SERIES_PARA + ChartConstants.BREAKLINE_PARA + ChartConstants.CATEGORY_PARA;
        if (isDatapointValue.isSelected() && !isDatapointPercent.isSelected()) {
            contents += ChartConstants.BREAKLINE_PARA + ChartConstants.VALUE_PARA;
        } else if (!isDatapointValue.isSelected() && isDatapointPercent.isSelected()) {
            contents += ChartConstants.BREAKLINE_PARA + ChartConstants.PERCENT_PARA;
        } else if (isDatapointValue.isSelected() && isDatapointPercent.isSelected()) {
            contents += ChartConstants.BREAKLINE_PARA + ChartConstants.VALUE_PARA
                    + ChartConstants.BREAKLINE_PARA + ChartConstants.PERCENT_PARA;
        } else {
            contents = null;
        }
        return contents;
    }

    private String getGisTooltipContent() {
        String contents = StringUtils.EMPTY;
        contents += ChartConstants.SERIES_PARA + ChartConstants.BREAKLINE_PARA + ChartConstants.CATEGORY_PARA;
        boolean noPara = true;
        if(isDatapointValue.isSelected()){
            contents += ChartConstants.BREAKLINE_PARA + ChartConstants.VALUE_PARA;
            noPara = false;
        }
        if(isAddressTittle.isSelected()){
            contents += ChartConstants.BREAKLINE_PARA + ChartConstants.AREA_TITTLE_PARA;
            noPara = false;
        }
        if(isAddress.isSelected()){
            contents += ChartConstants.BREAKLINE_PARA + ChartConstants.ADDRESS_PARA;
            noPara = false;
        }
        if(isAddressName.isSelected()){
            contents += ChartConstants.BREAKLINE_PARA + ChartConstants.ADDRESS_NAME_PARA;;
            noPara = false;
        }
        if(noPara){
            contents = null;
        }

        return contents;
    }

    private void updateAxisTip(Plot plot) {
        if (plot.isSupportAxisTip()) {
            plot.setInteractiveAxisTooltip(isAxisShowToolTip.isSelected());
        }
    }

    private void updateAutoRefresh(Plot plot) {
        if(plot.isSupportAutoRefresh()) {
            if(isAutoRefresh.isSelected() && autoRefreshTime.getValue() >= 2) {
                plot.setAutoRefreshPerSecond((int) autoRefreshTime.getValue());
            } else {
                plot.setAutoRefreshPerSecond(-1);
            }
        }
    }

    private void updateHyperlink(Plot plot) {
        NameJavaScriptGroup nameGroup = new NameJavaScriptGroup();
        nameGroup.clear();

        superLink.resetItemName();
        List list = superLink.updateBean();
        for(int i = 0; i < list.size(); i++) {
            UIMenuNameableCreator menu = (UIMenuNameableCreator)list.get(i);
            NameJavaScript nameJava = new NameJavaScript(menu.getName(), (JavaScript)menu.getObj());
            nameGroup.addNameHyperlink(nameJava);
        }
        plot.setHotHyperLink(nameGroup);
    }


    protected Class<? extends BasicBeanPane> getUseMap(HashMap map, Object key) {
        if(map.get(key) != null){
            return (Class<? extends BasicBeanPane>)map.get(key);
        }

        //引擎在这边放了个provider。。
        Iterator iterator = map.keySet().iterator();
        while (iterator.hasNext()){
            Class clz = (Class)iterator.next();
            if(clz.isAssignableFrom((Class)key)){
                return (Class<? extends BasicBeanPane>)map.get(clz);
            }
        }
        return null;
    }

    protected List<UIMenuNameableCreator> refreshList(HashMap map) {
        List<UIMenuNameableCreator> list = new ArrayList<UIMenuNameableCreator>();

        list.add(new UIMenuNameableCreator(Inter.getLocText("Chart-Link_Reportlet"),
                new ReportletHyperlink(), getUseMap(map, ReportletHyperlink.class)));
        list.add(new UIMenuNameableCreator(Inter.getLocText("Chart-Link_Mail"), new EmailJavaScript(), ChartEmailPane.class));
        list.add(new UIMenuNameableCreator(Inter.getLocText("Chart-Link_Web"),
                new WebHyperlink(), getUseMap(map, WebHyperlink.class)));
        list.add(new UIMenuNameableCreator(Inter.getLocText("Chart-Link_Dynamic_Parameters"),
                new ParameterJavaScript(), getUseMap(map, ParameterJavaScript.class)));
        list.add(new UIMenuNameableCreator("JavaScript", new JavaScriptImpl(), getUseMap(map, JavaScriptImpl.class)));

        list.add(new UIMenuNameableCreator(Inter.getLocText("Chart-Float_Chart"),
                new ChartHyperPoplink(), getUseMap(map, ChartHyperPoplink.class)));
        list.add(new UIMenuNameableCreator(Inter.getLocText("Chart-Link_Cell"),
                new ChartHyperRelateCellLink(), getUseMap(map, ChartHyperRelateCellLink.class)));
        list.add(new UIMenuNameableCreator(Inter.getLocText("Chart-Link_Float"),
                new ChartHyperRelateFloatLink(), getUseMap(map, ChartHyperRelateFloatLink.class)));

        FormHyperlinkProvider hyperlink = StableFactory.getMarkedInstanceObjectFromClass(FormHyperlinkProvider.XML_TAG, FormHyperlinkProvider.class);
        list.add(new UIMenuNameableCreator(Inter.getLocText("Chart-Link_Form"),
                hyperlink, getUseMap(map, FormHyperlinkProvider.class)));

        return list;
    }

    private HashMap renewMapWithPlot(Plot plot) {
        if(plot instanceof PiePlot) {
            return getPiePlotHyperMap();
        } else if(plot instanceof MapPlot) {
            return getMapPlotHyperMap();
        } else if(plot instanceof GisMapPlot) {
            return getGisPlotHyperMap();
        } else if(plot instanceof XYScatterPlot) {
            return getXYHyperMap();
        } else if(plot instanceof BubblePlot) {
            return getBubbleHyperMap();
        } else if(plot instanceof StockPlot) {
            return getStockHyperMap();
        } else if(plot instanceof GanttPlot) {
            return getGanttHyperMap();
        } else if(plot instanceof MeterPlot) {
            return getMeterHyperMap();
        }

        else {
            return getNormalPlotHyperMap();
        }
    }

    private HashMap getXYHyperMap() {
        if(xyMap.isEmpty()) {
            xyMap.put(ReportletHyperlink.class, ReportletHyperlinkPane.CHART_XY.class);
            xyMap.put(EmailJavaScript.class, ChartEmailPane.class);
            xyMap.put(WebHyperlink.class, WebHyperlinkPane.CHART_XY.class);
            xyMap.put(ParameterJavaScript.class, ParameterJavaScriptPane.CHART_XY.class);

            xyMap.put(JavaScriptImpl.class, JavaScriptImplPane.CHART_XY.class);
            xyMap.put(ChartHyperPoplink.class, ChartHyperPoplinkPane.CHART_XY.class);
            xyMap.put(ChartHyperRelateCellLink.class, ChartHyperRelateCellLinkPane.CHART_XY.class);
            xyMap.put(ChartHyperRelateFloatLink.class, ChartHyperRelateFloatLinkPane.CHART_XY.class);

            xyMap.put(FormHyperlinkProvider.class, FormHyperlinkPane.CHART_XY.class);
        }
        return xyMap;
    }

    private HashMap getBubbleHyperMap() {
        if(bubbleMap.isEmpty()) {
            bubbleMap.put(ReportletHyperlink.class, ReportletHyperlinkPane.CHART_BUBBLE.class);
            bubbleMap.put(EmailJavaScript.class, ChartEmailPane.class);
            bubbleMap.put(WebHyperlink.class, WebHyperlinkPane.CHART_BUBBLE.class);
            bubbleMap.put(ParameterJavaScript.class, ParameterJavaScriptPane.CHART_BUBBLE.class);

            bubbleMap.put(JavaScriptImpl.class, JavaScriptImplPane.CHART_BUBBLE.class);
            bubbleMap.put(ChartHyperPoplink.class, ChartHyperPoplinkPane.CHART_BUBBLE.class);
            bubbleMap.put(ChartHyperRelateCellLink.class, ChartHyperRelateCellLinkPane.CHART_BUBBLE.class);
            bubbleMap.put(ChartHyperRelateFloatLink.class, ChartHyperRelateFloatLinkPane.CHART_BUBBLE.class);
            bubbleMap.put(FormHyperlinkProvider.class, FormHyperlinkPane.CHART_BUBBLE.class);
        }
        return bubbleMap;
    }

    private HashMap getStockHyperMap() {
        if(stockMap.isEmpty()) {
            stockMap.put(ReportletHyperlink.class, ReportletHyperlinkPane.CHART_STOCK.class);
            stockMap.put(EmailJavaScript.class, ChartEmailPane.class);
            stockMap.put(WebHyperlink.class, WebHyperlinkPane.CHART_STOCK.class);
            stockMap.put(ParameterJavaScript.class, ParameterJavaScriptPane.CHART_STOCK.class);

            stockMap.put(JavaScriptImpl.class, JavaScriptImplPane.CHART_STOCK.class);
            stockMap.put(ChartHyperPoplink.class, ChartHyperPoplinkPane.CHART_STOCK.class);
            stockMap.put(ChartHyperRelateCellLink.class, ChartHyperRelateCellLinkPane.CHART_STOCK.class);
            stockMap.put(ChartHyperRelateFloatLink.class, ChartHyperRelateFloatLinkPane.CHART_STOCK.class);

            stockMap.put(FormHyperlinkProvider.class, FormHyperlinkPane.CHART_STOCK.class);
        }
        return stockMap;
    }

    private HashMap getGanttHyperMap() {
        if(ganttMap.isEmpty()) {
            ganttMap.put(ReportletHyperlink.class, ReportletHyperlinkPane.CHART_GANTT.class);
            ganttMap.put(EmailJavaScript.class, ChartEmailPane.class);
            ganttMap.put(WebHyperlink.class, WebHyperlinkPane.CHART_GANTT.class);
            ganttMap.put(ParameterJavaScript.class, ParameterJavaScriptPane.CHART_GANTT.class);

            ganttMap.put(JavaScriptImpl.class, JavaScriptImplPane.CHART_GANTT.class);
            ganttMap.put(ChartHyperPoplink.class, ChartHyperPoplinkPane.CHART_GANTT.class);
            ganttMap.put(ChartHyperRelateCellLink.class, ChartHyperRelateCellLinkPane.CHART_GANTT.class);
            ganttMap.put(ChartHyperRelateFloatLink.class, ChartHyperRelateFloatLinkPane.CHART_GANTT.class);

            ganttMap.put(FormHyperlinkProvider.class, FormHyperlinkPane.CHART_GANTT.class);
        }
        return ganttMap;
    }

    private HashMap getMeterHyperMap() {
        if(meterMap.isEmpty()) {
            meterMap.put(ReportletHyperlink.class, ReportletHyperlinkPane.CHART_METER.class);
            meterMap.put(EmailJavaScript.class, ChartEmailPane.class);
            meterMap.put(WebHyperlink.class, WebHyperlinkPane.CHART_METER.class);
            meterMap.put(ParameterJavaScript.class, ParameterJavaScriptPane.CHART_METER.class);

            meterMap.put(JavaScriptImpl.class, JavaScriptImplPane.CHART_METER.class);
            meterMap.put(ChartHyperPoplink.class, ChartHyperPoplinkPane.CHART_METER.class);
            meterMap.put(ChartHyperRelateCellLink.class, ChartHyperRelateCellLinkPane.CHART_METER.class);
            meterMap.put(ChartHyperRelateFloatLink.class, ChartHyperRelateFloatLinkPane.CHART_METER.class);

            meterMap.put(FormHyperlinkProvider.class, FormHyperlinkPane.CHART_METER.class);
        }
        return meterMap;
    }

    private HashMap getMapPlotHyperMap() {
        if(mapMap.isEmpty()) {
            mapMap.put(ReportletHyperlink.class, ReportletHyperlinkPane.CHART_MAP.class);
            mapMap.put(EmailJavaScript.class, ChartEmailPane.class);
            mapMap.put(WebHyperlink.class, WebHyperlinkPane.CHART_MAP.class);
            mapMap.put(ParameterJavaScript.class, ParameterJavaScriptPane.CHART_MAP.class);

            mapMap.put(JavaScriptImpl.class, JavaScriptImplPane.CHART_MAP.class);
            mapMap.put(ChartHyperPoplink.class, ChartHyperPoplinkPane.CHART_MAP.class);
            mapMap.put(ChartHyperRelateCellLink.class, ChartHyperRelateCellLinkPane.CHART_MAP.class);
            mapMap.put(ChartHyperRelateFloatLink.class, ChartHyperRelateFloatLinkPane.CHART_MAP.class);

            mapMap.put(FormHyperlinkProvider.class, FormHyperlinkPane.CHART_MAP.class);
        }
        return mapMap;
    }

    private HashMap getPiePlotHyperMap() {
        if(pieMap.isEmpty()) {
            pieMap.put(ReportletHyperlink.class, ReportletHyperlinkPane.CHART_PIE.class);
            pieMap.put(EmailJavaScript.class, ChartEmailPane.class);
            pieMap.put(WebHyperlink.class, WebHyperlinkPane.CHART_PIE.class);
            pieMap.put(ParameterJavaScript.class, ParameterJavaScriptPane.CHART_PIE.class);

            pieMap.put(JavaScriptImpl.class, JavaScriptImplPane.CHART_PIE.class);
            pieMap.put(ChartHyperPoplink.class, ChartHyperPoplinkPane.CHART_PIE.class);
            pieMap.put(ChartHyperRelateCellLink.class, ChartHyperRelateCellLinkPane.CHART_PIE.class);
            pieMap.put(ChartHyperRelateFloatLink.class, ChartHyperRelateFloatLinkPane.CHART_PIE.class);

            pieMap.put(FormHyperlinkProvider.class, FormHyperlinkPane.CHART_PIE.class);
        }
        return pieMap;
    }

    private HashMap getGisPlotHyperMap() {
        if(gisMap.isEmpty()) {
            gisMap.put(ReportletHyperlink.class, ReportletHyperlinkPane.CHART_GIS.class);
            gisMap.put(EmailJavaScript.class, ChartEmailPane.class);
            gisMap.put(WebHyperlink.class, WebHyperlinkPane.CHART_GIS.class);
            gisMap.put(ParameterJavaScript.class, ParameterJavaScriptPane.CHART_GIS.class);

            gisMap.put(JavaScriptImpl.class, JavaScriptImplPane.CHART_GIS.class);
            gisMap.put(ChartHyperPoplink.class, ChartHyperPoplinkPane.CHART_GIS.class);
            gisMap.put(ChartHyperRelateCellLink.class, ChartHyperRelateCellLinkPane.CHART_GIS.class);
            gisMap.put(ChartHyperRelateFloatLink.class, ChartHyperRelateFloatLinkPane.CHART_GIS.class);

            gisMap.put(FormHyperlinkProvider.class, FormHyperlinkPane.CHART_GIS.class);
        }
        return gisMap;
    }

    private HashMap getNormalPlotHyperMap() {
        if(normalMap.isEmpty()) {
            FormHyperlinkProvider fp = StableFactory.getMarkedInstanceObjectFromClass(FormHyperlinkProvider.XML_TAG, FormHyperlinkProvider.class);

            normalMap.put(ReportletHyperlink.class, ReportletHyperlinkPane.CHART.class);
            normalMap.put(EmailJavaScript.class, ChartEmailPane.class);
            normalMap.put(WebHyperlink.class, WebHyperlinkPane.CHART.class);
            normalMap.put(ParameterJavaScript.class, ParameterJavaScriptPane.CHART.class);

            normalMap.put(JavaScriptImpl.class, JavaScriptImplPane.CHART.class);
            normalMap.put(ChartHyperPoplink.class, ChartHyperPoplinkPane.class);
            normalMap.put(ChartHyperRelateCellLink.class, ChartHyperRelateCellLinkPane.class);
            normalMap.put(ChartHyperRelateFloatLink.class, ChartHyperRelateFloatLinkPane.class);

            normalMap.put(FormHyperlinkProvider.class, FormHyperlinkPane.CHART.class);
            //兼容老的FormHyperlink.class
            if(fp != null){
                normalMap.put(fp.getClass(), FormHyperlinkPane.CHART.class);
            }
        }
        return normalMap;
    }

    @Override
    public Chart updateBean() {
        return null;
    }

    /**
     * 给组件登记一个观察者监听事件
     *
     * @param listener 观察者监听事件
     */
    public void registerChangeListener(UIObserverListener listener) {
        timeSwitch.registerChangeListener(listener);
        timeSwitchPane.registerChangeListener(listener);
    }

    /**
     * 组件是否需要响应添加的观察者事件
     *
     * @return 如果需要响应观察者事件则返回true，否则返回false
     */
    public boolean shouldResponseChangeListener() {
        return true;
    }
}