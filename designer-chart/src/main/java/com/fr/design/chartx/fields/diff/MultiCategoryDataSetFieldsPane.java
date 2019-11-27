package com.fr.design.chartx.fields.diff;

import com.fr.chartx.data.field.ColumnField;
import com.fr.chartx.data.field.diff.MultiCategoryColumnFieldCollection;
import com.fr.design.chartx.component.CategorySeriesFilterPane;
import com.fr.design.chartx.component.MultiComboBoxPane;
import com.fr.design.gui.icombobox.UIComboBox;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import java.util.List;
import java.awt.BorderLayout;


/**
 * Created by shine on 2019/4/10.
 */
public class MultiCategoryDataSetFieldsPane extends AbstractDataSetFieldsWithSeriesValuePane<MultiCategoryColumnFieldCollection> {

    private MultiComboBoxPane multiCategoryPane;

    private CategorySeriesFilterPane filterPane;

    @Override
    protected void initComponents() {
        multiCategoryPane = new MultiComboBoxPane();
        filterPane = new CategorySeriesFilterPane();

        JPanel northPane = new JPanel(new BorderLayout(0, 6));
        northPane.add(multiCategoryPane, BorderLayout.NORTH);
        northPane.add(new JSeparator(), BorderLayout.CENTER);
        northPane.add(createCenterPane(), BorderLayout.SOUTH);
        northPane.setBorder(BorderFactory.createEmptyBorder(4, 24, 0, 15));

        this.setLayout(new BorderLayout(0, 6));
        this.add(northPane, BorderLayout.NORTH);
        this.add(filterPane, BorderLayout.CENTER);
    }

    @Override
    protected String[] fieldLabels() {
        return new String[0];
    }

    @Override
    protected UIComboBox[] filedComboBoxes() {
        List<UIComboBox> list = multiCategoryPane.componentList();
        return list.toArray(new UIComboBox[list.size()]);
    }

    @Override
    public void checkBoxUse(boolean hasUse) {
        super.checkBoxUse(hasUse);
        multiCategoryPane.checkEnable(hasUse);
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

        filterPane.populateMultiCategoryFieldCollection(multiCategoryColumnFieldCollection);
    }

    @Override
    public MultiCategoryColumnFieldCollection updateBean() {

        MultiCategoryColumnFieldCollection columnFieldCollection = new MultiCategoryColumnFieldCollection();
        List<ColumnField> categoryList = columnFieldCollection.getCategoryList();

        multiCategoryPane.update(categoryList);

        updateSeriesValuePane(columnFieldCollection);

        filterPane.updateMultiCategoryFieldCollection(columnFieldCollection);

        return columnFieldCollection;
    }
}
