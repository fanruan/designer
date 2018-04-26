/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.mainframe;

import java.awt.Adjustable;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JScrollBar;

import com.fr.design.scrollruler.ScrollRulerComponent;

/**
 * ScrollBar change its max value dynamically.
 */
public class FormScrollBar extends JScrollBar {

	private ScrollRulerComponent designer;

	public FormScrollBar(int orientation, ScrollRulerComponent designer) {
		super(orientation);

		this.designer = designer;

		this.setMinimum(0);
		this.setUnitIncrement(50);
		this.setBlockIncrement(50);

		this.addComponentListener(new ComponentListener() {
			
			public void componentResized(ComponentEvent e) {
				ajustValues();
			}

			public void componentMoved(ComponentEvent e) {
				ajustValues();
			}

			public void componentShown(ComponentEvent e) {
				ajustValues();
			}

			public void componentHidden(ComponentEvent e) {
				ajustValues();
			}

			private void ajustValues() {
				FormScrollBar.this.setValue(FormScrollBar.this.getValue());
			}
		});
	}

	/**
	 * 滚动条赋值
	 * @param value 值
	 */
	@Override
	public void setValue(int value) {
		if (designer == null) {
			return;
		}
		if (orientation == Adjustable.VERTICAL) {
			verticalBarHelper.setValue(value, designer, orientation);
		} else {
			horizontalBarHelper.setValue(value, designer, orientation);
		}
	}

	private ScrollBarHelper verticalBarHelper = new ScrollBarHelper() {

		@Override
		public int getMinSize() {
			return designer.getMinHeight();
		}

		@Override
		public int getDesignerSize() {
			return designer.getDesignerHeight();
		}
		
		@Override
		public void resetDesiger(int value, int max) {
			designer.setVerticalValue(value);
			designer.repaint();
		}

	};

	private ScrollBarHelper horizontalBarHelper = new ScrollBarHelper() {
		
		@Override
		public int getMinSize() {
			return designer.getMinWidth();
		}

		@Override
		public int getDesignerSize() {
			return designer.getDesignerWidth();
		}
		
		public void resetDesiger(int value, int max) {
			designer.setHorizontalValue(value);
			designer.repaint();
		}

	};

	private abstract class ScrollBarHelper {

		public abstract int getDesignerSize();

		public abstract int getMinSize();
		
		/**
		 * 以前的底层布局是absolute支持无限拖动，现在不需要，去掉设置rootComponent大小
		 * 
		 * @param value 滚动条的value
		 * @param max   滚动条的最大值
		 */ 
		public abstract void resetDesiger(int value, int max);
		
		/**
		 * 设置滚动条的值
		 * @param newValue 当前值
		 * @param designer 所在的容器
		 */
		public void setValue(int newValue, ScrollRulerComponent designer, int orientation) {
			int oldValue = getValue();
			int total = getMinSize();
			int visi = getDesignerSize();
			int oldmax = getMaximum();
			int max;
			if (newValue < oldValue) {
				max = oldmax;
				if (newValue < 0) {
					// 小于0 就是滚动条为0时继续点击移动箭头或者滚轮上滚
					// 目前表单底层布局不需要调整max值，否则滚动条消失，容器大小改变
					newValue = 0;
				} 
			} else {
				max = Math.max((newValue + visi), oldmax);
			}
			max =  Math.max(max, Math.max(total, visi));
			// 表单界面横纵向滚动条不支持点击滚动轴时无限扩大
			Point p = designer.calculateScroll(oldmax, max, newValue, oldValue, visi, orientation);
			newValue = p.x;
			max = p.y;
			model.setRangeProperties(newValue, visi, 0, max, true);
			resetDesiger(newValue, max);
		}
		
	}

}