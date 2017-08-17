package com.fr.plugin.chart.range.component;

import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;
import com.fr.plugin.chart.designer.TableLayout4VanChartHelper;
import com.fr.plugin.chart.designer.style.VanChartStylePane;
import com.fr.plugin.chart.range.GradualLegend;

import javax.swing.*;
import java.awt.*;

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
        JPanel intervalConfigPaneWithTitle = TableLayout4VanChartHelper.createExpandablePaneWithTitle(Inter.getLocText("Plugin-ChartF_Section_Config"),intervalConfigPane);
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