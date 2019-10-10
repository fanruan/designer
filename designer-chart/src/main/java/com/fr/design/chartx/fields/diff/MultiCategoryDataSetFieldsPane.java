package com.fr.design.chartx.fields.diff;

import com.fr.chartx.data.field.ColumnField;
import com.fr.chartx.data.field.SeriesValueCorrelationDefinition;
import com.fr.chartx.data.field.diff.MultiCategoryColumnFieldCollection;
import com.fr.design.chartx.component.AbstractSingleFilterPane;
import com.fr.design.chartx.component.MultiComboBoxPane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.van.chart.map.designer.VanChartGroupPane;

import javax.swing.JPanel;
import java.util.List;


/**
 * Created by shine on 2019/4/10.
 */
public class MultiCategoryDataSetFieldsPane extends AbstractDataSetFieldsWithSeriesValuePane<MultiCategoryColumnFieldCollection> {

    private MultiComboBoxPane multiCategoryPane;

    private AbstractSingleFilterPane seriesFilterPane;
    private AbstractSingleFilterPane categoryFilterPane;

    @Override
    protected String[] fieldLabels() {
        return new String[0];
    }

    @Override
    protected UIComboBox[] filedComboBoxes() {
        List<UIComboBox> list = initMultiCategoryPane().componentList();
        return list.toArray(new UIComboBox[list.size()]);
    }

    private MultiComboBoxPane initMultiCategoryPane() {
        if (multiCategoryPane == null) {
            multiCategoryPane = new MultiComboBoxPane();
        }
        return multiCategoryPane;
    }

    @Override
    protected JPanel createNorthPane() {
        return initMultiCategoryPane();
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
    public void refreshBoxListWithSelectTableData(List columnNameList) {
        super.refreshBoxListWithSelectTableData(columnNameList);
        multiCategoryPane.setCurrentBoxItems(columnNameList);
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

        MultiCategoryColumnFieldCollection columnFieldCollection = new MultiCategoryColumnFieldCollection();
        List<ColumnField> categoryList = columnFieldCollection.getCategoryList();

        multiCategoryPane.update(categoryList);

        updateSeriesValuePane(columnFieldCollection);

        SeriesValueCorrelationDefinition seriesValueCorrelationDefinition = columnFieldCollection.getSeriesValueCorrelationDefinition();
        if (seriesValueCorrelationDefinition != null) {
            seriesValueCorrelationDefinition.setFilterProperties(seriesFilterPane.updateBean());
        }

        if (categoryList != null && !categoryList.isEmpty()) {
            categoryList.get(0).setFilterProperties(categoryFilterPane.updateBean());
        }

        return columnFieldCollection;
    }
}
