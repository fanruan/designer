package com.fr.van.chart.range.component;

import com.fr.design.gui.frpane.AbstractAttrNoScrollPane;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.style.series.MapColorPickerPaneWithFormula;
import com.fr.general.Inter;
import com.fr.plugin.chart.range.SectionLegend;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;

public class SectionLegendPane extends JPanel{
    private static final long serialVersionUID = 1614283200308877353L;

    private MapColorPickerPaneWithFormula intervalConfigPane;
    private LegendLabelFormatPane labelFormPane;
    private AbstractAttrNoScrollPane parent;

    public SectionLegendPane(AbstractAttrNoScrollPane parent) {
        this.parent = parent;
        initComponents();
    }

    private void initComponents() {
        intervalConfigPane = createSectionIntervalConfigPane(this.parent);
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

    protected MapColorPickerPaneWithFormula createSectionIntervalConfigPane(AbstractAttrNoScrollPane parent) {
        return new SectionIntervalConfigPane(parent);
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