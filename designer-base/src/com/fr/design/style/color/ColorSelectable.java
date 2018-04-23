package com.fr.design.style.color;

import java.awt.Color;

public interface ColorSelectable {
	public void setColor(Color color);
	
	public Color getColor();
	
	/**
	 * 选中颜色
	 * @param colorCell 颜色单元格
	 */
	public void colorSetted(ColorCell colorCell);
}