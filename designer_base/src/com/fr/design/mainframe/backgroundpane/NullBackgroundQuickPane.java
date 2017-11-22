package com.fr.design.mainframe.backgroundpane;

import com.fr.design.event.UIObserverListener;
import com.fr.general.Background;
import com.fr.general.Inter;

import java.awt.*;

/**
 * @author zhou
 * @since 2012-5-29下午1:12:24
 */
public class NullBackgroundQuickPane extends BackgroundQuickPane {

    public Dimension getPreferredSize(){
        return new Dimension(0,0);
    }

	public void populateBean(Background background) {
		// do nothing.
	}

	public Background updateBean() {
		return null;
	}

	/**
	 * 给组件登记一个观察者监听事件
	 *
	 * @param listener 观察者监听事件
	 */
	public void registerChangeListener(final UIObserverListener listener) {

	}


    /**
     * 是否接受
     * @param background     背景
     * @return    是则返回true
     */
	public boolean accept(Background background) {
		return background == null;
	}

    /**
     * 名称
     * @return     名称
     */
	public String title4PopupWindow() {
		return Inter.getLocText("Background-Null");
	}

	@Override
	public void reset() {
		// do nothing
	}

}