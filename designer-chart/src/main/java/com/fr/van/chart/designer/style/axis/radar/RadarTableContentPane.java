package com.fr.van.chart.designer.style.axis.radar;

import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.data.table.AbstractTableDataContentPane;

import com.fr.plugin.chart.radar.data.RadarYAxisTableDefinition;

import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.List;

public class RadarTableContentPane extends AbstractTableDataContentPane {
    private UIComboBox categoryNameComboBox;
    private UIComboBox minValueComboBox;
    private UIComboBox maxValueComboBox;

    public RadarTableContentPane() {
        this.setLayout(new BorderLayout());
        initAllComponent();
        this.add(getContentPane(), BorderLayout.CENTER);
    }

    private void initAllComponent() {
        categoryNameComboBox = new UIComboBox();
        categoryNameComboBox.setPreferredSize(new Dimension(100, 20));

        minValueComboBox = new UIComboBox();
        minValueComboBox.setPreferredSize(new Dimension(100, 20));

        maxValueComboBox = new UIComboBox();
        maxValueComboBox.setPreferredSize(new Dimension(100, 20));

        addAutoItem();

        categoryNameComboBox.setEnabled(false);
        minValueComboBox.setEnabled(false);
        maxValueComboBox.setEnabled(false);


    }

    private JPanel getContentPane() {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] row = {p, p, p};
        double[] col = {p, f};

        Component[][] components = new Component[][]{
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Category") + ":", SwingConstants.RIGHT), categoryNameComboBox},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Min_Value") + ":", SwingConstants.RIGHT), minValueComboBox},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Max_Value") + ":", SwingConstants.RIGHT), maxValueComboBox}
        };

        return TableLayoutHelper.createTableLayoutPane(components, row, col);
    }

    @Override
    public void updateBean(ChartCollection ob) {
    }


    public void updateBean(RadarYAxisTableDefinition definition) {
        Object seriesName = categoryNameComboBox.getSelectedItem();
        Object minValue = minValueComboBox.getSelectedItem();
        Object maxValue = maxValueComboBox.getSelectedItem();

        if (seriesName != null) {
            definition.setCategoryName(seriesName.toString());
        } else {
            definition.setCategoryName(null);
        }
        if (minValue != null) {
            definition.setMinValue(minValue.toString());
        }else {
            definition.setCategoryName(null);
        }
        if (maxValue != null) {
            definition.setMaxValue(maxValue.toString());
        }else {
            definition.setCategoryName(null);
        }

    }

    public void populateBean(RadarYAxisTableDefinition definition) {
        categoryNameComboBox.setSelectedItem(definition.getCategoryName());
        minValueComboBox.setSelectedItem(definition.getMinValue());
        maxValueComboBox.setSelectedItem(definition.getMaxValue());
    }

    @Override
    public void clearAllBoxList() {
        clearBoxItems(categoryNameComboBox);

        clearBoxItems(minValueComboBox);

        clearBoxItems(maxValueComboBox);


    }

    @Override
    public void refreshBoxListWithSelectTableData(List columnNameList) {
        refreshBoxItems(categoryNameComboBox, columnNameList);
        refreshBoxItems(minValueComboBox, columnNameList);
        refreshBoxItems(maxValueComboBox, columnNameList);
        addAutoItem();
    }

    @Override
    public void checkBoxUse(boolean hasUse) {
        categoryNameComboBox.setEnabled(hasUse);
        minValueComboBox.setEnabled(hasUse);
        maxValueComboBox.setEnabled(hasUse);
    }
    public void addAutoItem(){
        Object autoItem = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Auto");
        minValueComboBox.addItem(autoItem);
        maxValueComboBox.addItem(autoItem);
        minValueComboBox.setSelectedItem(autoItem);
        maxValueComboBox.setSelectedItem(autoItem);

    }
}
