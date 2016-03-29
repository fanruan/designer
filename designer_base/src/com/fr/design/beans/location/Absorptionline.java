package com.fr.design.beans.location;

import java.awt.Color;
import java.awt.Graphics;

import com.fr.base.GraphHelper;
import com.fr.design.scrollruler.ScrollRulerComponent;
import com.fr.stable.ArrayUtils;

public class Absorptionline {
	private static Absorptionline lineInX = new Absorptionline(null, null, null, true);
	private static Absorptionline lineInY = new Absorptionline(null, null, null, false);

	private Color lineColor = new Color(228, 225, 199);
	private Color midLineColor = new Color(196, 227, 237);
	private Integer x1;
	private Integer x2;
	private Integer middle;
	private int[] verticalLines;
	private int[] horizontalLines;
	
	private boolean trans;

	/**
	 * 创建X轴连接线
	 * 
	 * @param x x坐标
	 * 
	 * @return X轴的连接线
	 * 
	 * @date 2015-2-12-下午2:45:49
	 * 
	 */
	public static Absorptionline createXAbsorptionline(int x) {
		lineInX.x1 = x;
		lineInX.x2 = lineInX.middle = null;
		lineInX.horizontalLines = lineInX.verticalLines = null;
		return lineInX;
	}

	/**
	 * 创建X中轴连接线
	 * 
	 * @param x x坐标
	 * 
	 * @return X中轴的连接线
	 * 
	 * @date 2015-2-12-下午2:45:49
	 * 
	 */
	public static Absorptionline createXMidAbsorptionline(int x) {
		lineInX.middle = x;
		lineInX.x1 = lineInX.x2 = null;
		lineInX.horizontalLines = lineInX.verticalLines = null;
		return lineInX;
	}

	/**
	 * 创建Y轴连接线
	 * 
	 * @param y y坐标
	 * 
	 * @return Y轴的连接线
	 * 
	 * @date 2015-2-12-下午2:45:49
	 * 
	 */
	public static Absorptionline createYAbsorptionline(int y) {
		lineInY.x1 = y;
		lineInY.x2 = lineInY.middle = null;
		lineInY.horizontalLines = lineInY.verticalLines = null;
		return lineInY;
	}

	/**
	 * 创建Y中轴连接线
	 * 
	 * @param y y坐标
	 * 
	 * @return Y中轴的连接线
	 * 
	 * @date 2015-2-12-下午2:45:49
	 * 
	 */
	public static Absorptionline createYMidAbsorptionline(int y) {
		lineInY.middle = y;
		lineInY.x1 = lineInY.x2 = null;
		lineInY.horizontalLines = lineInY.verticalLines = null;
		return lineInY;
	}

	private Absorptionline(Integer x1, Integer x2, Integer middleInX, boolean trans) {
		this.x1 = x1;
		this.x2 = x2;
		this.middle = middleInX;
		this.trans = trans;
	}

	public void setFirstLine(Integer x) {
		this.x1 = x;
	}

	public void setMidLine(Integer x) {
		this.middle = x;
	}

	public void setSecondLine(Integer x) {
		this.x2 = x;
	}

	/**
	 * 是否相关参数都已设置完毕
	 * 
	 * @return 相关参数都已设置完毕
	 * 
	 * @date 2015-2-12-下午2:44:46
	 * 
	 */
	public boolean isFull() {
		return x1 != null && x2 != null && middle != null;
	}
	
	public int[] getVerticalLines() {
		return verticalLines;
	}

	public void setVerticalLines(int[] verticalLines) {
		this.verticalLines = verticalLines;
	}

	public int[] getHorizontalLines() {
		return horizontalLines;
	}

	public void setHorizontalLines(int[] horizontalLines) {
		this.horizontalLines = horizontalLines;
	}

	public void paint(Graphics g, ScrollRulerComponent designer) {
		g = g.create();
		if (trans) {

			for (int i = 0; i < ArrayUtils.getLength(verticalLines); i++) {
				g.setColor(lineColor);
				GraphHelper.drawLine(g, verticalLines[i] - designer.getHorizontalValue(), 0, verticalLines[i] - designer.getHorizontalValue(),
						designer.getDesignerHeight());
			}
			
			if (x1 != null) {
				g.setColor(lineColor);
				GraphHelper.drawLine(g, x1 - designer.getHorizontalValue(), 0, x1 - designer.getHorizontalValue(),
						designer.getDesignerHeight());
			}
			if (x2 != null) {
				g.setColor(lineColor);
				GraphHelper.drawLine(g, x2 - designer.getHorizontalValue(), 0, x2 - designer.getHorizontalValue(),
						designer.getDesignerWidth());
			}
			if (middle != null) {
				g.setColor(midLineColor);
				GraphHelper.drawLine(g, middle - designer.getHorizontalValue(), 0, middle
						- designer.getHorizontalValue(), designer.getDesignerHeight());
			}
		} else {
			for (int i = 0; i < ArrayUtils.getLength(horizontalLines); i++) {
				g.setColor(lineColor);
				GraphHelper.drawLine(g, 0, horizontalLines[i] - designer.getVerticalValue(), 
						designer.getDesignerWidth(), horizontalLines[i]	- designer.getVerticalValue());
			}
			
			if (x1 != null) {

				g.setColor(lineColor);
				GraphHelper.drawLine(g, 0, x1 - designer.getVerticalValue(), designer.getDesignerWidth(), x1
						- designer.getVerticalValue());
			}
			if (x2 != null) {
				g.setColor(lineColor);
				GraphHelper.drawLine(g, 0, x2 - designer.getVerticalValue(), designer.getDesignerWidth(), x2
						- designer.getVerticalValue());
			}
			if (middle != null) {
				g.setColor(midLineColor);
				GraphHelper.drawLine(g, 0, middle - designer.getVerticalValue(), designer.getDesignerWidth(), middle
						- designer.getVerticalValue());
			}

		}
	}
}