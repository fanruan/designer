package com.fr.design.style;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.fr.base.CellBorderStyle;
import com.fr.base.Style;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.general.ComparatorUtils;
import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.FloatSelection;
import com.fr.grid.selection.Selection;
import com.fr.report.cell.CellElement;
import com.fr.report.cell.DefaultTemplateCellElement;
import com.fr.report.cell.FloatElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.elementcase.ElementCase;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.stable.Constants;

public abstract class BorderUtils {
    private static boolean insideModel;
    private static int NUMBER = 4;

    public static Object[] createCellBorderObject(ElementCasePane reportPane) {
        CellBorderStyle cellBorderStyle = new CellBorderStyle();
        insideModel = false;
        Object[] allBorder;
        ElementCase report = reportPane.getEditingElementCase();
        Selection sel = reportPane.getSelection();
        if (sel instanceof FloatSelection) {
            allBorder = new Object[NUMBER];
            floatSet(cellBorderStyle, report, sel);
            borderStyle(cellBorderStyle, allBorder, 0);
        } else {
            CellSelection cs = ((CellSelection) sel).clone();
            int cellRectangleCount = cs.getCellRectangleCount();
            if (cellRectangleCount == 1) {
                allBorder = new Object[NUMBER];
                int column = cs.getColumn();
                int row = cs.getRow();
                int columnSpan = cs.getColumnSpan();
                int rowSpan = cs.getRowSpan();
                calStyle(row, column, rowSpan, columnSpan, report, cellBorderStyle, true);
                borderStyle(cellBorderStyle, allBorder, 0);
            } else {
                int num = NUMBER * cellRectangleCount;
                allBorder = new Object[num];
                for (int rec = 0; rec < cellRectangleCount; rec++) {
                    cellBorderStyle = new CellBorderStyle();
                    insideModel = false;
                    Rectangle cellRectangle = cs.getCellRectangle(rec);
                    int column = cellRectangle.x;
                    int row = cellRectangle.y;
                    int columnSpan = cellRectangle.width;
                    int rowSpan = cellRectangle.height;
                    calStyle(row, column, rowSpan, columnSpan, report, cellBorderStyle, true);
                    borderStyle(cellBorderStyle, allBorder, rec * NUMBER);
                }
            }
        }
        return allBorder;
    }

    private static void calStyle(int row, int column, int rowSpan, int columnSpan, ElementCase report, CellBorderStyle cellBorderStyle, boolean isDrag) {
        Hashtable<ColumnRow, BorderStyleColor> topBorderHash = new Hashtable<ColumnRow, BorderStyleColor>();
        Hashtable<ColumnRow, BorderStyleColor> bottomBorderHash = new Hashtable<ColumnRow, BorderStyleColor>();
        Hashtable<ColumnRow, BorderStyleColor> leftBorderHash = new Hashtable<ColumnRow, BorderStyleColor>();
        Hashtable<ColumnRow, BorderStyleColor> rightBorderHash = new Hashtable<ColumnRow, BorderStyleColor>();
        // Cell转换器.
        // 检查上面边.
        checkUp(row, column, columnSpan, report, bottomBorderHash);
        // 检查最左边.
        checkLeft(row, column, rowSpan, report, rightBorderHash);
        // 检查最下边.
        checkDown(row, column, rowSpan, columnSpan, report, topBorderHash);
        // 检查最右边.
        checkRight(row, column, rowSpan, columnSpan, report, leftBorderHash);
        // 所选区域内部检查.
        checkAll(row, column, rowSpan, columnSpan, report, topBorderHash, bottomBorderHash, leftBorderHash, rightBorderHash);
        List<BorderStyleColor> tmpBorderStyleColorList = new ArrayList<BorderStyleColor>();
        // 检查Top.
        inspectUp(row, column, columnSpan, topBorderHash, cellBorderStyle, tmpBorderStyleColorList, isDrag);
        // 检查Left.
        inspectLeft(row, column, rowSpan, leftBorderHash, cellBorderStyle, tmpBorderStyleColorList, isDrag);
        // 检查Bottom.
        inspectDown(row, column, rowSpan, columnSpan, bottomBorderHash, cellBorderStyle, tmpBorderStyleColorList, isDrag);
        // 检查Right.
        inspectRight(row, column, rowSpan, columnSpan, rightBorderHash, cellBorderStyle, tmpBorderStyleColorList, isDrag);
        // peter:开始计算insideModel.
        inspectInsideModel(row, column, rowSpan, columnSpan, topBorderHash, bottomBorderHash, leftBorderHash, rightBorderHash, cellBorderStyle, tmpBorderStyleColorList, isDrag);
    }

