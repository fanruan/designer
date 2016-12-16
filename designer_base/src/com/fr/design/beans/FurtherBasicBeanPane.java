package com.fr.design.beans;

import com.fr.stable.StringUtils;

/**
 * 
 * @author zhou
 * @since 2012-5-30下午12:12:42
 */
public abstract class FurtherBasicBeanPane<T> extends BasicBeanPane<T> {
	/**
	 * 是否是指定类型
	 * @param ob 对象
	 * @return 是否是指定类型
	 */
	public abstract boolean accept(Object ob);

	/**
	 * title应该是一个属性，不只是对话框的标题时用到，与其他组件结合时，也会用得到
	 * @return 绥化狂标题
	 */
	@Deprecated
	public String title4PopupWindow(){
		return StringUtils.EMPTY;
	}

	/**
	 * 重置
	 */
	public abstract void reset();
	
}