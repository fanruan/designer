/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.webattr;

import com.fr.base.ConfigManager;
import com.fr.base.print.PrintSettingsAttrMark;
import com.fr.config.PrintConfig;
import com.fr.config.ServerPreferenceConfig;
import com.fr.design.gui.frpane.LoadingBasicPane;
import com.fr.design.gui.frpane.UITabbedPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.design.webattr.printsettings.PrintSettingPane;

import com.fr.report.core.ReportUtils;
import com.fr.report.web.WebPage;
import com.fr.report.web.WebView;
import com.fr.report.web.WebWrite;
import com.fr.web.attr.ReportWebAttr;

import javax.swing.*;
import java.awt.*;

/**
 * Edit Report Server Parameter.
 */
public class EditReportServerParameterPane extends LoadingBasicPane {

    private UITabbedPane tabbedPane;

    private PageToolBarPane pagePane;
    private ViewToolBarPane viewPane;
    //TODO 表单
//    private FormToolBarPane formPane;
    private WriteToolBarPane writePane;
    private ReportWebAttr webAttr;
    private PrintSettingsAttrMark printSettings;
    
    private WebCssPane cssPane;
    
    private WebJsPane jsPane;
    
    private ErrorTemplatePane errorTemplatePane;

    private PrintSettingPane printSettingPane;


    @Override
	protected synchronized void initComponents(JPanel container) {
        JPanel defaultPane = container;
        defaultPane.setLayout(FRGUIPaneFactory.createBorderLayout());

        //Tabbed Pane
        tabbedPane = new UITabbedPane();
        defaultPane.add(tabbedPane, BorderLayout.CENTER);

        tabbedPane.addTab(com.fr.design.i18n.Toolkit.i18nText("WEB-Pagination_Setting"), pagePane = new PageToolBarPane());
        tabbedPane.addTab(com.fr.design.i18n.Toolkit.i18nText("WEB-Write_Setting"), writePane = new WriteToolBarPane());
        tabbedPane.addTab(com.fr.design.i18n.Toolkit.i18nText("M-Data_Analysis_Settings"), viewPane = new ViewToolBarPane());
        tabbedPane.addTab(com.fr.design.i18n.Toolkit.i18nText("ReportServerP-Import_Css"), cssPane = new WebCssPane());
        tabbedPane.addTab(com.fr.design.i18n.Toolkit.i18nText("ReportServerP-Import_JavaScript"), jsPane = new WebJsPane());
        tabbedPane.addTab(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_ErrorHandlerTemplate"), errorTemplatePane = new ErrorTemplatePane());
        tabbedPane.addTab(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Print_Setting"), printSettingPane = new PrintSettingPane());
    }
    
    @Override
    protected String title4PopupWindow() {
    	return com.fr.design.i18n.Toolkit.i18nText("ReportServerP-Report_server_parameter");
    }

    public void populate(ServerPreferenceConfig reportServerPreferenceConfig) {
        //todo 原来界面上显示的xml路径
//        this.configFileTextField.setText(WorkContext.getCurrent().getPath() + File.separator +
//                ProjectConstants.RESOURCES_NAME +
//                File.separator + reportServerPreferenceConfig.fileName());

        webAttr = ((ReportWebAttr) ConfigManager.getProviderInstance().getGlobalAttribute(ReportWebAttr.class));
        WebPage webPage = webAttr.getWebPage();
        WebView webView = webAttr.getWebView();
        WebWrite webWrite = webAttr.getWebWrite();
        if(webPage != null){
            pagePane.populateBean(webPage);
        }
        if(webView != null){
            viewPane.populateBean(webView);
        }
        if(webWrite != null){
            writePane.populateBean(webWrite);
        }
        cssPane.populate(webAttr);
        jsPane.populate(webAttr);
        printSettings = ReportUtils.getPrintSettingsFromServerConfig();
        printSettingPane.populate(printSettings);
        
        this.errorTemplatePane.populateBean(reportServerPreferenceConfig.getErrorTemplate());
    }

    /**
     * Update.
     */
    public void update(ServerPreferenceConfig reportServerPreferenceConfig) {
        ReportWebAttr webAttr = ((ReportWebAttr)ConfigManager.getProviderInstance().getGlobalAttribute(ReportWebAttr.class));
        webAttr.setWebPage(pagePane.updateBean());
        webAttr.setWebView(viewPane.updateBean());
        webAttr.setWebWrite(writePane.updateBean());
        cssPane.update(webAttr);
        jsPane.update(webAttr);

        printSettings = printSettingPane.updateBean();
        PrintConfig.getInstance().setPrintSettings(printSettings);

        reportServerPreferenceConfig.setErrorTemplate(this.errorTemplatePane.updateBean());
    }
}