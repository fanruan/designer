package com.fr.design.chartx.fields;

import com.fr.chartx.data.field.AbstractColumnFieldCollection;
import com.fr.chartx.data.field.ColumnField;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.formula.TinyFormulaPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.stable.StringUtils;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.util.Arrays;
import java.awt.BorderLayout;
import java.awt.Component;

/**
 * Created by shine on 2019/5/16.
 * 单元格数据源 具体有哪些字段的一个抽象pane
 */
public abstract class AbstractCellDataFieldsPane<T extends AbstractColumnFieldCollection> extends BasicBeanPane<T> {

    public AbstractCellDataFieldsPane() {
        initComponents();
    }

    protected void initComponents() {

        this.setLayout(new BorderLayout(0, 6));

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

        this.setBorder(BorderFactory.createEmptyBorder(0, 24, 0, 15));
    }

    protected JPanel createCenterPane() {
        String[] labels = fieldLabels();
        Component[] formulaPanes = fieldComponents();

        int len = Math.min(labels.length, formulaPanes.length);

        if (len == 0) {
            return null;
        }

        Component[][] components = new Component[len][2];
        for (int i = 0; i < len; i++) {
            components[i] = new Component[]{new UILabel(labels[i], SwingConstants.LEFT), formulaPanes[i]};
        }

        double p = TableLayout.PREFERRED;
        double[] columnSize = {ChartDataPane.LABEL_WIDTH, 122};
        double[] rowSize = new double[len];
        Arrays.fill(rowSize, p);

        return TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, 0, 6);
    }


    protected JPanel createNorthPane() {
        return null;
    }

    protected JPanel createSouthPane() {
        return null;
    }

    protected Component[] fieldComponents() {
        return formulaPanes();
    }

    protected abstract String[] fieldLabels();

    protected abstract TinyFormulaPane[] formulaPanes();

    @Override
    public abstract T updateBean();

    public static void populateField(TinyFormulaPane formulaPane, ColumnField field) {
        formulaPane.populateBean(field.getFieldName());
    }

    public static void updateField(TinyFormulaPane formulaPane, ColumnField field) {
        field.setFieldName(formulaPane.updateBean());
    }

    @Override
    protected String title4PopupWindow() {
        return StringUtils.EMPTY;
    }
}
