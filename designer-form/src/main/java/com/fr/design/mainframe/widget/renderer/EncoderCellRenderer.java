package com.fr.design.mainframe.widget.renderer;

import java.awt.FontMetrics;
import java.awt.Graphics;

import com.fr.design.designer.properties.Encoder;

public class EncoderCellRenderer extends GenericCellRenderer {

    private static int LEFT = 1;
    protected Encoder encoder;
    protected Object value;

    public EncoderCellRenderer(Encoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public void paint(Graphics g) {
        int width = getWidth();
        int height = getHeight();
        g.setColor(getBackground());
        g.fillRect(0, 0, width, height);

        int x = LEFT;
        g.setColor(getForeground());

        FontMetrics fm = g.getFontMetrics();
        int y = ((height - fm.getHeight()) / 2) + fm.getAscent();
        String txt = getValueText();
        if (txt != null) {
            g.drawString(txt, x, y);
        }
        if (getBorder() != null) {
            getBorder().paintBorder(this, g, 0, 0, width, height);
        }
    }

    @Override
    public void setValue(Object value) {
        this.value = value;
    }

    private String getValueText() {
        return encoder.encode(value);
    }
}