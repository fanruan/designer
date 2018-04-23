package com.fr.design.chart.gui;

import java.util.HashMap;
import java.util.Map;

import com.fr.base.chart.Glyph;
import com.fr.chart.chartglyph.CategoryAxisGlyph;
import com.fr.chart.chartglyph.ChartAlertValueGlyph;
import com.fr.chart.chartglyph.ChartGlyph;
import com.fr.chart.chartglyph.DataSeries;
import com.fr.chart.chartglyph.DataSheetGlyph;
import com.fr.chart.chartglyph.DateAxisGlyph;
import com.fr.chart.chartglyph.LegendGlyph;
import com.fr.chart.chartglyph.PlotGlyph;
import com.fr.chart.chartglyph.RadarAxisGlyph;
import com.fr.chart.chartglyph.RangeAxisGlyph;
import com.fr.chart.chartglyph.TextGlyph;
import com.fr.chart.chartglyph.TitleGlyph;
import com.fr.chart.chartglyph.TrendLineGlyph;
import com.fr.chart.chartglyph.ValueAxisGlyph;
import com.fr.design.chart.gui.active.ActiveGlyph;
import com.fr.design.chart.gui.active.AlertValueActiveGlyph;
import com.fr.design.chart.gui.active.CategoryAxisActiveGlyph;
import com.fr.design.chart.gui.active.ChartActiveGlyph;
import com.fr.design.chart.gui.active.DataLabelActiveGlyph;
import com.fr.design.chart.gui.active.DataSeriesActiveGlyph;
import com.fr.design.chart.gui.active.DataSheetActiveGlyph;
import com.fr.design.chart.gui.active.DateAxisActiveGlyph;
import com.fr.design.chart.gui.active.LegendActiveGlyph;
import com.fr.design.chart.gui.active.PlotActiveGlyph;
import com.fr.design.chart.gui.active.RadarAxisActiveGlyph;
import com.fr.design.chart.gui.active.RangeAxisActiveGlyph;
import com.fr.design.chart.gui.active.TextActiveGlyph;
import com.fr.design.chart.gui.active.TrendLineActiveGlyph;
import com.fr.design.chart.gui.active.ValueAxisActiveGlyph;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date   : 11-11-23
 * Time   : 上午9:08
 */
public class ActiveGlyphFactory {
    private static Map<String, Class> glyphMap = new HashMap<String, Class>();

    static {
        glyphMap.put(DataSeries.class.getName(), DataSeriesActiveGlyph.class);
        glyphMap.put(RadarAxisGlyph.class.getName(), RadarAxisActiveGlyph.class);
        glyphMap.put(RangeAxisGlyph.class.getName(), RangeAxisActiveGlyph.class);
        glyphMap.put(TitleGlyph.class.getName(), TextActiveGlyph.class);
        glyphMap.put(DateAxisGlyph.class.getName(), DateAxisActiveGlyph.class);
        glyphMap.put(ValueAxisGlyph.class.getName(), ValueAxisActiveGlyph.class);
        glyphMap.put(CategoryAxisGlyph.class.getName(), CategoryAxisActiveGlyph.class);
        glyphMap.put(ChartGlyph.class.getName(), ChartActiveGlyph.class);
        glyphMap.put(DataSheetGlyph.class.getName(), DataSheetActiveGlyph.class);
        glyphMap.put(LegendGlyph.class.getName(), LegendActiveGlyph.class);
        glyphMap.put(TextGlyph.class.getName(), DataLabelActiveGlyph.class);
        glyphMap.put(TrendLineGlyph.class.getName(), TrendLineActiveGlyph.class);
        glyphMap.put(ChartAlertValueGlyph.class.getName(), AlertValueActiveGlyph.class);
    }

    private ActiveGlyphFactory() {

    }

    public static ActiveGlyph createActiveGlyph(ChartComponent chartComponent, Object glyph) {
         return createActiveGlyph(chartComponent, glyph, null);
    }

    public static ActiveGlyph createActiveGlyph(ChartComponent chartComponent, Object glyph, Glyph parentGlyph) {
        if (glyph == null) {
            return null;
        }
        String clsName = glyph.getClass().getName();
        Class cls = glyphMap.get(clsName);
        Class parameterCls = glyph.getClass();
        if (cls == null) {
            if (clsName.endsWith("PlotGlyph")) {
                cls = PlotActiveGlyph.class;
                parameterCls = PlotGlyph.class;
            } else if (clsName.endsWith("DataSeries4Area")) {
            	cls = DataSeriesActiveGlyph.class;
            	parameterCls = DataSeries.class;
            } else {
                cls = ChartActiveGlyph.class;
                parameterCls = ChartGlyph.class;
            }
        }
        try {
            Class[] argsClass = new Class[]{ChartComponent.class, parameterCls, Glyph.class};
            return (ActiveGlyph) cls.getConstructor(argsClass).newInstance(new Object[]{chartComponent, glyph, parentGlyph});
        } catch (Exception e) {
            return null;
        }
    }
}