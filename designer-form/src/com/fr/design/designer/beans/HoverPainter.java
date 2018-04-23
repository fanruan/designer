package com.fr.design.designer.beans;

import java.awt.Point;

import com.fr.design.designer.creator.XCreator;

/**
 * 渲染器，目的是为组件或者布局管理器提供额外的渲染入口。
 * @since 6.5.3
 */
public interface HoverPainter extends Painter {
	/**
	 *  当前焦点热点，即鼠标所在点
	 * @param p 焦点位置
	 */
	void setHotspot(Point p);

	/**
	 *  当前要放置的组件
	 * @param creator 组件
	 */
	void setCreator(XCreator creator);
}