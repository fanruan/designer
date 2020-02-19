package com.fr.van.chart.map.designer.data.component;

import com.fr.chart.chartdata.MoreNameCDDefinition;
import com.fr.design.mainframe.chart.gui.data.table.SeriesNameUseFieldNamePane;
import com.fr.plugin.chart.map.data.VanMapMoreNameCDDefinition;

import java.awt.Component;

/**
 * Created by Mitisky on 16/5/17.
 */
public class SeriesNameUseFieldNamePaneWithOutFilter extends SeriesNameUseFieldNamePane {

    @Override
    protected Component[][] getUseComponent() {
        return getUseComponentWithOutFilter();
    }

    @Override
    protected MoreNameCDDefinition createMoreNameCDDefinition() {
        return new VanMapMoreNameCDDefinition();
    }
}