    private static void inspectInsideModel(int row, int column, int rowSpan, int columnSpan, Hashtable<ColumnRow, BorderStyleColor> topBorderHash, Hashtable<ColumnRow, BorderStyleColor> bottomBorderHash, Hashtable<ColumnRow, BorderStyleColor> leftBorderHash, Hashtable<ColumnRow, BorderStyleColor> rightBorderHash, CellBorderStyle cellBorderStyle, List<BorderStyleColor> tmpBorderStyleColorList, boolean isDrag) {
        if (columnSpan > 1 || rowSpan > 1) {
            insideModel = true;
            // 检查Vertical和Horizontal.
            tmpBorderStyleColorList.clear();
            for (int i = column + 1; i < column + columnSpan; i++) {
                for (int j = row; j < row + rowSpan; j++) {
                    BorderStyleColor borderStyleColor = leftBorderHash.get(new ColumnRow(i, j, true));
                    tmpBorderStyleColorList.add(borderStyleColor);
                    borderStyleColor = rightBorderHash.get(new ColumnRow(i, j, true));
                    tmpBorderStyleColorList.add(borderStyleColor);
                }
            }
            if (!tmpBorderStyleColorList.isEmpty()) {
                if (isAllEquals(tmpBorderStyleColorList)) {
                    BorderStyleColor borderStyleColor = tmpBorderStyleColorList.get(0);
                    if (borderStyleColor != null) {
                        cellBorderStyle.setVerticalColor(borderStyleColor.getColor());
                        cellBorderStyle.setVerticalStyle(borderStyleColor.getStyle());
                    }
                } else {
                    if (isDrag) {
                        cellBorderStyle.setVerticalColor(Color.BLACK);
                        cellBorderStyle.setVerticalStyle(Constants.LINE_NONE);
                    } else {
                        cellBorderStyle.setVerticalColor(Color.GRAY);
                        cellBorderStyle.setVerticalStyle(Constants.LINE_DOUBLE_DOT);
                    }

                }
            }

            tmpBorderStyleColorList.clear();
            for (int i = column; i < column + columnSpan; i++) {
                for (int j = row + 1; j < row + rowSpan; j++) {
                    BorderStyleColor borderStyleColor = topBorderHash.get(new ColumnRow(i, j, false));
                    tmpBorderStyleColorList.add(borderStyleColor);
                    borderStyleColor = bottomBorderHash.get(new ColumnRow(i, j, false));
                    tmpBorderStyleColorList.add(borderStyleColor);
                }
            }
            if (!tmpBorderStyleColorList.isEmpty()) {
                if (isAllEquals(tmpBorderStyleColorList)) {
                    BorderStyleColor borderStyleColor = tmpBorderStyleColorList.get(0);
                    if (borderStyleColor != null) {
                        cellBorderStyle.setHorizontalColor(borderStyleColor.getColor());
                        cellBorderStyle.setHorizontalStyle(borderStyleColor.getStyle());
                    }
                } else {
                    if (isDrag) {
                        cellBorderStyle.setHorizontalColor(Color.BLACK);
                        cellBorderStyle.setHorizontalStyle(Constants.LINE_NONE);
                    } else {
                        cellBorderStyle.setHorizontalColor(Color.GRAY);
                        cellBorderStyle.setHorizontalStyle(Constants.LINE_DOUBLE_DOT);
                    }
                }
            }
        }
    }

