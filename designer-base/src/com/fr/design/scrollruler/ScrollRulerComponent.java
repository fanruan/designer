package com.fr.design.scrollruler;

import java.awt.Point;

public interface ScrollRulerComponent {

	public int getVerticalValue();

	public int getHorizontalValue();
	
	public void setHorizontalValue(int value);

	public void setVerticalValue(int value);

	public short getRulerLengthUnit();

	public int getDesignerHeight();

	public int getDesignerWidth();

	public int getMinWidth();

	public int getMinHeight();

	public void repaint();
	
	Point calculateScroll(int oldmax, int max, int newValue, int oldValue, int visi, int orientation);
}