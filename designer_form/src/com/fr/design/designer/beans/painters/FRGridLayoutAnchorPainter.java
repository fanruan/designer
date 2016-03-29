package com.fr.design.designer.beans.painters;

import java.awt.BasicStroke;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

import com.fr.design.designer.beans.adapters.layout.AbstractAnchorPainter;
import com.fr.design.form.layout.FRGridLayout;
import com.fr.design.form.util.XCreatorConstants;

public class FRGridLayoutAnchorPainter extends AbstractAnchorPainter {

    protected FRGridLayout grid_layout;

    public FRGridLayoutAnchorPainter(Container container) {
        super(container);
        grid_layout = (FRGridLayout) container.getLayout();
    }

    @Override
	public void paint(Graphics g, int startX, int startY) {
        int columns = grid_layout.getColumns();
        int rows = grid_layout.getRows();
        int width = hotspot.width;
        int height = hotspot.height;
        Graphics2D g2d = (Graphics2D) g;
        Stroke back = g2d.getStroke();
        g2d.setStroke(new BasicStroke(1));
        g.setColor(XCreatorConstants.LAYOUT_SEP_COLOR);
        g.drawRect(hotspot.x, hotspot.y, hotspot.width, hotspot.height);

        if (columns != 0) {
            for (int i = 1; i < columns; i++) {
                int x = (i * width) / columns;
                g.drawLine(hotspot.x + x, hotspot.y, hotspot.x + x, hotspot.y + height);
            }
        }
        if (rows != 0) {
            for (int i = 1; i < rows; i++) {
                int y = (i * height) / rows;
                g.drawLine(hotspot.x, hotspot.y + y, hotspot.x + width, hotspot.y + y);
            }
        }
        g2d.setStroke(back);
    }
}