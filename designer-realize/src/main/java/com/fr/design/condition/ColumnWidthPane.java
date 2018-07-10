package com.fr.design.condition;

import com.fr.general.Inter;
import com.fr.report.cell.cellattr.highlight.ColWidthHighlightAction;
import com.fr.report.cell.cellattr.highlight.HighlightAction;
import com.fr.stable.unit.UNIT;

/**
* @author richie
* @date 2015-03-26
* @since 8.0
*/
public class ColumnWidthPane extends WHPane {

    public ColumnWidthPane(ConditionAttributesPane conditionAttributesPane) {
        super(conditionAttributesPane, Inter.getLocText("Utils-Column_Width"));
    }

    @Override
    protected UNIT getUnit(HighlightAction ha) {
        return ((ColWidthHighlightAction) ha).getColumnWidth();
    }

    @Override
    protected HighlightAction returnAction(UNIT unit) {
        return new ColWidthHighlightAction(unit);
    }
}