package com.fr.design.chartx.fields.diff;

import com.fr.chartx.data.field.ColumnField;
import com.fr.chartx.data.field.SeriesValueCorrelationDefinition;
import com.fr.chartx.data.field.diff.MultiCategoryColumnFieldCollection;
import com.fr.design.chartx.component.AbstractSingleFilterPane;
import com.fr.design.chartx.component.MultiTinyFormulaPane;
import com.fr.design.formula.TinyFormulaPane;
import com.fr.van.chart.map.designer.VanChartGroupPane;

import javax.swing.JPanel;
import java.util.List;

/**
 * Created by shine on 2019/4/12.
 */
public class MultiCategoryCellDataFieldsPane extends AbstractCellDataFieldsWithSeriesValuePane<MultiCategoryColumnFieldCollection> {

    private MultiTinyFormulaPane multiCategoryPane;

    private AbstractSingleFilterPane seriesFilterPane;
    private AbstractSingleFilterPane categoryFilterPane;

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
    protected JPanel createSouthPane() {
        if (seriesFilterPane == null) {
            seriesFilterPane = new AbstractSingleFilterPane() {
                @Override
                public String title4PopupWindow() {
                    //todo@shinerefactor
                    return "series";
                }
            };
            categoryFilterPane = new AbstractSingleFilterPane() {
                @Override
                public String title4PopupWindow() {
                    return "category";
                }
            };
        }
        return new VanChartGroupPane(new String[]{seriesFilterPane.title4PopupWindow(), categoryFilterPane.title4PopupWindow()}
                , new JPanel[]{seriesFilterPane, categoryFilterPane}) {
        };
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
    public void populateBean(MultiCategoryColumnFieldCollection multiCategoryColumnFieldCollection) {

        List<ColumnField> categoryList = multiCategoryColumnFieldCollection.getCategoryList();

        multiCategoryPane.populate(categoryList);

        populateSeriesValuePane(multiCategoryColumnFieldCollection);

        SeriesValueCorrelationDefinition seriesValueCorrelationDefinition = multiCategoryColumnFieldCollection.getSeriesValueCorrelationDefinition();
        if (seriesValueCorrelationDefinition != null) {
            seriesFilterPane.populateBean(seriesValueCorrelationDefinition.getFilterProperties());

        }

        if (categoryList != null && !categoryList.isEmpty()) {
            categoryFilterPane.populateBean(categoryList.get(0).getFilterProperties());
        }
    }

    @Override
    public MultiCategoryColumnFieldCollection updateBean() {

        MultiCategoryColumnFieldCollection fieldCollection = new MultiCategoryColumnFieldCollection();
        List<ColumnField> categoryList = fieldCollection.getCategoryList();

        multiCategoryPane.update(categoryList);

        updateSeriesValuePane(fieldCollection);

        SeriesValueCorrelationDefinition seriesValueCorrelationDefinition = fieldCollection.getSeriesValueCorrelationDefinition();
        if (seriesValueCorrelationDefinition != null) {
            seriesValueCorrelationDefinition.setFilterProperties(seriesFilterPane.updateBean());
        }

        if (categoryList != null && !categoryList.isEmpty()) {
            categoryList.get(0).setFilterProperties(categoryFilterPane.updateBean());
        }

        return fieldCollection;
    }
}
