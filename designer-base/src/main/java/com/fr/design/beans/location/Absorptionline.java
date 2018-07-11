package com.fr.design.beans.location;

import java.awt.*;

import com.fr.base.GraphHelper;
import com.fr.design.scrollruler.ScrollRulerComponent;
import com.fr.stable.ArrayUtils;
import com.fr.third.org.hsqldb.lib.Collection;

public class Absorptionline {
	//箭头线前段为4px的等边三角形，给定了一个点的坐标，计算一下剩下两个点的坐标偏移量
	//而且箭头分为四个朝向，故有四组坐标(2根号3 约为 3)
	// 1.(x,y)(x+2,y±2根号3)(x-2,y±2根号3)
	// 2.(x,y)(x±2根号3,y-2)(x±2根号3,y+2)
	private static int RECTANGLE_OFFSET_X = 2;
	private static int RECTANGLE_OFFSET_Y = 3;
	private static int WIDGET_DISTANCE_MIN = 8;
	//控件周围八个拖拽框的大小
	private static int RESIZE_BOX_SIZE = 5;

	private static Absorptionline lineInX = new Absorptionline(null, null, null, true);
	private static Absorptionline lineInY = new Absorptionline(null, null, null, false);

	private static Absorptionline lineEquidistant = new Absorptionline(null, null, null, true);

	private Color lineColor = new Color(228, 225, 199);
	private Color midLineColor = new Color(196, 227, 237);
	private Color equidistantLineColor = new Color(0xff, 0x0d, 0x7b);

	private Integer x1;
	private Integer x2;
	private Integer middle;
	private int[] verticalLines;
	private int[] horizontalLines;

	private int top;
	private int left;
	private int bottom;
	private int right;
	private Rectangle equidistantStart;
	
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

	/**
	 * 创建等距线
	 * @return 等距线
	 */
	public static Absorptionline createEquidistantAbsorptionline(Rectangle equidistantStart, int top, int left, int bottom, int right) {
		lineEquidistant.equidistantStart = equidistantStart;
		lineEquidistant.top = top;
		lineEquidistant.left = left;
		lineEquidistant.bottom = bottom;
		lineEquidistant.right = right;
		return lineEquidistant;
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
		//画等距线
		if (equidistantStart != null) {
			g.setColor(equidistantLineColor);
			if (top > 0) {
				paintTopEquidistantLine(g);
			}
			if (left > 0) {
				paintLeftEquidistantLine(g);
			}
			if (bottom > 0) {
				paintBottomEquidistantLine(g);
			}
			if (right > 0) {
				paintRightEquidistantLine(g);
			}
		}
	}

