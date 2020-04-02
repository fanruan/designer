package com.fr.design.chartx.fields.diff;

import com.fr.chartx.data.field.diff.PointMapColumnFieldCollection;
import com.fr.design.chartx.data.map.PointMapAreaLngLatPaneWithTinyFormula;
import com.fr.design.formula.TinyFormulaPane;

import javax.swing.JPanel;

/**
 * @author shine
 * @version 10.0
 * Created by shine on 2019/11/8
 */
public class PointMapCellDataFieldsPane extends AbstractCellDataFieldsWithSeriesValuePane<PointMapColumnFieldCollection> {
    private PointMapAreaLngLatPaneWithTinyFormula areaLngLatPane;

    @Override
    protected JPanel createNorthPane() {
        if (areaLngLatPane == null) {
            areaLngLatPane = new PointMapAreaLngLatPaneWithTinyFormula();
        }
        return areaLngLatPane;
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
    public void populateBean(PointMapColumnFieldCollection ob) {
        areaLngLatPane.populate(ob);
        populateSeriesValuePane(ob);
    }

    @Override
    public PointMapColumnFieldCollection updateBean() {
        PointMapColumnFieldCollection fieldCollection = new PointMapColumnFieldCollection();
        areaLngLatPane.update(fieldCollection);
        updateSeriesValuePane(fieldCollection);
        return fieldCollection;
    }
}
