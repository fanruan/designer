package com.fr.design.fun;

import com.fr.stable.fun.mark.Immutable;

import javax.swing.plaf.ComponentUI;

/**
 * 自定义单元格ui接口
 *
 * @return
 */
public interface GridUIProcessor extends Immutable {

	String MARK_STRING = "GridUIProcessor";
	int CURRENT_LEVEL = 1;

	/**
	 * 自定义gridui, 用于实现一些自定义的格子绘制.
	 *
	 * @return 自定义gridui
	 */
	ComponentUI appearanceForGrid(int paramInt);
}