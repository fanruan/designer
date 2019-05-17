package com.fr.grid;

import com.fr.base.DynamicUnitList;
import com.fr.base.ScreenResolution;
import com.fr.design.cell.clipboard.CellElementsClip;
import com.fr.design.cell.clipboard.ElementsTransferable;
import com.fr.design.cell.clipboard.FloatElementsClip;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.mainframe.JSliderPane;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.ComparatorUtils;
import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.FloatSelection;
import com.fr.grid.selection.Selection;
import com.fr.poly.creator.ECBlockPane;
import com.fr.report.ReportHelper;
import com.fr.report.cell.CellElement;
import com.fr.report.cell.FloatElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.cell.cellattr.CellGUIAttr;
import com.fr.report.core.PaintUtils;
import com.fr.report.elementcase.ElementCase;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.report.stable.ReportConstants;
import com.fr.stable.ColumnRow;
import com.fr.stable.unit.FU;
import com.fr.stable.unit.PT;
import com.fr.stable.unit.UNIT;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;

/**
 * Some util method of GUI.
 */
public class GridUtils {

    private GridUtils() {
    }

    //peter:没有Drag
    public final static int DRAG_NONE = 0;
    //peter:Drag CellSelection的边框来移动元素.
    public final static int DRAG_CELLSELECTION = 1;
    //peter:Drag CellSelection的右下角落来复制元素
    public final static int DRAG_CELLSELECTION_BOTTOMRIGHT_CORNER = 2;
    public final static int DRAG_FLOAT = 3;

    //peter:下面这几个量是在Drag列的时候用.
    public final static int DRAG_CELL_SIZE = 1; //peter:drag的时候改变格子的宽度.
    public final static int DRAG_SELECT_UNITS = 2; //peter:drag的时候,选中单元格.//    public static int resolution = (int) (ScreenResolution.getScreenResolution()* JSliderPane.getInstance().resolutionTimes);

    /**
     * Is above float element.(the return may be null). <br>
     * The length of Object[] is 2, the first is FloatElement, the second is Cursor.<br>
     * The object[] is null
     */
    public static Object[] getAboveFloatElementCursor(ElementCasePane reportPane, double evtX, double evtY) {
        //peter: 将要返回的Objects
        Object[] returnObject = null;
        ElementCase report = reportPane.getEditingElementCase();
        Selection sel = reportPane.getSelection();
        //peter:检查所有的悬浮元素.
        Iterator flotIt = report.floatIterator();
        while (flotIt.hasNext()) {
            FloatElement tmpFloatElement = (FloatElement) flotIt.next();
            int resolution = reportPane.getGrid().getResolution();
            //peter:计算悬浮元素的四个角落的位置.
            double[] floatArray = caculateFloatElementLocations_withresolution(tmpFloatElement, ReportHelper.getColumnWidthList(report),
                    ReportHelper.getRowHeightList(report), reportPane.getGrid().getVerticalValue(), reportPane.getGrid().getHorizontalValue(), resolution);

            //peter:悬浮元素的范围.
            Rectangle2D floatElementRect = new Rectangle2D.Double(floatArray[0], floatArray[1], tmpFloatElement.getWidth().toPixD(resolution), tmpFloatElement.getHeight().toPixD(resolution));
            //peter:不是当前选中的悬浮元素,不支持六个改变大小的点.
            if (!(sel instanceof FloatSelection && ComparatorUtils.equals(tmpFloatElement.getName(), ((FloatSelection) sel).getSelectedFloatName()))) {
                if (floatElementRect.contains(evtX, evtY)) {
                    returnObject = new Object[]{tmpFloatElement, new Cursor(Cursor.MOVE_CURSOR)};
                }

                //peter:还要继续查找,如果当前的鼠标在选中的悬浮元素的六个可以移动的点上,优先.
                continue;
            }
            Cursor cursor = null;
            Rectangle2D[] cornerRect = getCornerRect(floatArray);
            //peter:悬浮元素对应的六种鼠标.
            int[] cursorType = {Cursor.NW_RESIZE_CURSOR, Cursor.N_RESIZE_CURSOR, Cursor.NE_RESIZE_CURSOR, Cursor.E_RESIZE_CURSOR, Cursor.SE_RESIZE_CURSOR,
                    Cursor.S_RESIZE_CURSOR, Cursor.SW_RESIZE_CURSOR, Cursor.W_RESIZE_CURSOR};
            for (int c = 0; c < cornerRect.length; c++) {
                if (cornerRect[c].contains(evtX, evtY)) {
                    cursor = new Cursor(cursorType[c]);
                    break;
                }
            }
            //peter:在悬浮元素区域内部,但是不在那六个点上,是移动的鼠标
            if (floatElementRect.contains(evtX, evtY) && cursor == null) {
                returnObject = new Object[]{tmpFloatElement, new Cursor(Cursor.MOVE_CURSOR)};
            }
            //peter:在当前选中元素的六个点上,最高优先级别,直接返回.
            if (cursor != null) {// select
                return new Object[]{tmpFloatElement, cursor};
            }
        }
        return returnObject;
    }

