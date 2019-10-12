package com.fr.design.chartx.component;

import com.fr.chartx.data.field.ColumnField;
import com.fr.chartx.data.field.DataFilterProperties;
import com.fr.chartx.data.field.SeriesValueCorrelationDefinition;
import com.fr.chartx.data.field.diff.MultiCategoryColumnFieldCollection;
import com.fr.design.i18n.Toolkit;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;
import com.fr.van.chart.map.designer.VanChartGroupPane;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.util.List;

/**
 * @author shine
 * @version 10.0
 * Created by shine on 2019/9/26
 */
public class CategorySeriesFilterPane extends JPanel {

    private AbstractSingleFilterPane seriesFilterPane;
    private AbstractSingleFilterPane categoryFilterPane;

    public CategorySeriesFilterPane() {
        seriesFilterPane = new AbstractSingleFilterPane() {
            @Override
            public String title4PopupWindow() {
                return Toolkit.i18nText("Fine-Design_Chart_Series");
            }
        };
        categoryFilterPane = new AbstractSingleFilterPane() {
            @Override
            public String title4PopupWindow() {
                return Toolkit.i18nText("Fine-Design_Chart_Style_Category");
            }
        };

        JPanel groupPane = new VanChartGroupPane(new String[]{seriesFilterPane.title4PopupWindow(), categoryFilterPane.title4PopupWindow()}
                , new JPanel[]{seriesFilterPane, categoryFilterPane}) {
        };

        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.add(new JPanel(), BorderLayout.NORTH);
        contentPane.add(groupPane, BorderLayout.CENTER);

        this.setLayout(new BorderLayout());
        this.add(TableLayout4VanChartHelper.createExpandablePaneWithTitle(Toolkit.i18nText("Fine-Design_Chart_Data_Filter"), contentPane), BorderLayout.CENTER);
    }

    public void populateMultiCategoryFieldCollection(MultiCategoryColumnFieldCollection fieldCollection) {

        SeriesValueCorrelationDefinition seriesValueCorrelationDefinition = fieldCollection.getSeriesValueCorrelationDefinition();
        if (seriesValueCorrelationDefinition != null) {
            populateSeries(seriesValueCorrelationDefinition.getFilterProperties());

        }

        List<ColumnField> categoryList = fieldCollection.getCategoryList();
        if (!categoryList.isEmpty()) {
            populateCategory(categoryList.get(0).getFilterProperties());
        }
    }

    public void updateMultiCategoryFieldCollection(MultiCategoryColumnFieldCollection fieldCollection) {

        SeriesValueCorrelationDefinition seriesValueCorrelationDefinition = fieldCollection.getSeriesValueCorrelationDefinition();
        if (seriesValueCorrelationDefinition != null) {
            seriesValueCorrelationDefinition.setFilterProperties(updateSeries());
        }

        List<ColumnField> categoryList = fieldCollection.getCategoryList();
        if (!categoryList.isEmpty()) {
            categoryList.get(0).setFilterProperties(updateCategory());
        }
    }

    private void populateSeries(DataFilterProperties series) {
        seriesFilterPane.populateBean(series);

    }

    private void populateCategory(DataFilterProperties category) {
        categoryFilterPane.populateBean(category);

    }

    private DataFilterProperties updateSeries() {
        return seriesFilterPane.updateBean();

    }

    private DataFilterProperties updateCategory() {
        return categoryFilterPane.updateBean();
    }


}
