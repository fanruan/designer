package com.fr.design.report.mobile;

import com.fr.design.beans.BasicBeanPane;
import com.fr.general.Inter;
import com.fr.report.mobile.ElementCaseMobileAttr;

import javax.swing.*;

/**
 * Created by Administrator on 2016/5/12/0012.
 */
public class ReportMobileAttrPane extends BasicBeanPane<ElementCaseMobileAttr>{


    private AppFitBrowserPane appFitBrowserPane;

    public ReportMobileAttrPane() {
        initComponents();
    }

    private void initComponents() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        AppFitPreviewPane appFitPreviewPane = new AppFitPreviewPane();

        appFitBrowserPane = new AppFitBrowserPane();
        appFitBrowserPane.setAppFitPreviewPane(appFitPreviewPane);
        this.add(appFitBrowserPane);

        this.add(appFitPreviewPane);
    }

    @Override
    public void populateBean(ElementCaseMobileAttr ob) {
        if (ob == null) {
            ob = new ElementCaseMobileAttr();
        }
        appFitBrowserPane.populateBean(ob);

    }

    @Override
    public ElementCaseMobileAttr updateBean() {
        return appFitBrowserPane.updateBean();
    }

    @Override
    protected String title4PopupWindow() {
        return Inter.getLocText("FR-Designer_Fit-App");
    }
}
