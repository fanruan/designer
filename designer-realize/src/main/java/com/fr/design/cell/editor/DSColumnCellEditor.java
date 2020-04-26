package com.fr.design.cell.editor;

import com.fr.design.data.DesignTableDataManager;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionListener;
import com.fr.design.dialog.FineJOptionPane;
import com.fr.design.dscolumn.DSColumnPane;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.grid.Grid;
import com.fr.log.FineLoggerFactory;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.core.SheetUtils;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.report.poly.PolyECBlock;
import com.fr.report.worksheet.WorkSheet;
import com.fr.stable.ProductConstants;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.awt.Component;

/**
 * CellEditor used to edit BindValuePainter object.
 */
public class DSColumnCellEditor extends AbstractCellEditor implements DialogActionListener {

    public DSColumnCellEditor(ElementCasePane<? extends TemplateElementCase> ePane) {
		super(ePane);
	}
    
	private DSColumnPane dsColumnPane = null;

	/**
	 * Return the value of the CellEditor.
	 */
	@Override
	public Object getCellEditorValue() throws Exception {
		return this.dsColumnPane.update();
	}

	/**
	 * Sets an initial <code>cellElement</code> for the editor. This will
	 * cause the editor to <code>stopCellEditing</code> and lose any partially
	 * edited value if the editor is editing when this method is called.
	 * <p/>
	 * <p/> Returns the component that should be added to the client's
	 * <code>Component</code> hierarchy. Once installed in the client's
	 * hierarchy this component will then be able to draw and receive user
	 * input.
	 *
	 * @param grid        the <code>Grid</code> that is asking the editor to edit; can
	 *                    be <code>null</code>
	 * @param cellElement the value of the cell to be edited; it is up to the specific
	 *                    editor to interpret and draw the value.
	 */
	@Override
	public Component getCellEditorComponent(Grid grid, TemplateCellElement cellElement, int resolution) {
		TemplateElementCase tplEC = grid.getElementCasePane().getEditingElementCase();

		//alex:如果是线性报表,这时还要计算一下默认父格是什么
		if (tplEC != null && (tplEC instanceof WorkSheet || tplEC instanceof PolyECBlock)) {
			SheetUtils.calculateDefaultParent(tplEC);
		}

		this.dsColumnPane = new DSColumnPane();
		dsColumnPane.putElementcase(grid.getElementCasePane());
		dsColumnPane.putCellElement(cellElement);
		BasicDialog dsColumnDialog = this.dsColumnPane.showWindowWithCustomSize(SwingUtilities.getWindowAncestor(grid), null, DSColumnPane.DEFAULT_DIMENSION);

		dsColumnDialog.addDialogActionListener(this);

		try {
			this.dsColumnPane.populate(DesignTableDataManager.getEditingTableDataSource(), cellElement);
		} catch (Exception exp) {
			FineJOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(grid), exp.getMessage(),
					com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Error"), JOptionPane.ERROR_MESSAGE);
            FineLoggerFactory.getLogger().error(exp.getMessage(), exp);
		}

		return dsColumnDialog;
	}

	@Override
	public void doOk() {
		fireEditingStopped();
	}

	@Override
	public void doCancel() {
		fireEditingCanceled();
	}
}