    private static void inspectUp(int row, int column, int columnSpan, Hashtable<ColumnRow, BorderStyleColor> topBorderHash, CellBorderStyle cellBorderStyle, List<BorderStyleColor> tmpBorderStyleColorList, boolean isDrag) {
        tmpBorderStyleColorList.clear();
        for (int i = column; i < column + columnSpan; i++) {
            BorderStyleColor borderStyleColor = topBorderHash.get(new ColumnRow(i, row, false));
            tmpBorderStyleColorList.add(borderStyleColor);
        }
        if (!tmpBorderStyleColorList.isEmpty()) {
            if (isAllEquals(tmpBorderStyleColorList)) {
                BorderStyleColor borderStyleColor = tmpBorderStyleColorList.get(0);
                if (borderStyleColor != null) {
                    cellBorderStyle.setTopColor(borderStyleColor.getColor());
                    cellBorderStyle.setTopStyle(borderStyleColor.getStyle());
                }
            } else {
                if (isDrag) {
                    cellBorderStyle.setTopColor(Color.BLACK);
                    cellBorderStyle.setTopStyle(Constants.LINE_NONE);
                } else {
                    cellBorderStyle.setTopColor(Color.GRAY);
                    cellBorderStyle.setTopStyle(Constants.LINE_DOUBLE_DOT);
                }

            }
        }
    }

    private static void inspectLeft(int row, int column, int rowSpan, Hashtable<ColumnRow, BorderStyleColor> leftBorderHash, CellBorderStyle cellBorderStyle, List<BorderStyleColor> tmpBorderStyleColorList, boolean isDrag) {
        tmpBorderStyleColorList.clear();
        for (int j = row; j < row + rowSpan; j++) {
            BorderStyleColor borderStyleColor = leftBorderHash.get(new ColumnRow(column, j, true));
            tmpBorderStyleColorList.add(borderStyleColor);
        }
        if (!tmpBorderStyleColorList.isEmpty()) {
            if (isAllEquals(tmpBorderStyleColorList)) {
                BorderStyleColor borderStyleColor = tmpBorderStyleColorList.get(0);
                if (borderStyleColor != null) {
                    cellBorderStyle.setLeftColor(borderStyleColor.getColor());
                    cellBorderStyle.setLeftStyle(borderStyleColor.getStyle());
                }
            } else {
                if (isDrag) {
                    cellBorderStyle.setLeftColor(Color.BLACK);
                    cellBorderStyle.setLeftStyle(Constants.LINE_NONE);
                } else {
                    cellBorderStyle.setLeftColor(Color.GRAY);
                    cellBorderStyle.setLeftStyle(Constants.LINE_DOUBLE_DOT);
                }

            }
        }
    }

    private static void inspectDown(int row, int column, int rowSpan, int columnSpan, Hashtable<ColumnRow, BorderStyleColor> bottomBorderHash, CellBorderStyle cellBorderStyle, List<BorderStyleColor> tmpBorderStyleColorList, boolean isDrag) {
        tmpBorderStyleColorList.clear();
        for (int i = column; i < column + columnSpan; i++) {
            BorderStyleColor borderStyleColor = bottomBorderHash.get(new ColumnRow(i, row + rowSpan, false));
            tmpBorderStyleColorList.add(borderStyleColor);
        }
        if (!tmpBorderStyleColorList.isEmpty()) {
            if (isAllEquals(tmpBorderStyleColorList)) {
                BorderStyleColor borderStyleColor = tmpBorderStyleColorList.get(0);
                if (borderStyleColor != null) {
                    cellBorderStyle.setBottomColor(borderStyleColor.getColor());
                    cellBorderStyle.setBottomStyle(borderStyleColor.getStyle());
                }
            } else {
                if (isDrag) {
                    cellBorderStyle.setBottomColor(Color.BLACK);
                    cellBorderStyle.setBottomStyle(Constants.LINE_NONE);
                } else {
                    cellBorderStyle.setBottomColor(Color.GRAY);
                    cellBorderStyle.setBottomStyle(Constants.LINE_DOUBLE_DOT);
                }

            }
        }
    }

    private static void inspectRight(int row, int column, int rowSpan, int columnSpan, Hashtable<ColumnRow, BorderStyleColor> rightBorderHash, CellBorderStyle cellBorderStyle, List<BorderStyleColor> tmpBorderStyleColorList, boolean isDrag) {
        tmpBorderStyleColorList.clear();
        for (int j = row; j < row + rowSpan; j++) {
            BorderStyleColor borderStyleColor = rightBorderHash.get(new ColumnRow(column + columnSpan, j, true));
            tmpBorderStyleColorList.add(borderStyleColor);
        }
        if (!tmpBorderStyleColorList.isEmpty()) {
            if (isAllEquals(tmpBorderStyleColorList)) {
                BorderStyleColor borderStyleColor = tmpBorderStyleColorList.get(0);
                if (borderStyleColor != null) {
                    cellBorderStyle.setRightColor(borderStyleColor.getColor());
                    cellBorderStyle.setRightStyle(borderStyleColor.getStyle());
                }
            } else {
                if (isDrag) {
                    cellBorderStyle.setRightColor(Color.BLACK);
                    cellBorderStyle.setRightStyle(Constants.LINE_NONE);
                } else {
                    cellBorderStyle.setRightColor(Color.GRAY);
                    cellBorderStyle.setRightStyle(Constants.LINE_DOUBLE_DOT);
                }

            }
        }
    }

