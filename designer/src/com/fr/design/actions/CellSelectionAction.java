package com.fr.design.actions;

import com.fr.design.mainframe.ElementCasePane;
import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.Selection;

public abstract class CellSelectionAction extends ElementCaseAction {
	protected CellSelectionAction(ElementCasePane t) {
		super(t);
	}
	
	@Override
	public boolean executeActionReturnUndoRecordNeeded() {
		ElementCasePane ePane = this.getEditingComponent();
		Selection s = ePane.getSelection();
		
		// TODO ALEX_SEP instanceof i hate it
		if (s instanceof CellSelection) {
			return executeActionReturnUndoRecordNeededWithCellSelection((CellSelection)s);
		}
		
		return false;
	}
	
	protected abstract boolean executeActionReturnUndoRecordNeededWithCellSelection(CellSelection cs);
	
	@Override
	public void update() {
		super.update();
		if (this.isEnabled()) {
			this.setEnabled(this.getEditingComponent().getSelection() instanceof CellSelection);
		}
	}
}