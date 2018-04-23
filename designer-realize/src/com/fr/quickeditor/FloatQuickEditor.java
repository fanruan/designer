package com.fr.quickeditor;

import com.fr.design.mainframe.ElementCasePane;
import com.fr.grid.selection.FloatSelection;
import com.fr.report.cell.FloatElement;
import com.fr.design.selection.QuickEditor;

/**
 * 
 * @author zhou
 * @since 2012-7-23下午5:17:23
 */
public abstract class FloatQuickEditor extends QuickEditor<ElementCasePane> {
	protected FloatElement floatElement;

	@Override
	protected void refresh() {
		FloatSelection fs = (FloatSelection)tc.getSelection();
		floatElement = tc.getEditingElementCase().getFloatElement(fs.getSelectedFloatName());
		refreshDetails();
	}

	protected abstract void refreshDetails();
}