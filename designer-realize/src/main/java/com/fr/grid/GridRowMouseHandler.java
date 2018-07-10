package com.fr.grid;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import com.fr.base.DynamicUnitList;
import com.fr.design.gui.imenu.UIPopupMenu;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.Selection;
import com.fr.report.ReportHelper;
import com.fr.report.elementcase.ElementCase;
import com.fr.stable.ColumnRow;

/**
 * peter:处理对GridRow的Mouse事件.
 */
public class GridRowMouseHandler extends AbstractGridHeaderMouseHandler {

	public GridRowMouseHandler(GridRow gridRow) {
		super(gridRow);
	}

	@Override
	protected void resetSelectionByRightButton(ColumnRow selectedCellPoint, Selection cs, ElementCasePane ePane) {
		int[] selectedRows = cs.getSelectedRows();
		if (selectedRows.length == 0
				|| selectedCellPoint.getRow() < selectedRows[0]
				|| selectedCellPoint.getRow() > selectedRows[selectedRows.length - 1]) {
			resetGridSelectionBySelect(selectedCellPoint.getRow(), ePane);
		}
	}


	protected int doChooseFrom() {
		return CellSelection.CHOOSE_ROW;
	}

	@Override
	protected int getScrollValue(ElementCasePane casePane) {
		return casePane.getGrid().getVerticalValue();
	}

	@Override
	protected int getScrollExtent(ElementCasePane casePane) {
		return casePane.getGrid().getVerticalExtent();
	}

	@Override
	protected int getBeginValue(ElementCasePane casePane) {
		return casePane.getGrid().getVerticalBeginValue();
	}

	@Override
	protected Rectangle resetSelectedBoundsByShift(Rectangle editRectangle, ColumnRow selectedCellPoint, ElementCasePane reportPane) {
		int tempOldSelectedCellY = editRectangle.y;// editRectangle.x;

		// ajust them to got the correct selected bounds.
		if (selectedCellPoint.getRow() >= editRectangle.y) {
			selectedCellPoint = ColumnRow.valueOf(selectedCellPoint.getColumn(), selectedCellPoint.getRow() + 1);
		} else {
			tempOldSelectedCellY++;
		}

		int lastColumn = GridUtils.getAdjustLastColumnRowOfReportPane(reportPane).getColumn();
		return new Rectangle(0, Math.min(tempOldSelectedCellY, selectedCellPoint.getRow()),
				lastColumn, Math.max(editRectangle.height, Math.abs(tempOldSelectedCellY - selectedCellPoint.getRow())));
	}

	@Override
	protected int[] getGridSelectionIndices(CellSelection cs) {
		return cs.getSelectedRows();
	}

	@Override
	protected int getColumnOrRowByGridHeader(ColumnRow selectedCellPoint) {
		return selectedCellPoint.getRow();
	}


	@Override
	protected void resetGridSelectionBySelect(int row, ElementCasePane ePane) {
		int lastColumn = GridUtils.getAdjustLastColumnRowOfReportPane(ePane).getColumn();
		CellSelection cellSelection = new CellSelection(0, row, lastColumn, 1);
		cellSelection.setSelectedType(CellSelection.CHOOSE_ROW);
		ePane.setSelection(cellSelection);
	}

	/**
	 * Checks whether is on zero separator line.
	 */
	@Override
	protected boolean isOnSeparatorLineIncludeZero(MouseEvent evt, double tmpHeight2, double tmpIncreaseHeight) {
		return tmpIncreaseHeight <= 1 && (evt.getY() >= tmpHeight2 + 2 && evt.getY() <= tmpHeight2 + SEPARATOR_GAP);
	}

	@Override
	protected boolean between(MouseEvent evt, double from, double to) {
		return evt.getY() > from && evt.getY() <= to;
	}

	@Override
	protected DynamicUnitList getSizeList(ElementCase elementCase) {
		return ReportHelper.getRowHeightList(elementCase);
	}

	@Override
	protected String methodName() {
		return "setRowHeight";
	}

	/**
	 * Checks whether is on normal separator line.
	 */
	@Override
	protected boolean isOnNormalSeparatorLine(MouseEvent evt, double tmpHeight2) {
		return (evt.getY() >= tmpHeight2 - 2) && (evt.getY() <= tmpHeight2 + 2);
	}

	@Override
	protected int evtOffset(MouseEvent evt, int offset) {
		return evt.getY() - offset;
	}

	@Override
	protected String getSelectedHeaderTooltip(int rowSelectedCount) {
		return rowSelectedCount + "R";
	}

	@Override
	protected Point getTipLocationByMouseEvent(MouseEvent evt, GridHeader gHeader, Dimension tipPreferredSize) {
		Point convertPoint = new Point(0, evt.getY());
		SwingUtilities.convertPointToScreen(convertPoint, gHeader);

		convertPoint.x = convertPoint.x + gHeader.getSize().width + 2;
		convertPoint.y = convertPoint.y - tipPreferredSize.height / 2;

		return convertPoint;
	}

	@Override
	protected String nameOfMoveCursorGIF() {
		return "cursor_vmove";
	}

	@Override
	protected String nameOfSelectCursorGIF() {
		return "cursor_vselect";
	}

	@Override
	protected String nameOfSplitCursorGIF() {
		return "cursor_vsplit";
	}

	@Override
	protected UIPopupMenu createPopupMenu(ElementCasePane reportPane,
										  MouseEvent evt, int rowIndex) {
		return reportPane.createRowPopupMenu(evt, rowIndex);
	}

	@Override
	protected void resetGridSelectionByDrag(CellSelection gridSelection, ElementCasePane reportPane,
											int startMultiSelectIndex, int endMultiSelectIndex) {
		int lastColumn = GridUtils.getAdjustLastColumnRowOfReportPane(reportPane).getColumn();
		gridSelection.setLastRectangleBounds(0, Math.min(endMultiSelectIndex, startMultiSelectIndex), lastColumn, Math.abs(startMultiSelectIndex - endMultiSelectIndex) + 1);

	}
}