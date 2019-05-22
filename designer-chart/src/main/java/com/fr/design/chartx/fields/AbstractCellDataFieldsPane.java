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
import java.awt.BorderLayout;
import java.awt.Component;
import java.util.Arrays;

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

        this.add(addNorthPane(), BorderLayout.NORTH);
        this.add(addCenterPane(), BorderLayout.CENTER);
        this.add(addSouthPane(), BorderLayout.SOUTH);

        this.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 8));
    }

    protected JPanel addCenterPane() {
        String[] labels = fieldLabels();
        Component[] formulaPanes = fieldComponents();

        int len = Math.min(labels.length, formulaPanes.length);

        Component[][] components = new Component[len][2];
        for (int i = 0; i < len; i++) {
            components[i] = new Component[]{new UILabel(labels[i], SwingConstants.LEFT), formulaPanes[i]};
        }


        double p = TableLayout.PREFERRED;
        double[] columnSize = {ChartDataPane.LABEL_WIDTH, 124};
        double[] rowSize = new double[len];
        Arrays.fill(rowSize, p);

        return TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, 0, 6);
    }


    protected JPanel addNorthPane() {
        return new JPanel();
    }

    protected JPanel addSouthPane() {
        return new JPanel();
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
