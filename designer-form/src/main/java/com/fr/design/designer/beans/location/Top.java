package com.fr.design.designer.beans.location;

import java.awt.Cursor;
import java.awt.Rectangle;

import com.fr.design.mainframe.FormDesigner;

public class Top extends AccessDirection {

    public Top() {
    }

	@Override
	public Rectangle getDraggedBounds(int dx, int dy, Rectangle current_bounds, FormDesigner designer,
			Rectangle oldbounds) {
		current_bounds.y = sorption(0, dy + oldbounds.y, current_bounds, designer)[1];
		current_bounds.height = oldbounds.height - current_bounds.y + oldbounds.y;
		return current_bounds;
	}

    @Override
    public int getCursor() {
        return Cursor.N_RESIZE_CURSOR;
    }

     @Override
    public int getActual() {
        return Direction.TOP;
    }
}