package com.fr.design.mainframe.vcs.ui;


import com.fr.design.gui.ilable.ActionLabel;
import com.fr.design.gui.ilable.UILabel;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Created by XiaXiang on 2019/5/15.
 */
public class VcsLabel extends ActionLabel {


    public VcsLabel(String text, Color color) {
        super(text);
        this.setForeground(color);
    }

    public void paintComponent(Graphics g) {
        if (ui != null && g != null) {
            Graphics scratchGraphics = g.create();
            try {
                ui.update(scratchGraphics, this);
            } finally {
                scratchGraphics.dispose();
            }
        }
    }
}