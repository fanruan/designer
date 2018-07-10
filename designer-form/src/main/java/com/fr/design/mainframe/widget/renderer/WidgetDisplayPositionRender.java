package com.fr.design.mainframe.widget.renderer;

import com.fr.design.mainframe.widget.wrappers.WidgetDisplayPositionWrapper;

/**
 * Created with IntelliJ IDEA.
 * User: zx
 * Date: 14-8-13
 * Time: 下午2:22
 */
public class WidgetDisplayPositionRender extends EncoderCellRenderer {

    public WidgetDisplayPositionRender() {
        super(new WidgetDisplayPositionWrapper());
    }
}