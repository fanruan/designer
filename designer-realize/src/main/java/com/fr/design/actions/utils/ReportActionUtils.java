package com.fr.design.actions.utils;

import com.fr.base.Style;
import com.fr.design.actions.cell.style.StyleActionInterface;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.FloatSelection;
import com.fr.grid.selection.Selection;
import com.fr.report.cell.CellElement;
import com.fr.report.cell.DefaultTemplateCellElement;
import com.fr.report.cell.FloatElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.elementcase.ElementCase;
import com.fr.report.elementcase.TemplateElementCase;

import java.awt.*;

public class ReportActionUtils {
	private ReportActionUtils() {
	}


	public static interface IterAction {
		public void dealWith(CellElement editCellElement);
	}

	public static boolean executeAction(
			final StyleActionInterface styleActionInterface, ElementCasePane reportPane) {

		Selection sel = reportPane.getSelection();
		if (sel instanceof FloatSelection) {
			ElementCase report = reportPane.getEditingElementCase();
			FloatElement selectedFloatElement =
					report.getFloatElement(((FloatSelection) sel).getSelectedFloatName());

			Style selectedStyle = selectedFloatElement.getStyle();
			selectedFloatElement.setStyle(
					styleActionInterface.executeStyle(selectedStyle, selectedStyle));
		} else {
			TemplateElementCase report = reportPane.getEditingElementCase();
			TemplateCellElement editCellElement = report.getTemplateCellElement(((CellSelection) sel).getColumn(), ((CellSelection) sel).getRow());
			if (editCellElement == null) {
				editCellElement = new DefaultTemplateCellElement(((CellSelection) sel).getColumn(), ((CellSelection) sel).getRow());
				report.addCellElement(editCellElement);
			}

			final Style selectedStyle = editCellElement.getStyle();

			actionIterateWithCellSelection((CellSelection) sel, report, new IterAction() {
				@Override
				public void dealWith(CellElement editCellElement) {
					Style style2Mod = editCellElement.getStyle();
					editCellElement.setStyle(
							styleActionInterface.executeStyle(style2Mod, selectedStyle));

				}
			});
		}

		return true;
	}

	public static void actionIterateWithCellSelection(
			CellSelection gridSelection, TemplateElementCase report, IterAction action) {
		//需要先行后列地增加新元素。
		// 从最后循环起以保证最后一个修改标准单元格（originalStyle）。
		int cellRectangleCount = gridSelection.getCellRectangleCount();
		for (int rec = 0; rec < cellRectangleCount; rec++) {
			Rectangle cellRectangle = gridSelection.getCellRectangle(rec);
			// 从最后循环起以保证最后一个修改标准单元格（originalStyle）。
			for (int j = cellRectangle.height - 1; j >= 0; j--) {
				for (int i = cellRectangle.width - 1; i >= 0; i--) {
					int column = i + cellRectangle.x;
					int row = j + cellRectangle.y;

					TemplateCellElement editCellElement = report.getTemplateCellElement(column, row);
					if (editCellElement == null) {
						editCellElement = new DefaultTemplateCellElement(column, row);
						report.addCellElement(editCellElement);
					} else {
						// 对于合并的格子,我们不多次计算的Style.
						if (editCellElement.getColumn() != column
								|| editCellElement.getRow() != row) {
							continue;
						}
					}

					action.dealWith(editCellElement);
				}
			}
		}
	}

	/**
	 * peter:是只读的分析然后获得当前的Style.
	 *
	 * @param reportPane the current rpt pane.
	 * @return current style.
	 */
	public static Style getCurrentStyle(ElementCasePane reportPane) {
		//got simple cell element from row and column
		ElementCase report = reportPane.getEditingElementCase();

		Selection sel = reportPane.getSelection();
		if (sel instanceof FloatSelection) {
			FloatElement selectedFloatElement = report.getFloatElement(((FloatSelection) sel).getSelectedFloatName());
			return selectedFloatElement.getStyle();
		}

		//需要先行后列地增加新元素。
		//vivian: 如excel那样改变style.是状态的改变由第一个的状态决定，即如果只选择了第一个值那么就改为相反的值，
		// 如果是选择了多个值则首先变成和第一个值的一样

		CellElement editCellElement = report.getCellElement(((CellSelection) sel).getColumn(), ((CellSelection) sel).getRow());
		if (editCellElement == null) {
			return Style.DEFAULT_STYLE;
		}

		//peter:直接返回当前编辑元素的Style
		return editCellElement.getStyle();
	}
}