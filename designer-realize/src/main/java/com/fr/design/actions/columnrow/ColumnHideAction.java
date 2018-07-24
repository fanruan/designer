package com.fr.design.actions.columnrow;


import com.fr.design.mainframe.ElementCasePane;
import com.fr.grid.selection.CellSelection;
import com.fr.report.elementcase.ElementCase;
import com.fr.stable.unit.UNIT;

public class ColumnHideAction extends AbstractColumnRowIndexAction {

	public ColumnHideAction(ElementCasePane t, int indexOfColumnOrRow) {
		super(t, indexOfColumnOrRow);
		this.setName(com.fr.design.i18n.Toolkit.i18nText("Hide"));
	}

	@Override
	protected boolean executeActionReturnUndoRecordNeededWithCellSelection(CellSelection cs) {
		ElementCasePane ePane = this.getEditingComponent();
		ElementCase report = ePane.getEditingElementCase();
		int[] cols = cs.getSelectedColumns();
		for (int i = 0; i < cols.length; i++) {
			report.setColumnWidth(cols[i], UNIT.ZERO);
		}
		ePane.fireTargetModified();
		ePane.repaint();
		return false;
	}

}