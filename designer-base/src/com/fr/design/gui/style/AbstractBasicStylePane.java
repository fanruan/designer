package com.fr.design.gui.style;

import com.fr.base.Style;
import com.fr.design.beans.BasicBeanPane;

/**
 * 
 * @author zhou
 * @since 2012-5-24下午1:42:53
 */
public abstract class AbstractBasicStylePane extends BasicBeanPane<Style> {


	public abstract Style update(Style style);

	@Override
	protected abstract String title4PopupWindow();

	@Override
	public Style updateBean() {
		return null;
	}
	
	@Override 
	public void populateBean(Style ob) {
		
	}

}