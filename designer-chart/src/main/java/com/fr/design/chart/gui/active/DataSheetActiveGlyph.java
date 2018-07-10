package com.fr.design.chart.gui.active;

import com.fr.base.chart.Glyph;
import com.fr.chart.chartglyph.DataSheetGlyph;
import com.fr.design.chart.gui.ChartComponent;
import com.fr.design.chart.gui.active.action.SetDataSheetAction;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date   : 11-11-22
 * Time   : 下午3:57
 */
public class DataSheetActiveGlyph extends ActiveGlyph {
    private DataSheetGlyph dataSheetGlyph;

    public DataSheetActiveGlyph(ChartComponent chartComponent, DataSheetGlyph dataSheetGlyph, Glyph parentGlyph) {
        super(chartComponent, parentGlyph);
        this.dataSheetGlyph = dataSheetGlyph;
    }

    public Glyph getGlyph() {
        return this.dataSheetGlyph;
    }
    
    public void goRightPane() {
        new SetDataSheetAction(chartComponent).showDataSheetStylePane();
    }
}