package com.fr.design.fun;

import com.fr.plugin.injectable.SpecialLevel;
import com.fr.stable.fun.mark.Mutable;

import javax.swing.*;

/**
 * 导出菜单设计器端拓展，用于控制该菜单是否在web端显示
 */
public interface ExportToolBarProvider extends Mutable{
	
	String XML_TAG = SpecialLevel.ExportToolBarProvider.getTagName();

	int CURRENT_LEVEL = 1;

	/**
	 *
	 * 用于添加 控制web端是否显示该菜单的checkbox的面板
	 * 
	 * @param pane 面板
	 * @return 该面板
	 */
	JPanel updateCenterPane(JPanel pane);
	
	/**
	 * 更新界面
	 */
	void populate();
	
	/**
	 * 保存界面设置
	 */
	void update();
}