package com.fr.design.designer.beans.location;

import java.awt.Cursor;
import java.awt.Rectangle;

import com.fr.design.mainframe.FormDesigner;

public class RightBottom extends AccessDirection {

    public RightBottom() {
    }

    @Override
    public Rectangle getDraggedBounds(int dx, int dy, Rectangle current_bounds, FormDesigner designer,
			Rectangle oldbounds) {
		int[] xy = sorption(oldbounds.x + dx + oldbounds.width, oldbounds.height + dy + oldbounds.y, current_bounds, designer);
        current_bounds.width = xy[0] - oldbounds.x;
        current_bounds.height = xy[1] - oldbounds.y;
        return current_bounds;
    }

    @Override
    public int getCursor() {
        return Cursor.SE_RESIZE_CURSOR;
    }

     @Override
    public int getActual() {
        return Direction.RIGHT_BOTTOM;
    }
}