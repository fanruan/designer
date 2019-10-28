package com.fr.design.chartx.fields.diff;

import com.fr.chartx.data.field.ColumnField;
import com.fr.chartx.data.field.diff.MultiCategoryColumnFieldCollection;
import com.fr.design.chartx.component.CategorySeriesFilterPane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.i18n.Toolkit;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.extended.chart.UIComboBoxWithNone;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

/**
 * @author Bjorn
 * @version 10.0
 * Created by Bjorn on 2019-10-24
 */
public class SingleCategoryDataSetFieldsPane extends AbstractDataSetFieldsWithSeriesValuePane<MultiCategoryColumnFieldCollection> {

    private UIComboBox categoryPane;

    private CategorySeriesFilterPane filterPane;

    @Override
    protected void initComponents() {
        categoryPane = new UIComboBoxWithNone();
        filterPane = new CategorySeriesFilterPane();

        UILabel label = new BoldFontTextLabel(Toolkit.i18nText("Fine-Design_Chart_Style_Category"));
        label.setPreferredSize(new Dimension(ChartDataPane.LABEL_WIDTH, ChartDataPane.LABEL_HEIGHT));

        JPanel northPane = new JPanel(new BorderLayout(0, 6));
        northPane.add(GUICoreUtils.createBorderLayoutPane(new Component[]{categoryPane, null, null, label, null}), BorderLayout.NORTH);
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
        return new UIComboBox[]{categoryPane};
    }


    @Override
    public void populateBean(MultiCategoryColumnFieldCollection columnFieldCollection) {
        if (columnFieldCollection.getCategoryList().size() > 0) {
            populateField(categoryPane, columnFieldCollection.getCategoryList().get(0));
        }

        populateSeriesValuePane(columnFieldCollection);

        filterPane.populateMultiCategoryFieldCollection(columnFieldCollection);
    }

    @Override
    public MultiCategoryColumnFieldCollection updateBean() {

        MultiCategoryColumnFieldCollection columnFieldCollection = new MultiCategoryColumnFieldCollection();
        columnFieldCollection.getCategoryList().add(new ColumnField());

        updateField(categoryPane, columnFieldCollection.getCategoryList().get(0));

        updateSeriesValuePane(columnFieldCollection);

        filterPane.updateMultiCategoryFieldCollection(columnFieldCollection);

        return columnFieldCollection;
    }

}
