package com.fr.design.chartx.fields.diff;

import com.fr.chartx.data.field.ColumnField;
import com.fr.chartx.data.field.diff.MultiCategoryColumnFieldCollection;
import com.fr.design.chartx.component.MultiTinyFormulaPane;
import com.fr.design.formula.TinyFormulaPane;

import javax.swing.JPanel;
import java.util.List;

/**
 * Created by shine on 2019/4/12.
 */
public class MultiCategoryCellDataFieldsPane extends AbstractCellDataFieldsWithSeriesValuePane<MultiCategoryColumnFieldCollection> {

    private MultiTinyFormulaPane multiCategoryPane;

    private void createMultiFormulaPane() {
        if (multiCategoryPane == null) {
            multiCategoryPane = new MultiTinyFormulaPane();
        }
    }

    @Override
    protected JPanel createNorthPane() {

        createMultiFormulaPane();

        return multiCategoryPane;
    }

    @Override
    protected String[] fieldLabels() {
        return new String[0];
    }

    @Override
    protected TinyFormulaPane[] formulaPanes() {

        createMultiFormulaPane();

        List<TinyFormulaPane> list = multiCategoryPane.componentList();
        return list.toArray(new TinyFormulaPane[list.size()]);
    }

    @Override
    public void populateBean(MultiCategoryColumnFieldCollection ob) {

        List<ColumnField> categoryList = ob.getCategoryList();

        multiCategoryPane.populate(categoryList);

        populateSeriesValuePane(ob);
    }

    @Override
    public MultiCategoryColumnFieldCollection updateBean() {

        MultiCategoryColumnFieldCollection fieldCollection = new MultiCategoryColumnFieldCollection();
        List<ColumnField> categoryList = fieldCollection.getCategoryList();

        multiCategoryPane.update(categoryList);

        updateSeriesValuePane(fieldCollection);

        return fieldCollection;
    }
}
