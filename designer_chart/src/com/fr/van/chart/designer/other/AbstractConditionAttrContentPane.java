package com.fr.van.chart.designer.other;

import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.condition.ConditionAttributesPane;

/**
 * Created by Mitisky on 16/5/23.
 */
public abstract class AbstractConditionAttrContentPane extends BasicBeanPane<Plot> {

    public abstract void populateBean(Plot plot, Class<? extends ConditionAttributesPane> showPane);

    @Override
    public void populateBean(Plot ob) {

    }

    /**
     * Update.
     */
    @Override
    public Plot updateBean() {
        return null;
    }

    @Override
    protected String title4PopupWindow() {
        return "conditionContent";
    }
}
