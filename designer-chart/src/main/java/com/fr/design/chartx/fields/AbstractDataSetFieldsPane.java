package com.fr.design.chartx.fields;

import com.fr.chartx.data.field.AbstractColumnFieldCollection;
import com.fr.chartx.data.field.ColumnField;
import com.fr.data.util.function.AbstractDataFunction;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.CalculateComboBox;
import com.fr.design.mainframe.chart.gui.data.table.DataPaneHelper;
import com.fr.general.GeneralUtils;
import com.fr.stable.StringUtils;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.util.Arrays;
import java.util.List;
import java.awt.BorderLayout;
import java.awt.Component;

import static com.fr.design.mainframe.chart.gui.data.table.DataPaneHelper.refreshBoxItems;

/**
 * Created by shine on 2019/5/16.
 * 数据集数据源 具体有哪些字段的一个抽象pane
 */
public abstract class AbstractDataSetFieldsPane<T extends AbstractColumnFieldCollection> extends BasicBeanPane<T> {

    public AbstractDataSetFieldsPane() {
        initComponents();
    }

    protected void initComponents() {

        this.setLayout(new BorderLayout(0, 6));
        this.setBorder(BorderFactory.createEmptyBorder(2, 24, 0, 15));

        JPanel north = createNorthPane(),
                center = createCenterPane(),
                south = createSouthPane();

        if (north != null) {
            this.add(north, BorderLayout.NORTH);
        }

        if (center != null) {
            this.add(center, BorderLayout.CENTER);
        }

        if (south != null) {
            this.add(south, BorderLayout.SOUTH);
        }
    }

    protected JPanel createNorthPane() {
        return null;
    }

    protected JPanel createCenterPane() {
        String[] labels = fieldLabels();
        Component[] fieldComponents = fieldComponents();

        int len = Math.min(labels.length, fieldComponents.length);

        if (len == 0) {
            return null;
        }

        Component[][] components = new Component[len][2];
        for (int i = 0; i < len; i++) {
            components[i] = new Component[]{new UILabel(labels[i], SwingConstants.LEFT), fieldComponents[i]};
        }
        double p = TableLayout.PREFERRED;
        double[] columnSize = {ChartDataPane.LABEL_WIDTH, 122};
        double[] rowSize = new double[len];
        Arrays.fill(rowSize, p);

        return TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, 0, 6);
    }

    protected JPanel createSouthPane() {
        return null;
    }

    protected Component[] fieldComponents() {
        return filedComboBoxes();
    }

    protected abstract String[] fieldLabels();

    protected abstract UIComboBox[] filedComboBoxes();

    public void checkBoxUse(boolean hasUse) {
        for (Component component : fieldComponents()) {
            component.setEnabled(hasUse);
        }
    }

    public void clearAllBoxList() {
        for (UIComboBox comboBox : filedComboBoxes()) {
            DataPaneHelper.clearBoxItems(comboBox);
        }
    }

    public void refreshBoxListWithSelectTableData(List columnNameList) {
        for (UIComboBox comboBox : filedComboBoxes()) {
            refreshBoxItems(comboBox, columnNameList);
        }
    }

    @Override
    public abstract T updateBean();

    public static void populateField(UIComboBox comboBox, ColumnField field) {
        populateFunctionField(comboBox, null, field);
    }

    public static void updateField(UIComboBox comboBox, ColumnField field) {
        updateFunctionField(comboBox, null, field);
    }

    protected static void populateFunctionField(UIComboBox comboBox, CalculateComboBox calculateComboBox, ColumnField field) {
        comboBox.setSelectedItem(field.getFieldName());
        if (calculateComboBox != null) {
            calculateComboBox.populateBean((AbstractDataFunction) field.getDataFunction());
        }
    }

    protected static void updateFunctionField(UIComboBox comboBox, CalculateComboBox calculateComboBox, ColumnField field) {
        field.setFieldName(GeneralUtils.objectToString(comboBox.getSelectedItem()));
        if (calculateComboBox != null) {
            field.setDataFunction(calculateComboBox.updateBean());
        } else {
            field.setDataFunction(null);
        }
    }


    @Override
    protected String title4PopupWindow() {
        return StringUtils.EMPTY;
    }

}
