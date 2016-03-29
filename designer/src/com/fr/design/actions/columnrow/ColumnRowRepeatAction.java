package com.fr.design.actions.columnrow;

import com.fr.page.ReportPageAttrProvider;
import com.fr.design.actions.CellSelectionAction;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.grid.selection.CellSelection;
import com.fr.report.elementcase.ElementCase;
import com.fr.stable.bridge.StableFactory;

public abstract class ColumnRowRepeatAction extends CellSelectionAction {
	
	public ColumnRowRepeatAction(ElementCasePane t) {
		super(t);
	}
	
	@Override
	protected boolean executeActionReturnUndoRecordNeededWithCellSelection(
			CellSelection cs) {
		ElementCasePane ePane = this.getEditingComponent();
		ElementCase report = ePane.getEditingElementCase();
		
		int[] indices;
		if (isColumn()) {
			indices = cs.getSelectedColumns();
		} else {
			indices = cs.getSelectedRows();
		}
		
		doReportPageAttrSet(isColumn(), isFoot(), report, indices[0], indices[indices.length - 1]);
		
		return true;
	}
	
	protected abstract boolean isColumn();
	protected abstract boolean isFoot();

	private void doReportPageAttrSet(boolean isColumn, boolean isFoot, ElementCase elementCase, int from, int to) {
		ReportPageAttrProvider reportPageAttr = elementCase.getReportPageAttr();
		if (reportPageAttr == null) {
			reportPageAttr = (ReportPageAttrProvider) StableFactory.createXmlObject(ReportPageAttrProvider.XML_TAG);
			elementCase.setReportPageAttr(reportPageAttr);
		}

		reportPageAttr.setRepeatFromTo(isFoot, isColumn, from, to);
	}
	
}