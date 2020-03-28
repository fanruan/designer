package com.fr.design.chartx.fields.diff;

import com.fr.chartx.data.field.diff.LineMapColumnFieldCollection;
import com.fr.design.chartx.data.map.LineMapAreaLngLatPaneWithTinyFormula;
import com.fr.design.formula.TinyFormulaPane;

import javax.swing.JPanel;

/**
 * @author shine
 * @version 10.0
 * Created by shine on 2019/11/11
 */
public class LineMapCellDataFieldsPane extends AbstractCellDataFieldsWithSeriesValuePane<LineMapColumnFieldCollection> {

    private LineMapAreaLngLatPaneWithTinyFormula areaLngLatPane;

    private TinyFormulaPane lineName;

    @Override
    protected JPanel createNorthPane() {
        if (areaLngLatPane == null) {
            areaLngLatPane = new LineMapAreaLngLatPaneWithTinyFormula();
        }
        return areaLngLatPane;
    }

    @Override
    protected String[] fieldLabels() {
        return new String[]{
                "lineName"
        };
    }

    @Override
    protected TinyFormulaPane[] formulaPanes() {
        if (lineName == null) {
            lineName = new TinyFormulaPane();
        }
        return new TinyFormulaPane[]{
                lineName
        };
    }

    @Override
    public void populateBean(LineMapColumnFieldCollection ob) {
        areaLngLatPane.populate(ob);

        populateField(lineName, ob.getLineName());

        populateSeriesValuePane(ob);
    }

    @Override
    public LineMapColumnFieldCollection updateBean() {
        LineMapColumnFieldCollection fieldCollection = new LineMapColumnFieldCollection();
        areaLngLatPane.update(fieldCollection);

        updateField(lineName, fieldCollection.getLineName());

        updateSeriesValuePane(fieldCollection);
        return fieldCollection;
    }
}
