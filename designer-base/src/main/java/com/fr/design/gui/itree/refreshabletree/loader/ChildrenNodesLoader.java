package com.fr.design.gui.itree.refreshabletree.loader;

import com.fr.design.gui.itree.refreshabletree.ExpandMutableTreeNode;


/**
 * 生成树上的子节点
 * 
 * @editor zhou
 * @since 2012-3-28下午9:57:40
 */
public interface ChildrenNodesLoader {

	/**
	 * 生成子节点
	 * 
	 * @return
	 */
	ExpandMutableTreeNode[] load();

	public static ChildrenNodesLoader NULL = new ChildrenNodesLoader() {

		@Override
		public ExpandMutableTreeNode[] load() {
			return new ExpandMutableTreeNode[0];
		}
	};

}