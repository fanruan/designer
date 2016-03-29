package com.fr.design.condition;

import com.fr.design.gui.ispinner.UIBasicSpinner;
import com.fr.general.Inter;
import com.fr.report.cell.cellattr.highlight.HighlightAction;
import com.fr.report.cell.cellattr.highlight.RowHeightHighlightAction;
import com.fr.stable.unit.UNIT;

/**
* @author richie
* @date 2015-03-26
* @since 8.0
*/
public class RowHeightPane extends WHPane {
    private UIBasicSpinner rowHeightSpinner;

    public RowHeightPane(ConditionAttributesPane conditionAttributesPane) {
        super(conditionAttributesPane, Inter.getLocText("Utils-Row_Height"));
    }

    @Override
    protected UNIT getUnit(HighlightAction ha) {
        return ((RowHeightHighlightAction) ha).getRowHeight();
    }

    @Override
    protected HighlightAction returnAction(UNIT unit) {
        return new RowHeightHighlightAction(unit);
    }
}