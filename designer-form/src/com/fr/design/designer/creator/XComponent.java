package com.fr.design.designer.creator;

import java.awt.Rectangle;

import javax.swing.JComponent;

import com.fr.design.mainframe.BaseJForm;
import com.fr.design.mainframe.FormDesigner;

public interface XComponent {

	/**
	 * 返回组件的位置大小
	 * @return 返回bound
	 */
	Rectangle getBounds();

	/**
	 * 设置组件的位置大小
	 * @param oldbounds  bound大小
	 */
	void setBounds(Rectangle oldbounds);

	/**
	 * 生成工具菜单界面
	 * @param jform BaseJForm类
	 * @param formeditor 设计界面组件
	 * @return 返回工具界面
	 */
	JComponent createToolPane(BaseJForm jform, FormDesigner formeditor);
	
}