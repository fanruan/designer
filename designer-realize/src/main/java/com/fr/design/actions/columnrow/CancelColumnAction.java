package com.fr.design.actions.columnrow;

import com.fr.page.ReportPageAttrProvider;
import com.fr.design.actions.CellSelectionAction;

import com.fr.design.mainframe.ElementCasePane;
import com.fr.grid.selection.CellSelection;
import com.fr.report.elementcase.ElementCase;

public class CancelColumnAction extends CellSelectionAction{
	public CancelColumnAction(ElementCasePane t) {
		super(t);
		
		this.setName(com.fr.design.i18n.Toolkit.i18nText("Cancel_Repeat_Attributes"));
	}
	
	@Override
	protected boolean executeActionReturnUndoRecordNeededWithCellSelection(
			CellSelection cs) {
		ElementCasePane ePane = this.getEditingComponent();
		ElementCase report = ePane.getEditingElementCase();
		if (report.getReportPageAttr() == null) {
			return false;
		}
		int[] columns = cs.getSelectedColumns();
		ReportPageAttrProvider pageAttr = report.getReportPageAttr();
		int i = columns[0];
		if (i == pageAttr.getRepeatHeaderColumnFrom() || i == pageAttr.getRepeatHeaderColumnTo()) {
			pageAttr.setRepeatHeaderColumnFrom(-1);
			pageAttr.setRepeatHeaderColumnTo(-1);
		}
		if (i == pageAttr.getRepeatFooterColumnFrom() || i == pageAttr.getRepeatFooterColumnTo()) {
			pageAttr.setRepeatFooterColumnFrom(-1);
			pageAttr.setRepeatFooterColumnTo(-1);
		}
		return true;
	}
}