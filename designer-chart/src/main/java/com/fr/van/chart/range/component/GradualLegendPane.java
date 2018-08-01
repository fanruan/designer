package com.fr.van.chart.range.component;

import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;

import com.fr.plugin.chart.range.GradualLegend;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;
import com.fr.van.chart.designer.style.VanChartStylePane;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;

public class GradualLegendPane extends JPanel{
    private static final long serialVersionUID = 1614283200308877353L;

    private GradualIntervalConfigPane intervalConfigPane;
    private LegendLabelFormatPane labelFormPane;


    public GradualLegendPane(){
        initComponents();
    }

    public void setParentPane(VanChartStylePane parent) {
        labelFormPane.setParentPane(parent);
    }
    private void initComponents() {
        intervalConfigPane = createGradualIntervalConfigPane();
        JPanel intervalConfigPaneWithTitle = TableLayout4VanChartHelper.createExpandablePaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Section_Config"),intervalConfigPane);
        labelFormPane = new LegendLabelFormatPane();

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] col = {f};
        double[] row = {p, p, p, p};
        Component[][] components = new Component[][]{
                new Component[]{null},
                new Component[]{intervalConfigPaneWithTitle},
                new Component[]{labelFormPane},
        };

        JPanel panel = TableLayoutHelper.createTableLayoutPane(components, row, col);
        this.setLayout(new BorderLayout());
        this.add(panel,BorderLayout.CENTER);
    }

    protected GradualIntervalConfigPane createGradualIntervalConfigPane() {
        return new GradualIntervalConfigPane();
    }

    public void populate(GradualLegend gradualLegend){
        if (intervalConfigPane != null) {
            intervalConfigPane.populate(gradualLegend.getGradualIntervalConfig());
        }
        if (labelFormPane != null) {
            labelFormPane.populate(gradualLegend.getLegendLabelFormat());
        }
    }

    public void update(GradualLegend gradualLegend){
        if (intervalConfigPane != null) {
            intervalConfigPane.update(gradualLegend.getGradualIntervalConfig());
        }
        if (labelFormPane != null) {
            labelFormPane.update(gradualLegend.getLegendLabelFormat());
        }
    }
}