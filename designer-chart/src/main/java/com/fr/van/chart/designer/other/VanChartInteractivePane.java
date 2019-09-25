package com.fr.van.chart.designer.other;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.chart.chartglyph.ConditionCollection;
import com.fr.chartx.attr.LargeDataAttribute;
import com.fr.chartx.attr.LargeDataModeType;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ibutton.UIToggleButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.i18n.Toolkit;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.plugin.chart.attr.axis.VanChartAxis;
import com.fr.plugin.chart.attr.plot.VanChartPlot;
import com.fr.plugin.chart.attr.plot.VanChartRectanglePlot;
import com.fr.plugin.chart.axis.type.AxisPlotType;
import com.fr.plugin.chart.base.AttrEffect;
import com.fr.plugin.chart.base.AttrLabel;
import com.fr.plugin.chart.base.RefreshMoreLabel;
import com.fr.plugin.chart.base.VanChartAttrMarker;
import com.fr.plugin.chart.base.VanChartTools;
import com.fr.plugin.chart.map.line.condition.AttrLineEffect;
import com.fr.plugin.chart.scatter.attr.ScatterAttrLabel;
import com.fr.plugin.chart.vanchart.VanChart;
import com.fr.van.chart.custom.component.VanChartHyperLinkPane;
import com.fr.van.chart.designer.AbstractVanChartScrollPane;
import com.fr.van.chart.designer.PlotFactory;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;
import com.fr.van.chart.designer.other.zoom.ZoomPane;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VanChartInteractivePane extends AbstractVanChartScrollPane<Chart> {

    private static final long serialVersionUID = 8135452818502145597L;
    private static final int AUTO_REFRESH_LEFT_GAP = 18;

    protected UICheckBox isSort;
    protected UICheckBox exportImages;
    protected UICheckBox fullScreenDisplay;
    protected UIToggleButton collapse;

    protected VanChart chart;
    private UIComboBox largeDataMode;
    private UISpinner largeModeThresholdNumber;

    protected UIButtonGroup isChartAnimation;

    //坐标轴翻转属性
    private UIButtonGroup<Integer> axisRotation;

    private AutoRefreshPane autoRefreshPane;

    private ZoomPane zoomPane;

    protected VanChartHyperLinkPane superLink;
    private JPanel largeModeThresholdNumberPane;
    protected JPanel interactivePane;

    /**
     * 界面标题.
     *
     * @return 返回标题.
     */


    public String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Interactive_Tab");
    }

    @Override
    protected JPanel createContentPane() {
        return new JPanel();
    }

    private void reLayoutContentPane(VanChartPlot plot) {
        if (interactivePane != null) {
            interactivePane.removeAll();
        }
        interactivePane = getInteractivePane(plot);
        reloaPane(interactivePane);
    }

    protected JPanel getInteractivePane(VanChartPlot plot) {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double e = TableLayout4VanChartHelper.EDIT_AREA_WIDTH;
        double[] columnSize = {f, e};
        double[] rowSize = {p, p, p, p, p, p};


        Component[][] components = new Component[][]{
                new Component[]{createToolBarPane(getToolBarRowSize(), columnSize), null},
                new Component[]{createLargeDataModePane(), null},
                new Component[]{createAnimationPane(), null},
                new Component[]{createAxisRotationPane(new double[]{p, p}, columnSize, plot), null},
                new Component[]{createZoomPane(new double[]{p, p, p}, columnSize, plot), null},
                new Component[]{createAutoRefreshPane(plot), null},
                new Component[]{createHyperlinkPane(), null}
        };

        return TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
    }

    private JPanel createLargeDataModePane() {
        if (!isCurrentChartSupportLargeDataMode()) {
            return null;
        }
        largeDataMode = new UIComboBox(new LargeDataModeType[]{LargeDataModeType.CLOSE, LargeDataModeType.OPEN_BEYOND_THRESHOLD});
        largeModeThresholdNumber = new UISpinner(0, Integer.MAX_VALUE, 100, chart.getPlot().getLargeDataAttribute().getLargeModeThresholdNumber());

        largeDataMode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkLargeDataMode();
            }
        });

        Component[][] comps1 = new Component[][]{
                new Component[]{new UILabel(Toolkit.i18nText("Fine-Design_Chart_Large_Model")), largeDataMode}
        };
        Component[][] comps2 = new Component[][]{
                new Component[]{new UILabel(Toolkit.i18nText("Fine-Design_Chart_Threshold_Number")), largeModeThresholdNumber}
        };

        double[] row = {TableLayout.PREFERRED}, col = {TableLayout.FILL, TableLayout4VanChartHelper.EDIT_AREA_WIDTH};


        JPanel contentPane = new JPanel(new BorderLayout(0, 6));

        contentPane.add(TableLayout4VanChartHelper.createGapTableLayoutPane(comps1, row, col), BorderLayout.CENTER);
        largeModeThresholdNumberPane = TableLayout4VanChartHelper.createGapTableLayoutPane(comps2, row, col);
        contentPane.add(largeModeThresholdNumberPane, BorderLayout.SOUTH);

        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(Toolkit.i18nText("Fine-Design_Chart_Large_Data"), contentPane);
    }

    protected boolean isCurrentChartSupportLargeDataMode() {
        return false;
    }

    protected JPanel createZoomPane(double[] row, double[] col, VanChartPlot plot) {
        zoomPane = createZoomPane();
        if (zoomPane == null) {
            return null;
        }
        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Use_Zoom"), zoomPane);
    }

    protected ZoomPane createZoomPane() {
        return null;
    }

    private JPanel createAxisRotationPane(double[] row, double[] col, VanChartPlot plot) {
        if (!(plot.getAxisPlotType() == AxisPlotType.RECTANGLE)) {
            return null;
        }
        axisRotation = new UIButtonGroup<Integer>(new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Open"),
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Close")});

        Component[][] components = new Component[][]{
                new Component[]{null, null},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Reversal")), axisRotation}
        };
        JPanel panel = TableLayout4VanChartHelper.createGapTableLayoutPane(components, row, col);
        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Axis"), panel);
    }


    protected JPanel createToolBarPane(double[] row, double[] col) {
        isSort = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Sort"));
        exportImages = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Export_Image"));
        fullScreenDisplay = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_FullScreen_Display"));
        collapse = new UIToggleButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Collapse"));

        Component[][] components = createToolBarComponents();

        JPanel panel = TableLayout4VanChartHelper.createGapTableLayoutPane(components, row, col);
        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_ToolBar"), panel);
    }

    protected double[] getToolBarRowSize() {
        double p = TableLayout.PREFERRED;
        return new double[]{p, p, p, p, p};
    }

    protected Component[][] createToolBarComponents() {
        return new Component[][]{
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Content")), isSort},
                new Component[]{null, exportImages},
                new Component[]{null, fullScreenDisplay},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Layout")), collapse},
        };
    }

    protected Component[][] createToolBarComponentsWithOutSort() {
        return new Component[][]{
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Content")), exportImages},
                new Component[]{null, fullScreenDisplay},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Layout")), collapse}
        };
    }


    protected JPanel createAnimationPane() {
        isChartAnimation = new UIButtonGroup(new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Open"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Close")});
        JPanel panel = TableLayout4VanChartHelper.createGapTableLayoutPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Animation_Effects"), isChartAnimation);
        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Animation"), panel);
    }

    protected JPanel createAutoRefreshPane(VanChartPlot plot) {

        autoRefreshPane = getMoreLabelPane(plot);

        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Moniter_refresh"), autoRefreshPane);
    }

    protected AutoRefreshPane getMoreLabelPane(VanChartPlot plot) {
        boolean isLargeModel = largeModel(plot);
        return new AutoRefreshPane(chart, isLargeModel);
    }

    protected JPanel createHyperlinkPane() {
        superLink = new VanChartHyperLinkPane();
        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_M_Insert_Hyperlink"), superLink);
    }

    private void checkLargeDataMode() {
        largeModeThresholdNumberPane.setVisible(largeDataMode.getSelectedItem() == LargeDataModeType.OPEN_BEYOND_THRESHOLD);

        resetAttr();
    }

    private void resetAttr() {
        if (largeDataMode.getSelectedItem() == LargeDataModeType.OPEN_BEYOND_THRESHOLD) {
            VanChartPlot plot = this.chart.getPlot();

            AttrLabel attrLabel = plot.getAttrLabelFromConditionCollection();
            if (attrLabel == null) {
                attrLabel = plot.getDefaultAttrLabel();
                ConditionAttr defaultAttr = plot.getConditionCollection().getDefaultAttr();
                defaultAttr.addDataSeriesCondition(attrLabel);
            }
            attrLabel.setEnable(false);

            resetCustomCondition(plot.getConditionCollection());
        }
    }


    private void resetCustomCondition(ConditionCollection conditionCollection) {
        for (int i = 0, len = conditionCollection.getConditionAttrSize(); i < len; i++) {
            ConditionAttr conditionAttr = conditionCollection.getConditionAttr(i);
            conditionAttr.remove(AttrLabel.class);
            conditionAttr.remove(ScatterAttrLabel.class);
            conditionAttr.remove(AttrEffect.class);
            conditionAttr.remove(AttrLineEffect.class);

            VanChartAttrMarker attrMarker = conditionAttr.getExisted(VanChartAttrMarker.class);
            if (attrMarker != null && !attrMarker.isCommon()) {
                conditionAttr.remove(VanChartAttrMarker.class);
            }
        }
    }

    @Override
    public void populateBean(Chart chart) {
        if (chart == null || chart.getPlot() == null) {
            return;
        }
        this.chart = (VanChart) chart;
        VanChartPlot plot = chart.getPlot();

        if (interactivePane == null) {
            this.remove(leftcontentPane);
            reLayoutContentPane(plot);
        }

        if (zoomPane != null) {
            zoomPane.populateBean(this.chart.getZoomAttribute());
        }

        if (plot.getAxisPlotType() == AxisPlotType.RECTANGLE) {
            populateChartAxisRotation(plot);
        }

        populateChartTools(this.chart);
        populateLargeMode(plot);
        populateChartAnimate(this.chart, plot);
        populateAutoRefresh(this.chart);

        populateHyperlink(plot);
    }


    protected void populateHyperlink(Plot plot) {
        superLink.populate(plot);
    }

    private void populateChartTools(VanChart chart) {
        VanChartTools vanChartTools = chart.getVanChartTools();
        isSort.setSelected(vanChartTools.isSort());
        exportImages.setSelected(vanChartTools.isExport());
        fullScreenDisplay.setSelected(vanChartTools.isFullScreen());
        collapse.setSelected(vanChartTools.isHidden());
    }

    private void populateLargeMode(Plot plot) {
        if (largeDataMode != null) {
            LargeDataAttribute attribute = plot.getLargeDataAttribute();

            largeDataMode.setSelectedItem(attribute.getLargeDataModeType());
            largeModeThresholdNumber.setValue(attribute.getLargeModeThresholdNumber());
        }
    }

    private void populateChartAxisRotation(VanChartPlot plot) {
        axisRotation.setSelectedIndex(plot.isAxisRotation() ? 0 : 1);
    }

    private void populateChartAnimate(Chart chart, Plot plot) {
        if (plot.isSupportAnimate()) {
            isChartAnimation.setSelectedIndex(chart.isJSDraw() ? 0 : 1);
            isChartAnimation.setEnabled(!largeModel(plot));
        }
    }

    protected boolean largeModel(Plot plot) {
        return PlotFactory.largeDataModel(plot);
    }

    protected void populateAutoRefresh(VanChart chart) {
        VanChartPlot plot = chart.getPlot();

        RefreshMoreLabel refreshMoreLabel = chart.getRefreshMoreLabel();
        if (refreshMoreLabel == null) {
            refreshMoreLabel = new RefreshMoreLabel(((VanChartPlot) chart.getPlot()).getAutoAttrTooltip());
        }

        autoRefreshPane.populateBean(refreshMoreLabel);

    }

    @Override
    public void updateBean(Chart chart) {
        if (chart == null || chart.getPlot() == null) {
            return;
        }

        VanChart vanChart = (VanChart) chart;
        VanChartPlot plot = chart.getPlot();

        if (zoomPane != null) {
            vanChart.setZoomAttribute(zoomPane.updateBean());
        }

        if (plot.getAxisPlotType() == AxisPlotType.RECTANGLE) {
            updateChartAxisRotation(vanChart);
        }
        updateChartTools(vanChart);
        updateChartAnimate(vanChart, plot);
        updateLargeData(plot);
        updateAutoRefresh(vanChart);
        updateHyperlink(plot);
    }

    protected void updateHyperlink(Plot plot) {
        superLink.update(plot);
    }

    private void updateChartTools(VanChart chart) {
        VanChartTools vanChartTools = new VanChartTools();
        vanChartTools.setExport(exportImages.isSelected());
        vanChartTools.setFullScreen(fullScreenDisplay.isSelected());
        vanChartTools.setSort(isSort.isSelected());
        vanChartTools.setHidden(collapse.isSelected());
        chart.setVanChartTools(vanChartTools);
    }

    private void updateChartAxisRotation(VanChart chart) {
        //坐标轴和plot都需要这个属性，因为坐标轴和plot是分开画的
        VanChartPlot plot = chart.getPlot();
        plot.setAxisRotation(axisRotation.getSelectedIndex() == 0);
        //同时更新坐标轴旋转属性
        for (VanChartAxis axis : ((VanChartRectanglePlot) plot).getXAxisList()) {
            axis.setRotation(plot.isAxisRotation());
        }

        for (VanChartAxis axis : ((VanChartRectanglePlot) plot).getYAxisList()) {
            axis.setRotation(plot.isAxisRotation());
        }

        //更新数据表属性
        if (plot.isAxisRotation()) {
            plot.getDataSheet().setVisible(false);
        }
    }

    private void updateLargeData(Plot plot) {
        if (largeDataMode != null) {
            LargeDataAttribute attribute = new LargeDataAttribute();
            attribute.setLargeDataModeType((LargeDataModeType) largeDataMode.getSelectedItem());
            attribute.setLargeModeThresholdNumber(largeModeThresholdNumber.getValue());
            plot.setLargeDataAttribute(attribute);
        }
    }

    private void updateChartAnimate(Chart chart, Plot plot) {
        if (plot.isSupportAnimate()) {
            chart.setJSDraw(isChartAnimation.getSelectedIndex() == 0);
        }
    }


    private void updateAutoRefresh(VanChart chart) {

        RefreshMoreLabel refreshMoreLabel = chart.getRefreshMoreLabel();
        if (refreshMoreLabel == null) {
            refreshMoreLabel = new RefreshMoreLabel(((VanChartPlot) chart.getPlot()).getAutoAttrTooltip());
            chart.setRefreshMoreLabel(refreshMoreLabel);
        }
        autoRefreshPane.updateBean(refreshMoreLabel);
    }

    @Override
    public VanChart updateBean() {
        return null;
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
