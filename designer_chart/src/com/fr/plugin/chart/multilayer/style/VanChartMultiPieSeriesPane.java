package com.fr.plugin.chart.multilayer.style;

import com.fr.chart.chartattr.Plot;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.general.Inter;
import com.fr.plugin.chart.designer.TableLayout4VanChartHelper;
import com.fr.plugin.chart.multilayer.VanChartMultiPiePlot;
import com.fr.plugin.chart.pie.VanChartPieSeriesPane;

import javax.swing.*;
import java.awt.*;

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
        gradualLevel = new UIButtonGroup<Integer>(new String[]{Inter.getLocText("Plugin-ChartF_Gradual_Light"),
                Inter.getLocText("Plugin-ChartF_Gradual_Deep")});
        startAngle = new UISpinner(MIN_ANGLE, MAX_ANGLE, 1, 0);
        endAngle = new UISpinner(MIN_ANGLE, MAX_ANGLE, 1, 0);
        innerRadius = new UISpinner(0, 100, 1, 0);
        supportRotation = new UIButtonGroup<Integer>(new String[]{Inter.getLocText("Plugin-ChartF_Open"),
                Inter.getLocText("Plugin-ChartF_Close")});
        supportDrill = new UIButtonGroup<Integer>(new String[]{Inter.getLocText("Plugin-ChartF_Open"),
                Inter.getLocText("Plugin-ChartF_Close")});

        Component[][] components = new Component[][]{
                new Component[]{null,null},
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_Gradual_Level") ),gradualLevel},
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_StartAngle") ),startAngle},
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_EndAngle")),endAngle},
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_InnerRadius")),innerRadius},
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_Drill")),supportDrill},
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_Rotation")),supportRotation}

        };
        JPanel panel = TableLayout4VanChartHelper.createGapTableLayoutPane(components, row, col);
        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(Inter.getLocText("FR-Designer-Widget_Style"), panel);
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
}
