package com.fr.design.chartx;

import com.fr.design.chartx.fields.diff.StructureCellDataFieldsPane;
import com.fr.design.chartx.fields.diff.StructureDataSetFieldsPane;
import com.fr.design.chartx.single.SingleDataPane;
import com.fr.design.gui.frpane.AttributeChangeListener;

/**
 * @author Bjorn
 * @version 10.0
 * Created by Bjorn on 2019-09-02
 */
public class StructureChartDataPane extends MultiCategoryChartDataPane {

    public StructureChartDataPane(AttributeChangeListener listener) {
        super(listener);
    }

    @Override
    protected SingleDataPane createSingleDataPane() {
        return new SingleDataPane(new StructureDataSetFieldsPane(), new StructureCellDataFieldsPane());
    }
}
