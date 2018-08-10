package com.fr.design.mainframe.chart.gui.style.series;

import com.fr.chart.base.AreaColor;
import com.fr.chart.chartattr.MeterPlot;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartglyph.MapHotAreaColor;
import com.fr.chart.chartglyph.MeterInterval;
import com.fr.chart.chartglyph.MeterStyle;
import com.fr.design.gui.frpane.UINumberDragPane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.style.ChartFillStylePane;


import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;
import java.awt.Component;
import java.util.List;

/**
 * 属性表, 仪表盘, 图表样式-系列 界面.
 *
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2013-1-6 上午12:41:54
 */
public class MeterSeriesPane extends AbstractPlotSeriesPane {

    private UITextField unit;
    private UINumberDragPane angleMax;
    private UIComboBox order;
    private UIColorPickerPane colorPickerPane;

    public MeterSeriesPane(ChartStylePane parent, Plot plot) {
        super(parent, plot, false);
    }

    @Override
    protected JPanel getContentInPlotType() {
        unit = new UITextField();
        angleMax = new UINumberDragPane(0, 360);
        String[] orderUnit = new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Ge"), com.fr.design.i18n.Toolkit.i18nText("Unit_Ten"), com.fr.design.i18n.Toolkit.i18nText("Unit_Hundred"),
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Unit_Thousand"), com.fr.design.i18n.Toolkit.i18nText("Unit_Ten_Thousand")};
        order = new UIComboBox(orderUnit);
        colorPickerPane = createColorPickerPane();

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {p, f};
        double[] rowSize = {p, p, p, p, p};
        return TableLayoutHelper.createTableLayoutPane(createComponents(), rowSize, columnSize);
    }

    protected UIColorPickerPane createColorPickerPane(){
        return new ColorPickerPaneWithFormula(parentPane, "meterString") {
            protected double getEditAreaWidth () {
                return 120;
            }
            protected int getColorgroupMarginLeft () {
                return 20;
            }
        };
    }


    private Component[][] createComponents() {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {p, f};
        double[] rowSize = {p};
        Component[][] tmpComponent = new Component[][]{
                new Component[]{new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Pointer_A_Tick_Order") + ":", SwingUtilities.LEFT), order}
        };
        JPanel orderPane = TableLayoutHelper.createTableLayoutPane(tmpComponent, rowSize, columnSize);
        Component[][] components = new Component[][]{
                new Component[]{new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nText("Chart_Needle_Max_Range") + ":", SwingUtilities.LEFT), angleMax},
                new Component[]{new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Units") + ":", SwingUtilities.LEFT), unit},
                new Component[]{new JSeparator(), null},
                new Component[]{orderPane, null},
                new Component[]{colorPickerPane, null},

        };
        if (plot.isMeterPlot()) {
            MeterPlot meter = (MeterPlot) plot;
            if (meter.getMeterStyle().getMeterType() == MeterStyle.METER_NORMAL) {
                return components;
            } else {
                components = new Component[][]{
                        new Component[]{new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Units") + ":", SwingUtilities.LEFT), unit},
                        new Component[]{new JSeparator(), null},
                        new Component[]{orderPane, null},
                        new Component[]{colorPickerPane, null},

                };
            }
        }


        return components;
    }

    protected ChartFillStylePane getFillStylePane() {
        return null;
    }

    public void populateBean(Plot plot) {
        if (plot.isMeterPlot()) {
            MeterPlot merter = (MeterPlot) plot;
            MeterStyle meterChartStyle = merter.getMeterStyle();
            colorPickerPane.populateBean(meterChartStyle.getMapHotAreaColor());
            order.setSelectedIndex(meterChartStyle.getOrder());
            unit.setText(meterChartStyle.getUnits());
            angleMax.populateBean(new Double(meterChartStyle.getMaxArrowAngle()));
        }
    }

    public void updateBean(Plot plot) {
        if (plot.isMeterPlot()) {
            MeterPlot meter = (MeterPlot) plot;
            MeterStyle meterChartStyle = meter.getMeterStyle();
            MapHotAreaColor hotAreaColor = meterChartStyle.getMapHotAreaColor();
            colorPickerPane.updateBean(hotAreaColor);
            meterChartStyle.setOrder(order.getSelectedIndex());
            meterChartStyle.setMaxArrowAngle(angleMax.updateBean().intValue());
            meterChartStyle.setDesignTyle(colorPickerPane.getDesignType());
            meterChartStyle.setUnits(unit.getText());
            if (meterChartStyle.getDesignType() == MeterStyle.CUSTOM) {
                meterChartStyle.clearAllInterval();
                int areaNumber = hotAreaColor.getAreaNumber();
                List areaColorList = hotAreaColor.getAreaColorList();
                for (int i = areaNumber - 1; i >= 0; i--) {
                    AreaColor areaColor = (AreaColor) areaColorList.get(i);
                    MeterInterval meterInterval = new MeterInterval();
                    meterInterval.setBeginValue(areaColor.getMax());
                    meterInterval.setEndValue(areaColor.getMin());
                    meterInterval.setBackgroundColor(areaColor.getAreaColor());
                    meterChartStyle.addInterval(meterInterval);
                }
            }
        }
    }
}
