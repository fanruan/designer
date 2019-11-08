package com.fr.design.form.mobile;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.designer.IntervalConstants;
import com.fr.design.dialog.mobile.MobileRadioCheckPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.form.main.mobile.FormMobileAttr;


import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;

/**
 * Created by plough on 2018/5/31.
 */
public class FormMobileOthersPane extends BasicBeanPane<FormMobileAttr> {

    private MobileRadioCheckPane appearRefreshCheckPane;  // 页面再现时刷新
    private MobileRadioCheckPane promptWhenLeaveWithoutSubmitCheckPane;  // 数据未提交离开提示
    private MobileRadioCheckPane allowDoubleClickOrZoomCheckPane;   // 允许双击/双指缩放

    public FormMobileOthersPane() {
        this.initComponents();
    }

    private void initComponents() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        JPanel borderPane = FRGUIPaneFactory.createTitledBorderPane(this.title4PopupWindow());
        JPanel contentPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        contentPane.setBorder(BorderFactory.createEmptyBorder(0, IntervalConstants.INTERVAL_L1, 0, 0));
        appearRefreshCheckPane = new MobileRadioCheckPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Appear_Refresh"));
        contentPane.add(appearRefreshCheckPane, BorderLayout.WEST);
        promptWhenLeaveWithoutSubmitCheckPane = new MobileRadioCheckPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Prompt_When_Leave_Without_Submit"));
        contentPane.add(promptWhenLeaveWithoutSubmitCheckPane, BorderLayout.CENTER);
        allowDoubleClickOrZoomCheckPane = new MobileRadioCheckPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Mobile_Attr_Allow_Zoom"));
        contentPane.add(allowDoubleClickOrZoomCheckPane, BorderLayout.EAST);
        borderPane.add(contentPane);
        this.add(borderPane);
    }

    @Override
    public void populateBean(FormMobileAttr ob) {
        if (ob == null) {
            ob = new FormMobileAttr();
        }
        this.appearRefreshCheckPane.populateBean(ob.isAppearRefresh());
        this.promptWhenLeaveWithoutSubmitCheckPane.populateBean(ob.isPromptWhenLeaveWithoutSubmit());
        this.allowDoubleClickOrZoomCheckPane.populateBean(ob.isAllowDoubleClickOrZoom());
    }

    @Override
    public FormMobileAttr updateBean() {
        return null;
    }

    @Override
    public void updateBean(FormMobileAttr mobileAttr) {
        if(mobileAttr != null) {
            mobileAttr.setAppearRefresh(this.appearRefreshCheckPane.updateBean());
            mobileAttr.setPromptWhenLeaveWithoutSubmit(this.promptWhenLeaveWithoutSubmitCheckPane.updateBean());
            mobileAttr.setAllowDoubleClickOrZoom(this.allowDoubleClickOrZoomCheckPane.updateBean());
        }
    }

    @Override
    protected String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Other");
    }
}
