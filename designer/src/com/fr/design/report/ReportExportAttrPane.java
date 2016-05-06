package com.fr.design.report;

import javax.swing.*;

import com.fr.design.ExtraDesignClassManager;
import com.fr.design.dialog.BasicPane;
import com.fr.design.fun.AbstractExportPane;
import com.fr.design.fun.ExportAttrTabProvider;
import com.fr.design.gui.frpane.UITabbedPane;
import com.fr.general.Inter;
import com.fr.io.attr.ReportExportAttr;

import java.util.ArrayList;
import java.util.List;


public class ReportExportAttrPane extends BasicPane {
    private ExcelExportPane excelExportPane;
    private PDFExportPane pdfExportPane;
    private WordExportPane wordExportPane;
    List<AbstractExportPane> paneList;

    public ReportExportAttrPane() {
        UITabbedPane uiTabbedPane = new UITabbedPane();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        excelExportPane = new ExcelExportPane();
        uiTabbedPane.addTab("Excel", excelExportPane);
        pdfExportPane = new PDFExportPane();
        uiTabbedPane.addTab("PDF", pdfExportPane);
        wordExportPane = new WordExportPane();
        uiTabbedPane.addTab("Word", wordExportPane);
        ExportAttrTabProvider[] providers = ExtraDesignClassManager.getInstance().getExportAttrTabProviders();
        paneList = new ArrayList<AbstractExportPane>();
        for (ExportAttrTabProvider provider : providers) {
            uiTabbedPane.addTab(provider.title(), provider.toSwingComponent());
            paneList.add(provider.toExportPane());
        }
        this.add(uiTabbedPane);
    }

    @Override
    protected String title4PopupWindow() {
        return Inter.getLocText("ReportD-Excel_Export");
    }

    public void populate(ReportExportAttr reportExportAttr) {
        if (reportExportAttr == null) {
            reportExportAttr = new ReportExportAttr();
        }

        if (this.excelExportPane != null) {
            this.excelExportPane.populate(reportExportAttr.getExcelExportAttr());
        }

        if (this.pdfExportPane != null) {
            this.pdfExportPane.populate(reportExportAttr.getPDFExportAttr());
        }

        if (this.wordExportPane != null) {
            this.wordExportPane.populate(reportExportAttr.getWordExportAttr());
        }

        for (AbstractExportPane exportpane : paneList) {
            exportpane.populate(reportExportAttr);
        }
    }

    public ReportExportAttr update() {

        ReportExportAttr reportExportAttr = new ReportExportAttr();
        if (this.excelExportPane != null) {
            reportExportAttr.setExcelExportAttr(this.excelExportPane.update());
        }

        if (this.pdfExportPane != null) {
            reportExportAttr.setPDFExportAttr(this.pdfExportPane.update());
        }

        if (this.wordExportPane != null) {
            reportExportAttr.setWordExportAttr(this.wordExportPane.update());
        }

        for (AbstractExportPane exportPane : paneList) {
            ReportExportAttr exportAttr = exportPane.update(reportExportAttr, ReportExportAttr.class);
            if (exportAttr != null) {
                reportExportAttr = exportAttr;
            }
        }
        return reportExportAttr;
    }
}