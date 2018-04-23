package com.fr.design.selection;

import com.fr.design.designer.TargetComponent;

/**
 * 
 * @author zhou
 * @since 2012-7-26上午10:20:22
 */
public interface SelectableElement {

	/**
	 * 获取选中元素的快速编辑区域
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public QuickEditor getQuickEditor(TargetComponent tc);

}