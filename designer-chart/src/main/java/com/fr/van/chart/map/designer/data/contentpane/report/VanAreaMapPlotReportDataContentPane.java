package com.fr.van.chart.map.designer.data.contentpane.report;

import com.fr.base.chart.chartdata.TopDefinitionProvider;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartdata.SeriesDefinition;
import com.fr.design.formula.TinyFormulaPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.report.AbstractReportDataContentPane;
import com.fr.plugin.chart.map.data.VanMapReportDefinition;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

/**
 * Created by Mitisky on 16/5/16.
 */
public class VanAreaMapPlotReportDataContentPane extends AbstractReportDataContentPane {
    protected TinyFormulaPane areaName;

    public VanAreaMapPlotReportDataContentPane(ChartDataPane parent) {
        initEveryPane();
        initAreaName();
        JPanel panel = getContent();
        panel.setBorder(BorderFactory.createEmptyBorder(0, 24, 0, 15));
        this.add(panel, "0,0,2,0");
    }

    protected void initAreaName() {
        areaName = new TinyFormulaPane() {
            @Override
            protected void initLayout() {
                this.setLayout(new BorderLayout(4, 0));

                UILabel label = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Area_Name"));
                label.setPreferredSize(new Dimension(75, 20));
                this.add(label, BorderLayout.WEST);

                formulaTextField.setPreferredSize(new Dimension(100, 20));
                this.add(formulaTextField, BorderLayout.CENTER);
                this.add(formulaTextFieldButton, BorderLayout.EAST);
            }
        };
    }

    protected JPanel getContent() {
        return getFormulaPane();
    }

    protected JPanel getFormulaPane() {
        return areaName;
    }

    @Override
    protected String[] columnNames() {
        return new String[]{
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Series_Name"),
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Series_Value")
        };
    }

    public void populateBean(ChartCollection collection) {
        TopDefinitionProvider topDefinitionProvider = collection.getSelectedChart().getFilterDefinition();
        if (topDefinitionProvider instanceof VanMapReportDefinition) {

            VanMapReportDefinition mapReportDefinition = (VanMapReportDefinition) topDefinitionProvider;

            populateDefinition(mapReportDefinition);
        }
    }

    protected void populateDefinition(VanMapReportDefinition mapReportDefinition) {
        if (mapReportDefinition.getCategoryName() != null) {
            areaName.getUITextField().setText(mapReportDefinition.getCategoryName().toString());
        }
        if (mapReportDefinition.getSeriesSize() > 0) {
            seriesPane.populateBean(mapReportDefinition.getEntryList());
        }
    }

    public void updateBean(ChartCollection collection) {
        collection.getSelectedChart().setFilterDefinition(new VanMapReportDefinition());

        TopDefinitionProvider topDefinitionProvider = collection.getSelectedChart().getFilterDefinition();
        if (topDefinitionProvider instanceof VanMapReportDefinition) {

            VanMapReportDefinition mapReportDefinition = (VanMapReportDefinition) topDefinitionProvider;

            updateDefinition(mapReportDefinition);
        }
    }

    protected void updateDefinition(VanMapReportDefinition mapReportDefinition) {
        mapReportDefinition.setCategoryName(canBeFormula(areaName.getUITextField().getText()));
        mapReportDefinition.setLatitude(null);
        mapReportDefinition.setLongitude(null);

        List<Object[]> list = seriesPane.updateBean();
        for (Object[] o : list) {
            SeriesDefinition sd = new SeriesDefinition();

            sd.setSeriesName(canBeFormula(o[0]));
            sd.setValue(canBeFormula(o[1]));
            mapReportDefinition.addSeriesValue(sd);
        }
    }
}