    private static void floatSet(CellBorderStyle cellBorderStyle, ElementCase report, Selection sel) {
        FloatElement selectedFloatElement = report.getFloatElement(((FloatSelection) sel).getSelectedFloatName());
        Style style = selectedFloatElement.getStyle();

        cellBorderStyle.setTopColor(style.getBorderTopColor());
        cellBorderStyle.setTopStyle(style.getBorderTop());
        cellBorderStyle.setLeftColor(style.getBorderLeftColor());
        cellBorderStyle.setLeftStyle(style.getBorderLeft());
        cellBorderStyle.setBottomColor(style.getBorderBottomColor());
        cellBorderStyle.setBottomStyle(style.getBorderBottom());
        cellBorderStyle.setRightColor(style.getBorderRightColor());
        cellBorderStyle.setRightStyle(style.getBorderRight());
    }

    private static void borderStyle(CellBorderStyle cellBorderStyle, Object[] allBorder, int begin) {
        int currentStyle = Constants.LINE_NONE;
        Color currentColor = Color.BLACK;
        if (cellBorderStyle.getLeftStyle() != Constants.LINE_NONE && cellBorderStyle.getLeftStyle() != Constants.LINE_DOUBLE_DOT) {
            currentStyle = cellBorderStyle.getLeftStyle();
            currentColor = cellBorderStyle.getLeftColor();
        } else if (cellBorderStyle.getTopStyle() != Constants.LINE_NONE && cellBorderStyle.getTopStyle() != Constants.LINE_DOUBLE_DOT) {
            currentStyle = cellBorderStyle.getTopStyle();
            currentColor = cellBorderStyle.getTopColor();
        } else if (cellBorderStyle.getBottomStyle() != Constants.LINE_NONE && cellBorderStyle.getBottomStyle() != Constants.LINE_DOUBLE_DOT) {
            currentStyle = cellBorderStyle.getBottomStyle();
            currentColor = cellBorderStyle.getBottomColor();
        } else if (cellBorderStyle.getRightStyle() != Constants.LINE_NONE && cellBorderStyle.getRightStyle() != Constants.LINE_DOUBLE_DOT) {
            currentStyle = cellBorderStyle.getRightStyle();
            currentColor = cellBorderStyle.getRightColor();
        } else if (cellBorderStyle.getVerticalStyle() != Constants.LINE_NONE && cellBorderStyle.getVerticalStyle() != Constants.LINE_DOUBLE_DOT) {
            currentStyle = cellBorderStyle.getVerticalStyle();
            currentColor = cellBorderStyle.getVerticalColor();
        } else if (cellBorderStyle.getHorizontalStyle() != Constants.LINE_NONE && cellBorderStyle.getHorizontalStyle() != Constants.LINE_DOUBLE_DOT) {
            currentStyle = cellBorderStyle.getHorizontalStyle();
            currentColor = cellBorderStyle.getHorizontalColor();
        }

        allBorder[begin] = cellBorderStyle;
        allBorder[begin + 1] = insideModel ? Boolean.TRUE : Boolean.FALSE;
        allBorder[begin + 2] = new Integer(currentStyle);
        allBorder[begin + 3] = currentColor;

//		return new Object[]{cellBorderStyle, insideModel ? Boolean.TRUE : Boolean.FALSE, new Integer(currentStyle), currentColor};
    }

