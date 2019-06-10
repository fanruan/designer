package com.fr.design.chartx.fields.diff;

import com.fr.chartx.data.field.diff.AbstractColumnFieldCollectionWithSeriesValue;
import com.fr.design.chartx.component.CellDataSeriesValueCorrelationPane;
import com.fr.design.chartx.fields.AbstractCellDataFieldsPane;

import javax.swing.JPanel;
import java.awt.BorderLayout;

/**
 * Created by shine on 2019/6/4.
 */
public abstract class AbstractCellDataFieldsWithSeriesValuePane<T extends AbstractColumnFieldCollectionWithSeriesValue>
        extends AbstractCellDataFieldsPane<T> {

    private CellDataSeriesValueCorrelationPane seriesValueFieldsPane;

    @Override
    protected JPanel createCenterPane() {
        JPanel normalCenter = super.createCenterPane();
        seriesValueFieldsPane = new CellDataSeriesValueCorrelationPane();

        if (normalCenter != null) {
            JPanel panel = new JPanel(new BorderLayout());
            panel.add(normalCenter, BorderLayout.CENTER);
            panel.add(seriesValueFieldsPane, BorderLayout.SOUTH);
            return panel;
        } else {
            return seriesValueFieldsPane;
        }
    }

    protected void populateSeriesValuePane(AbstractColumnFieldCollectionWithSeriesValue fieldCollectionWithSeriesValue) {
        seriesValueFieldsPane.populateBean(fieldCollectionWithSeriesValue.getSeriesValueCorrelationDefinition());
    }

    protected void updateSeriesValuePane(AbstractColumnFieldCollectionWithSeriesValue fieldCollectionWithSeriesValue) {
        seriesValueFieldsPane.updateBean(fieldCollectionWithSeriesValue.getSeriesValueCorrelationDefinition());
    }

}
