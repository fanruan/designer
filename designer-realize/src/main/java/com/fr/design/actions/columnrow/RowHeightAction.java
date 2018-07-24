/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.columnrow;


import com.fr.design.mainframe.ElementCasePane;
import com.fr.grid.selection.CellSelection;
import com.fr.report.elementcase.ElementCase;
import com.fr.stable.unit.UNIT;

/**
 * Row height
 */
public class RowHeightAction extends ColumnRowSizingAction {

	public RowHeightAction(ElementCasePane t, int indexOfColumnOrRow) {
		super(t, indexOfColumnOrRow);
		
        this.setName(com.fr.design.i18n.Toolkit.i18nText("Utils-Row_Height") + "...");
        this.setMnemonic('R');
    }
	
	@Override
	protected String title4UnitInputPane() {
		return com.fr.design.i18n.Toolkit.i18nText("Utils-Row_Height");
	}
    
    @Override
	protected void updateAction(ElementCase report, UNIT len, CellSelection cs) {
    	int[] rows = cs.getSelectedRows();
    	for (int i = 0; i < rows.length; i++) {
            report.setRowHeight(rows[i], len);
        }
    }
    
    @Override
	protected UNIT getShowLen(ElementCase report, CellSelection cs) {
        int[] rows = cs.getSelectedRows();
    	return getSelectedCellsLen(rows, report);
    }

    protected UNIT getIndexLen(int index, ElementCase report){
        return report.getRowHeight(index);
    }

}