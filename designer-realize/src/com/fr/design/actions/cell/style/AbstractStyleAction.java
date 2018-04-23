/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.cell.style;

import com.fr.base.Style;
import com.fr.base.chart.BaseChartCollection;
import com.fr.design.actions.ElementCaseAction;
import com.fr.design.actions.utils.ReportActionUtils;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.FloatSelection;
import com.fr.grid.selection.Selection;
import com.fr.report.cell.FloatElement;
import com.fr.report.elementcase.TemplateElementCase;

/**
 * Abstract cellstyle.
 */
public abstract class AbstractStyleAction extends ElementCaseAction implements StyleActionInterface {
    protected AbstractStyleAction(ElementCasePane t) {
        super(t);
    }

    @Override
    public abstract Style executeStyle(Style style, Style defStyle);

    @Override
    public boolean executeActionReturnUndoRecordNeeded() {
        ElementCasePane reportPane = this.getEditingComponent();
        if (reportPane == null) {
            return false;
        }

        return ReportActionUtils.executeAction(this, reportPane);
    }

    /**
     * Update Style.
     *
     * @param style the updated style.
     */
    @Override
    public void updateStyle(Style style) {
        // peter:这个方法默认情况下什么都不做.
    }

    public boolean isFontStye() {
        return true;
    }

    @Override
    public void update() {
        // james:应该从Action中的ReportPane拿Style，而不是从reportFrame中拿
        this.setEnabled(true);
        ElementCasePane reportPane = this.getEditingComponent();
        if (reportPane == null) {
            this.setEnabled(false);
            return;
        }
        if (isFontStye()) {
            Selection cs = reportPane.getSelection();
            TemplateElementCase tplEC = reportPane.getEditingElementCase();

            if (tplEC != null && cs instanceof FloatSelection) {
                FloatElement selectedFloat = tplEC.getFloatElement(((FloatSelection) cs).getSelectedFloatName());
                if (selectedFloat == null) {
                    //有情况是撤销worksheet里面的元素撤消了，但是reportpane里面的selection没有变
                    reportPane.setSelection(new CellSelection());
                    return;
                }
                Object value = selectedFloat.getValue();
                if (value instanceof BaseChartCollection) {
                    this.setEnabled(false);
                    return;
                }
            }
        }
        // 更新Style
        this.updateStyle(ReportActionUtils.getCurrentStyle(reportPane));
    }
}