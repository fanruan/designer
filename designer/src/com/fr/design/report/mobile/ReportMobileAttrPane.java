package com.fr.design.report.mobile;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.general.Inter;
import com.fr.report.mobile.ElementCaseMobileAttr;

import javax.swing.*;

/**
 * Created by Administrator on 2016/5/12/0012.
 */
public class ReportMobileAttrPane extends BasicBeanPane<ElementCaseMobileAttr>{

    private ReportMobileTemplateSettingsPane reportMobileTemplateSettingsPane;  // 模版设置面板
    private AppFitBrowserPane appFitBrowserPane;
    //工具栏容器
    private MobileToolBarPane mobileToolBarPane;
    // 其他
    private MobileOthersPane mobileOthersPane;

    public ReportMobileAttrPane() {
        initComponents();
    }

    private void initComponents() {
        AppFitPreviewPane appFitPreviewPane = new AppFitPreviewPane();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
        jPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        reportMobileTemplateSettingsPane = new ReportMobileTemplateSettingsPane();
        jPanel.add(reportMobileTemplateSettingsPane);

        appFitBrowserPane = new AppFitBrowserPane();
        appFitBrowserPane.setAppFitPreviewPane(appFitPreviewPane);
        jPanel.add(appFitBrowserPane);

        jPanel.add(mobileToolBarPane = new MobileToolBarPane());
        jPanel.add(mobileOthersPane = new MobileOthersPane());

        jPanel.add(appFitPreviewPane);
        UIScrollPane scrollPane = new UIScrollPane(jPanel);
        this.add(scrollPane);
    }

    @Override
    public void populateBean(ElementCaseMobileAttr ob) {
        if (ob == null) {
            ob = new ElementCaseMobileAttr();
        }
        appFitBrowserPane.populateBean(ob);
        mobileToolBarPane.populateBean(ob);
        mobileOthersPane.populateBean(ob);
        reportMobileTemplateSettingsPane.populateBean(ob);
    }

    @Override
    public ElementCaseMobileAttr updateBean() {
        ElementCaseMobileAttr caseMobileAttr = appFitBrowserPane.updateBean();
        mobileToolBarPane.updateBean(caseMobileAttr);
        mobileOthersPane.updateBean(caseMobileAttr);
        reportMobileTemplateSettingsPane.updateBean(caseMobileAttr);

        return caseMobileAttr;
    }

    @Override
    protected String title4PopupWindow() {
        return Inter.getLocText("FR-Designer_Mobile-Attr");
    }
}
