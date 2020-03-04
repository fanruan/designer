package com.fr.van.chart.gauge;

import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.style.series.ColorPickerPaneWithFormula;
import com.fr.design.mainframe.chart.gui.style.series.UIColorPickerPane;
import com.fr.design.style.color.ColorSelectBox;
import com.fr.general.ComparatorUtils;

import com.fr.plugin.chart.attr.GaugeDetailStyle;
import com.fr.plugin.chart.base.AttrLabel;
import com.fr.plugin.chart.gauge.VanChartGaugePlot;
import com.fr.plugin.chart.type.GaugeStyle;
import com.fr.stable.Constants;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;
import com.fr.van.chart.designer.style.series.VanChartAbstractPlotSeriesPane;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.awt.Component;

/**
 * Created by Mitisky on 15/11/27.
 */
public class VanChartGaugeSeriesPane extends VanChartAbstractPlotSeriesPane {

    private static final long serialVersionUID = -4414343926082129759L;
    private UIButtonGroup gaugeLayout;//布局：横向、纵向

    private ColorSelectBox hingeColor;//枢纽颜色
    private ColorSelectBox hingeBackgroundColor;//枢纽背景颜色
    private ColorSelectBox needleColor;//指针颜色
    private ColorSelectBox paneBackgroundColor;//底盘背景颜色

    private ColorSelectBox slotBackgroundColor;//刻度槽颜色

    private UIButtonGroup rotate;//旋转方向
    private ColorSelectBox innerPaneBackgroundColor;//内底盘背景颜色

    private UIColorPickerPane colorPickerPane;

    private UISpinner thermometerWidth;

    public VanChartGaugeSeriesPane(ChartStylePane parent, Plot plot) {
        super(parent, plot);
    }

    protected JPanel getContentInPlotType() {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double e = TableLayout4VanChartHelper.EDIT_AREA_WIDTH;
        double[] columnSize = {f};
        double[] rowSize = {p,p,p,p,p,p};
        Component[][] components = new Component[][]{
                new Component[]{createGaugeLayoutPane()},
                new Component[]{createGaugeStylePane(rowSize, new double[]{f,e})},
                new Component[]{createGaugeBandsPane()}
        };

       return TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
    }