    private static void checkUp(int row, int column, int columnSpan, ElementCase report, Hashtable<ColumnRow, BorderStyleColor> bottomBorderHash) {
        if (row - 1 >= 0) {
            for (int i = column; i < column + columnSpan; i++) {
                CellElement tmpCellElement = report.getCellElement(i, row - 1);
                if (tmpCellElement == null) {
                    continue;
                }

                Style style = tmpCellElement.getStyle();
                if (style.getBorderBottom() != Constants.LINE_NONE) {
                    // 需要指定所有的列和行.
                    for (int k = tmpCellElement.getColumn(); k < tmpCellElement.getColumn() + tmpCellElement.getColumnSpan(); k++) {
                        bottomBorderHash.put(new ColumnRow(k, row, false), new BorderStyleColor(style.getBorderBottom(), style.getBorderBottomColor()));
                    }
                }
            }
        }
    }

    private static void checkLeft(int row, int column, int rowSpan, ElementCase report, Hashtable<ColumnRow, BorderStyleColor> rightBorderHash) {
        if (column - 1 >= 0) {
            for (int j = row; j < row + rowSpan; j++) {
                CellElement tmpCellElement = report.getCellElement(column - 1, j);
                if (tmpCellElement == null) {
                    continue;
                }

                Style style = tmpCellElement.getStyle();
                if (style.getBorderRight() != Constants.LINE_NONE) {
                    // 需要指定所有的列和行.
                    for (int k = tmpCellElement.getRow(); k < tmpCellElement.getRow() + tmpCellElement.getRowSpan(); k++) {
                        rightBorderHash.put(new ColumnRow(column, k, true), new BorderStyleColor(style.getBorderRight(), style.getBorderRightColor()));
                    }
                }
            }
        }
    }

    private static void checkDown(int row, int column, int rowSpan, int columnSpan, ElementCase report, Hashtable<ColumnRow, BorderStyleColor> topBorderHash) {
        for (int i = column; i < column + columnSpan; i++) {
            CellElement tmpCellElement = report.getCellElement(i, row + rowSpan);
            if (tmpCellElement == null) {
                continue;
            }

            Style style = tmpCellElement.getStyle();
            if (style.getBorderTop() != Constants.LINE_NONE) {
                // 需要指定所有的列和行.
                for (int k = tmpCellElement.getColumn(); k < tmpCellElement.getColumn() + tmpCellElement.getColumnSpan(); k++) {
                    topBorderHash.put(new ColumnRow(k, row + rowSpan, false), new BorderStyleColor(style.getBorderTop(), style.getBorderTopColor()));
                }
            }
        }
    }

    private static void checkRight(int row, int column, int rowSpan, int columnSpan, ElementCase report, Hashtable<ColumnRow, BorderStyleColor> leftBorderHash) {
        for (int j = row; j < row + rowSpan; j++) {
            CellElement tmpCellElement = report.getCellElement(column + columnSpan, j);
            if (tmpCellElement == null) {
                continue;
            }

            Style style = tmpCellElement.getStyle();
            if (style.getBorderLeft() != Constants.LINE_NONE) {
                // 需要指定所有的列和行.
                for (int k = tmpCellElement.getRow(); k < tmpCellElement.getRow() + tmpCellElement.getRowSpan(); k++) {
                    leftBorderHash.put(new ColumnRow(column + columnSpan, k, true), new BorderStyleColor(style.getBorderLeft(), style.getBorderLeftColor()));
                }
            }
        }
    }

