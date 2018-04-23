package com.fr.design.actions.columnrow;

import com.fr.page.ReportPageAttrProvider;
import com.fr.design.actions.CellSelectionAction;
import com.fr.general.Inter;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.grid.selection.CellSelection;
import com.fr.report.elementcase.ElementCase;

public class CancelRowAction extends CellSelectionAction {

	public CancelRowAction(ElementCasePane t) {
		super(t);
		
        this.setName(Inter.getLocText("Cancel_Repeat_Attributes"));
    }
	
	@Override
	protected boolean executeActionReturnUndoRecordNeededWithCellSelection(
			CellSelection cs) {
        ElementCasePane ePane = getEditingComponent();
        ElementCase report = ePane.getEditingElementCase();
        
        int[] rows = cs.getSelectedRows();
        if (report.getReportPageAttr() == null) {
            return false;
        }
        ReportPageAttrProvider pageAttr = report.getReportPageAttr();
        int i = rows[0];
        if (i == pageAttr.getRepeatHeaderRowFrom() || i == pageAttr.getRepeatHeaderRowTo()) {
            pageAttr.setRepeatHeaderRowFrom(-1);
            pageAttr.setRepeatHeaderRowTo(-1);
        }
        if (i == pageAttr.getRepeatFooterRowFrom() || i == pageAttr.getRepeatFooterRowTo()) {
            pageAttr.setRepeatFooterRowFrom(-1);
            pageAttr.setRepeatFooterRowTo(-1);
        }
        return true;
    }
}