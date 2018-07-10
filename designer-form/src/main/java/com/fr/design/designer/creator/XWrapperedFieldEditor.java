/**
 * 
 */
package com.fr.design.designer.creator;

import java.awt.Dimension;

import javax.swing.JComponent;

import com.fr.form.ui.FieldEditor;

/**
 *
 *
 * @date: 2014-11-25-下午5:08:06
 */
public abstract class XWrapperedFieldEditor extends XFieldEditor {

	/**
	 * 构造函数
	 */
	public XWrapperedFieldEditor(FieldEditor widget, Dimension initSize) {
		super(widget, initSize);
	}

	protected abstract JComponent initEditor();

	protected abstract String getIconName();

    
	/**
	 * 获取当前XCreator的一个封装父容器
	 * 
	 * @param widgetName 当前组件名
	 * 
	 * @return 封装的父容器
	 * 
	 *
	 * @date 2014-11-25-下午4:47:23
	 * 
	 */
	protected XLayoutContainer getCreatorWrapper(String widgetName){
		return new XWScaleLayout();
	}
	
	/**
	 * 将当前对象添加到父容器中
	 * 
	 * @param parentPanel 父容器组件
	 * 
	 *
	 * @date 2014-11-25-下午4:57:55
	 * 
	 */
	protected void addToWrapper(XLayoutContainer parentPanel, int width, int minHeight){			
		this.setSize(width, minHeight);
		parentPanel.add(this);
	}
    
    /**
	 * 此控件在自适应布局要保持原样高度
	 * 
	 * @return 是则返回true
	 */
	@Override
	public boolean shouldScaleCreator() {
		return true;
	}
}