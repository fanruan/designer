package com.fr.design.chart.series.SeriesCondition.dlp;

import java.awt.Component;

import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.general.Inter;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date   : 11-11-29
 * Time   : 上午10:35
 * 
 * 甘特图的标签界面
 */
public class GanttDataLabelPane extends DataLabelPane {
	private static final long serialVersionUID = 5409845868543668181L;

    protected Component[] createComponents4ShowCategoryName() {
        if (showCategoryNameCB == null) {
            showCategoryNameCB = new UICheckBox(Inter.getLocText("Chart_Step_Name"));
        }
        return new Component[]{null, showCategoryNameCB};
    }

    protected Component[] createComponents4Value() {
        return new Component[0];
    }
}