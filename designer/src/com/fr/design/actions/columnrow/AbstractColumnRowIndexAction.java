package com.fr.design.actions.columnrow;

import com.fr.design.actions.CellSelectionAction;
import com.fr.design.mainframe.ElementCasePane;

public abstract class AbstractColumnRowIndexAction extends CellSelectionAction {
	private int index = 0;
	
	protected AbstractColumnRowIndexAction(ElementCasePane t, int indexOfColumnOrRow) {
		super(t);
		this.index = indexOfColumnOrRow;
	}

	/**
	 * Return index.
	 */
	public int getIndex() {
		return this.index;
	}
}