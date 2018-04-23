package com.fr.grid;

import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputAdapter;

import com.fr.design.constants.UIConstants;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.grid.selection.CellSelection;
import com.fr.stable.ColumnRow;

/**
 * the MouseListener of GridCorner
 * 
 * @editor zhou
 * @since 2012-3-22下午4:04:08
 */
public class GridCornerMouseHandler extends MouseInputAdapter {
	GridCorner gridCorner;

	public GridCornerMouseHandler(GridCorner gridCorner) {
		this.gridCorner = gridCorner;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		doSelectAll();
	}

	private void doSelectAll() {
		ElementCasePane reportPane = this.gridCorner.getElementCasePane();
		ColumnRow lastColumnRow = GridUtils.getAdjustLastColumnRowOfReportPane(reportPane);

		reportPane.setSelection(new CellSelection(0, 0, lastColumnRow.getColumn(), lastColumnRow.getRow()));

		GridColumn gridColumn = reportPane.getGridColumn();
		GridColumnMouseHandler gridColumnMouseHandler = new GridColumnMouseHandler(gridColumn);
		gridColumnMouseHandler.setStartMultiSelectIndex(0);
		gridColumnMouseHandler.setEndMultiSelectIndex(lastColumnRow.getColumn());
		gridColumn.addMouseListener(gridColumnMouseHandler);
		gridColumn.addMouseMotionListener(gridColumnMouseHandler);

		GridRow gridRow = reportPane.getGridRow();
		GridRowMouseHandler gridRowMouseHandler = new GridRowMouseHandler(gridRow);
		gridRowMouseHandler.setStartMultiSelectIndex(0);
		gridRowMouseHandler.setEndMultiSelectIndex(lastColumnRow.getRow());
		gridRow.addMouseListener(gridRowMouseHandler);
		gridRow.addMouseMotionListener(gridRowMouseHandler);
		reportPane.getHorizontalScrollBar().setValue(reportPane.getHorizontalScrollBar().getValue());
		reportPane.getVerticalScrollBar().setValue(reportPane.getVerticalScrollBar().getValue());
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		gridCorner.setCursor(UIConstants.CELL_DEFAULT_CURSOR);
	}
}