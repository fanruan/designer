package com.fr.design.report.mobile;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.designer.IntervalConstants;
import com.fr.design.dialog.mobile.MobileRadioCheckPane;
import com.fr.design.layout.FRGUIPaneFactory;

import com.fr.report.mobile.ElementCaseMobileAttr;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;

/**
 * Created by plough on 2018/5/31.
 */
public class MobileOthersPane extends BasicBeanPane<ElementCaseMobileAttr> {

    private MobileRadioCheckPane appearRefreshCheckPane;  // 页面再现时刷新

    // 允许双击/双指缩放
    private MobileRadioCheckPane allowDoubleClickOrZoomCheckPane;

    public MobileOthersPane() {
        this.initComponents();
    }

    private void initComponents() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        JPanel borderPane = FRGUIPaneFactory.createTitledBorderPane(this.title4PopupWindow());
        JPanel contentPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        contentPane.setBorder(BorderFactory.createEmptyBorder(0, IntervalConstants.INTERVAL_L1, 0, 0));
        appearRefreshCheckPane = new MobileRadioCheckPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Appear_Refresh"));
        contentPane.add(appearRefreshCheckPane, BorderLayout.WEST);
        allowDoubleClickOrZoomCheckPane = new MobileRadioCheckPane(com.fr.design.i18n.Toolkit.i18nText("允许双击/双指缩放"));
        contentPane.add(allowDoubleClickOrZoomCheckPane, BorderLayout.CENTER);
        borderPane.add(contentPane);
        this.add(borderPane);
    }

    @Override
    public void populateBean(ElementCaseMobileAttr ob) {
        if (ob == null) {
            ob = new ElementCaseMobileAttr();
        }
        this.appearRefreshCheckPane.populateBean(ob.isAppearRefresh());
        this.allowDoubleClickOrZoomCheckPane.populateBean(ob.isAllowDoubleClickOrZoom());
    }

    @Override
    public ElementCaseMobileAttr updateBean() {
        return null;
    }

    @Override
    public void updateBean(ElementCaseMobileAttr mobileAttr) {
        if(mobileAttr != null) {
            mobileAttr.setAppearRefresh(this.appearRefreshCheckPane.updateBean());
            mobileAttr.setAllowDoubleClickOrZoom(this.allowDoubleClickOrZoomCheckPane.updateBean());
        }
    }

    @Override
    protected String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Other");
    }
}
