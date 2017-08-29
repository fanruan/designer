package com.fr.design.widget;

import com.fr.design.mainframe.ElementCasePane;

import java.awt.*;

/**
 * 没有悬浮弹窗的控件事件编辑面板
 * Created by plough on 2017/8/28.
 */
public class WidgetEventPaneNoPop extends WidgetEventPane {
    public WidgetEventPaneNoPop(ElementCasePane ePane) {
        super(ePane);
    }

    @Override
    protected boolean isNewStyle() {
        return false;
    }

    @Override
    protected void popupEditDialog(Point mousePos) {
        // no pop, do nothing
    }
}