	//朝向上方的等距线
	private void paintTopEquidistantLine(Graphics g){
		int x[] = {
				equidistantStart.x + equidistantStart.width / 2,
				equidistantStart.x + equidistantStart.width / 2 - RECTANGLE_OFFSET_X,
				equidistantStart.x + equidistantStart.width / 2 + RECTANGLE_OFFSET_X
		};
		int[] y1, y2;
		if (equidistantStart.y - top > WIDGET_DISTANCE_MIN) {
			y1 = new int[] {
					top,
					top + RECTANGLE_OFFSET_Y,
					top + RECTANGLE_OFFSET_Y
			};
			y2 = new int[]{
					equidistantStart.y - RESIZE_BOX_SIZE,
					equidistantStart.y - RESIZE_BOX_SIZE - RECTANGLE_OFFSET_Y,
					equidistantStart.y - RESIZE_BOX_SIZE - RECTANGLE_OFFSET_Y
			};
		}
		else{
			y1 = new int[] {
					top,
					top - RECTANGLE_OFFSET_Y,
					top - RECTANGLE_OFFSET_Y
			};
			y2 = new int[] {
					equidistantStart.y,
					equidistantStart.y + RECTANGLE_OFFSET_Y,
					equidistantStart.y + RECTANGLE_OFFSET_Y
			};
		}
		g.fillPolygon(x, y1, 3);
		g.fillPolygon(x, y2, 3);
		GraphHelper.drawLine(g,
				equidistantStart.x + equidistantStart.width / 2, top,
				equidistantStart.x + equidistantStart.width / 2, equidistantStart.y - RESIZE_BOX_SIZE);
	}
	//朝向左侧的等距线
	private void paintLeftEquidistantLine(Graphics g){
		int y[] = {
				equidistantStart.y + equidistantStart.height / 2,
				equidistantStart.y + equidistantStart.height / 2 - RECTANGLE_OFFSET_X,
				equidistantStart.y + equidistantStart.height / 2 + RECTANGLE_OFFSET_X,
		};
		int[] x1, x2;
		if (equidistantStart.x - left > WIDGET_DISTANCE_MIN) {
			x1 = new int[] {
					left,
					left + RECTANGLE_OFFSET_Y,
					left + RECTANGLE_OFFSET_Y,
			};

			x2 = new int[] {
					equidistantStart.x - RESIZE_BOX_SIZE,
					equidistantStart.x - RESIZE_BOX_SIZE - RECTANGLE_OFFSET_Y,
					equidistantStart.x - RESIZE_BOX_SIZE - RECTANGLE_OFFSET_Y
			};
		}
		else{
			x1 = new int[] {
					left,
					left - RECTANGLE_OFFSET_Y,
					left - RECTANGLE_OFFSET_Y,
			};
			x2 = new int[] {
					equidistantStart.x,
					equidistantStart.x + RECTANGLE_OFFSET_Y,
					equidistantStart.x + RECTANGLE_OFFSET_Y
			};
		}
		g.fillPolygon(x1, y, 3);
		g.fillPolygon(x2, y, 3);
		GraphHelper.drawLine(g,
				left, equidistantStart.y + equidistantStart.height / 2,
				equidistantStart.x, equidistantStart.y + equidistantStart.height / 2);
	}
	//朝向下方的等距线
	private void paintBottomEquidistantLine(Graphics g) {
		int x[] = {
				equidistantStart.x + equidistantStart.width / 2,
				equidistantStart.x + equidistantStart.width / 2 + RECTANGLE_OFFSET_X,
				equidistantStart.x + equidistantStart.width / 2 - RECTANGLE_OFFSET_X,
		};
		int[] y1, y2;
		if (bottom - equidistantStart.y + equidistantStart.height > WIDGET_DISTANCE_MIN) {
			y1 = new int[] {
					equidistantStart.y + equidistantStart.height + RESIZE_BOX_SIZE,
					equidistantStart.y + equidistantStart.height + RESIZE_BOX_SIZE + RECTANGLE_OFFSET_Y,
					equidistantStart.y + equidistantStart.height + RESIZE_BOX_SIZE + RECTANGLE_OFFSET_Y,
			};
			y2 = new int[] {
					bottom,
					bottom - RECTANGLE_OFFSET_Y,
					bottom - RECTANGLE_OFFSET_Y
			};
		}
		else{
			y1 = new int[] {
					equidistantStart.y + equidistantStart.height,
					equidistantStart.y + equidistantStart.height - RECTANGLE_OFFSET_Y,
					equidistantStart.y + equidistantStart.height - RECTANGLE_OFFSET_Y,
			};
			y2 = new int[] {
					bottom,
					bottom + RECTANGLE_OFFSET_Y,
					bottom + RECTANGLE_OFFSET_Y
			};
		}
		g.fillPolygon(x, y1, 3);
		g.fillPolygon(x, y2, 3);
		GraphHelper.drawLine(g,
				equidistantStart.x + equidistantStart.width / 2, equidistantStart.y + equidistantStart.height,
				equidistantStart.x + equidistantStart.width / 2, bottom);
	}
	//朝向右侧的等距线
	private void paintRightEquidistantLine(Graphics g){
		int y[] = {
				equidistantStart.y + equidistantStart.height / 2,
				equidistantStart.y + equidistantStart.height / 2 - RECTANGLE_OFFSET_X,
				equidistantStart.y + equidistantStart.height / 2 + RECTANGLE_OFFSET_X
		};
		int[] x1, x2;
		if(right - equidistantStart.x > WIDGET_DISTANCE_MIN) {
			x1 = new int[]{
					right,
					right - RECTANGLE_OFFSET_Y,
					right - RECTANGLE_OFFSET_Y
			};
			x2 = new int[]{
					equidistantStart.x + equidistantStart.width + RESIZE_BOX_SIZE,
					equidistantStart.x + equidistantStart.width + RESIZE_BOX_SIZE + RECTANGLE_OFFSET_Y,
					equidistantStart.x + equidistantStart.width + RESIZE_BOX_SIZE + RECTANGLE_OFFSET_Y
			};
		}
		else{
			x1 = new int[]{
					right,
					right + RECTANGLE_OFFSET_Y,
					right + RECTANGLE_OFFSET_Y
			};
			x2 = new int[]{
					equidistantStart.x + equidistantStart.width,
					equidistantStart.x + equidistantStart.width - RECTANGLE_OFFSET_Y,
					equidistantStart.x + equidistantStart.width - RECTANGLE_OFFSET_Y
			};
		}
		g.fillPolygon(x1, y, 3);
		g.fillPolygon(x2, y, 3);
		GraphHelper.drawLine(g,
				equidistantStart.x + equidistantStart.width, equidistantStart.y + equidistantStart.height / 2,
				right, equidistantStart.y + equidistantStart.height / 2);
	}
}