    //peter:悬浮元素的六个可移动的小矩形点.
    //marks:当选择的悬浮元素多余一个时候，我们应该不返回悬浮元素的光标
    private static Rectangle2D[] getCornerRect(double[] floatArray) {
        double floatX1 = floatArray[0];
        double floatY1 = floatArray[1];
        double floatX2 = floatArray[2];
        double floatY2 = floatArray[3];
        Rectangle2D[] cornerRect = {new Rectangle2D.Double(floatX1 - 3, floatY1 - 3, 6, 6),
                new Rectangle2D.Double((floatX1 + floatX2) / 2 - 3, floatY1 - 3, 6, 6), new Rectangle2D.Double(floatX2 - 3, floatY1 - 3, 6, 6),
                new Rectangle2D.Double(floatX2 - 3, (floatY1 + floatY2) / 2 - 3, 6, 6), new Rectangle2D.Double(floatX2 - 3, floatY2 - 3, 6, 6),
                new Rectangle2D.Double((floatX1 + floatX2) / 2 - 3, floatY2 - 3, 6, 6), new Rectangle2D.Double(floatX1 - 3, floatY2 - 3, 6, 6),
                new Rectangle2D.Double(floatX1 - 3, (floatY1 + floatY2) / 2 - 3, 6, 6)};
        return cornerRect;
    }


    /**
     * Gets float element locations. Returns[] {flaotX1, floatY1, floatX2, floatY2}.
     */
    public static double[] caculateFloatElementLocations(FloatElement floatElement, DynamicUnitList columnWidthList, DynamicUnitList rowHeightList,
                                                         int verticalValue, int horizentalValue) {
        int resolution = (int) (ScreenResolution.getScreenResolution() * JSliderPane.getInstance().resolutionTimes);

        double floatX = columnWidthList.getRangeValue(horizentalValue, 0).toPixD(resolution) + floatElement.getLeftDistance().toPixD(resolution);
        double floatY = rowHeightList.getRangeValue(verticalValue, 0).toPixD(resolution) + floatElement.getTopDistance().toPixD(resolution);

        double floatX2 = floatX + floatElement.getWidth().toPixD(resolution);
        double floatY2 = floatY + floatElement.getHeight().toPixD(resolution);

        return new double[]{floatX, floatY, floatX2, floatY2};
    }

    public static double[] caculateFloatElementLocations_withresolution(FloatElement floatElement, DynamicUnitList columnWidthList, DynamicUnitList rowHeightList,
                                                                        int verticalValue, int horizentalValue, int resolution) {

        double floatX = columnWidthList.getRangeValue(horizentalValue, 0).toPixD(resolution) + floatElement.getLeftDistance().toPixD(resolution);
        double floatY = rowHeightList.getRangeValue(verticalValue, 0).toPixD(resolution) + floatElement.getTopDistance().toPixD(resolution);

        double floatX2 = floatX + floatElement.getWidth().toPixD(resolution);
        double floatY2 = floatY + floatElement.getHeight().toPixD(resolution);

        return new double[]{floatX, floatY, floatX2, floatY2};
    }

