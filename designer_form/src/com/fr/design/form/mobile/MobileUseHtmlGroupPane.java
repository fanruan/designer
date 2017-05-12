package com.fr.design.form.mobile;

import com.fr.design.dialog.mobile.MobileUseHtmlGroupBeanPane;
import com.fr.form.main.mobile.FormMobileAttr;

/**
 * 直接copyreport中的MobileUseHtmlGroupPane
 * Created by fanglei on 2016/12/28.
 */
public class MobileUseHtmlGroupPane extends MobileUseHtmlGroupBeanPane<FormMobileAttr> {
    public MobileUseHtmlGroupPane(String annotation) {
        super(annotation);
    }

    @Override
    public void populateBean(FormMobileAttr mobileAttr) {
        if(mobileAttr != null) {
            selectIndexButton(mobileAttr.isUseHTML() ? 1 : 0);
        }
    }

    @Override
    public FormMobileAttr updateBean() {
        return null;
    }

    @Override
    public void updateBean(FormMobileAttr mobileAttr) {
        if(mobileAttr != null) {
            mobileAttr.setUseHTML(getSelectRadioIndex() == 1);
        }
    }
}