    private static void checkAll(int row, int column, int rowSpan, int columnSpan, ElementCase report,
                                 Hashtable<ColumnRow, BorderStyleColor> topBorderHash, Hashtable<ColumnRow, BorderStyleColor> bottomBorderHash,
                                 Hashtable<ColumnRow, BorderStyleColor> leftBorderHash, Hashtable<ColumnRow, BorderStyleColor> rightBorderHash) {
        for (int i = column; i < column + columnSpan; i++) {
            for (int j = row; j < row + rowSpan; j++) {
                CellElement tmpCellElement = report.getCellElement(i, j);
                if (tmpCellElement == null) {
                    continue;
                }

                Style style = tmpCellElement.getStyle();

                if (style.getBorderTop() != Constants.LINE_NONE) {
                    // 需要指定所有的列和行.
                    for (int k = tmpCellElement.getColumn(); k < tmpCellElement.getColumn() + tmpCellElement.getColumnSpan(); k++) {
                        topBorderHash.put(new ColumnRow(k, tmpCellElement.getRow(), false), new BorderStyleColor(style.getBorderTop(), style.getBorderTopColor()));
                    }
                }
                if (style.getBorderLeft() != Constants.LINE_NONE) {
                    // 需要指定所有的列和行.
                    for (int k = tmpCellElement.getRow(); k < tmpCellElement.getRow() + tmpCellElement.getRowSpan(); k++) {
                        leftBorderHash.put(new ColumnRow(tmpCellElement.getColumn(), k, true), new BorderStyleColor(style.getBorderLeft(), style.getBorderLeftColor()));
                    }
                }
                if (style.getBorderBottom() != Constants.LINE_NONE) {
                    // 需要指定所有的列和行.
                    for (int k = tmpCellElement.getColumn(); k < tmpCellElement.getColumn() + tmpCellElement.getColumnSpan(); k++) {
                        bottomBorderHash.put(new ColumnRow(k, tmpCellElement.getRow() + tmpCellElement.getRowSpan(), false), new BorderStyleColor(style.getBorderBottom(),
                                style.getBorderBottomColor()));
                    }
                }
                if (style.getBorderRight() != Constants.LINE_NONE) {
                    // 需要指定所有的列和行.
                    for (int k = tmpCellElement.getRow(); k < tmpCellElement.getRow() + tmpCellElement.getRowSpan(); k++) {
                        rightBorderHash.put(new ColumnRow(tmpCellElement.getColumn() + tmpCellElement.getColumnSpan(), k, true), new BorderStyleColor(style.getBorderRight(),
                                style.getBorderRightColor()));
                    }
                }
            }
        }
    }


    public static boolean updateCellBorderStyle(ElementCasePane reportPane, CellBorderStyle newCellBorderStyle) {
        TemplateElementCase report = reportPane.getEditingElementCase();
        Selection sel = reportPane.getSelection();
        int cellRectangleCount = ((CellSelection) sel).getCellRectangleCount();
        for (int rect = 0; rect < cellRectangleCount; rect++) {
            Rectangle cellRectangle = ((CellSelection) sel).getCellRectangle(rect);
            int column = cellRectangle.x;
            int row = cellRectangle.y;
            int columnSpan = cellRectangle.width;
            int rowSpan = cellRectangle.height;
            for (int i = column; i < column + columnSpan; i++) {
                for (int j = row; j < row + rowSpan; j++) {
                    TemplateCellElement tmpCellElement = report.getTemplateCellElement(i, j);
                    if (tmpCellElement == null) {
                        tmpCellElement = new DefaultTemplateCellElement(i, j);
                        report.addCellElement(tmpCellElement);
                    }
                    Style style = tmpCellElement.getStyle();
                    style = updateToStyle(newCellBorderStyle, style);
                    tmpCellElement.setStyle(style);
                }
            }
        }
        return true;
    }


    private static Style updateToStyle(CellBorderStyle newCellBorderStyle, Style style) {
        style = style.deriveBorder(newCellBorderStyle.getTopStyle(), newCellBorderStyle.getTopColor(),
                newCellBorderStyle.getBottomStyle(), newCellBorderStyle.getBottomColor(),
                newCellBorderStyle.getLeftStyle(), newCellBorderStyle.getLeftColor(),
                newCellBorderStyle.getRightStyle(), newCellBorderStyle.getRightColor());
        style.deriveBorder(newCellBorderStyle.getHorizontalStyle(), newCellBorderStyle.getHorizontalColor(),
                newCellBorderStyle.getHorizontalStyle(), newCellBorderStyle.getHorizontalColor(),
                newCellBorderStyle.getVerticalStyle(), newCellBorderStyle.getVerticalColor(),
                newCellBorderStyle.getVerticalStyle(), newCellBorderStyle.getVerticalColor());

        return style;
    }

