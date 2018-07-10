/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.insert.flot;

import com.fr.design.actions.ElementCaseAction;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.report.cell.FloatElement;

/**
 * Abstract shape.
 */
public abstract class AbstractShapeAction extends ElementCaseAction {
    protected AbstractShapeAction(ElementCasePane t) {
        super(t);
    }

    public void startDraw(FloatElement floatElement) {
        ElementCasePane jws = (ElementCasePane) HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().getCurrentElementCasePane();
        if (jws == null) {
            return;
        }
        jws.getGrid().setDrawingFloatElement(floatElement);
    }

    @Override
    public boolean executeActionReturnUndoRecordNeeded() {
        return false;
    }
}