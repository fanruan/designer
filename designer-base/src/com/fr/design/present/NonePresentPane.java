package com.fr.design.present;

import com.fr.base.present.Present;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.general.Inter;

/**
 * 
 * @author zhou
 * @since 2012-6-1下午3:46:24
 */
public class NonePresentPane extends FurtherBasicBeanPane<Present> {

	@Override
	public boolean accept(Object ob) {
		return ob == null;
	}

	@Override
	public String title4PopupWindow() {
		return Inter.getLocText("Present-No_Present");
	}
	
	public void reset() {
		
	}

	@Override
	public void populateBean(Present ob) {
		
	}

	@Override
	public Present updateBean() {
		return null;
	}

}