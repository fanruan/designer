package com.fr.van.chart.pie;


import com.fr.chart.chartattr.Plot;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartStylePane;

import com.fr.plugin.chart.PiePlot4VanChart;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;
import com.fr.van.chart.designer.style.series.VanChartAbstractPlotSeriesPane;

import javax.swing.JPanel;
import java.awt.Component;

//图表属性-样式 饼图系列界面

public class VanChartPieSeriesPane extends VanChartAbstractPlotSeriesPane {

    private static final long serialVersionUID = 2800670681079289596L;
    protected static final double MIN_ANGLE = PiePlot4VanChart.START_ANGLE;
    protected static final double MAX_ANGLE = PiePlot4VanChart.END_ANGLE;

    protected UISpinner startAngle;
    protected UISpinner endAngle;
    protected UISpinner innerRadius;
    private UIButtonGroup<Boolean> supportRotation;

    public VanChartPieSeriesPane(ChartStylePane parent, Plot plot) {
        super(parent, plot);
    }

    protected JPanel getContentInPlotType() {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double e = TableLayout4VanChartHelper.EDIT_AREA_WIDTH;
        double[] columnSize = {f};
        double[] rowSize = {p,p,p,p,p,p};
        Component[][] components = new Component[][]{
                new Component[]{getColorPane()},
                new Component[]{createSeriesStylePane(rowSize, new double[]{f, e})},
                new Component[]{createBorderPane()},
        };

        return TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize);
    }


    protected JPanel createSeriesStylePane(double[] row, double[] col) {
        startAngle = new UISpinner(MIN_ANGLE, MAX_ANGLE, 1, 0);
        endAngle = new UISpinner(MIN_ANGLE, MAX_ANGLE, 1, 0);
        innerRadius = new UISpinner(0, 100, 1, 0);
        supportRotation = new UIButtonGroup<Boolean>(new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_On"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Off")}, new Boolean[]{true, false});


        Component[][] components = new Component[][]{
                new Component[]{null,null},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Start_Angle")),startAngle},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_End_Angle")),endAngle},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Inner_Radius")),innerRadius},
                new Component[]{createRadiusPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Radius_Set")),null},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Rotation")),supportRotation}
        };


        JPanel panel = TableLayout4VanChartHelper.createGapTableLayoutPane(components, row, col);
        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Widget_Style"), panel);
    }

    public void populateBean(Plot plot) {
        if(plot == null) {
            return;
        }

        super.populateBean(plot);

        populatePieAttr();

    }

    protected void populatePieAttr() {
        if(plot.accept(PiePlot4VanChart.class)) {
            PiePlot4VanChart piePlot4VanChart = (PiePlot4VanChart) plot;
            startAngle.setValue(piePlot4VanChart.getStartAngle());
            endAngle.setValue(piePlot4VanChart.getEndAngle());
            innerRadius.setValue(piePlot4VanChart.getInnerRadiusPercent());
            supportRotation.setSelectedIndex(piePlot4VanChart.isSupportRotation() == true ?0:1);
        }
    }

    public void updateBean(Plot plot) {
        if(plot == null) {
            return;
        }

        super.updateBean(plot);

        updatePieAttr();

    }

    protected void updatePieAttr() {
        if(plot.accept(PiePlot4VanChart.class)){
            PiePlot4VanChart piePlot4VanChart = (PiePlot4VanChart)plot;
            piePlot4VanChart.setStartAngle(startAngle.getValue());
            piePlot4VanChart.setEndAngle(endAngle.getValue());
            piePlot4VanChart.setInnerRadiusPercent(innerRadius.getValue());
            piePlot4VanChart.setSupportRotation(supportRotation.getSelectedItem());
        }
    }
}
