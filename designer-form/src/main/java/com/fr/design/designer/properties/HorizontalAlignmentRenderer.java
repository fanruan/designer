package com.fr.design.designer.properties;

import com.fr.design.mainframe.widget.renderer.EncoderCellRenderer;

public class HorizontalAlignmentRenderer extends EncoderCellRenderer {

    public HorizontalAlignmentRenderer() {
        super(new HorizontalAlignmentWrapper());
    }
}