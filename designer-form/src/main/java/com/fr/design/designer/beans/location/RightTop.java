package com.fr.design.designer.beans.location;

import java.awt.Cursor;
import java.awt.Rectangle;

import com.fr.design.mainframe.FormDesigner;

public class RightTop extends AccessDirection {

    public RightTop() {
    }

	public Rectangle getDraggedBounds(int dx, int dy, Rectangle current_bounds, FormDesigner designer,
			Rectangle oldbounds) {
		int[] xy = sorption(oldbounds.x + dx + oldbounds.width, dy + oldbounds.y, current_bounds, designer);
		current_bounds.y = xy[1];
		current_bounds.height = oldbounds.height - current_bounds.y + oldbounds.y;
		current_bounds.width = xy[0] - oldbounds.x;
		return current_bounds;
	}

    @Override
    public int getCursor() {
        return Cursor.NE_RESIZE_CURSOR;
    }

     @Override
    public int getActual() {
        return Direction.RIGHT_TOP;
    }
}