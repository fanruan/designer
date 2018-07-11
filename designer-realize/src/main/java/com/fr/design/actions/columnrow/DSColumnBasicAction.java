package com.fr.design.actions.columnrow;

import javax.swing.SwingUtilities;

import com.fr.base.BaseUtils;
import com.fr.design.actions.CellSelectionAction;
import com.fr.design.dscolumn.DSColumnBasicPane;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.general.Inter;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.grid.selection.CellSelection;
import com.fr.report.cell.DefaultTemplateCellElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.core.SheetUtils;
import com.fr.report.elementcase.TemplateElementCase;

public class DSColumnBasicAction extends CellSelectionAction {

	public DSColumnBasicAction(ElementCasePane t) {
		super(t);
		
        this.setName(Inter.getLocText("Basic"));
        // this.setMnemonic('B');
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/expand/cellAttr.gif"));
    }
	
	@Override
	protected boolean executeActionReturnUndoRecordNeededWithCellSelection(
			CellSelection cs) {
        final ElementCasePane reportPane = this.getEditingComponent();
        final DSColumnBasicPane dsColumnBasicPane = new DSColumnBasicPane();
        
        BasicDialog dSColumnBasicDialog = dsColumnBasicPane.showWindow(SwingUtilities.getWindowAncestor(reportPane));

        // Got editCellElement.
        TemplateCellElement editCellElement = null;

        // got simple cell element from column and row.
        final TemplateElementCase report = reportPane.getEditingElementCase();
        editCellElement = report.getTemplateCellElement(cs.getColumn(), cs.getRow());
        
        dsColumnBasicPane.putElementcase(getEditingComponent());
        dsColumnBasicPane.putCellElement(editCellElement);

        // alex:如果是线性报表,这时还要计算一下默认父格是什么z
        if (report != null) {
            SheetUtils.calculateDefaultParent(report);
        }
        dsColumnBasicPane.populate(null, editCellElement);
        final CellSelection finalCS = cs;
        dSColumnBasicDialog.addDialogActionListener(new CellElementDialogActionListenr(editCellElement) {

            @Override
            public void doOk() {
                // 需要先行后列地增加新元素。
                for (int j = 0; j < finalCS.getRowSpan(); j++) {
                    for (int i = 0; i < finalCS.getColumnSpan(); i++) {
                        int column = i + finalCS.getColumn();
                        int row = j + finalCS.getRow();

                        editCellElement = report.getTemplateCellElement(column, row);
                        if (editCellElement == null) {
                            editCellElement = new DefaultTemplateCellElement(column, row);
                            report.addCellElement(editCellElement);
                        }
                        // update cell attributes
                        dsColumnBasicPane.update(editCellElement);
                    }
                }
                
                reportPane.fireTargetModified();
            }
        });
        
        dSColumnBasicDialog.setVisible(true);
        return false;
    }

    public static class CellElementDialogActionListenr extends DialogActionAdapter {

        TemplateCellElement editCellElement;

        public CellElementDialogActionListenr(TemplateCellElement editCellElement) {
            this.editCellElement = editCellElement;
        }
    }
}