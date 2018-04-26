package com.fr.design.designer.beans.location;

import java.awt.Cursor;
import java.awt.Rectangle;

import com.fr.design.mainframe.FormDesigner;

public class Bottom extends AccessDirection {

    public Bottom() {
    }

    @Override
	public Rectangle getDraggedBounds(int dx, int dy, Rectangle current_bounds, FormDesigner designer,
			Rectangle oldbounds) {
		current_bounds.height = sorption(0, oldbounds.height + dy + oldbounds.y, current_bounds, designer)[1]
				- oldbounds.y;
		return current_bounds;
	}

    @Override
    public int getCursor() {
        return Cursor.S_RESIZE_CURSOR;
    }

    @Override
    public int getActual() {
        return Direction.BOTTOM;
    }
}