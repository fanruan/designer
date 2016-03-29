package com.fr.design.extra;

import com.fr.design.gui.ilable.UILabel;

import java.awt.*;

/**
 * @author richie
 * @date 2015-03-10
 * @since 8.0
 */
public class PluginDescriptionLabel extends UILabel {
    private static final Dimension S = new Dimension(120, 24);

    public PluginDescriptionLabel() {
        super();
    }

    public Dimension getPreferredSize() {
        return S;
    }

}