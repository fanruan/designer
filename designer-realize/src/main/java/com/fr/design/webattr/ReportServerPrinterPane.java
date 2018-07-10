/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.webattr;


import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.report.web.Printer;
/**
 * 打印机设置面板
 * @editor zhou
 * @since 2012-3-23下午3:55:18
 */
public class ReportServerPrinterPane extends JPanel {
    private ServerPrinterPane serverPrinterPane;

    public ReportServerPrinterPane() {
        this.initComponents();
    }

    protected void initComponents() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(6, 2, 4, 2));
        serverPrinterPane = new ServerPrinterPane();
        this.add(serverPrinterPane);
    }

    public void populate(Printer printer) {
        serverPrinterPane.populate(printer);
    }

    public Printer update() {
        return serverPrinterPane.update();
    }
}