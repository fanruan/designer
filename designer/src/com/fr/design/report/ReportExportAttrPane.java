package com.fr.design.report;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;

import com.fr.design.dialog.BasicPane;
import com.fr.general.Inter;
import com.fr.io.attr.ReportExportAttr;


public class ReportExportAttrPane extends BasicPane {
	private ExcelExportPane excelExportPane;
	private PDFExportPane pdfExportPane;
	private WordExportPane wordExportPane;
	
	public ReportExportAttrPane() {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		excelExportPane = new ExcelExportPane();
		this.add(excelExportPane);
		pdfExportPane = new PDFExportPane();
		this.add(pdfExportPane);
		wordExportPane = new WordExportPane();
		this.add(wordExportPane);
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
		
		return reportExportAttr;
	}
}