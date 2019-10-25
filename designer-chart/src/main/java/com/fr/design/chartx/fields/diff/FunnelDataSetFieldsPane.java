package com.fr.design.chartx.fields.diff;

import com.fr.chartx.data.field.diff.MultiCategoryColumnFieldCollection;
import com.fr.design.chartx.component.CategorySeriesFilterPane;
import com.fr.design.gui.icombobox.UIComboBox;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import java.awt.BorderLayout;

/**
 * Created by Wim on 2019/10/23.
 */
public class FunnelDataSetFieldsPane extends AbstractDataSetFieldsWithSeriesValuePane<MultiCategoryColumnFieldCollection> {


    private CategorySeriesFilterPane filterPane;

    @Override
    protected void initComponents() {
        filterPane = new CategorySeriesFilterPane();

        JPanel northPane = new JPanel(new BorderLayout(0, 6));
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
        return new UIComboBox[0];
    }

    @Override
    public MultiCategoryColumnFieldCollection updateBean() {
        MultiCategoryColumnFieldCollection collection = new MultiCategoryColumnFieldCollection();
        updateSeriesValuePane(collection);
        filterPane.updateMultiCategoryFieldCollection(collection);
        return collection;
    }

    @Override
    public void populateBean(MultiCategoryColumnFieldCollection ob) {
        populateSeriesValuePane(ob);
        filterPane.populateMultiCategoryFieldCollection(ob);
    }
}
