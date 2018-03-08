/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.webattr;

import com.fr.base.ConfigManager;
import com.fr.config.ServerPreferenceConfig;
import com.fr.design.gui.frpane.LoadingBasicPane;
import com.fr.design.gui.frpane.UITabbedPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import com.fr.web.attr.ReportWebAttr;

import javax.swing.*;
import java.awt.*;

/**
 * Edit Report Server Parameter.
 */
public class EditReportServerParameterPane extends LoadingBasicPane {
    private UITextField configFileTextField;

    private UITabbedPane tabbedPane;

    private PageToolBarPane pagePane;
    private ViewToolBarPane viewPane;
    //TODO 表单
//    private FormToolBarPane formPane;
    private WriteToolBarPane writePane;
    private ReportWebAttr webAttr;
    
    private WebCssPane cssPane;
    
    private WebJsPane jsPane;
    
    private ErrorTemplatePane errorTemplatePane;


    @Override
	protected synchronized void initComponents(JPanel container) {
        JPanel defaultPane = container;
        defaultPane.setLayout(FRGUIPaneFactory.createBorderLayout());

        //path pane
        JPanel datasourcePathPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        defaultPane.add(datasourcePathPane, BorderLayout.NORTH);
        
        datasourcePathPane.add(GUICoreUtils.createFlowPane(new Component[] {
        		new UILabel(Inter.getLocText(
                "ReportServerP-Report_server_parameter") + ":"),
                (this.configFileTextField = new UITextField(40))
        }, FlowLayout.LEFT), BorderLayout.WEST);
        
        this.configFileTextField.setEditable(false);
        //Tabbed Pane
        tabbedPane = new UITabbedPane();
        defaultPane.add(tabbedPane, BorderLayout.CENTER);

        tabbedPane.addTab(Inter.getLocText("WEB-Pagination_Setting"), pagePane = new PageToolBarPane());
        tabbedPane.addTab(Inter.getLocText("WEB-Write_Setting"), writePane = new WriteToolBarPane());
        tabbedPane.addTab(Inter.getLocText("M-Data_Analysis_Settings"), viewPane = new ViewToolBarPane());
        tabbedPane.addTab(Inter.getLocText("ReportServerP-Import_Css"), cssPane = new WebCssPane());
        tabbedPane.addTab(Inter.getLocText("ReportServerP-Import_JavaScript"), jsPane = new WebJsPane());
        tabbedPane.addTab(Inter.getLocText("FR-Designer_ErrorHandlerTemplate"), errorTemplatePane = new ErrorTemplatePane());
    }
    
    @Override
    protected String title4PopupWindow() {
    	return Inter.getLocText("ReportServerP-Report_server_parameter");
    }

    public void populate(ServerPreferenceConfig reportServerPreferenceConfig) {
        //todo 原来界面上显示的xml路径
//        this.configFileTextField.setText(FRContext.getCurrentEnv().getPath() + File.separator +
//                ProjectConstants.RESOURCES_NAME +
//                File.separator + reportServerPreferenceConfig.fileName());

        webAttr = ((ReportWebAttr) ConfigManager.getProviderInstance().getGlobalAttribute(ReportWebAttr.class));
        if (webAttr != null) {
        	pagePane.populateBean(webAttr.getWebPage());
        	viewPane.populateBean(webAttr.getWebView());
        	writePane.populateBean(webAttr.getWebWrite());
        	cssPane.populate(webAttr);
            
        	jsPane.populate(webAttr);
        }
        
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

        reportServerPreferenceConfig.setErrorTemplate(this.errorTemplatePane.updateBean());
    }
}