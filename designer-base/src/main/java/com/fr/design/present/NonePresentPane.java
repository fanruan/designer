package com.fr.design.present;

import com.fr.base.present.Present;
import com.fr.design.beans.FurtherBasicBeanPane;


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
		return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Present_No_Present");
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