package com.fr.design.actions.columnrow;


import com.fr.design.mainframe.ElementCasePane;
import com.fr.grid.selection.CellSelection;
import com.fr.report.elementcase.ElementCase;
import com.fr.stable.unit.UNIT;

public class RowHideAction extends AbstractColumnRowIndexAction {

	public RowHideAction(ElementCasePane t, int indexOfColumnOrRow) {
		super(t, indexOfColumnOrRow);
		this.setName(com.fr.design.i18n.Toolkit.i18nText("Hide"));
	}

	@Override
	protected boolean executeActionReturnUndoRecordNeededWithCellSelection(CellSelection cs) {
		ElementCasePane ePane = this.getEditingComponent();
		ElementCase report = ePane.getEditingElementCase();
		int[] rows = cs.getSelectedRows();
		for (int i = 0; i < rows.length; i++) {
			report.setRowHeight(rows[i], UNIT.ZERO);
		}
		ePane.fireTargetModified();
		ePane.repaint();
		return false;
	}

}