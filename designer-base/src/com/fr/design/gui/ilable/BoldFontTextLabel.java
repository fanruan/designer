/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.gui.ilable;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;

import com.fr.design.gui.ilable.UILabel;

public class BoldFontTextLabel extends UILabel {
    public BoldFontTextLabel() {
        this("");
    }

    public BoldFontTextLabel(String text) {
        super(text);
    }

    public BoldFontTextLabel(String text,int horizontalAlignment) {
            super(text,horizontalAlignment);
        }

    public Dimension getPreferredSize() {
        Font font = this.getFont();
        FontMetrics fm = this.getFontMetrics(font);

        if (font.isBold()) {
            return new Dimension(fm.stringWidth(this.getText()),
                    super.getPreferredSize().height);
        } else {
            return super.getPreferredSize();
        }
    }
}