package com.fr.extended.chart;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.data.util.function.AbstractDataFunction;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.CalculateComboBox;
import com.fr.design.mainframe.chart.gui.data.table.AbstractTableDataContentPane;
import com.fr.general.GeneralUtils;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Component;
import java.util.Arrays;
import java.util.List;

/**
 * Created by shine on 2018/3/2.
 */
public abstract class AbstractExtendedChartTableDataPane<T extends AbstractDataConfig> extends AbstractTableDataContentPane {

    private ExtendedCustomFieldComboBoxPane customFieldComboBoxPane;

    public AbstractExtendedChartTableDataPane() {
        initComponents();
    }

    protected void initComponents() {

        String[] labels = fieldLabels();
        Component[] fieldComponents = fieldComponents();

        int len = Math.min(labels.length, fieldComponents.length);

        Component[][] components = new Component[len][2];
        for (int i = 0; i < len; i++) {
            components[i] = new Component[]{new UILabel(labels[i], SwingConstants.LEFT), fieldComponents[i]};
        }

        double p = TableLayout.PREFERRED;
        double[] columnSize = {ChartDataPane.LABEL_WIDTH, 122};
        double[] rowSize = new double[len];
        Arrays.fill(rowSize, p);

        JPanel panel = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize,0,6);

        this.setLayout(new BorderLayout(0, 4));
        this.setBorder(BorderFactory.createEmptyBorder(2, 24, 0, 15));
        this.add(panel, BorderLayout.NORTH);

        customFieldComboBoxPane = createExtendedCustomFieldComboBoxPane();
        if (customFieldComboBoxPane != null) {
            this.add(customFieldComboBoxPane, BorderLayout.CENTER);
        }

        this.add(addSouthPane(), BorderLayout.SOUTH);
    }

    protected JPanel addSouthPane() {
        return new JPanel();
    }

    protected ExtendedCustomFieldComboBoxPane createExtendedCustomFieldComboBoxPane() {
        return null;
    }

    protected Component[] fieldComponents() {
        return filedComboBoxes();
    }

    protected abstract String[] fieldLabels();

    protected abstract UIComboBox[] filedComboBoxes();

    protected abstract void populate(T dataConf);

    protected abstract T update();

    @Override
    public void populateBean(ChartCollection collection) {
        if (collection == null || collection.getSelectedChart() == null) {
            return;
        }

        Chart chart = collection.getSelectedChart();

        if (chart.getFilterDefinition() instanceof ExtendedTableDataSet) {
            ExtendedTableDataSet dataSet = (ExtendedTableDataSet) chart.getFilterDefinition();

            populateDataSet(dataSet);

        }

    }

    public void populateDataSet(DataSet dataSet) {

        if (dataSet == null) {
            return;
        }

        AbstractDataConfig dataConfig = dataSet.getDataConfig();

        if (dataConfig != null) {
            populate((T) dataConfig);

            if (customFieldComboBoxPane != null) {
                customFieldComboBoxPane.populateBean(dataConfig);
            }
        }
    }

    public ExtendedTableDataSet updateDataSet() {

        ExtendedTableDataSet dataSet = new ExtendedTableDataSet();

        dataSet.setDataConfig(update());

        if (customFieldComboBoxPane != null) {
            customFieldComboBoxPane.updateBean(dataSet.getDataConfig());
        }

        return dataSet;
    }


    @Override
    public void updateBean(ChartCollection ob) {
        if (ob != null) {
            Chart chart = ob.getSelectedChart();
            if (chart != null) {

                chart.setFilterDefinition(updateDataSet());
            }
        }
    }

    @Override
    public void checkBoxUse(boolean hasUse) {
        for (Component component : fieldComponents()) {
            component.setEnabled(hasUse);
        }
        if (customFieldComboBoxPane != null) {
            customFieldComboBoxPane.checkBoxUse(hasUse);
        }
    }

    @Override
    public void clearAllBoxList() {
        for (UIComboBox comboBox : filedComboBoxes()) {
            clearBoxItems(comboBox);
        }
        if (customFieldComboBoxPane != null) {
            customFieldComboBoxPane.clearAllBoxList();
        }
    }

    @Override
    protected void refreshBoxListWithSelectTableData(List columnNameList) {
        for (UIComboBox comboBox : filedComboBoxes()) {
            refreshBoxItems(comboBox, columnNameList);
        }
        if (customFieldComboBoxPane != null) {
            customFieldComboBoxPane.refreshBoxListWithSelectTableData(columnNameList);
        }
    }

    protected void populateField(UIComboBox comboBox, ExtendedField field) {
        populateFunctionField(comboBox, null, field);
    }

    protected void updateField(UIComboBox comboBox, ExtendedField field) {
        updateFunctionField(comboBox, null, field);
    }

    protected void populateFunctionField(UIComboBox comboBox, CalculateComboBox calculateComboBox, ExtendedField field) {
        comboBox.setSelectedItem(field.getFieldName());
        if (calculateComboBox != null) {
            calculateComboBox.populateBean((AbstractDataFunction) field.getDataFunction());
        }
    }

    protected void updateFunctionField(UIComboBox comboBox, CalculateComboBox calculateComboBox, ExtendedField field) {
        field.setFieldName(GeneralUtils.objectToString(comboBox.getSelectedItem()));
        if (calculateComboBox != null) {
            field.setDataFunction(calculateComboBox.updateBean());
        } else {
            field.setDataFunction(null);
        }
    }

}
