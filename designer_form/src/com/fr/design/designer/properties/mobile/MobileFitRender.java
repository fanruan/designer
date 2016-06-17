package com.fr.design.designer.properties.mobile;

import com.fr.design.mainframe.widget.renderer.EncoderCellRenderer;

public class MobileFitRender extends EncoderCellRenderer {

    public MobileFitRender(){
        super(new MobileFitWrapper());
    }
}
