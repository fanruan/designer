package com.fr.design.designer.beans.location;

import java.awt.Cursor;
import java.awt.Rectangle;

import com.fr.design.mainframe.FormDesigner;

public class Outer extends AccessDirection {

    public Outer() {
    }

    public Rectangle getDraggedBounds(int dx, int dy, Rectangle current_bounds, FormDesigner designer,
			Rectangle oldbounds) {
		return null;
    }

    @Override
    public int getCursor() {
        return Cursor.DEFAULT_CURSOR;
    }

     @Override
    public int getActual() {
        return Direction.OUTER;
    }
}