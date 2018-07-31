/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.webattr;

import com.fr.design.gui.frpane.LoadingBasicPane;
import com.fr.design.gui.frpane.UITabbedPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.style.background.BackgroundPane;
import com.fr.design.style.background.BackgroundPane4Browser;

import com.fr.web.attr.ReportWebAttr;

import javax.swing.*;
import java.awt.*;

/**
 * ReportWebAttr Dialog
 */
public class ReportWebAttrPane extends LoadingBasicPane {
    private ReportWebAttr reportWebAttr;

    private UITabbedPane tabbedPane;
    private CommonPane commonPane;
    private ReportServerPrinterPane serverPrintPane;
    
    private PageWebSettingPane pageWeb;
	private WriteWebSettingPane writeWeb;
	private ViewWebSettingPane viewWeb;
	
    private BackgroundPane backgroundPane;
    
    protected WebCssPane cssPane;
    
    protected WebJsPane jsPane;
    

    @Override
	protected synchronized void initComponents(JPanel container) {
        JPanel defaultPane = container;
        defaultPane.setLayout(FRGUIPaneFactory.createBorderLayout());

        //Tabbed Pane
        tabbedPane = new UITabbedPane();
        defaultPane.add(tabbedPane, BorderLayout.CENTER);
        tabbedPane.addTab(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Basic"), commonPane = new CommonPane());
        tabbedPane.addTab(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Printers(Server)"), serverPrintPane = new ReportServerPrinterPane());
        
        tabbedPane.add(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Pagination_Setting"), pageWeb = new PageWebSettingPane());
        tabbedPane.add(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Write_Setting"), writeWeb = new WriteWebSettingPane());
        tabbedPane.add(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Data_Analysis_Settings"), viewWeb = new ViewWebSettingPane());
        
        tabbedPane.addTab(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Browser_Background"), backgroundPane = new BackgroundPane4Browser());
        tabbedPane.addTab(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Import_Css"), cssPane = new WebCssPane());
        tabbedPane.addTab(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Import_JavaScript"), jsPane = new WebJsPane());
    }
    
    @Override
    protected String title4PopupWindow() {
    	return com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Template_Web_Attributes");
    }
        
    public void populate(ReportWebAttr reportWebAttr) {
        if (reportWebAttr == null) {
            reportWebAttr = new ReportWebAttr();
        }
        this.reportWebAttr = reportWebAttr;

        this.commonPane.populate(reportWebAttr);
        this.serverPrintPane.populate(reportWebAttr.getPrinter());
        
        this.pageWeb.populateBean(reportWebAttr);
        this.viewWeb.populateBean(reportWebAttr);
        this.writeWeb.populateBean(reportWebAttr);
        
        this.backgroundPane.populate(reportWebAttr.getBackground());
        this.cssPane.populate(reportWebAttr);
        this.jsPane.populate(reportWebAttr);
        
    }

    public ReportWebAttr update() {
    	reportWebAttr.setPrinter(this.serverPrintPane.update());
    	pageWeb.update(reportWebAttr);
    	writeWeb.update(reportWebAttr);
    	viewWeb.update(reportWebAttr);
    	reportWebAttr.setBackground(this.backgroundPane.update());
    	this.cssPane.update(reportWebAttr);
    	this.jsPane.update(reportWebAttr);
    	this.commonPane.update(reportWebAttr);
        return reportWebAttr;
    }
}