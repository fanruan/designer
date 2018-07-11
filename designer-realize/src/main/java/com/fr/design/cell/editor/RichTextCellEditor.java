package com.fr.design.cell.editor;

import java.awt.Component;

import javax.swing.SwingUtilities;

import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.dialog.DialogActionListener;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.report.RichTextPane;
import com.fr.grid.Grid;
import com.fr.report.cell.CellElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.cell.cellattr.CellGUIAttr;
import com.fr.report.core.SheetUtils;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.report.poly.PolyECBlock;
import com.fr.report.worksheet.WorkSheet;

public class RichTextCellEditor extends AbstractCellEditor implements
		DialogActionListener {

	public RichTextCellEditor(
			ElementCasePane<? extends TemplateElementCase> ePane) {
		super(ePane);
	}

	private RichTextPane richTextPane;

	@Override
	public Component getCellEditorComponent(Grid grid, 
			TemplateCellElement cellElement, int resolution) {
		TemplateElementCase parentTplEC = grid.getElementCasePane().getEditingElementCase();
		calculateParent(parentTplEC);
		this.richTextPane = new RichTextPane();

		BasicDialog richTextDialog = this.richTextPane.showMediumWindow(
			SwingUtilities.getWindowAncestor(grid),
			new DialogActionAdapter() {

				@Override
				public void doOk() {
					RichTextCellEditor.this.fireEditingStopped();
				}

				@Override
				public void doCancel() {
					RichTextCellEditor.this.fireEditingCanceled();
				}
			});
		richTextDialog.addDialogActionListener(this);
		this.richTextPane.populate(parentTplEC, cellElement);

		setShowAsHtml(cellElement);
		return richTextDialog;
	}
	
	private void setShowAsHtml(CellElement cellElement){
		CellGUIAttr guiAttr = cellElement.getCellGUIAttr();
		if(guiAttr == null){
			guiAttr = new CellGUIAttr();
			cellElement.setCellGUIAttr(guiAttr);
		}
		
		guiAttr.setShowAsHTML(true);
	}
	
	private void calculateParent(TemplateElementCase parentTplEC){
		if (parentTplEC != null
				&& (parentTplEC instanceof WorkSheet || parentTplEC instanceof PolyECBlock)) {
			SheetUtils.calculateDefaultParent(parentTplEC);
		}
	}

	@Override
	public Object getCellEditorValue() throws Exception {
		return this.richTextPane.update();
	}

	/**
	 * 确定
	 * 
	 *
	 * @date 2014-12-7-下午10:39:13
	 * 
	 */
	public void doOk() {
		this.fireEditingStopped();
	}

	/**
	 * 取消
	 * 
	 *
	 * @date 2014-12-7-下午10:39:01
	 * 
	 */
	public void doCancel() {
		this.fireEditingCanceled();
	}
}