/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.columnrow;

import com.fr.design.mainframe.ElementCasePane;
import com.fr.general.Inter;
import com.fr.grid.selection.CellSelection;
import com.fr.report.elementcase.ElementCase;
import com.fr.stable.unit.UNIT;

/**
 * Column width.
 */
public class ColumnWidthAction extends ColumnRowSizingAction {

	public ColumnWidthAction(ElementCasePane t, int indexOfColumnOrRow) {
		super(t, indexOfColumnOrRow);
		
        this.setName(Inter.getLocText("Utils-Column_Width") + "...");
        this.setMnemonic('C');
    }
	
	@Override
	protected String title4UnitInputPane() {
		return Inter.getLocText("Utils-Column_Width");
	}
    
    @Override
	protected void updateAction(ElementCase report, UNIT len, CellSelection cs) {
    	int[] columns = cs.getSelectedColumns();
    	for (int i = 0; i < columns.length; i++) {
            report.setColumnWidth(columns[i], len);
        }
    }

    protected UNIT getIndexLen(int index, ElementCase report){
        return report.getColumnWidth(index);
    }

    @Override
	protected UNIT getShowLen(ElementCase report, CellSelection cs) {
        int[] columns = cs.getSelectedColumns();
        return getSelectedCellsLen(columns, report);
    }
}