/**
 * 
 */
package com.fr.design.designer.creator;

import java.awt.Component;
import java.util.ArrayList;

/**
 * @author jim
 * @date 2014-11-7
 * 
 */
public interface XCreatorTools {

	/**
	 * 控件树不显示此组件
	 * @param path 控件树list
	 */
	void notShowInComponentTree(ArrayList<Component> path);
	
	/**
	 * 重置组件的名称
	 * @param name 名称
	 */
	void resetCreatorName(String name);
	
	/**
	 * 返回编辑的子组件，scale为其内部组件
	 * @return 组件
	 */
	XCreator getEditingChildCreator();
	
	/**
	 * 返回对应属性表的组件，scale和title返回其子组件
	 * @return 组件
	 */
	XCreator getPropertyDescriptorCreator();
	
	/**
	 * 更新子组件的Bound; 没有不处理
	 *  @param minHeight 最小高度
	 */
	void updateChildBound(int minHeight);
	
	/**
	 * 是否作为控件树的叶子节点
	 * @return 是则返回true
	 */
	boolean isComponentTreeLeaf();
	
	/**
	 * 是否为sclae和title专属容器
	 * @return 是则返回true
	 */
	boolean isDedicateContainer();
	
}