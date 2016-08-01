package com.fr.design.designer.properties;

import com.fr.design.mainframe.widget.renderer.EncoderCellRenderer;

/**
 * Created by zhouping on 2016/8/1.
 */
public class AbsoluteStateRenderer extends EncoderCellRenderer {
    public AbsoluteStateRenderer() {
        super(new AbsoluteStateWrapper());
    }
}