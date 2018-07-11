package com.fr.design.mainframe.widget.renderer;

import com.fr.design.mainframe.widget.wrappers.WatermarkWrapper;

/**
 * Created by plough on 2018/5/15.
 */
public class WatermarkRenderer extends EncoderCellRenderer {

    public WatermarkRenderer() {
        super(new WatermarkWrapper());
    }

}