package com.fr.plugin.chart.range.component;

import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.style.series.MapColorPickerPaneWithFormula;
import com.fr.general.Inter;
import com.fr.plugin.chart.designer.TableLayout4VanChartHelper;
import com.fr.plugin.chart.range.SectionLegend;

import javax.swing.*;
import java.awt.*;

public class SectionLegendPane extends JPanel{
    private static final long serialVersionUID = 1614283200308877353L;

    private MapColorPickerPaneWithFormula intervalConfigPane;
    private LegendLabelFormatPane labelFormPane;

    public SectionLegendPane(){
        initComponents();
    }

    private void initComponents() {
        intervalConfigPane = createSectionIntervalConfigPane();
        JPanel intervalConfigPaneWithTitle = TableLayout4VanChartHelper.createExpandablePaneWithTitle(Inter.getLocText("Plugin-ChartF_Section_Config"), intervalConfigPane);
        labelFormPane = new LegendLabelFormatPane(){
            @Override
            protected void checkCustomLabelText() {
                setCustomFormatterText(SectionLegend.DEFAULT_LABEL_FUNCTION);
            }
        };

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] col = {f};
        double[] row = {p, p, p};
        Component[][] components = new Component[][]{
                new Component[]{null},
                new Component[]{intervalConfigPaneWithTitle},
                new Component[]{labelFormPane},
        };

        JPanel panel = TableLayoutHelper.createTableLayoutPane(components, row, col);

        this.setLayout(new BorderLayout());
        this.add(panel, BorderLayout.CENTER);
    }

    protected MapColorPickerPaneWithFormula createSectionIntervalConfigPane() {
        return new SectionIntervalConfigPane();
    }

    public void populate(SectionLegend sectionLegend){
        if (intervalConfigPane != null) {
            intervalConfigPane.populateBean(sectionLegend.getMapHotAreaColor());
        }
        if (labelFormPane != null) {
            labelFormPane.populate(sectionLegend.getLegendLabelFormat());
        }
    }

    public void update(SectionLegend sectionLegend){
        if (intervalConfigPane != null) {
            intervalConfigPane.updateBean(sectionLegend.getMapHotAreaColor());
        }
        if (labelFormPane != null) {
            labelFormPane.update(sectionLegend.getLegendLabelFormat());
        }
    }
}