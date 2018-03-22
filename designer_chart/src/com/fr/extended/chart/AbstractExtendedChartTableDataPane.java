package com.fr.extended.chart;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.data.table.AbstractTableDataContentPane;
import com.fr.general.GeneralUtils;

import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Arrays;
import java.util.List;

/**
 * Created by shine on 2018/3/2.
 */
public abstract class AbstractExtendedChartTableDataPane<T extends AbstractDataConfig> extends AbstractTableDataContentPane {

    public AbstractExtendedChartTableDataPane() {
        initComponents();
    }

    protected void initComponents() {

        String[] labels = fieldLabel();
        UIComboBox[] comboBoxes = filedComboBoxes();

        int len = Math.min(labels.length, comboBoxes.length);

        Component[][] components = new Component[len][2];
        for (int i = 0; i < len; i++) {
            UIComboBox comboBox = comboBoxes[i];
            comboBox.setPreferredSize(new Dimension(100, 20));

            components[i] = new Component[]{new UILabel(labels[i], SwingConstants.LEFT), comboBox};
        }

        double p = TableLayout.PREFERRED;
        double[] columnSize = {p, p};
        double[] rowSize = new double[len];
        Arrays.fill(rowSize, p);

        JPanel panel = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);

        this.setLayout(new BorderLayout());
        this.add(panel, BorderLayout.CENTER);
    }

    protected abstract String[] fieldLabel();

    protected abstract UIComboBox[] filedComboBoxes();

    protected abstract void populate(T dataConf);

    protected abstract T update();

    @Override
    public void populateBean(ChartCollection collection) {

        if (collection != null) {
            Chart chart = collection.getSelectedChart();
            if (chart != null && chart instanceof AbstractChart) {
                AbstractDataConfig dataConfig = ((AbstractChart) chart).getDataConfig();
                if (dataConfig != null) {
                    populate((T) dataConfig);
                }
            }
        }
    }


    @Override
    public void updateBean(ChartCollection ob) {
        if (ob != null) {
            Chart chart = ob.getSelectedChart();
            if (chart != null) {

                ExtendedTableDataSet dataSet = new ExtendedTableDataSet();
                dataSet.setDataConfig(update());

                chart.setFilterDefinition(dataSet);
            }
        }
    }

    @Override
    public void checkBoxUse(boolean hasUse) {
        for (UIComboBox comboBox : filedComboBoxes()) {
            comboBox.setEnabled(hasUse);
        }
    }

    @Override
    public void clearAllBoxList() {
        for (UIComboBox comboBox : filedComboBoxes()) {
            clearBoxItems(comboBox);
        }
    }

    @Override
    protected void refreshBoxListWithSelectTableData(List columnNameList) {
        for (UIComboBox comboBox : filedComboBoxes()) {
            refreshBoxItems(comboBox, columnNameList);
        }
    }

    protected void populateField(UIComboBox comboBox, ExtendedField field) {
        comboBox.setSelectedItem(field.getFieldName());
    }

    protected void updateField(UIComboBox comboBox, ExtendedField field) {
        field.setFieldName(GeneralUtils.objectToString(comboBox.getSelectedItem()));
    }

}
