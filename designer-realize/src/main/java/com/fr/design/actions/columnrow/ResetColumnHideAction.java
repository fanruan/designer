package com.fr.design.actions.columnrow;

import com.fr.general.Inter;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.Selection;
import com.fr.report.elementcase.ElementCase;
import com.fr.stable.unit.UNITConstants;

public class ResetColumnHideAction extends AbstractColumnRowIndexAction {

	public ResetColumnHideAction(ElementCasePane t, int indexOfColumnOrRow) {
		super(t, indexOfColumnOrRow);
		this.setName(Inter.getLocText(new String[]{"MConfig-CancelButton", "Hide"}));
	}

	@Override
	protected boolean executeActionReturnUndoRecordNeededWithCellSelection(CellSelection cs) {
		ElementCasePane ePane = this.getEditingComponent();
		ElementCase report = ePane.getEditingElementCase();

		int[] columns = cs.getSelectedColumns();
		boolean ischanged = false;
		for (int column : columns) {
			if (report.getColumnWidth(column).equal_zero()) {
				report.setColumnWidth(column, UNITConstants.DEFAULT_COL_WIDTH);
				ischanged = true;
			}
		}
		if (ischanged) {
			ePane.repaint();
			ePane.fireTargetModified();
		}

		return false;
	}

	@Override
	public void update() {
		ElementCasePane ePane = this.getEditingComponent();
		ElementCase report = ePane.getEditingElementCase();
		Selection s = ePane.getSelection();
		if (s instanceof CellSelection) {
			CellSelection cs = (CellSelection)s;
			int[] rows = cs.getSelectedRows();
			for (int i = 0; i < rows.length; i++) {
				if (report.getColumnWidth(i).equal_zero()) {
					this.setEnabled(true);
					return;
				}
			}
		}
		this.setEnabled(false);

	}

}