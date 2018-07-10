/**
 * 
 */
package com.fr.design.designer.beans.adapters.layout;

import com.fr.design.beans.GroupModel;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XLayoutContainer;

/**
 * @author jim
 * @date 2014-8-5
 */
public class FRScaleLayoutAdapter extends AbstractLayoutAdapter {

	/**
	 * 构造函数
	 * 
	 * @param container
	 *            布局容器
	 */
	public FRScaleLayoutAdapter(XLayoutContainer container) {
		super(container);
	}

	/**
	 * 能否对应位置放置当前组件
	 * 
	 * @param creator
	 *            组件
	 * @param x
	 *            添加的位置x，该位置是相对于container的
	 * @param y
	 *            添加的位置y，该位置是相对于container的
	 * @return 是否可以放置
	 */
	@Override
	public boolean accept(XCreator creator, int x, int y) {
		return false;
	}

	/**
	 * 
	 * @see com.fr.design.designer.beans.adapters.layout.AbstractLayoutAdapter#addComp(com.fr.design.designer.creator.XCreator,
	 *      int, int)
	 */
	@Override
	protected void addComp(XCreator creator, int x, int y) {
		return;
	}

	@Override
	public GroupModel getLayoutProperties() {
		return null;
	}

}