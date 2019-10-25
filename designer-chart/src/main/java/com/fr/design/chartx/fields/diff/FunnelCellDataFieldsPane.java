package com.fr.design.chartx.fields.diff;

import com.fr.chartx.data.field.diff.MultiCategoryColumnFieldCollection;
import com.fr.design.chartx.component.CategorySeriesFilterPane;
import com.fr.design.formula.TinyFormulaPane;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;

/**
 * Created by Wim on 2019/10/23.
 */
public class FunnelCellDataFieldsPane extends AbstractCellDataFieldsWithSeriesValuePane<MultiCategoryColumnFieldCollection> {

    private CategorySeriesFilterPane filterPane;

    @Override
    protected void initComponents() {
        filterPane = new CategorySeriesFilterPane();

        JPanel northPane = new JPanel(new BorderLayout(0, 6));
        northPane.add(createCenterPane(), BorderLayout.CENTER);
        northPane.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 8));

        this.setLayout(new BorderLayout(0, 6));
        this.add(northPane, BorderLayout.NORTH);
        this.add(filterPane, BorderLayout.CENTER);
    }

    @Override
    protected String[] fieldLabels() {
        return new String[0];
    }

    @Override
    protected TinyFormulaPane[] formulaPanes() {
        return new TinyFormulaPane[0];
    }

    @Override
    public MultiCategoryColumnFieldCollection updateBean() {
        MultiCategoryColumnFieldCollection fieldCollection = new MultiCategoryColumnFieldCollection();
        updateSeriesValuePane(fieldCollection);
        filterPane.updateMultiCategoryFieldCollection(fieldCollection);
        return fieldCollection;
    }

    @Override
    public void populateBean(MultiCategoryColumnFieldCollection ob) {
        populateSeriesValuePane(ob);
        filterPane.populateMultiCategoryFieldCollection(ob);
    }

}
