package com.fr.design.chartx.fields.diff;

import com.fr.chartx.data.field.diff.AbstractColumnFieldCollectionWithSeriesValue;
import com.fr.design.chartx.component.CellDataSeriesValueFieldsPane;
import com.fr.design.chartx.fields.AbstractCellDataFieldsPane;

import javax.swing.JPanel;
import java.awt.BorderLayout;

/**
 * Created by shine on 2019/6/4.
 */
public abstract class AbstractCellDataFieldsWithSeriesValuePane<T extends AbstractColumnFieldCollectionWithSeriesValue>
        extends AbstractCellDataFieldsPane<T> {

    private CellDataSeriesValueFieldsPane seriesValueFieldsPane;

    @Override
    protected JPanel createCenterPane() {
        JPanel normalCenter = super.createCenterPane();
        seriesValueFieldsPane = new CellDataSeriesValueFieldsPane();

        if (normalCenter != null) {
            JPanel panel = new JPanel(new BorderLayout());
            panel.add(normalCenter, BorderLayout.CENTER);
            panel.add(seriesValueFieldsPane, BorderLayout.SOUTH);
            return panel;
        } else {
            return seriesValueFieldsPane;
        }
    }

    protected void populateSeriesValuePane(AbstractColumnFieldCollectionWithSeriesValue t) {
        seriesValueFieldsPane.populateBean(t.getSeriesValueColumnFields());
    }

    protected void updateSeriesValuePane(AbstractColumnFieldCollectionWithSeriesValue t) {
        seriesValueFieldsPane.updateBean(t.getSeriesValueColumnFields());
    }

}
