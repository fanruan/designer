package com.fr.design.mainframe;

import com.fr.design.designer.EditingState;
import com.fr.main.impl.WorkBook;

public class WorkBookUndoState extends BaseUndoState<JWorkBook> {
	private WorkBook wb;
	private int selectedReportIndex;
	private EditingState editingState;

	public WorkBookUndoState(JWorkBook t, int selectedReportIndex, EditingState selectedEditingState) {
		super(t);

		try {
			this.wb = (WorkBook) t.getTarget().clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
		this.selectedReportIndex = selectedReportIndex;
		this.editingState = selectedEditingState;
	}

	public WorkBook getWorkBook() {
		return wb;
	}

	public int getSelectedReportIndex() {
		return selectedReportIndex;
	}

	public EditingState getSelectedEditingState() {
		return editingState;
	}

	@Override
	public void applyState() {
		this.getApplyTarget().applyUndoState(this);
	}
}