    /**
     * Update ElementCasePane. Return whether cell border changed. true is
     * changed, need to support undo/redo
     */
    public static boolean update(ElementCasePane reportPane, CellBorderStyle newCellBorderStyle) {
        boolean isBorderColorStyleChanged = false;
        Object[] fourObjectArray = createCellBorderObject(reportPane);
        if (fourObjectArray == null || fourObjectArray.length < NUMBER) {
            return false;
        }
        CellBorderStyle cellBorderStyle = (CellBorderStyle) fourObjectArray[0];
        TemplateElementCase report = reportPane.getEditingElementCase();
        Selection sel = reportPane.getSelection();
        if (sel instanceof FloatSelection) {
            FloatElement selectedFloatElement = report.getFloatElement(((FloatSelection) sel).getSelectedFloatName());
            // Border变化了.
            if (!ComparatorUtils.equals(cellBorderStyle, newCellBorderStyle)) {
                isBorderColorStyleChanged = true;
                // peter:复制边框.
                Style style = selectedFloatElement.getStyle();

                selectedFloatElement.setStyle(style.deriveBorder(newCellBorderStyle.getTopStyle(), newCellBorderStyle.getTopColor(), newCellBorderStyle.getBottomStyle(),
                        newCellBorderStyle.getBottomColor(), newCellBorderStyle.getLeftStyle(), newCellBorderStyle.getLeftColor(), newCellBorderStyle.getRightStyle(),
                        newCellBorderStyle.getRightColor()));
            }
            return isBorderColorStyleChanged;
        } else {
            int cellRectangleCount = ((CellSelection) sel).getCellRectangleCount();
            for (int rect = 0; rect < cellRectangleCount; rect++) {
                cellBorderStyle = (CellBorderStyle) fourObjectArray[rect * NUMBER];
                Rectangle cellRectangle = ((CellSelection) sel).getCellRectangle(rect);
                int column = cellRectangle.x;
                int row = cellRectangle.y;
                int columnSpan = cellRectangle.width;
                int rowSpan = cellRectangle.height;

                // Border变化了.
                if (!ComparatorUtils.equals(cellBorderStyle, newCellBorderStyle)) {
                    isBorderColorStyleChanged = true;
                    setStyle(row, column, rowSpan, columnSpan, report, cellBorderStyle, newCellBorderStyle);
                }
            }
        }
        reportPane.repaint();
        return isBorderColorStyleChanged;
    }

    private static void setStyle(int row, int column, int rowSpan, int columnSpan, TemplateElementCase report, CellBorderStyle cellBorderStyle, CellBorderStyle newCellBorderStyle) {
        for (int i = column; i < column + columnSpan; i++) {
            for (int j = row; j < row + rowSpan; j++) {
                TemplateCellElement tmpCellElement = report.getTemplateCellElement(i, j);
                if (tmpCellElement == null) {
                    tmpCellElement = new DefaultTemplateCellElement(i, j);
                    report.addCellElement(tmpCellElement);
                }
                Style style = tmpCellElement.getStyle();
                style = inspectStyle(row, column, rowSpan, columnSpan, cellBorderStyle, newCellBorderStyle, tmpCellElement, style);
                tmpCellElement.setStyle(style);
            }
        }
    }

