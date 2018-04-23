package com.fr.design.designer.beans.location;

import java.awt.Cursor;
import java.awt.Rectangle;

import com.fr.design.mainframe.FormDesigner;

public class LeftTop extends AccessDirection {
    public LeftTop() {
    }

    @Override
    public Rectangle getDraggedBounds(int dx, int dy, Rectangle current_bounds, FormDesigner designer,
			Rectangle oldbounds) {
		int[] xy = sorption(oldbounds.x + dx, oldbounds.y + dy, current_bounds, designer);
        current_bounds.x = xy[0];
        current_bounds.y = xy[1];
        current_bounds.width = oldbounds.width - current_bounds.x + oldbounds.x;
        current_bounds.height = oldbounds.height - current_bounds.y + oldbounds.y;
        return current_bounds;
    }

    @Override
    public int getCursor() {
        return Cursor.NW_RESIZE_CURSOR;
    }

     @Override
    public int getActual() {
        return Direction.LEFT_TOP;
    }
}