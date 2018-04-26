package com.fr.design.actions;

import java.awt.event.ActionEvent;

public abstract class UndoableAction extends UpdateAction {
    @Override
    public void actionPerformed(ActionEvent evt) {
    	actionPerformedUndoable();
    }
    
    protected void actionPerformedUndoable() {
        boolean executeValue = this.executeActionReturnUndoRecordNeeded();
        if (executeValue) {
        	prepare4Undo();
        }
    }

    public abstract boolean executeActionReturnUndoRecordNeeded();
    
    public abstract void prepare4Undo();
}