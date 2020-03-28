package com.fr.design.chartx.fields.diff;

import com.fr.chartx.data.field.SeriesValueCorrelationDefinition;
import com.fr.chartx.data.field.diff.ColumnFieldCollectionWithSeriesValue;
import com.fr.design.chartx.component.AbstractSingleFilterPane;
import com.fr.design.formula.TinyFormulaPane;
import com.fr.design.i18n.Toolkit;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;

/**
 * Created by Wim on 2019/10/23.
 * 饼图、多指针仪表盘 在组合图中 没有分类配置项, 也用这个。
 */
public class FunnelCellDataFieldsPane extends AbstractCellDataFieldsWithSeriesValuePane<ColumnFieldCollectionWithSeriesValue> {

    private AbstractSingleFilterPane filterPane;

    @Override
    protected void initComponents() {
        filterPane = new AbstractSingleFilterPane() {
            @Override
            public String title4PopupWindow() {
                return Toolkit.i18nText("Fine-Design_Chart_Series");
            }
        };

        JPanel northPane = new JPanel(new BorderLayout(0, 6));
        northPane.add(createCenterPane(), BorderLayout.CENTER);
        northPane.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 8));

        this.setLayout(new BorderLayout(0, 6));
        this.add(northPane, BorderLayout.NORTH);
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.add(new JPanel(), BorderLayout.NORTH);
        contentPane.add(filterPane, BorderLayout.CENTER);
        this.add(TableLayout4VanChartHelper.createExpandablePaneWithTitle(Toolkit.i18nText("Fine-Design_Chart_Data_Filter"), contentPane), BorderLayout.CENTER);
    }

    @Override
    protected String[] fieldLabels() {
        return new String[0];
    }

    @Override
    protected TinyFormulaPane[] formulaPanes() {
        return new TinyFormulaPane[0];
    }

    @Override
    public ColumnFieldCollectionWithSeriesValue updateBean() {
        ColumnFieldCollectionWithSeriesValue fieldCollection = new ColumnFieldCollectionWithSeriesValue();
        updateSeriesValuePane(fieldCollection);
        SeriesValueCorrelationDefinition seriesValueCorrelationDefinition = fieldCollection.getSeriesValueCorrelationDefinition();
        seriesValueCorrelationDefinition.setFilterProperties(filterPane.updateBean());
        return fieldCollection;
    }

    @Override
    public void populateBean(ColumnFieldCollectionWithSeriesValue ob) {
        populateSeriesValuePane(ob);
        SeriesValueCorrelationDefinition seriesValueCorrelationDefinition = ob.getSeriesValueCorrelationDefinition();
        filterPane.populateBean(seriesValueCorrelationDefinition.getFilterProperties());
    }

}
