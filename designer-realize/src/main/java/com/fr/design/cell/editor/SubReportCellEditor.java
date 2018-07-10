package com.fr.design.cell.editor;

import java.awt.Component;

import javax.swing.SwingUtilities;

import com.fr.design.report.SubReportPane;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionListener;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.grid.Grid;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.core.SheetUtils;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.report.poly.PolyECBlock;
import com.fr.report.worksheet.WorkSheet;

public class SubReportCellEditor extends AbstractCellEditor implements
		DialogActionListener {

	public SubReportCellEditor(
			ElementCasePane<? extends TemplateElementCase> ePane) {
		super(ePane);
	}

	private SubReportPane subReportPane;

	@Override
	public Component getCellEditorComponent(Grid grid,
			TemplateCellElement cellElement, int resolution) {
		TemplateElementCase parentTplEC = grid.getElementCasePane()
				.getEditingElementCase();

		// alex:如果是线性报表,这时还要计算一下默认父格是什么
		if (parentTplEC != null
				&& (parentTplEC instanceof WorkSheet || parentTplEC instanceof PolyECBlock)) {
			SheetUtils.calculateDefaultParent(parentTplEC);
		}

		this.subReportPane = new SubReportPane();

		BasicDialog subReportDialog = this.subReportPane
				.showWindow(SwingUtilities.getWindowAncestor(grid));
		subReportDialog.addDialogActionListener(this);
		this.subReportPane.populate(parentTplEC, cellElement);

		return subReportDialog;
	}

	@Override
	public Object getCellEditorValue() throws Exception {
		return this.subReportPane.update();
	}

	@Override
	public void doOk() {
		this.fireEditingStopped();
	}

	@Override
	public void doCancel() {
		this.fireEditingCanceled();
	}
}