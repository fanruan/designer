package com.fr.design.designer.beans.location;

import java.awt.Cursor;
import java.awt.Rectangle;

import com.fr.design.mainframe.FormDesigner;
import com.fr.design.utils.gui.LayoutUtils;

/**
 * 这个类用可以用来拖拽表单最底层容器的大小。目前只用于参数界面
 */
public abstract class RootResizeDirection implements Direction {

	public static RootResizeDirection BOTTOM_RESIZE = new RootResizeDirection(Direction.BOTTOM) {

		@Override
		public Cursor getCursor() {
			return Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR);
		}

		@Override
		public void resizeRootBounds(Rectangle rec, int dx, int dy) {
			rec.height += dy;
		}

	};
	public static RootResizeDirection RIGHT_RESIZE = new RootResizeDirection(Direction.RIGHT) {

		@Override
		public Cursor getCursor() {
			return Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR);
		}

		@Override
		public void resizeRootBounds(Rectangle rec, int dx, int dy) {
			rec.width += dx;
		}

	};
	public static RootResizeDirection RIGHT_BOTTOM_RESIZE = new RootResizeDirection(Direction.RIGHT_BOTTOM) {

		@Override
		public Cursor getCursor() {
			return Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR);
		}

		@Override
		public void resizeRootBounds(Rectangle rec, int dx, int dy) {
			rec.height += dy;
			rec.width += dx;
		}

	};

	private int actual;
	private Rectangle oldBounds;

	private RootResizeDirection(int actual) {
		this.actual = actual;
	}

	@Override
	public void drag(int dx, int dy, FormDesigner designer) {
		Rectangle rec = new Rectangle(oldBounds);
		if (actual == Direction.BOTTOM) {
			rec.height += dy;
		} else if (actual == Direction.RIGHT) {
			rec.width += dx;
		} else if (actual == Direction.RIGHT_BOTTOM) {
			rec.height += dy;
			rec.width += dx;
		}
		designer.getRootComponent().setBounds(rec);
		designer.populateRootSize();
		LayoutUtils.layoutRootContainer(designer.getRootComponent());
	}

	protected abstract void resizeRootBounds(Rectangle rec, int dx, int dy);

	protected abstract Cursor getCursor();

	@Override
	public int getActual() {
		return actual;
	}

	@Override
	public void updateCursor(FormDesigner formEditor) {
		formEditor.setCursor(getCursor());
	}

	@Override
	public void backupBounds(FormDesigner designer) {
		oldBounds = designer.getRootComponent().getBounds();
	}
}