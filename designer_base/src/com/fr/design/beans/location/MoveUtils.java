/*
 * Copyright(c) 2001-2011, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.beans.location;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * 
 * @author: Richer
 * @since : 6.5.5 Date: 11-7-1 Time: 下午2:22
 */
public class MoveUtils {

	public static final int SORPTION_UNIT = 5;

	private MoveUtils() {

	}

	public interface RectangleDesigner {

		/**
		 * 获取块边界的迭代器
		 * 
		 * @return 块边界的迭代器
		 * 
		 * @date 2015-2-12-下午2:43:47
		 * 
		 */
		RectangleIterator createRectangleIterator();

		/**
		 * 设置X轴的线
		 * 
		 * @param line 连接线
		 * 
		 * @date 2015-2-12-下午2:44:04
		 * 
		 */
		void setXAbsorptionline(Absorptionline line);

		/**
		 * 设置Y轴的线
		 * 
		 * @param line 连接线
		 * 
		 * @date 2015-2-12-下午2:44:04
		 * 
		 */
		void setYAbsorptionline(Absorptionline line);
		
		/**
		 * 获取当前选中块的垂直线数组
		 * 
		 * @return 块的垂直线数组
		 * 
		 */
		int[] getVerticalLine();
		
		/**
		 * 获取当前选中块的水平线数组
		 * 
		 * @return 块的水平线数组
		 * 
		 */
		int[] getHorizontalLine();
	}

	public interface RectangleIterator {

		/**
		 * 是否存在下一个块
		 * 
		 * @return 是否存在下一个块
		 * 
		 * @date 2015-2-12-下午2:41:32
		 * 
		 */
		boolean hasNext();

		/**
		 * 获取下一个块的bounds
		 * 
		 * @return 下一个块的bounds
		 * 
		 * @date 2015-2-12-下午2:41:55
		 * 
		 */
		Rectangle nextRectangle();
		
		/**
		 * 获取块的垂直线数组
		 * 
		 * @return 块的垂直线数组
		 * 
		 * @date 2015-2-12-下午2:42:27
		 * 
		 */
		int[] getVerticalLine();
		
		/**
		 * 获取块的水平线数组
		 * 
		 * @return 块的水平线数组
		 * 
		 * @date 2015-2-12-下午2:42:27
		 * 
		 */
		int[] getHorizontalLine();
	}

	private static class PlacePointing {
		public PlacePointing(int x) {
			this.palce = x;
		}

		private boolean isFind() {
			return direction != -1;
		}

		private int palce;
		private int direction = -1;
	}

	private static void findX(PlacePointing px, Rectangle bounds, int left, int right, int width) {
		if (px.isFind()) {
			return;
		}
		if (Math.abs(bounds.x + bounds.width / 2 - (left + right) / 2) <= SORPTION_UNIT) {
			px.palce = bounds.x + bounds.width / 2 - width / 2;
			px.direction = SwingConstants.CENTER;
		}
		int x1 = bounds.x;
		if (Math.abs(x1 - left) <= SORPTION_UNIT) {
			px.palce = x1;
			px.direction = SwingConstants.LEFT;
		}
		if (Math.abs(x1 - right) <= SORPTION_UNIT) {
			px.palce = x1 - width;
			px.direction = SwingConstants.RIGHT;
		}
		int x2 = bounds.x + bounds.width;
		if (Math.abs(x2 - left) <= SORPTION_UNIT) {
			px.palce = x2;
			px.direction = SwingConstants.LEFT;
		}
		if (Math.abs(x2 - right) <= SORPTION_UNIT) {
			px.palce = x2 - width;
			px.direction = SwingConstants.RIGHT;
		}
	}

	private static void findY(PlacePointing py, Rectangle bounds, int top, int bottom, int height) {
		if (py.isFind()) {
			return;
		}

		if (Math.abs(bounds.y + bounds.height / 2 - (top + bottom) / 2) <= SORPTION_UNIT) {
			py.palce = bounds.y + bounds.height / 2 - height / 2;
			py.direction = SwingConstants.CENTER;
		}
		int y1 = bounds.y;
		if (Math.abs(y1 - top) <= SORPTION_UNIT) {
			py.palce = y1;
			py.direction = SwingConstants.TOP;
		}
		if (Math.abs(y1 - bottom) <= SORPTION_UNIT) {
			py.palce = y1 - height;
			py.direction = SwingConstants.BOTTOM;
		}
		int y2 = bounds.y + bounds.height;
		if (Math.abs(y2 - top) <= SORPTION_UNIT) {
			py.palce = y2;
			py.direction = SwingConstants.TOP;
		}
		if (Math.abs(y2 - bottom) <= SORPTION_UNIT) {
			py.palce = y2 - height;
			py.direction = SwingConstants.BOTTOM;
		}
	}

