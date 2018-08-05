package com.fr.van.chart.designer.style.label;

import com.fr.chart.chartattr.Plot;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.style.ChartTextAttrPane;

import com.fr.van.chart.designer.TableLayout4VanChartHelper;
import com.fr.van.chart.designer.style.VanChartStylePane;

import javax.swing.JPanel;
import java.awt.Component;

/**
 * Created by mengao on 2017/8/13.
 */
public class VanChartGaugeLabelDetailPane extends VanChartPlotLabelDetailPane {

    public VanChartGaugeLabelDetailPane(Plot plot, VanChartStylePane parent) {
        super(plot, parent);
    }

    protected JPanel createLabelStylePane(double[] row, double[] col, Plot plot) {
        style = new UIButtonGroup<Integer>(new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Automatic"),
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Custom")});
        textFontPane = initTextFontPane();

        initStyleListener();

        return TableLayoutHelper.createTableLayoutPane(getLabelStyleComponents(plot), row, col);
    }


    protected ChartTextAttrPane initTextFontPane () {
        return new ChartTextAttrPane();
    }

    protected JPanel getLabelPositionPane (Component[][] comps, double[] row, double[] col){
        return TableLayoutHelper.createTableLayoutPane(comps,row,col);
    }

    protected JPanel createTableLayoutPaneWithTitle(String title, JPanel panel) {
        return TableLayout4VanChartHelper.createGapTableLayoutPane(title, panel);
    }
}
