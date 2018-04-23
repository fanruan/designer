package com.fr.grid;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import com.fr.base.DynamicUnitList;
import com.fr.base.ScreenResolution;
import com.fr.design.gui.imenu.UIPopupMenu;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.Selection;
import com.fr.report.ReportHelper;
import com.fr.report.elementcase.ElementCase;
import com.fr.stable.ColumnRow;

/**
 * peter:处理对GridColumn的Mouse事件.
 */
public class GridColumnMouseHandler extends AbstractGridHeaderMouseHandler {

	private int resolution;

	public GridColumnMouseHandler(GridColumn gridColumn) {
		super(gridColumn);
		this.resolution = gridColumn.resolution;
	}

	public void setResolution(int resolution){
		this.resolution = resolution;
	}

	@Override
	protected void resetSelectionByRightButton(ColumnRow selectedCellPoint, Selection cs, ElementCasePane ePane) {
		int[] selectedColumns = cs.getSelectedColumns();
		if (selectedColumns.length == 0
				|| selectedCellPoint.getColumn() < selectedColumns[0]
				|| selectedCellPoint.getColumn() > selectedColumns[selectedColumns.length - 1]) {
			resetGridSelectionBySelect(selectedCellPoint.getColumn(), ePane);
		}
	}


	protected int doChooseFrom() {
		return CellSelection.CHOOSE_COLUMN;
	}

	@Override
	protected Rectangle resetSelectedBoundsByShift(Rectangle editRectangle, ColumnRow selectedCellPoint, ElementCasePane reportPane) {
		int tempOldSelectedCellX = editRectangle.x;// editRectangle.x;

		// adjust them to got the correct selected bounds.
		if (selectedCellPoint.getColumn() >= editRectangle.x) {
			selectedCellPoint = ColumnRow.valueOf(selectedCellPoint.getColumn() + 1, selectedCellPoint.getRow());
		} else {
			tempOldSelectedCellX++;
		}

		int lastRow = GridUtils.getAdjustLastColumnRowOfReportPane(reportPane).getRow();
		return new Rectangle(Math.min(tempOldSelectedCellX, selectedCellPoint.getColumn()), 0, Math.max(editRectangle.width, Math.abs(tempOldSelectedCellX
				- selectedCellPoint.getColumn())), lastRow);
	}

	@Override
	protected int[] getGridSelectionIndices(CellSelection cs) {
		return cs.getSelectedColumns();
	}

	@Override
	protected int getScrollValue(ElementCasePane casePane) {
		return casePane.getGrid().getHorizontalValue();
	}

	@Override
	protected int getScrollExtent(ElementCasePane casePane) {
		return casePane.getGrid().getHorizontalExtent();
	}

	@Override
	protected int getBeginValue(ElementCasePane casePane) {
		return casePane.getGrid().getHorizontalBeginValue();
	}

	@Override
	protected int getColumnOrRowByGridHeader(ColumnRow selectedCellPoint) {
		return selectedCellPoint.getColumn();
	}


	/**
	 * Checks whether is on zero separator line.
	 *
	 */
	@Override
	protected boolean isOnSeparatorLineIncludeZero(MouseEvent evt, double tmpWidth2, double tmpIncreaseWidth) {
		return tmpIncreaseWidth <= 1 && (evt.getX() >= tmpWidth2 + 2 && (evt.getX() <= tmpWidth2 + SEPARATOR_GAP ));
	}

	@Override
	protected boolean between(MouseEvent evt, double from, double to) {
		return evt.getX() > from && evt.getX() <= to;
	}

	/**
	 * Checks whether is on normal separator line.
	 */
	@Override
	protected boolean isOnNormalSeparatorLine(MouseEvent evt, double tmpWidth2) {
		return (evt.getX() >= tmpWidth2 - 2) && (evt.getX() <= tmpWidth2 + 2);
	}

	@Override
	protected int evtOffset(MouseEvent evt, int offset) {
		return evt.getX() - offset;
	}

	@Override
	protected DynamicUnitList getSizeList(ElementCase elementCase) {
		return ReportHelper.getColumnWidthList(elementCase);
	}

	@Override
	protected String methodName() {
		return "setColumnWidth";
	}

	@Override
	protected String getSelectedHeaderTooltip(int selectedColumnCount) {
		return selectedColumnCount + "C";
	}

	@Override
	protected Point getTipLocationByMouseEvent(MouseEvent evt, GridHeader gHeader, Dimension tipPreferredSize) {
		Point convertPoint = new Point(evt.getX(), 0);
		SwingUtilities.convertPointToScreen(convertPoint, gHeader);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		convertPoint.x = Math.max(0, Math.min(convertPoint.x - tipPreferredSize.width / 2, screenSize.width - tipPreferredSize.width));
		convertPoint.y = convertPoint.y - tipPreferredSize.height - 2;

		return convertPoint;
	}

	@Override
	protected void resetGridSelectionBySelect(int column, ElementCasePane ePane) {
		int lastRow = GridUtils.getAdjustLastColumnRowOfReportPane(ePane).getRow();
		CellSelection cellSelection = new CellSelection(column, 0, 1, lastRow);
		cellSelection.setSelectedType(CellSelection.CHOOSE_COLUMN);
		ePane.setSelection(cellSelection);
	}

	@Override
	protected String nameOfMoveCursorGIF() {
		return "cursor_hmove";
	}

	@Override
	protected String nameOfSelectCursorGIF() {
		return "cursor_hselect";
	}

	@Override
	protected String nameOfSplitCursorGIF() {
		return "cursor_hsplit";
	}

	@Override
	protected UIPopupMenu createPopupMenu(ElementCasePane reportPane,
										  MouseEvent evt, int columnIndex) {
		return reportPane.createColumnPopupMenu(evt, columnIndex);
	}

	@Override
	protected void resetGridSelectionByDrag(CellSelection gridSelection, ElementCasePane reportPane,
											int startMultiSelectIndex, int endMultiSelectIndex) {
		int lastRow = GridUtils.getAdjustLastColumnRowOfReportPane(reportPane).getRow();
		gridSelection.setLastRectangleBounds(Math.min(endMultiSelectIndex, startMultiSelectIndex), 0, Math.abs(startMultiSelectIndex - endMultiSelectIndex) + 1, lastRow);
	}
}