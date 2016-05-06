package com.fr.design.report;

import javax.swing.*;

import com.fr.design.ExtraDesignClassManager;
import com.fr.design.dialog.BasicPane;
import com.fr.design.fun.AbstractExportPane;
import com.fr.design.fun.ExportAttrTabProvider;
import com.fr.design.gui.frpane.UITabbedPane;
import com.fr.general.Inter;
import com.fr.io.attr.ImageExportAttr;
import com.fr.io.attr.ReportExportAttr;

import java.util.HashMap;
import java.util.Map;


public class ReportExportAttrPane extends BasicPane {
    private ExcelExportPane excelExportPane;
    private PDFExportPane pdfExportPane;
    private WordExportPane wordExportPane;
    Map<String, AbstractExportPane> paneMap;

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
        paneMap = new HashMap<String, AbstractExportPane>();
        for (ExportAttrTabProvider provider : providers) {
            uiTabbedPane.addTab(provider.title(), provider.toSwingComponent());
            paneMap.put(provider.tag(),  provider.toExportPane());
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

        AbstractExportPane  exportPane = paneMap.get("Image");
        if(exportPane != null){
            exportPane.populate(reportExportAttr.getImageExportAttr());
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

        AbstractExportPane exportPane = paneMap.get("Image");
        if(exportPane != null){
            reportExportAttr.setImageExportAttr(exportPane.update(ImageExportAttr.class));
        }
        return reportExportAttr;
    }
}