	/**
	 * 吸附
	 * 
	 * @param x x坐标
	 * @param y y坐标
	 * @param width 宽度
	 * @param height 高度
	 * @param designer 块设计器
	 * 
	 * @return 吸附后坐标
	 * 
	 * @date 2015-2-12-下午2:39:16
	 * 
	 */
	public static Point sorption(int x, int y, int width, int height, RectangleDesigner designer) {

		int left = x, top = y, bottom = top + height, right = left + width;

		PlacePointing px = new PlacePointing(x);
		PlacePointing py = new PlacePointing(y);
		RectangleIterator iterator = designer.createRectangleIterator();

		java.util.List<Rectangle> cacheRecs = new ArrayList<Rectangle>();
		while (iterator.hasNext()) {
			Rectangle bounds = iterator.nextRectangle();
			cacheRecs.add(bounds);
			findX(px, bounds, left, right, width);
			findY(py, bounds, top, bottom, height);
			if (px.isFind() && py.isFind()) {
				break;
			}
		}

		createXAbsorptionline(px, designer, width, cacheRecs);
		createYAbsorptionline(py, designer, height, cacheRecs);
		return new Point(px.palce, py.palce);
	}

	private static void createXAbsorptionline(PlacePointing px, RectangleDesigner designer, int width, java.util.List<Rectangle> cacheRecs) {
		Absorptionline line = null;
		RectangleIterator iterator = designer.createRectangleIterator();
		int[] selfVertical = designer.getVerticalLine();
		if (px.direction == SwingConstants.CENTER) {
			line = Absorptionline.createXMidAbsorptionline(px.palce + width / 2);
			int left = px.palce;
			int right = px.palce + width;
			for (Rectangle bounds : cacheRecs) {
				if (bounds.x == left || bounds.x + bounds.width == left) {
					line.setFirstLine(left);
				}
				if (bounds.x == right || bounds.x + bounds.width == right) {
					line.setSecondLine(right);
				}
				updateVerticalLine(selfVertical, iterator, line);
				if (line.isFull()) {
					break;
				}
			}
		} else if (px.direction == SwingConstants.LEFT || px.direction == SwingConstants.RIGHT) {
			int left = px.direction == SwingConstants.LEFT ? px.palce + width : px.palce;
			line = Absorptionline.createXAbsorptionline(px.direction == SwingConstants.LEFT ? px.palce : px.palce + width);
			int middle = px.palce + width / 2;
			for (Rectangle bounds : cacheRecs) {
				if (bounds.x == left || bounds.x + bounds.width == left) {
					line.setSecondLine(left);
				}
				if (bounds.x + bounds.width / 2 == middle) {
					line.setMidLine(middle);
				}
				updateVerticalLine(selfVertical, iterator, line);
				if (line.isFull()) {
					break;
				}
			}
		}
		designer.setXAbsorptionline(line);
	}

	private static void createYAbsorptionline(PlacePointing py, RectangleDesigner designer, int height, java.util.List<Rectangle> cacheRecs) {
		Absorptionline line = null;
		RectangleIterator iterator = designer.createRectangleIterator();
		int[] selfHorizontal = designer.getHorizontalLine();
		if (py.direction == SwingConstants.CENTER) {
			line = Absorptionline.createYMidAbsorptionline(py.palce + height / 2);
			int top = py.palce;
			int bottom = py.palce + height;
			for (Rectangle bounds : cacheRecs) {
				if (bounds.y == top || bounds.y + bounds.height == top) {
					line.setFirstLine(top);
				}
				if (bounds.y == bottom || bounds.y + bounds.height == bottom) {
					line.setSecondLine(bottom);
				}
				updateHorizontalLine(selfHorizontal, iterator, line);
				if (line.isFull()) {
					break;
				}
			}
		} else if (py.direction == SwingConstants.TOP || py.direction == SwingConstants.BOTTOM) {
			int top = py.direction == SwingConstants.TOP ? py.palce + height : py.palce;
			line = Absorptionline.createYAbsorptionline(py.direction == SwingConstants.TOP ? py.palce : py.palce + height);
			int middle = py.palce + height / 2;
			for (Rectangle bounds : cacheRecs) {
				if (bounds.y == top || bounds.y + bounds.height == top) {
					line.setSecondLine(top);
				}
				if (bounds.y + bounds.height / 2 == middle) {
					line.setMidLine(middle);
				}
				updateHorizontalLine(selfHorizontal, iterator, line);
				if (line.isFull()) {
					break;
				}
			}
		}
		designer.setYAbsorptionline(line);
	}
	
	//更新纵向行列线
	private static void updateVerticalLine(int[] selfVertical, RectangleIterator iterator, Absorptionline line){
		int[] targetArray = iterator.getVerticalLine();
		if (intersectArrays(targetArray, selfVertical)){
			line.setVerticalLines(targetArray);
		}
	}
	
	//更新横向行列线
	private static void updateHorizontalLine(int[] selfHorizontal, RectangleIterator iterator, Absorptionline line){
		int[] targetArray = iterator.getHorizontalLine();
		if (intersectArrays(targetArray, selfHorizontal)){
			line.setHorizontalLines(targetArray);
		}
	}
	
	//检测两个数组是否有相交的部分
	private static boolean intersectArrays(int[] targetArray, int[] selfArray){
		for (int i : targetArray) {
			for (int j : selfArray) {
				if(i == j){
					return true;
				}
			}
		}
		
		return false;
	}
}