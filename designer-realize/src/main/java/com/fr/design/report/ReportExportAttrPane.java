package com.fr.design.report;

import javax.swing.*;

import com.fr.design.ExtraDesignClassManager;
import com.fr.design.dialog.BasicPane;
import com.fr.design.fun.ExportAttrTabProvider;
import com.fr.design.gui.frpane.UITabbedPane;

import com.fr.io.attr.ReportExportAttr;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class ReportExportAttrPane extends BasicPane {
    private ExcelExportPane excelExportPane;
    private PDFExportPane pdfExportPane;
    private WordExportPane wordExportPane;
    private List<AbstractExportPane> paneList;

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
        Set<ExportAttrTabProvider> providers = ExtraDesignClassManager.getInstance().getArray(ExportAttrTabProvider.XML_TAG);
        paneList = new ArrayList<>();
        for (ExportAttrTabProvider provider : providers) {
            uiTabbedPane.addTab(provider.title(), provider.toSwingComponent());
            paneList.add(provider.toExportPane());
        }
        this.add(uiTabbedPane);
    }

    @Override
    protected String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("ReportD-Excel_Export");
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
            exportPane.update(reportExportAttr);
        }
        return reportExportAttr;
    }
}