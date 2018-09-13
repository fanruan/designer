package com.fr.extended.chart;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.formula.TinyFormulaPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.data.report.AbstractReportDataContentPane;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by shine on 2018/3/7.
 */
public abstract class AbstractExtendedChartReportDataPane<T extends AbstractDataConfig> extends AbstractReportDataContentPane {

    private TinyFormulaPane seriesPane;
    private TinyFormulaPane valuePane;

    public AbstractExtendedChartReportDataPane() {
        initComponents();
    }

    protected void initComponents() {
        String[] labels = fieldLabel();
        Component[] formulaPanes = fieldComponents();

        int len = Math.min(labels.length, formulaPanes.length);

        Component[][] components = new Component[len + (hasCustomFieldPane() ? 2 : 0)][2];
        for (int i = 0; i < len; i++) {
            components[i] = new Component[]{new UILabel(labels[i], SwingConstants.LEFT), formulaPanes[i]};
        }

        if (hasCustomFieldPane()) {
            seriesPane = new TinyFormulaPane();
            valuePane = new TinyFormulaPane();
            components[len] = new Component[]{new UILabel("series", SwingConstants.LEFT), seriesPane};
            components[len + 1] = new Component[]{new UILabel("series", SwingConstants.LEFT), valuePane};
        }

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {f, COMPONENT_WIDTH};
        double[] rowSize = new double[len];
        Arrays.fill(rowSize, p);

        JPanel panel = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, 24, 6);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 15));


        this.setLayout(new BorderLayout());
        this.add(panel, BorderLayout.CENTER);
    }

    protected boolean hasCustomFieldPane() {
        return false;
    }

    protected Component[] fieldComponents() {
        return formulaPanes();
    }

    protected abstract String[] fieldLabel();

    protected abstract TinyFormulaPane[] formulaPanes();

    protected abstract void populate(T dataConf);

    protected abstract T update();

    public void populateBean(ChartCollection collection) {
        if (collection == null || collection.getSelectedChart() == null) {
            return;
        }

        Chart chart = collection.getSelectedChart();

        if (chart.getFilterDefinition() instanceof ExtendedReportDataSet) {
            ExtendedReportDataSet dataSet = (ExtendedReportDataSet) chart.getFilterDefinition();
            AbstractDataConfig dataConfig = dataSet.getDataConfig();

            if (dataConfig != null) {
                populate((T) dataConfig);

                if (hasCustomFieldPane() && dataConfig.getCustomFields().size() == 2) {
                    populateField(seriesPane, dataConfig.getCustomFields().get(0));
                    populateField(valuePane, dataConfig.getCustomFields().get(1));
                }
            }
        }
    }


    @Override
    public void updateBean(ChartCollection ob) {
        if (ob != null) {
            Chart chart = ob.getSelectedChart();
            if (chart != null) {
                ExtendedReportDataSet dataSet = new ExtendedReportDataSet();

                AbstractDataConfig dataConfig = update();
                dataSet.setDataConfig(dataConfig);

                List<ExtendedField> fieldList = new ArrayList<ExtendedField>();
                if (hasCustomFieldPane()) {
                    fieldList.add(new ExtendedField(seriesPane.updateBean()));
                    fieldList.add(new ExtendedField(valuePane.updateBean()));
                }
                dataConfig.setCustomFields(fieldList);

                chart.setFilterDefinition(dataSet);
            }
        }
    }

    protected void populateField(TinyFormulaPane formulaPane, ExtendedField field) {
        formulaPane.populateBean(field.getFieldName());
    }

    protected void updateField(TinyFormulaPane formulaPane, ExtendedField field) {
        field.setFieldName(formulaPane.updateBean());
    }

    @Override
    protected String[] columnNames() {
        return new String[0];
    }
}
