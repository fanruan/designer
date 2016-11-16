package com.fr.design.report.mobile;

import com.fr.design.dialog.mobile.MobileToolBarBeanPane;
import com.fr.report.mobile.ElementCaseMobileAttr;

/**
 * Created by 方磊 on 2016/11/8.
 */
public class MobileToolBarPane extends MobileToolBarBeanPane<ElementCaseMobileAttr> {

    @Override
    public void populateBean(ElementCaseMobileAttr ob) {
        if (ob == null) {
            ob = new ElementCaseMobileAttr();
        }
        super.getZoomCheckPane().populateBean(ob.isZoom());
        super.getRefreshCheckPane().populateBean(ob.isRefresh());
    }

    @Override
    public ElementCaseMobileAttr updateBean() {
        return null;
    }

    @Override
    public void updateBean(ElementCaseMobileAttr mobileAttr) {
        if(mobileAttr != null) {
            mobileAttr.setZoom(super.getZoomCheckPane().updateBean());
            mobileAttr.setRefresh(super.getRefreshCheckPane().updateBean());
        }
    }
}
