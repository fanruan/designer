package com.fr.design.actions.columnrow;

import com.fr.general.Inter;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.Selection;
import com.fr.report.elementcase.ElementCase;
import com.fr.stable.unit.UNITConstants;

public class ResetRowHideAction extends AbstractColumnRowIndexAction {

	public ResetRowHideAction(ElementCasePane t, int indexOfColumnOrRow) {
		super(t, indexOfColumnOrRow);
		this.setName(Inter.getLocText(new String[]{"MConfig-CancelButton", "Hide"}));
	}

	@Override
	protected boolean executeActionReturnUndoRecordNeededWithCellSelection(CellSelection cs) {
		ElementCasePane ePane = this.getEditingComponent();
		ElementCase report = ePane.getEditingElementCase();

		int[] rows = cs.getSelectedRows();
		boolean ischanged = false;
		for (int row : rows) {
			if (report.getRowHeight(row).equal_zero()) {
				report.setRowHeight(row, UNITConstants.DEFAULT_ROW_HEIGHT);
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
				if (report.getRowHeight(i).equal_zero()) {
					this.setEnabled(true);
					return;
				}
			}
		}
		this.setEnabled(false);

	}

}