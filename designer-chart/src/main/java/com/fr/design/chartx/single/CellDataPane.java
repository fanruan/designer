package com.fr.design.chartx.single;

import com.fr.chartx.data.CellDataDefinition;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.chartx.fields.AbstractCellDataFieldsPane;
import com.fr.design.i18n.Toolkit;
import com.fr.stable.AssistUtils;

import java.awt.BorderLayout;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by shine on 2019/5/21.
 */
public class CellDataPane extends FurtherBasicBeanPane<CellDataDefinition> {

    private AbstractCellDataFieldsPane cellDataFieldsPane;

    public CellDataPane(AbstractCellDataFieldsPane cellDataFieldsPane) {
        initComps(cellDataFieldsPane);
    }

    private void initComps(AbstractCellDataFieldsPane cellDataFieldsPane) {
        this.cellDataFieldsPane = cellDataFieldsPane;

        this.setLayout(new BorderLayout());
        this.add(cellDataFieldsPane, BorderLayout.CENTER);
    }

    @Override
    public String title4PopupWindow() {
        return Toolkit.i18nText("Fine-Design_Chart_Cell_Data");
    }


    @Override
    public boolean accept(Object ob) {
        return ob instanceof CellDataDefinition;
    }

    @Override
    public void reset() {
        this.removeAll();
    }

    @Override
    public void populateBean(CellDataDefinition ob) {

        if (ob == null || ob.getColumnFieldCollection() == null) {
            return;
        }

        Type dataType = ((ParameterizedType) cellDataFieldsPane.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        if (AssistUtils.equals(ob.getColumnFieldCollection().getClass(), dataType)) {
            cellDataFieldsPane.populateBean(ob.getColumnFieldCollection());
        }
    }

    @Override
    public CellDataDefinition updateBean() {
        CellDataDefinition cellDataDefinition = new CellDataDefinition();
        cellDataDefinition.setColumnFieldCollection(cellDataFieldsPane.updateBean());

        return cellDataDefinition;
    }
}
