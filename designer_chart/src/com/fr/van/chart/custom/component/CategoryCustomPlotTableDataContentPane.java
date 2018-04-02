package com.fr.van.chart.custom.component;

import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.table.CategoryPlotTableDataContentPane;

/**
 * Created by Fangjie on 2016/5/18.
 */
public class CategoryCustomPlotTableDataContentPane extends CategoryPlotTableDataContentPane {
    public CategoryCustomPlotTableDataContentPane() {
        super();
    }

    public CategoryCustomPlotTableDataContentPane(ChartDataPane parent) {
        super(parent);
    }

    /**
     * 检查 某些Box是否可用
     * 分类不可用
     * @param hasUse  是否使用
     */
    public void checkBoxUse(boolean hasUse) {
        categoryCombox.setEnabled(false);
        checkSeriseUse(hasUse);
    }
}
