package com.fr.design.widget;

import com.fr.design.mainframe.ElementCasePane;

/**
 * Created by kerry on 2017/8/29.
 */
public class WidgetManageCardPane extends CellWidgetCardPane{

    public WidgetManageCardPane(ElementCasePane pane) {
        super(pane);
    }

    protected WidgetEventPane initWidgetEventPane(ElementCasePane pane){
        return new WidgetEventPaneNoPop(pane);
    }
}
