package com.fr.design.actions;

import com.fr.design.mainframe.ElementCasePane;
import com.fr.grid.selection.FloatSelection;
import com.fr.grid.selection.Selection;

public abstract class FloatSelectionAction extends ElementCaseAction {

	protected FloatSelectionAction(ElementCasePane t) {
		super(t);
	}
	
	@Override
	public boolean executeActionReturnUndoRecordNeeded() {
		ElementCasePane ePane = this.getEditingComponent();
		Selection s = ePane.getSelection();
		
		// TODO ALEX_SEP instanceof i hate it
		if (s instanceof FloatSelection) {
			return executeActionReturnUndoRecordNeededWithFloatSelection((FloatSelection)s);
		}
		
		return false;
	}
	
	protected abstract boolean executeActionReturnUndoRecordNeededWithFloatSelection(FloatSelection fs);
	
	@Override
	public void update() {
		super.update();
		
		if (this.isEnabled()) {
			this.setEnabled(this.getEditingComponent().getSelection() instanceof FloatSelection);
		}
	}

}