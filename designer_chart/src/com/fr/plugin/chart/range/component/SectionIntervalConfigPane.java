package com.fr.plugin.chart.range.component;

import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.mainframe.chart.gui.style.series.MapColorPickerPaneWithFormula;
import com.fr.general.Inter;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * Author : hu
 * Version: 7.1.1
 */
public class SectionIntervalConfigPane extends MapColorPickerPaneWithFormula {

    @Override
    protected Component[][] createComponents(){
        return new Component[][]{
                new Component[]{new BoldFontTextLabel(""),getDesignTypeButtonGroup()},
                new Component[]{new BoldFontTextLabel(Inter.getLocText(new String[]{"FR-Chart-Color_Subject", "FR-Chart-Color_Color"})), getFillStyleCombox()},
                new Component[]{new BoldFontTextLabel(Inter.getLocText("FR-Chart-Value_Divided_stage")), getRegionNumPane()},
        };
    }
}