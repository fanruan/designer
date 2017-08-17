package com.fr.plugin.chart.designer.style.datasheet;

import com.fr.chart.chartglyph.DataSheet;
import com.fr.plugin.chart.base.AttrDataSheet;

import javax.swing.*;
import java.awt.*;

/**
 * Created by mengao on 2017/5/24.
 */
public class VanchartDataSheetNoCheckPane extends VanChartDataSheetPane {

    protected Component[][] creatComponent(JPanel dataSheetPane) {

        Component[][] components = new Component[][]{
                new Component[]{dataSheetPane}
        };
        return components;
    }

    public void populate(AttrDataSheet attrDataSheet) {
        populate(attrDataSheet.getDataSheet());
    }

    public AttrDataSheet update() {
        AttrDataSheet attrDataSheet = new AttrDataSheet();
        DataSheet dataSheet = attrDataSheet.getDataSheet();
        dataSheet.setVisible(true);
        update(dataSheet);
        return attrDataSheet;
    }
}
