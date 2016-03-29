/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.mainframe.widget.renderer;

import com.fr.design.mainframe.widget.wrappers.BackgroundWrapper;

/**
 * @author richer
 * @since 6.5.3
 */
public class BackgroundRenderer extends EncoderCellRenderer {
    public BackgroundRenderer() {
        super(new BackgroundWrapper());
    }
}