    /**
     * Gets column and row which located on (evtX, evtY)
     * peter:这个方法重复考虑到Frozen的情况,没有任何BUG,这个方法没有考虑那种不无限增大的情况.
     *
     * @param reportPane 当前的ReportPane
     * @param evtX       event x
     * @param evtY       event y
     * @return the event located column and row.
     */
    public static ColumnRow getEventColumnRow(ElementCasePane reportPane, double evtX, double evtY) {
        ElementCase report = reportPane.getEditingElementCase();

        // Width and height list.
        DynamicUnitList rowHeightList = ReportHelper.getRowHeightList(report);
        DynamicUnitList columnWidthList = ReportHelper.getColumnWidthList(report);

        int verticalValue = reportPane.getGrid().getVerticalValue();
        int horizentalValue = reportPane.getGrid().getHorizontalValue();

        // denny: get verticalBeginValue and horizontalBeginValue;
        int verticalBeginValue = reportPane.getGrid().getVerticalBeginValue();
        int horizontalBeginValue = reportPane.getGrid().getHorizontalBeginValue();
        return ColumnRow.valueOf(
                cc_selected_column_or_row(evtX, horizontalBeginValue, horizentalValue, columnWidthList),
                cc_selected_column_or_row(evtY, verticalBeginValue, verticalValue, rowHeightList)
        );
    }

    private static int cc_selected_column_or_row(double mouseEvtPosition, int beginValue, int value, DynamicUnitList sizeList) {
        double tmpIntIndex = 0;
        int selectedCellIndex = 0;
        int resolution = (int) (ScreenResolution.getScreenResolution() * JSliderPane.getInstance().resolutionTimes);
        if (mouseEvtPosition < 0) {
            selectedCellIndex = value;
            for (; true; selectedCellIndex--) {
                if (tmpIntIndex < mouseEvtPosition) {
                    break;
                }
                tmpIntIndex -= sizeList.get(selectedCellIndex).toPixD(resolution);

            }
        } else {
            boolean isInnerFrozen = false;
            for (int i = beginValue; i < 0; i++) {
                tmpIntIndex += sizeList.get(i).toPixD(resolution);

                if (tmpIntIndex > mouseEvtPosition) {
                    selectedCellIndex = i;
                    isInnerFrozen = true;
                    break;
                }
            }

            if (!isInnerFrozen) {
                selectedCellIndex = value;
                for (; true; selectedCellIndex++) {
                    tmpIntIndex += sizeList.get(selectedCellIndex).toPixD(resolution);
                    if (tmpIntIndex > mouseEvtPosition) {
                        break;
                    }
                }
            }
        }

        return selectedCellIndex;
    }

    public static ColumnRow getEventColumnRow_withresolution(ElementCasePane reportPane, double evtX, double evtY, int resolution) {
        ElementCase report = reportPane.getEditingElementCase();

        // Width and height list.
        DynamicUnitList rowHeightList = ReportHelper.getRowHeightList(report);
        DynamicUnitList columnWidthList = ReportHelper.getColumnWidthList(report);

        int verticalValue = reportPane.getGrid().getVerticalValue();
        int horizentalValue = reportPane.getGrid().getHorizontalValue();

        // denny: get verticalBeginValue and horizontalBeginValue;
        int verticalBeginValue = reportPane.getGrid().getVerticalBeginValue();
        int horizontalBeginValue = reportPane.getGrid().getHorizontalBeginValue();
        return ColumnRow.valueOf(
                cc_selected_column_or_row_withresolution(evtX, horizontalBeginValue, horizentalValue, columnWidthList, resolution),
                cc_selected_column_or_row_withresolution(evtY, verticalBeginValue, verticalValue, rowHeightList, resolution)
        );
    }

