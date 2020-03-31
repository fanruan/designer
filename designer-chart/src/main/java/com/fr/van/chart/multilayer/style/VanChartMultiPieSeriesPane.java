package com.fr.van.chart.multilayer.style;

import com.fr.chart.chartattr.Plot;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.plugin.chart.multilayer.VanChartMultiPiePlot;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;
import com.fr.van.chart.designer.component.VanChartBeautyPane;
import com.fr.van.chart.pie.VanChartPieSeriesPane;

import javax.swing.JPanel;
import java.awt.Component;

/**
 * Created by Fangjie on 2016/6/15.
 */
public class VanChartMultiPieSeriesPane extends VanChartPieSeriesPane {

    private UIButtonGroup<Integer> gradualLevel;
    private UIButtonGroup<Integer> supportRotation;
    protected UIButtonGroup<Integer> supportDrill;

    public VanChartMultiPieSeriesPane(ChartStylePane parent, Plot plot) {
        super(parent, plot);
    }

    protected JPanel getContentInPlotType() {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {f};
        double[] rowSize = {p,p,p,p,p,p,p};
        Component[][] components = new Component[][]{
                new Component[]{createSeriesStylePane(rowSize, new double[]{p,f})},
                new Component[]{createBorderPane()},
        };

        return TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
    }

    protected JPanel createSeriesStylePane(double[] row, double[] col) {
        gradualLevel = new UIButtonGroup<Integer>(new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Gradual_Light"),
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Gradual_Deep")});
        startAngle = new UISpinner(MIN_ANGLE, MAX_ANGLE, 1, 0);
        endAngle = new UISpinner(MIN_ANGLE, MAX_ANGLE, 1, 0);
        innerRadius = new UISpinner(0, 100, 1, 0);
        supportRotation = new UIButtonGroup<Integer>(new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Open"),
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Close")});
        supportDrill = new UIButtonGroup<Integer>(new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Open"),
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Close")});

        Component[][] components = new Component[][]{
                new Component[]{null,null},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Gradual_Level") ),gradualLevel},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Start_Angle") ),startAngle},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_End_Angle")),endAngle},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Inner_Radius")),innerRadius},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Drill")),supportDrill},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Rotation")),supportRotation}

        };
        JPanel panel = TableLayout4VanChartHelper.createGapTableLayoutPane(components, row, col);
        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Widget_Style"), panel);
    }

    protected void populatePieAttr() {
        if(plot.accept(VanChartMultiPiePlot.class)){
            VanChartMultiPiePlot multiPiePlot = (VanChartMultiPiePlot)plot;
            gradualLevel.setSelectedIndex(multiPiePlot.isLight() ? 0 : 1);
            startAngle.setValue(multiPiePlot.getStartAngle());
            endAngle.setValue(multiPiePlot.getEndAngle());
            innerRadius.setValue(multiPiePlot.getInnerRadiusPercent());
            supportDrill.setSelectedIndex(multiPiePlot.isSupportDrill() ? 0 : 1);
            supportRotation.setSelectedIndex(multiPiePlot.isSupportRotation() ? 0 : 1);
        }
    }

    @Override
    protected void updatePieAttr() {
        if(plot.accept(VanChartMultiPiePlot.class)){
            VanChartMultiPiePlot multiPiePlot = (VanChartMultiPiePlot)plot;
            multiPiePlot.setLight(gradualLevel.getSelectedIndex() == 0);
            multiPiePlot.setStartAngle(startAngle.getValue());
            multiPiePlot.setEndAngle(endAngle.getValue());
            multiPiePlot.setInnerRadiusPercent(innerRadius.getValue());
            multiPiePlot.setSupportDrill(supportDrill.getSelectedIndex() == 0);
            multiPiePlot.setSupportRotation(supportRotation.getSelectedIndex() == 0);
        }
    }

    @Override
    protected VanChartBeautyPane createStylePane() {
        return null;
    }
}
