package com.fr.design.designer.beans.location;

import java.awt.Cursor;
import java.awt.Rectangle;

import com.fr.design.mainframe.FormDesigner;

public class Left extends AccessDirection  {

    public Left() {
    }

    @Override
	public Rectangle getDraggedBounds(int dx, int dy, Rectangle current_bounds, FormDesigner designer,
			Rectangle oldbounds) {
		current_bounds.x = sorption(oldbounds.x + dx, 0, current_bounds, designer)[0];
		current_bounds.width = oldbounds.width - current_bounds.x + oldbounds.x;
		return current_bounds;
	}

    @Override
    public int getCursor() {
        return Cursor.W_RESIZE_CURSOR;
    }

     @Override
    public int getActual() {
        return Direction.LEFT;
    }
}