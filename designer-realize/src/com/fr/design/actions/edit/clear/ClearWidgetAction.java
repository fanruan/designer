package com.fr.design.actions.edit.clear;

import com.fr.general.Inter;
import com.fr.design.mainframe.ElementCasePane;

//:jackie 用于清除控件
public class ClearWidgetAction extends ClearAction {

    /**
     * Constructor
     */
	public ClearWidgetAction(ElementCasePane t) {
		super(t);
		
        this.setName(Inter.getLocText("Widget"));
        this.setMnemonic('W');
    }

    @Override
    public boolean executeActionReturnUndoRecordNeeded() {
        ElementCasePane reportPane = getEditingComponent();
        if (reportPane == null) {
            return false;
        }
        return reportPane.clearWidget();
    }
}