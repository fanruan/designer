package com.fr.design.mainframe;

import java.awt.Rectangle;


public class AutoScrollSource {

	private int x = 0;
	private int y = 0;
	private AutoScrollChangeListener l;

	public static int sab(int i, int delay) {
		if (i > delay) {
			return i - delay;
		} else if (i < 0) {
			return i;
		} else {
			return 0;
		}
	}

	public static AutoScrollSource creatAutoScrollSource(int x, int y, AutoScrollChangeListener l, FormDesigner designer) {
		AutoScrollSource as = new AutoScrollSource();
		as.y = sab(y, designer.getHeight());
		as.x = sab(x, designer.getWidth());
		as.l = l;
		return as;
	}

	public static AutoScrollSource creatAutoScrollSource(Rectangle rec, int rx, int ry, AutoScrollChangeListener l,
			FormDesigner designer) {
		AutoScrollSource as = new AutoScrollSource();
		if (rec.x + rx < designer.getArea().getHorizontalValue()) {
			as.x = rec.x + rx - designer.getArea().getHorizontalValue();
		} else if (rec.x + rx + rec.width > designer.getArea().getHorizontalValue() + designer.getWidth()) {
			as.x = rec.x + rx + rec.width - designer.getArea().getHorizontalValue() - designer.getWidth();
		}
		if (rec.y + ry < designer.getArea().getVerticalValue()) {
			as.y = rec.y + ry - designer.getArea().getVerticalValue();
		} else if (rec.y + ry + rec.height > designer.getArea().getVerticalValue() + designer.getHeight()) {
			as.y = rec.y + ry + rec.height - designer.getArea().getVerticalValue() - designer.getHeight();
		}
		as.l = l;
		return as;
	}

	private AutoScrollSource() {

	}

	public int getHorizontalExtent() {
		return x;

	}

	public int getVerticalExtent() {
		return y;
	}

	public void propertyChange() {
		if (l != null) {
			l.propertyChange(x,y);
		}
	}
	
	public interface AutoScrollChangeListener {
		void propertyChange(int dx, int dy);
	}
}