    private static Style inspectStyle(int row, int column, int rowSpan, int columnSpan, CellBorderStyle cellBorderStyle, CellBorderStyle newCellBorderStyle, TemplateCellElement tmpCellElement, Style style) {
        if (tmpCellElement.getColumn() == column) {
            if (cellBorderStyle.getLeftStyle() != newCellBorderStyle.getLeftStyle()
                    || !ComparatorUtils.equals(cellBorderStyle.getLeftColor(), newCellBorderStyle.getLeftColor())) {
                style = style.deriveBorderLeft(newCellBorderStyle.getLeftStyle(), newCellBorderStyle.getLeftColor());
            }
        } else {
            if (cellBorderStyle.getVerticalStyle() != newCellBorderStyle.getVerticalStyle()
                    || !ComparatorUtils.equals(cellBorderStyle.getVerticalColor(), newCellBorderStyle.getVerticalColor())) {
                style = style.deriveBorderLeft(newCellBorderStyle.getVerticalStyle(), newCellBorderStyle.getVerticalColor());
            }
        }
        if (tmpCellElement.getColumn() + tmpCellElement.getColumnSpan() == column + columnSpan) {
            if (cellBorderStyle.getRightStyle() != newCellBorderStyle.getRightStyle()
                    || !ComparatorUtils.equals(cellBorderStyle.getRightColor(), newCellBorderStyle.getRightColor())) {
                style = style.deriveBorderRight(newCellBorderStyle.getRightStyle(), newCellBorderStyle.getRightColor());
            }
        } else {
            if (cellBorderStyle.getVerticalStyle() != newCellBorderStyle.getVerticalStyle()
                    || !ComparatorUtils.equals(cellBorderStyle.getVerticalColor(), newCellBorderStyle.getVerticalColor())) {
                style = style.deriveBorderRight(newCellBorderStyle.getVerticalStyle(), newCellBorderStyle.getVerticalColor());
            }
        }
        if (tmpCellElement.getRow() == row) {
            if (cellBorderStyle.getTopStyle() != newCellBorderStyle.getTopStyle()
                    || !ComparatorUtils.equals(cellBorderStyle.getTopColor(), newCellBorderStyle.getTopColor())) {
                style = style.deriveBorderTop(newCellBorderStyle.getTopStyle(), newCellBorderStyle.getTopColor());
            }
        } else {
            if (cellBorderStyle.getHorizontalStyle() != newCellBorderStyle.getHorizontalStyle()
                    || !ComparatorUtils.equals(cellBorderStyle.getHorizontalColor(), newCellBorderStyle.getHorizontalColor())) {
                style = style.deriveBorderTop(newCellBorderStyle.getHorizontalStyle(), newCellBorderStyle.getHorizontalColor());
            }
        }
        if (tmpCellElement.getRow() + tmpCellElement.getRowSpan() == row + rowSpan) {
            if (cellBorderStyle.getBottomStyle() != newCellBorderStyle.getBottomStyle()
                    || !ComparatorUtils.equals(cellBorderStyle.getBottomColor(), newCellBorderStyle.getBottomColor())) {
                style = style.deriveBorderBottom(newCellBorderStyle.getBottomStyle(), newCellBorderStyle.getBottomColor());
            }
        } else {
            if (cellBorderStyle.getHorizontalStyle() != newCellBorderStyle.getHorizontalStyle()
                    || !ComparatorUtils.equals(cellBorderStyle.getHorizontalColor(), newCellBorderStyle.getHorizontalColor())) {
                style = style.deriveBorderBottom(newCellBorderStyle.getHorizontalStyle(), newCellBorderStyle.getHorizontalColor());
            }
        }
        return style;
    }

    private static boolean isAllEquals(List borderStyleColorList) {
        for (int i = 0; (i + 1) < borderStyleColorList.size(); i++) {
            if (!ComparatorUtils.equals(borderStyleColorList.get(i), borderStyleColorList.get(i + 1))) {
                return false;
            }
        }

        return true;
    }

    /**
     * 这是一个局部类，用来帮助保存各个Border Style 和 Color.
     */
    public static class ColumnRow {
        private int column = -1;
        private int row = -1;
        private boolean isVertical = true;

        public ColumnRow() {
            this(-1, -1, true);
        }

        public ColumnRow(int column, int row, boolean isVertical) {
            this.column = column;
            this.row = row;
            this.isVertical = isVertical;
        }

        public int getColumn() {
            return this.column;
        }

        public void setColumn(int column) {
            this.column = column;
        }

        public int getRow() {
            return this.row;
        }

        public void setRow(int row) {
            this.row = row;
        }

        public int hashCode() {
            return this.toString().hashCode();
        }

        public boolean equals(Object obj) {
            if (obj instanceof ColumnRow) {
                ColumnRow newColumnRow = (ColumnRow) obj;

                if (this.getColumn() == newColumnRow.getColumn() && this.getRow() == newColumnRow.getRow()) {
                    return true;
                }
            }

            return false;
        }

        public String toString() {
            if (isVertical) {
                return "ColumnRow[" + this.getColumn() + ", " + this.getRow() + ", vertical]";
            } else {
                return "ColumnRow[" + this.getColumn() + ", " + this.getRow() + ", horizontal]";
            }
        }
    }

    public static class BorderStyleColor {
        private int style;
        private Color color;

        public BorderStyleColor(int style, Color color) {
            this.setStyle(style);
            this.setColor(color);
        }

        public Color getColor() {
            return color;
        }

        public void setColor(Color color) {
            this.color = color;
        }

        public int getStyle() {
            return style;
        }

        public void setStyle(int style) {
            this.style = style;
        }

        public boolean equals(Object obj) {
            if (obj instanceof BorderStyleColor) {
                BorderStyleColor newBorderStyleColor = (BorderStyleColor) obj;

                if (this.getStyle() == newBorderStyleColor.getStyle() && ComparatorUtils.equals(this.getColor(), newBorderStyleColor.getColor())) {
                    return true;
                }
            }

            return false;
        }
    }
}