package com.fr.van.chart.map.designer.data.component;

import com.fr.chart.chartdata.OneValueCDDefinition;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.chart.gui.data.table.SeriesNameUseFieldValuePane;
import com.fr.plugin.chart.map.data.VanMapOneValueCDDefinition;

import java.awt.Component;

/**
 * Created by Mitisky on 16/5/16.
 */
public class SeriesNameUseFieldValuePaneWithOutFilter extends SeriesNameUseFieldValuePane {

    @Override
    protected Component[][] getUseComponent(UILabel Label1, UILabel Label2, UILabel Label3) {
        return getUseComponentWithOutFilter(Label1, Label2, Label3);
    }

    @Override
    protected Component[][] getUseComponentWithOutSummary(UILabel Label1, UILabel Label2, UILabel Label3) {
        return getUseComponentWithOutFilterAndSummary(Label1, Label2, Label3);
    }

    @Override
    protected OneValueCDDefinition createOneValueCDDefinition() {
        return new VanMapOneValueCDDefinition();
    }
}
