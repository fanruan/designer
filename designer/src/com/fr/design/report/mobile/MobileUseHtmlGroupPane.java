package com.fr.design.report.mobile;

import com.fr.design.dialog.mobile.MobileUseHtmlGroupBeanPane;
import com.fr.report.mobile.ElementCaseMobileAttr;

/**
 * Created by kunsnat on 2016/8/3.
 */
public class MobileUseHtmlGroupPane extends MobileUseHtmlGroupBeanPane<ElementCaseMobileAttr> {
    @Override
    public void populateBean(ElementCaseMobileAttr mobileAttr) {
        if(mobileAttr != null) {
            selectIndexButton(mobileAttr.isUseHTML() ? 1 : 0);
        }
    }

    @Override
    public ElementCaseMobileAttr updateBean() {
        return null;
    }

    @Override
    public void updateBean(ElementCaseMobileAttr mobileAttr) {
        if(mobileAttr != null) {
            mobileAttr.setUseHTML(getSelectRadioIndex() == 1);
        }
    }
}
