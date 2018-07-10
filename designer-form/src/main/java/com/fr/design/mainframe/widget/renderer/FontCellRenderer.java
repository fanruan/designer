package com.fr.design.mainframe.widget.renderer;

import java.awt.FontMetrics;
import java.awt.Graphics;

import com.fr.general.FRFont;
import com.fr.stable.StringUtils;
import com.fr.design.mainframe.widget.wrappers.FontWrapper;
import com.fr.design.designer.properties.Encoder;


public class FontCellRenderer extends GenericCellRenderer {

    private static Encoder wrapper = new FontWrapper();
    private FRFont fontValue;

    public FontCellRenderer() {
    }

    @Override
    public void paint(Graphics g) {
        int width = getWidth();
        int height = getHeight();
        g.setColor(getBackground());
        g.fillRect(0, 0, width, height);
        g.setColor(getForeground());

        FontMetrics fm = g.getFontMetrics();
        int y = ((height - fm.getHeight()) / 2) + fm.getAscent();
        g.drawString(getFontText(), 0, y);

        if (getBorder() != null) {
            getBorder().paintBorder(this, g, 0, 0, width, height);
        }
    }
    
    private String getFontText() {
        return (fontValue == null) ? StringUtils.EMPTY : wrapper.encode(fontValue);
    }

    @Override
    public void setValue(Object value) {
        FRFont font = (FRFont) value;

        if (font != null) {
            fontValue = font;
        } else {
            fontValue = FRFont.getInstance();
        }
    }
}