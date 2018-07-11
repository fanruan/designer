package com.fr.design.designer.beans.painters;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.Point;

import com.fr.design.designer.beans.HoverPainter;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.form.util.XCreatorConstants;

public class FRGridLayoutPainter extends FRGridLayoutAnchorPainter implements HoverPainter {

    private Point point;
    private XCreator current;

    public FRGridLayoutPainter(Container container) {
        super(container);
    }

    @Override
    public void setHotspot(Point p) {
        point = p;
    }

    @Override
    public void setCreator(XCreator creator) {
        this.current = creator;
    }

    @Override
	public void paint(Graphics g, int startX, int startY) {
		super.paint(g, startX, startY);
        int x = point.x;
        int y = point.y;
        int column = grid_layout.getColumns();
        int row = grid_layout.getRows();
        if (column == 0) {
            column = 1;
        }
        if (row == 0) {
            row = 1;
        }
        double w = (double) hotspot.width / column;
        double h = (double) hotspot.height / row;
        int ix = (int) (x / w);
        int iy = (int) (y / h);
        x = (int) (ix * w + hotspot.x);
        y = (int) (iy * h + hotspot.y);
        drawHotspot(g, x, y, (int) w, (int) h, XCreatorConstants.SELECTION_COLOR);
    }
}