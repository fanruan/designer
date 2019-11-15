package com.fr.design.chartx.fields.diff;

import com.fr.chartx.data.field.diff.AreaMapColumnFieldCollection;
import com.fr.design.formula.TinyFormulaPane;
import com.fr.design.i18n.Toolkit;

/**
 * @author shine
 * @version 10.0
 * Created by shine on 2019/11/7
 */
public class AreaMapCellDataFieldsPane extends AbstractCellDataFieldsWithSeriesValuePane<AreaMapColumnFieldCollection> {
    private TinyFormulaPane areaName;

    @Override
    protected String[] fieldLabels() {
        return new String[]{
                Toolkit.i18nText("Fine-Design_Chart_Area_Name")
        };
    }

    @Override
    protected TinyFormulaPane[] formulaPanes() {
        return new TinyFormulaPane[]{
                createAreaName()
        };
    }

    private TinyFormulaPane createAreaName() {
        if (areaName == null) {
            areaName = new TinyFormulaPane();
        }
        return areaName;
    }

    @Override
    public void populateBean(AreaMapColumnFieldCollection ob) {
        populateField(areaName, ob.getAreaName());
        populateSeriesValuePane(ob);
    }

    @Override
    public AreaMapColumnFieldCollection updateBean() {
        AreaMapColumnFieldCollection fieldCollection = new AreaMapColumnFieldCollection();
        updateField(areaName, fieldCollection.getAreaName());
        updateSeriesValuePane(fieldCollection);
        return fieldCollection;
    }
}