    private static int cc_selected_column_or_row_withresolution(double mouseEvtPosition, int beginValue, int value, DynamicUnitList sizeList, int resolution) {
        double tmpIntIndex = 0;
        int selectedCellIndex = 0;
        if (mouseEvtPosition < 0) {
            selectedCellIndex = value;
            for (; true; selectedCellIndex--) {
                if (tmpIntIndex < mouseEvtPosition) {
                    break;
                }
                tmpIntIndex -= sizeList.get(selectedCellIndex).toPixD(resolution);

            }
        } else {
            boolean isInnerFrozen = false;
            for (int i = beginValue; i < 0; i++) {
                tmpIntIndex += sizeList.get(i).toPixD(resolution);

                if (tmpIntIndex > mouseEvtPosition) {
                    selectedCellIndex = i;
                    isInnerFrozen = true;
                    break;
                }
            }

            if (!isInnerFrozen) {
                selectedCellIndex = value;
                for (; true; selectedCellIndex++) {
                    tmpIntIndex += sizeList.get(selectedCellIndex).toPixD(resolution);
                    if (tmpIntIndex > mouseEvtPosition) {
                        break;
                    }
                }
            }
        }

        return selectedCellIndex;
    }

    /**
     * Gets column and row which located on (evtX, evtY)
     * peter:这个方法是调整过的Column,Row,不能小于0, 不能大于最大值,这个方法充分考虑了不无限增大的情况.
     * 一般经常用这个方法, 对应的getEventColumnRow(...)反而不常用.
     *
     * @param reportPane 当前的ReportPane
     * @param evtX       event x
     * @param evtY       event y
     * @return the event located column and row.
     */
    public static ColumnRow getAdjustEventColumnRow(ElementCasePane reportPane, double evtX, double evtY) {
        ColumnRow selectedCellPoint = GridUtils.getEventColumnRow(reportPane, evtX, evtY);

        int col = Math.max(selectedCellPoint.getColumn(), 0);
        int row = Math.max(selectedCellPoint.getRow(), 0);


        return ColumnRow.valueOf(col, row);
    }

    public static ColumnRow getAdjustEventColumnRow_withresolution(ElementCasePane reportPane, double evtX, double evtY, int resolution) {
        ColumnRow selectedCellPoint = GridUtils.getEventColumnRow_withresolution(reportPane, evtX, evtY, resolution);

        int col = Math.max(selectedCellPoint.getColumn(), 0);
        int row = Math.max(selectedCellPoint.getRow(), 0);


        return ColumnRow.valueOf(col, row);
    }

    /**
     * 是否可将当前单元格变为可见的格子
     */
    public static boolean canMove(ElementCasePane reportPane, int cellColumn, int cellRow) {
        if (reportPane.mustInVisibleRange()) {
            Grid grid = reportPane.getGrid();
            checkGridCount(reportPane);
            int verticalEndValue = grid.getVerticalValue() + grid.getVerticalExtent() - 1;
            int horizontalEndValue = grid.getHorizontalValue() + grid.getHorizontalExtent() - 1;
            if (cellColumn > horizontalEndValue) {
                return false;
            }
            if (cellRow > verticalEndValue) {
                return false;
            }
        }
        return true;
    }

    /**
     *聚合报表的报表块增加单元格的数量时 当单元格拉动一半会自动扩展成完整的单元格
     * 该单元格不被记录 check下重新计算下单元格数量
     */
    private static void checkGridCount(Object obj) {
        if (obj instanceof ECBlockPane) {
            ((ECBlockPane) obj).getTarget().firePropertyChange();
        }
    }

