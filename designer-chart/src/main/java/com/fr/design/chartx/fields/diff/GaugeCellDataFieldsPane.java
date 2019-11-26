package com.fr.design.chartx.fields.diff;

import com.fr.chartx.data.field.diff.GaugeColumnFieldCollection;
import com.fr.design.chartx.component.AbstractSingleFilterPane;
import com.fr.design.chartx.fields.AbstractCellDataFieldsPane;
import com.fr.design.formula.TinyFormulaPane;
import com.fr.design.i18n.Toolkit;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;

/**
 * Created by Wim on 2019/11/07.
 */
public class GaugeCellDataFieldsPane extends AbstractCellDataFieldsPane<GaugeColumnFieldCollection> {

    private TinyFormulaPane category;
    private TinyFormulaPane value;
    private AbstractSingleFilterPane filterPane;

    @Override
    protected void initComponents() {
        category = new TinyFormulaPane();
        value = new TinyFormulaPane();

        filterPane = new AbstractSingleFilterPane() {
            @Override
            public String title4PopupWindow() {
                return Toolkit.i18nText("Fine-Design_Chart_Category");
            }
        };

        JPanel northPane = new JPanel(new BorderLayout(0, 6));
        northPane.add(createCenterPane(), BorderLayout.CENTER);
        northPane.setBorder(BorderFactory.createEmptyBorder(0, 24, 0, 15));

        this.setLayout(new BorderLayout(0, 6));
        this.add(northPane, BorderLayout.NORTH);
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.add(new JPanel(), BorderLayout.NORTH);
        contentPane.add(filterPane, BorderLayout.CENTER);
        this.add(TableLayout4VanChartHelper.createExpandablePaneWithTitle(Toolkit.i18nText("Fine-Design_Chart_Data_Filter"), contentPane), BorderLayout.CENTER);
    }

    @Override
    protected JPanel createSouthPane() {
        return filterPane;
    }

    @Override
    protected String[] fieldLabels() {
        return new String[]{
                Toolkit.i18nText("Fine-Design_Chart_Style_Format_Category_Name"),
                Toolkit.i18nText("Fine-Design_Chart_Pointer_Value")
        };
    }

    @Override
    protected TinyFormulaPane[] formulaPanes() {
        return new TinyFormulaPane[]{
                category, value
        };
    }

    @Override
    public GaugeColumnFieldCollection updateBean() {
        GaugeColumnFieldCollection gauge = new GaugeColumnFieldCollection();
        updateField(category, gauge.getCategory());
        updateField(value, gauge.getValue());
        filterPane.updateBean(gauge.getCategory().getFilterProperties());
        return gauge;
    }

    @Override
    public void populateBean(GaugeColumnFieldCollection ob) {
        populateField(category, ob.getCategory());
        populateField(value, ob.getValue());
        filterPane.populateBean(ob.getCategory().getFilterProperties());
    }
}