    private JPanel createGaugeLayoutPane() {
        gaugeLayout = new UIButtonGroup(new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Direction_Horizontal"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Direction_Vertical")});
        JPanel panel = TableLayout4VanChartHelper.createGapTableLayoutPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Page_Setup_Orientation"),gaugeLayout);
        gaugeLayout.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                changeLabelPosition();
            }
        });
        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Layout"), panel);
    }

    private void changeLabelPosition() {
        if(plot instanceof VanChartGaugePlot){
            VanChartGaugePlot gaugePlot = (VanChartGaugePlot)plot;
            if (ComparatorUtils.equals(gaugePlot.getGaugeStyle(), GaugeStyle.THERMOMETER)){
                ConditionAttr attrList = gaugePlot.getConditionCollection().getDefaultAttr();
                AttrLabel attrLabel = (AttrLabel)attrList.getExisted(AttrLabel.class);
                if(attrLabel == null){
                    return;
                }
                if(gaugeLayout.getSelectedIndex() == 0){
                    attrLabel.getAttrLabelDetail().setPosition(Constants.LEFT);
                    attrLabel.getAttrLabelDetail().getTextAttr().setFRFont(VanChartGaugePlot.THERMOMETER_VERTICAL_PERCENT_LABEL_FONT);
                    attrLabel.getGaugeValueLabelDetail().setPosition(Constants.LEFT);
                } else {
                    attrLabel.getAttrLabelDetail().setPosition(Constants.BOTTOM);
                    attrLabel.getAttrLabelDetail().getTextAttr().setFRFont(VanChartGaugePlot.THERMOMETER_PERCENT_LABEL_FONT);
                    attrLabel.getGaugeValueLabelDetail().setPosition(Constants.BOTTOM);
                }
            }
        }
    }

    private JPanel createGaugeStylePane(double[] row, double[] col) {
        JPanel panel = new JPanel(new BorderLayout(0, 6));
        JPanel centerPanel = TableLayoutHelper.createTableLayoutPane(getDiffComponentsWithGaugeStyle(), row, col);
        panel.add(centerPanel, BorderLayout.CENTER);
        if(rotate != null){
            JPanel panel1 = TableLayout4VanChartHelper.createGapTableLayoutPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Rotation_Direction"), rotate);
            panel.add(panel1, BorderLayout.NORTH);
        }
        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Widget_Style"), panel);
    }

    private Component[][] getDiffComponentsWithGaugeStyle() {
        GaugeStyle style = plot == null ? GaugeStyle.POINTER : ((VanChartGaugePlot)plot).getGaugeStyle();
        switch (style) {
            case RING:
                initRotate();
                return new Component[][]{
                        new Component[]{null, null},
                        getPaneBackgroundColor(),
                        getInnerPaneBackgroundColor(),
                        new Component[]{createRadiusPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Radius_Set")), null}
                };
            case SLOT:
                return new Component[][]{
                        new Component[]{null, null},
                        getNeedleColor(),
                        getSlotBackgroundColor(),
                        new Component[]{createRadiusPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Radius_Set")), null}
                };
            case THERMOMETER:
                return new Component[][]{
                        new Component[]{null, null},
                        getNeedleColor(),
                        getSlotBackgroundColor(),
                        new Component[]{createRadiusPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Length_Set")), null},
                        getThermometerWidth()
                };
            default:
                return new Component[][]{
                        new Component[]{null, null},
                        getHingeColor(),
                        getHingeBackgroundColor(),
                        getNeedleColor(),
                        getPaneBackgroundColor(),
                        new Component[]{createRadiusPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Radius_Set")), null}
                };
        }
    }

    private Component[] getHingeColor() {
        hingeColor = new ColorSelectBox(120);
        return new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Hinge")),hingeColor};
    }

    private Component[] getHingeBackgroundColor() {
        hingeBackgroundColor = new ColorSelectBox(120);
        return new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Hinge_Background")),hingeBackgroundColor};
    }

    private Component[] getNeedleColor() {
        needleColor = new ColorSelectBox(120);
        return new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Needle")),needleColor};
    }

    private Component[] getPaneBackgroundColor() {
        paneBackgroundColor = new ColorSelectBox(120);
        return  new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Pane_Background")),paneBackgroundColor};
    }

    private Component[] getSlotBackgroundColor() {
        slotBackgroundColor = new ColorSelectBox(120);
        return new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Slot_Background")),slotBackgroundColor};
    }

    private Component[] getThermometerWidth() {
        thermometerWidth = new UISpinner(0, Double.MAX_VALUE, 0.1, 10);
        return new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Thermometer_Width")),thermometerWidth};
    }

    private void initRotate() {
        rotate = new UIButtonGroup(new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_AntiClockWise"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_ClockWise")});
    }

    private Component[] getInnerPaneBackgroundColor() {
        innerPaneBackgroundColor = new ColorSelectBox(120);
        return new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Inner_Pane_Background")),innerPaneBackgroundColor};
    }

    private JPanel createGaugeBandsPane() {
        colorPickerPane = new ColorPickerPaneWithFormula(parentPane, "meterString");
        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Range"), colorPickerPane);
    }


    public void populateBean(Plot plot) {
        if(plot == null) {
            return;
        }
        super.populateBean(plot);
        if(plot instanceof VanChartGaugePlot){
            VanChartGaugePlot gaugePlot = (VanChartGaugePlot)plot;
            GaugeDetailStyle detailStyle = gaugePlot.getGaugeDetailStyle();
            gaugeLayout.setSelectedIndex(detailStyle.isHorizontalLayout() ? 0 : 1);

            if(hingeColor != null){
                hingeColor.setSelectObject(detailStyle.getHingeColor());
            }
            if(hingeBackgroundColor != null){
                hingeBackgroundColor.setSelectObject(detailStyle.getHingeBackgroundColor());
            }
            if(needleColor != null){
                needleColor.setSelectObject(detailStyle.getNeedleColor());
            }
            if(paneBackgroundColor != null){
                paneBackgroundColor.setSelectObject(detailStyle.getPaneBackgroundColor());
            }
            if(slotBackgroundColor != null){
                slotBackgroundColor.setSelectObject(detailStyle.getSlotBackgroundColor());
            }
            if(rotate != null){
                rotate.setSelectedIndex(detailStyle.isAntiClockWise() ? 0 : 1);
            }
            if(innerPaneBackgroundColor != null){
                innerPaneBackgroundColor.setSelectObject(detailStyle.getInnerPaneBackgroundColor());
            }
            if(thermometerWidth != null){
                thermometerWidth.setValue(detailStyle.getThermometerWidth());
            }

            colorPickerPane.populateBean(detailStyle.getHotAreaColor());
        }
    }

    @Override
    public void updateBean(Plot plot) {
        if(plot == null){
            return;
        }
        super.updateBean(plot);
        if(plot instanceof VanChartGaugePlot){
            VanChartGaugePlot gaugePlot = (VanChartGaugePlot)plot;

            GaugeDetailStyle detailStyle = gaugePlot.getGaugeDetailStyle();
            detailStyle.setHorizontalLayout(gaugeLayout.getSelectedIndex() == 0);

            if(hingeColor != null){
                detailStyle.setHingeColor(hingeColor.getSelectObject());
            }
            if(hingeBackgroundColor != null){
                detailStyle.setHingeBackgroundColor(hingeBackgroundColor.getSelectObject());
            }
            if(needleColor != null){
                detailStyle.setNeedleColor(needleColor.getSelectObject());
            }
            if(paneBackgroundColor != null){
                detailStyle.setPaneBackgroundColor(paneBackgroundColor.getSelectObject());
            }
            if(slotBackgroundColor != null){
                detailStyle.setSlotBackgroundColor(slotBackgroundColor.getSelectObject());
            }
            if(rotate != null){
                detailStyle.setAntiClockWise(rotate.getSelectedIndex() == 0);
            }
            if(innerPaneBackgroundColor != null){
                detailStyle.setInnerPaneBackgroundColor(innerPaneBackgroundColor.getSelectObject());
            }
            if(thermometerWidth != null){
                detailStyle.setThermometerWidth(thermometerWidth.getValue());
            }

            colorPickerPane.updateBean(detailStyle.getHotAreaColor());
        }
    }
}
