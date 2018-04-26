package com.fr.design.designer.beans.location;

import java.awt.Cursor;
import java.awt.Rectangle;

import com.fr.design.mainframe.FormDesigner;

public class Right extends AccessDirection  {

    public Right() {
    }

	@Override
	public Rectangle getDraggedBounds(int dx, int dy, Rectangle current_bounds, FormDesigner designer,
			Rectangle oldbounds) {
		current_bounds.width = sorption(oldbounds.x + dx + oldbounds.width, 0, current_bounds, designer)[0]
				- oldbounds.x;
		return current_bounds;
	}

    @Override
    public int getCursor() {
        return Cursor.E_RESIZE_CURSOR;
    }

     @Override
    public int getActual() {
        return Direction.RIGHT;
    }
}