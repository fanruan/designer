package com.fr.design.chartx.fields.diff;

import com.fr.chartx.data.field.ColumnField;
import com.fr.chartx.data.field.diff.MultiCategoryColumnFieldCollection;
import com.fr.design.chartx.component.CategorySeriesFilterPane;
import com.fr.design.chartx.component.MultiTinyFormulaPane;
import com.fr.design.formula.TinyFormulaPane;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.util.List;
import java.awt.BorderLayout;

/**
 * Created by shine on 2019/4/12.
 */
public class MultiCategoryCellDataFieldsPane extends AbstractCellDataFieldsWithSeriesValuePane<MultiCategoryColumnFieldCollection> {

    private MultiTinyFormulaPane multiCategoryPane;

    private CategorySeriesFilterPane filterPane;

    @Override
    protected void initComponents() {
        multiCategoryPane = new MultiTinyFormulaPane();
        filterPane = new CategorySeriesFilterPane();

        JPanel northPane = new JPanel(new BorderLayout(0, 6));
        northPane.add(multiCategoryPane, BorderLayout.NORTH);
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
        List<TinyFormulaPane> list = multiCategoryPane.componentList();
        return list.toArray(new TinyFormulaPane[list.size()]);
    }

    @Override
    public void populateBean(MultiCategoryColumnFieldCollection multiCategoryColumnFieldCollection) {

        List<ColumnField> categoryList = multiCategoryColumnFieldCollection.getCategoryList();

        multiCategoryPane.populate(categoryList);

        populateSeriesValuePane(multiCategoryColumnFieldCollection);

        filterPane.populateMultiCategoryFieldCollection(multiCategoryColumnFieldCollection);
    }

    @Override
    public MultiCategoryColumnFieldCollection updateBean() {

        MultiCategoryColumnFieldCollection fieldCollection = new MultiCategoryColumnFieldCollection();
        List<ColumnField> categoryList = fieldCollection.getCategoryList();

        multiCategoryPane.update(categoryList);

        updateSeriesValuePane(fieldCollection);

        filterPane.updateMultiCategoryFieldCollection(fieldCollection);

        return fieldCollection;
    }

    public void setCategoryAxis(boolean categoryAxis){
        multiCategoryPane.setCategoryAxis(categoryAxis);
    }
}
