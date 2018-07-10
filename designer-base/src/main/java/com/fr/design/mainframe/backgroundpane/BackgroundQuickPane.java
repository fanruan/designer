package com.fr.design.mainframe.backgroundpane;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.event.UIObserver;
import com.fr.general.Background;

/**
 * @author zhou
 * @since 2012-5-29下午1:12:28
 */
public abstract class BackgroundQuickPane extends BasicBeanPane<Background> implements UIObserver {

	public abstract boolean accept(Background background);

	@Override
	public abstract void populateBean(Background background);

	@Override
	public abstract Background updateBean();

	@Override
	public abstract String title4PopupWindow();

	public abstract void reset();

	/**
	 * 组件是否需要响应添加的观察者事件
	 *
	 * @return 如果需要响应观察者事件则返回true，否则返回false
	 */
	public boolean shouldResponseChangeListener() {

		return true;
	}
}