package com.fr.design.chartx.fields.diff;

import com.fr.chartx.data.field.ColumnField;
import com.fr.chartx.data.field.diff.MultiCategoryColumnFieldCollection;
import com.fr.design.chartx.component.MultiComboBoxPane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.stable.StringUtils;

import javax.swing.JPanel;
import java.util.List;


/**
 * Created by shine on 2019/4/10.
 */
public class MultiCategoryDataSetFieldsPane extends AbstractDataSetFieldsWithSeriesValuePane<MultiCategoryColumnFieldCollection> {

    private MultiComboBoxPane multiCategoryPane;

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
    public void populateBean(MultiCategoryColumnFieldCollection multiCategoryColumnFieldCollection) {
        List<ColumnField> categoryList = multiCategoryColumnFieldCollection.getCategoryList();

        multiCategoryPane.populate(categoryList);

        populateSeriesValuePane(multiCategoryColumnFieldCollection);
    }

    @Override
    public MultiCategoryColumnFieldCollection updateBean() {

        MultiCategoryColumnFieldCollection columnFieldCollection = new MultiCategoryColumnFieldCollection();
        List<ColumnField> categoryList = columnFieldCollection.getCategoryList();

        multiCategoryPane.update(categoryList);

        updateSeriesValuePane(columnFieldCollection);

        return columnFieldCollection;
    }

    @Override
    protected String title4PopupWindow() {
        return StringUtils.EMPTY;
    }
}
