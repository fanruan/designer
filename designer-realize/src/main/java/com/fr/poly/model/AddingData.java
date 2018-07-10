/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.poly.model;

import java.awt.Point;
import java.awt.event.MouseEvent;

import com.fr.poly.creator.BlockCreator;

/**
 * @author richer
 * @since 6.5.4 创建于2011-4-1
 */
public class AddingData {

	private BlockCreator creator;
	// 记录当前鼠标的位置信息
	private int current_x;
	private int current_y;

	public AddingData(BlockCreator creator) {
		this.creator = creator;
		current_x = -this.creator.getWidth();
		current_y = -this.creator.getHeight();
	}
	
	// 隐藏当前组件的图标
	public void reset() {
		current_x = -this.creator.getWidth();
		current_y = -this.creator.getHeight();
	}
	
	public int getCurrentX() {
		return current_x;
	}

	public int getCurrentY() {
		return current_y;
	}

	// 移动组件图标到鼠标事件发生的位置
	public void moveTo(MouseEvent e) {
		current_x = e.getX() - (this.creator.getWidth() / 2);
		current_y = e.getY() - (this.creator.getHeight() / 2);
	}

	public void moveTo(int x, int y) {
		current_x = x - (this.creator.getWidth() / 2);
		current_y = y - (this.creator.getHeight() / 2);
	}
	
	public void moveTo(Point p) {
		current_x = p.x - (this.creator.getWidth() / 2);
		current_y = p.y - (this.creator.getHeight() / 2);
	}

	public BlockCreator getCreator() {
		return creator;
	}

}