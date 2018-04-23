package com.fr.design.chart.series.PlotSeries;

import java.awt.Point;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.List;

/**
 * 地图选中的shape, 封装选中的点, GeneralPath 做处理
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2012-11-12 下午05:00:34
 */
public class MapSelectShape {
	
	private List<Point> selectPoint = new ArrayList<Point>();
	private List<Shape> selectShape = new ArrayList<Shape>();
	
	private int selectType;
	
	public MapSelectShape(Shape[] shape, Point[] point, int selectType) {
		
		for(int i = 0; i < shape.length; i++) {
			this.selectShape.add(shape[i]);
		}
		for(int i = 0; i < point.length; i++) {
			this.selectPoint.add(point[i]);
		}
		this.selectType = selectType;
	}
	
	/**
	 * 判断多个区域中 是否包含Point
	 */
	public boolean containsPoint(Point point) {
		boolean contains = false;
		for(int i = 0; selectShape != null && i < selectShape.size(); i++) {
			Shape shape = (Shape)selectShape.get(i);
			contains = shape.contains(point);
			if(contains) {
				break;
			}
		}
		
		return contains;
	}
	
	/**
	 * 添加对应的选中点和选中的区域
	 */
	public void addSelectValue(Point point, Shape shape) {
		if(!selectShape.contains(shape)) {
			this.selectShape.add(shape);
		}
		if(!this.selectPoint.contains(point)) {
			this.selectPoint.add(point);
		}
	}
	
	/**
	 * 返回选中的点 数组
	 */
	public Point[] getSelectPoints() {
		return (Point[])this.selectPoint.toArray(new Point[selectPoint.size()]);
	}
	
	/**
	 * 返回选中的区域 数组
	 */
	public Shape[] getSelectShapes() {
		return (Shape[])this.selectShape.toArray(new Shape[selectShape.size()]);
	}
	
	/**
	 * 设置选中的类型: 区域 或者点
	 */
	public void setSelectType(int selectType) {
		this.selectType = selectType;
	}

	/**
	 * 返回选中的类型: 区域 或者点
	 */
	public int getSelectType() {
		return selectType;
	}
}