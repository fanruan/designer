package com.fr.design.chartx.fields.diff;

import com.fr.chartx.data.field.ColumnField;
import com.fr.chartx.data.field.diff.MultiCategoryColumnFieldCollection;
import com.fr.design.chartx.component.CategorySeriesFilterPane;
import com.fr.design.formula.TinyFormulaPane;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.i18n.Toolkit;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.utils.gui.GUICoreUtils;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

/**
 * @author Bjorn
 * @version 10.0
 * Created by Bjorn on 2019-10-24
 */
public class SingleCategoryCellDataFieldsPane extends AbstractCellDataFieldsWithSeriesValuePane<MultiCategoryColumnFieldCollection> {

    private TinyFormulaPane categoryPane;

    private CategorySeriesFilterPane filterPane;

    @Override
    protected void initComponents() {
        categoryPane = new TinyFormulaPane();
        filterPane = new CategorySeriesFilterPane();

        UILabel label = new BoldFontTextLabel(Toolkit.i18nText("Fine-Design_Chart_Style_Category"));
        label.setPreferredSize(new Dimension(ChartDataPane.LABEL_WIDTH, ChartDataPane.LABEL_HEIGHT));

        JPanel northPane = new JPanel(new BorderLayout(0, 6));
        northPane.add(GUICoreUtils.createBorderLayoutPane(new Component[]{categoryPane, null, null, label, null}), BorderLayout.NORTH);
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
        return new TinyFormulaPane[]{categoryPane};
    }

    @Override
    public void populateBean(MultiCategoryColumnFieldCollection fieldCollection) {
        if (fieldCollection.getCategoryList().size() > 0) {
            populateField(categoryPane, fieldCollection.getCategoryList().get(0));
        }

        populateSeriesValuePane(fieldCollection);

        filterPane.populateMultiCategoryFieldCollection(fieldCollection);
    }

    @Override
    public MultiCategoryColumnFieldCollection updateBean() {

        MultiCategoryColumnFieldCollection fieldCollection = new MultiCategoryColumnFieldCollection();
        fieldCollection.getCategoryList().add(new ColumnField());

        updateField(categoryPane, fieldCollection.getCategoryList().get(0));

        updateSeriesValuePane(fieldCollection);

        filterPane.updateMultiCategoryFieldCollection(fieldCollection);

        return fieldCollection;
    }

}
