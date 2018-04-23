package com.fr.design.designer.beans.adapters.layout;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;

import com.fr.design.designer.beans.Painter;
import com.fr.design.form.util.XCreatorConstants;

public abstract class AbstractAnchorPainter implements Painter {

    protected Container container;
    protected Rectangle hotspot;

    public AbstractAnchorPainter(Container container) {
        this.container = container;
    }

    @Override
    public void setRenderingBounds(Rectangle rect) {
        this.hotspot = rect;
    }

    protected void drawHotspot(Graphics g, Rectangle box, Color bColor) {
        drawHotspot(g, box.x, box.y, box.width, box.height, bColor);
    }

    protected void drawHotspot(Graphics g, int x, int y, int width, int height, Color bColor) {
        Graphics2D g2d = (Graphics2D) g;
        Stroke backup = g2d.getStroke();
        g2d.setStroke(XCreatorConstants.STROKE);
        Color color = g2d.getColor();
        g2d.setColor(bColor);
        g2d.drawRect(x, y, width, height);
        g2d.setColor(color);
        g2d.setStroke(backup);
    }
}