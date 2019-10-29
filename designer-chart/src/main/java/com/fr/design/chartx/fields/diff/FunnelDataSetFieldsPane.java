package com.fr.design.chartx.fields.diff;

import com.fr.chartx.data.field.SeriesValueCorrelationDefinition;
import com.fr.chartx.data.field.diff.AbstractColumnFieldCollectionWithSeriesValue;
import com.fr.design.chartx.component.AbstractSingleFilterPane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.i18n.Toolkit;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import java.awt.BorderLayout;

/**
 * Created by Wim on 2019/10/23.
 */
public class FunnelDataSetFieldsPane extends AbstractDataSetFieldsWithSeriesValuePane<AbstractColumnFieldCollectionWithSeriesValue> {


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
        northPane.add(new JSeparator(), BorderLayout.CENTER);
        northPane.add(createCenterPane(), BorderLayout.SOUTH);
        northPane.setBorder(BorderFactory.createEmptyBorder(4, 24, 0, 15));

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
    protected UIComboBox[] filedComboBoxes() {
        return new UIComboBox[0];
    }

    @Override
    public AbstractColumnFieldCollectionWithSeriesValue updateBean() {
        AbstractColumnFieldCollectionWithSeriesValue collection = new AbstractColumnFieldCollectionWithSeriesValue();
        updateSeriesValuePane(collection);
        SeriesValueCorrelationDefinition seriesValueCorrelationDefinition = collection.getSeriesValueCorrelationDefinition();
        filterPane.updateBean(seriesValueCorrelationDefinition.getFilterProperties());
        return collection;
    }

    @Override
    public void populateBean(AbstractColumnFieldCollectionWithSeriesValue ob) {
        populateSeriesValuePane(ob);
        SeriesValueCorrelationDefinition seriesValueCorrelationDefinition = ob.getSeriesValueCorrelationDefinition();
        filterPane.populateBean(seriesValueCorrelationDefinition.getFilterProperties());
    }
}
