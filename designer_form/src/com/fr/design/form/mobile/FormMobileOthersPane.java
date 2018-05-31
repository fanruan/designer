package com.fr.design.form.mobile;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.designer.IntervalConstants;
import com.fr.design.dialog.mobile.MobileRadioCheckPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.form.main.mobile.FormMobileAttr;
import com.fr.general.Inter;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;

/**
 * Created by plough on 2018/5/31.
 */
public class FormMobileOthersPane extends BasicBeanPane<FormMobileAttr> {

    private MobileRadioCheckPane appearRefreshCheckPane;  // 页面再现时刷新

    public FormMobileOthersPane() {
        this.initComponents();
    }

    private void initComponents() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        JPanel borderPane = FRGUIPaneFactory.createTitledBorderPane(this.title4PopupWindow());
        JPanel contentPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        contentPane.setBorder(BorderFactory.createEmptyBorder(0, IntervalConstants.INTERVAL_L1, 0, 0));
        appearRefreshCheckPane = new MobileRadioCheckPane(Inter.getLocText("FR-Designer_Appear_Refresh"));
        contentPane.add(appearRefreshCheckPane, BorderLayout.WEST);
        borderPane.add(contentPane);
        this.add(borderPane);
    }

    @Override
    public void populateBean(FormMobileAttr ob) {
        if (ob == null) {
            ob = new FormMobileAttr();
        }
        this.appearRefreshCheckPane.populateBean(ob.isAppearRefresh());
    }

    @Override
    public FormMobileAttr updateBean() {
        return null;
    }

    @Override
    public void updateBean(FormMobileAttr mobileAttr) {
        if(mobileAttr != null) {
            mobileAttr.setAppearRefresh(this.appearRefreshCheckPane.updateBean());
        }
    }

    @Override
    protected String title4PopupWindow() {
        return Inter.getLocText("FR-Designer_Other");
    }
}