    /**
     * 选择一个Cell, 支持Merge.
     */
    public static void doSelectCell(ElementCasePane reportPane, int cellColumn, int cellRow) {
        ElementCase report = reportPane.getEditingElementCase();


        CellElement cellElement = report.getCellElement(cellColumn, cellRow);
        if (cellElement == null) {
            reportPane.setSelection(new CellSelection(cellColumn, cellRow, 1, 1));
        } else {
            reportPane.setSelection(new CellSelection(cellElement.getColumn(), cellElement.getRow(), cellElement.getColumnSpan(), cellElement.getRowSpan()));
        }
    }

    /**
     * peter: 从ReportPane选中的区域产生ElementsCopy
     */
    public static ElementsTransferable caculateElementsTransferable(ElementCasePane reportPane) {
        ElementsTransferable elementsTransferable = new ElementsTransferable();

        //p:获得当前的Report对象.
        ElementCase report = reportPane.getEditingElementCase();

        Selection sel = reportPane.getSelection();
        //p:先判断悬浮元素.
        if (sel instanceof FloatSelection) {
            FloatSelection fs = (FloatSelection) sel;
            //p:需要构建floatElementsClip.
            FloatElementsClip floatElementsClip = new FloatElementsClip(report.getFloatElement(fs.getSelectedFloatName()));

            elementsTransferable.addObject(floatElementsClip);
        } else {
            CellSelection cs = (CellSelection) sel;
            java.util.List<TemplateCellElement> elList = new java.util.ArrayList<TemplateCellElement>();
            //p:获得所有相交的CellElement.
            Rectangle selectionBounds = new Rectangle(cs.getColumn(), cs.getRow(), cs.getColumnSpan(), cs.getRowSpan());
            Iterator cells = report.intersect(cs.getColumn(), cs.getRow(), cs.getColumnSpan(), cs.getRowSpan());
            while (cells.hasNext()) {
                TemplateCellElement cellElement = (TemplateCellElement) cells.next();
                Rectangle tmpCellBound = new Rectangle(cellElement.getColumn(), cellElement.getRow(), cellElement.getColumnSpan(), cellElement.getRowSpan());
                //peter,相等或者包含
                if (GUICoreUtils.isTheSameRect(selectionBounds, tmpCellBound) || selectionBounds.contains(tmpCellBound)) {
                    //peter:添加到CellElementsClip
                    elList.add((TemplateCellElement) cellElement.deriveCellElement(cellElement.getColumn() - cs.getColumn(), cellElement.getRow() - cs.getRow()));
                }
            }

            elementsTransferable.addObject(new CellElementsClip(
                    cs.getColumnSpan(), cs.getRowSpan(), elList.toArray(new TemplateCellElement[elList.size()])
            ));
        }

        return elementsTransferable;
    }

    /**
     * james: Gets adjust last columrow of reportpane especially used in whole row/column selected
     * the area before last columnrow should contain all the cellelement of the reportpane
     */
    public static ColumnRow getAdjustLastColumnRowOfReportPane(ElementCasePane reportPane) {
        ElementCase report = reportPane.getEditingElementCase();

        //james：全选到最后一个有内容的格子
        return ColumnRow.valueOf(Math.max(1, report.getColumnCount()), Math.max(1, report.getRowCount()));
    }

    /**
     * 计算可见区域的行/列数
     */
    public static int getExtentValue(int start, DynamicUnitList sizeList, double visibleSize, int dpi) {
        double sumSize = 0;
        int maxIndex = Integer.MAX_VALUE;
        for (int i = start; i <= maxIndex; i++) {
            sumSize += sizeList.get(i).toPixD(dpi);

            if (sumSize > visibleSize) {
                start = i;

                // check zero value.
                for (int j = i; true; j++) {
                    if (sizeList.get(j).equal_zero()) {
                        start = j;
                    } else {
                        break;
                    }
                }
                break;
            }
        }
        return start;
    }

