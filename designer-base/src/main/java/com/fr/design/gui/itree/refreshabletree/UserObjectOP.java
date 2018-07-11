package com.fr.design.gui.itree.refreshabletree;

import java.util.List;
import java.util.Map;

import com.fr.design.gui.itree.refreshabletree.loader.ChildrenNodesLoader;


/**
 * UserObjectRefreshJTree的操作
 * 
 * @editor zhou
 * @since 2012-3-28下午9:49:31
 */

public interface UserObjectOP<T> extends ChildrenNodesLoader {

	/*
	 * 初始化返回name, T键值对
	 */
	public List<Map<String, T>> init();

	/*
	 * ButtonEnabled intercept
	 */
	public boolean interceptButtonEnabled();
	
	/*
	 * 移除名字是name的TableData
	 */
	public void removeAction(String name);

}