    public static void shrinkToFit(int reportShrinkMode, TemplateElementCase tplEC, TemplateCellElement editCellElement) {
        DynamicUnitList columnWidthList = ReportHelper.getColumnWidthList(tplEC);
        DynamicUnitList rowHeightList = ReportHelper.getRowHeightList(tplEC);

        CellGUIAttr cellGUIAttr = editCellElement.getCellGUIAttr();
        if (cellGUIAttr == null) {
            cellGUIAttr = new CellGUIAttr();
        }

        // carl:根据用户设置来调整行高或者列宽
        if (cellGUIAttr.getAdjustMode() == ReportConstants.AUTO_SHRINK_TO_FIT_HEIGHT
                || (cellGUIAttr.getAdjustMode() == ReportConstants.AUTO_SHRINK_TO_FIT_DEFAULT
                && reportShrinkMode == ReportConstants.AUTO_SHRINK_TO_FIT_HEIGHT)) {
            fitHetght(editCellElement, columnWidthList, rowHeightList);
        } else if (cellGUIAttr.getAdjustMode() == ReportConstants.AUTO_SHRINK_TO_FIT_WIDTH
                || (cellGUIAttr.getAdjustMode() == ReportConstants.AUTO_SHRINK_TO_FIT_DEFAULT
                && reportShrinkMode == ReportConstants.AUTO_SHRINK_TO_FIT_WIDTH)) {
            fitWidth(editCellElement, columnWidthList, rowHeightList);
        }
    }

    private static void fitHetght(TemplateCellElement editCellElement, DynamicUnitList columnWidthList, DynamicUnitList rowHeightList) {
        int editElementcolumn = editCellElement.getColumn();
        UNIT preferredHeight = PaintUtils.analyzeCellElementPreferredHeight(
                editCellElement,
                columnWidthList.getRangeValue(editElementcolumn, editElementcolumn + editCellElement.getColumnSpan()));
        if (editCellElement.getRowSpan() == 1) {
            rowHeightList.set(editCellElement.getRow(),
                    UNIT.max(preferredHeight, rowHeightList.get(editCellElement.getRow())));
        } else {
            int lastRowIndex = editCellElement.getRow() + editCellElement.getRowSpan() - 1;
            // kurt 画单元格时增加的高度
            long extraHeight = preferredHeight.toFU() - rowHeightList.getRangeValue(editCellElement.getRow(), lastRowIndex + 1).toFU();
            if (extraHeight > 0) {
                // kurt 平分给这些行
                for (int m = editCellElement.getRow(); m <= lastRowIndex; m++) {
                    rowHeightList.set(m, FU.getInstance(rowHeightList.get(m).toFU() + extraHeight
                            / editCellElement.getRowSpan()));
                }
            }
        }
    }

    private static void fitWidth(TemplateCellElement editCellElement, DynamicUnitList columnWidthList, DynamicUnitList rowHeightList) {
        UNIT preferredWidth = PaintUtils.getPreferredWidth(editCellElement, PT.valueOfFU(rowHeightList.getRangeValue(editCellElement.getRow(),
                editCellElement.getRow() + editCellElement.getRowSpan()).toFU()));
        // carl：照着调整行高来弄
        if (editCellElement.getColumnSpan() == 1) {
            columnWidthList.set(editCellElement.getColumn(), UNIT.max(preferredWidth,
                    columnWidthList.get(editCellElement.getColumn())));
        } else {
            int lastColumnIndex = editCellElement.getColumn() + editCellElement.getColumnSpan() - 1;
            long extraWidth = preferredWidth.toFU() - columnWidthList.getRangeValue(editCellElement.getColumn(), lastColumnIndex + 1).toFU();
            if (extraWidth > 0) {
                for (int m = editCellElement.getColumn(); m <= lastColumnIndex; m++) {
                    columnWidthList.set(m, FU.getInstance(columnWidthList.get(m).toFU() + extraWidth
                            / editCellElement.getColumnSpan() + 1));
                }
            }
        }
    }


}