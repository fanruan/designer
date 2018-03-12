package com.fr.grid;

import com.fr.base.BaseUtils;
import com.fr.base.DynamicUnitList;
import com.fr.base.ScreenResolution;
import com.fr.base.vcs.DesignerMode;
import com.fr.common.inputevent.InputEventBaseOnOS;
import com.fr.design.constants.UIConstants;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.EastRegionContainerPane;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.mainframe.JSliderPane;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.FloatSelection;
import com.fr.grid.selection.Selection;
import com.fr.poly.creator.ECBlockPane;
import com.fr.report.ReportHelper;
import com.fr.report.cell.CellElement;
import com.fr.report.cell.FloatElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.cell.cellattr.CellGUIAttr;
import com.fr.report.elementcase.ElementCase;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.stable.ArrayUtils;
import com.fr.stable.ColumnRow;
import com.fr.stable.StringUtils;
import com.fr.stable.unit.FU;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

/**
 * the MouseListener of the Grid
 *
 * @editor zhou 2012-3-22下午1:53:59
 */
public class GridMouseAdapter implements MouseListener, MouseWheelListener, MouseMotionListener {
    private static final int WIDGET_WIDTH = 13;
    private static final int TIME_DELAY = 100;
    private static final int TOOLTIP_X = 30;
    private static final int TOOLTIP_X_Y_FIX = 4;
    private static final double COPY_CROSS_INNER_DISTANCE = 1.5;
    private static final double COPY_CROSS_OUTER_DISTANCE = 2.5;
    /**
     * 拖拽时候刷新时间间隔
     */
    private static int DRAG_REFRESH_TIME = 10;
    /**
     * 对应的表格-Grid
     */
    private Grid grid;
    /**
     * the Point(x,y) where the mouse pressed
     */
    private int oldEvtX = 0;
    private int oldEvtY = 0;
    // the old location, used for Move float element.
    private int oldLocationX;
    private int oldLocationY;
    private long lastMouseMoveTime = 0; // 最后的MouseMove时间.
    // 保存各个悬浮元素到oldLocation距离
    private Map<String, Point> floatNamePointMap;
    /**
     * august:因为CellSelection里面没有记录的变量了，必须要有个变量来存按住shift键的位置之前的鼠标的位置
     * 用户可能一直按住shift键不放，所以按住shift键之前的鼠标位置是必须有的.
     */
    private ColumnRow tempOldSelectedCell;

    private int ECBlockGap = 40;

    private int resolution = (int) (ScreenResolution.getScreenResolution() * JSliderPane.getInstance().resolutionTimes);

    protected GridMouseAdapter(Grid grid) {
        this.grid = grid;
    }

    /**
     * @param evt
     */
    public void mousePressed(MouseEvent evt) {
        if (!grid.isEnabled()) {
            return;
        }
        oldEvtX = evt.getX();
        oldEvtY = evt.getY();
        grid.stopEditing();

        if (!grid.hasFocus() && grid.isRequestFocusEnabled()) {
            grid.requestFocus();
        }

        if (SwingUtilities.isRightMouseButton(evt)) {
            doWithRightButtonPressed();
        } else {
            doWithLeftButtonPressed(evt);
        }
        // 用户没有按住Shift键时，tempOldSelectedCell是一直变化的。如果一直按住shift，是不变的
        ElementCasePane ePane = grid.getElementCasePane();
        if (!evt.isShiftDown() && ePane.getSelection() instanceof CellSelection) {
            tempOldSelectedCell = GridUtils.getAdjustEventColumnRow_withresolution(ePane, oldEvtX, oldEvtY, resolution);
        }

    }

    /**
     * 将悬浮元素(只有文本和公式)添加到鼠标点击的位置
     */
    private void doWithDrawingFloatElement() {
        ElementCasePane reportPane = grid.getElementCasePane();
        TemplateElementCase report = reportPane.getEditingElementCase();
        DynamicUnitList columnWidthList = ReportHelper.getColumnWidthList(report);
        DynamicUnitList rowHeightList = ReportHelper.getRowHeightList(report);

        int horizentalScrollValue = grid.getHorizontalValue();
        int verticalScrollValue = grid.getVerticalValue();
//        int resolution = ScreenResolution.getScreenResolution();
        FU evtX_fu = FU.valueOfPix(this.oldEvtX, resolution);
        FU evtY_fu = FU.valueOfPix(this.oldEvtY, resolution);

        FU leftDistance = FU.getInstance(evtX_fu.toFU() + columnWidthList.getRangeValue(0, horizentalScrollValue).toFU());
        FU topDistance = FU.getInstance(evtY_fu.toFU() + rowHeightList.getRangeValue(0, verticalScrollValue).toFU());

        grid.getDrawingFloatElement().setLeftDistance(leftDistance);
        grid.getDrawingFloatElement().setTopDistance(topDistance);

        report.addFloatElement(grid.getDrawingFloatElement());
        reportPane.setSelection(new FloatSelection(grid.getDrawingFloatElement().getName()));
    }

    /**
     * 处理右击事件，弹出右键菜单.
     */
    private void doWithRightButtonPressed() {
        ElementCasePane reportPane = grid.getElementCasePane();
        Object[] tmpFloatElementCursor = GridUtils.getAboveFloatElementCursor(reportPane, this.oldEvtX, this.oldEvtY);
        if (!ArrayUtils.isEmpty(tmpFloatElementCursor)) {
            FloatElement selectedFloatElement = (FloatElement) tmpFloatElementCursor[0];
            reportPane.setSelection(new FloatSelection(selectedFloatElement.getName()));
        } else {
            ColumnRow selectedCellPoint = GridUtils.getAdjustEventColumnRow_withresolution(reportPane, this.oldEvtX, this.oldEvtY, this.resolution);
            if (!reportPane.getSelection().containsColumnRow(selectedCellPoint)) {
                GridUtils.doSelectCell(reportPane, selectedCellPoint.getColumn(), selectedCellPoint.getRow());
            }
        }
        reportPane.repaint();
        JPopupMenu cellPopupMenu = reportPane.createPopupMenu();
        if (cellPopupMenu != null) {
            GUICoreUtils.showPopupMenu(cellPopupMenu, this.grid, this.oldEvtX - 1, this.oldEvtY - 1);
        }
    }

    /**
     * 处理左击事件
     */
    private void doWithLeftButtonPressed(MouseEvent evt) {
        if (DesignerMode.isAuthorityEditing()) {
            grid.setEditable(false);
        }

        ElementCasePane reportPane = grid.getElementCasePane();
        TemplateElementCase report = reportPane.getEditingElementCase();
        boolean isShiftDown = evt.isShiftDown();
        boolean isControlDown = InputEventBaseOnOS.isControlDown(evt);
        int clickCount = evt.getClickCount();
        // peter:需要判断是否在可移动CellSelection的区域
        grid.setDragType(isMoveCellSelection(this.oldEvtX, this.oldEvtY));
        if (clickCount >= 2) {
            grid.setDragType(GridUtils.DRAG_NONE);
        }
        if (grid.getDragType() != GridUtils.DRAG_NONE) {// Drag的标志.
            Selection selection = reportPane.getSelection();
            if (selection instanceof CellSelection) {
                // peter:设置DragRecatagle的标志.
                if (grid.getDragRectangle() == null) {
                    grid.setDragRectangle(new Rectangle());
                }
                CellSelection cs = ((CellSelection) selection).clone();
                grid.getDragRectangle().setBounds(cs.toRectangle());
                return;
            }
        }
        // peter:选择GridSelection,支持Shift
        doOneClickSelection(this.oldEvtX, this.oldEvtY, isShiftDown, isControlDown);
        // 得到点击所在的column and row
        ColumnRow columnRow = GridUtils.getEventColumnRow_withresolution(reportPane, this.oldEvtX, this.oldEvtY, this.resolution);
        TemplateCellElement cellElement = report.getTemplateCellElement(columnRow.getColumn(), columnRow.getRow());
        if (clickCount >= 2 && !DesignerMode.isAuthorityEditing()) {
            grid.startEditing();
        }
        if (clickCount == 1 && cellElement != null && cellElement.getWidget() != null && !DesignerMode.isAuthorityEditing()) {
            showWidetWindow(cellElement, report);
        }
        reportPane.repaint();
    }

    /**
     * 显示控件编辑窗口
     *
     * @param cellElement
     * @param report
     */

    private void showWidetWindow(TemplateCellElement cellElement, TemplateElementCase report) {
        DynamicUnitList columnWidthList = ReportHelper.getColumnWidthList(report);
        DynamicUnitList rowHeightList = ReportHelper.getRowHeightList(report);
        double fixed_pos_x = this.oldEvtX - columnWidthList.getRangeValue(grid.getHorizontalValue(), cellElement.getColumn()).toPixD(resolution);
        double fixed_pos_y = this.oldEvtY - rowHeightList.getRangeValue(grid.getVerticalValue(), cellElement.getRow()).toPixD(resolution);
        double cell_width = columnWidthList.getRangeValue(cellElement.getColumn(), cellElement.getColumn() + cellElement.getColumnSpan()).toPixD(resolution);
        double cell_height = rowHeightList.getRangeValue(cellElement.getRow(), cellElement.getRow() + cellElement.getRowSpan()).toPixD(resolution);
        if (fitSizeToShow(cell_width, cell_height, fixed_pos_x, fixed_pos_y)) {
            EastRegionContainerPane.getInstance().switchTabTo(EastRegionContainerPane.KEY_WIDGET_SETTINGS);
        }
    }

    private boolean fitSizeToShow(double cell_width, double cell_height, double fixed_pos_x, double fixed_pos_y) {
        return cell_width - fixed_pos_x > 0 && cell_height - fixed_pos_y > 0
                && cell_width - fixed_pos_x < WIDGET_WIDTH && cell_height - fixed_pos_y < WIDGET_WIDTH;
    }

    public void setResolution(int resolution) {
        this.resolution = resolution;
    }

    /**
     * @param evt
     */
    public void mouseReleased(MouseEvent evt) {
        if (!grid.isEnabled() || !grid.isEditable()) {
            return;
        }
        boolean isDataChanged = false;
        ElementCasePane reportPane = grid.getElementCasePane();
        Selection selection = reportPane.getSelection();

        if (selection instanceof FloatSelection) {
            grid.setCursor(new Cursor(Cursor.MOVE_CURSOR));
        }
        if (grid.getDragType() == GridUtils.DRAG_CELLSELECTION) {
            if (selection instanceof CellSelection) {
                grid.getElementCasePane().cut();
                // mouse release的时候要判断下是否在reportPane范围内
                if (outOfBounds(evt, reportPane)) {
                    GridUtils.doSelectCell(reportPane, grid.getDragRectangle().x, grid.getDragRectangle().y);
                } else {
                    mousePressed(evt);
                }
                grid.getElementCasePane().paste();
                isDataChanged = true;
            }
        } else if (grid.getDragType() == GridUtils.DRAG_CELLSELECTION_BOTTOMRIGHT_CORNER) {
            if (selection instanceof CellSelection) {
                CellSelection cs = (CellSelection) selection;
                // august：智能拖拽扩展单元格值
                IntelliElements.iterating(reportPane, cs.toRectangle(), grid.getDragRectangle());
                if (grid.getDragRectangle() != null) {
                    reportPane.setSelection(new CellSelection(grid.getDragRectangle().x, grid.getDragRectangle().y, grid.getDragRectangle().width, grid.getDragRectangle().height));
                }
                isDataChanged = true;
            }
        } else if (grid.getDragType() == GridUtils.DRAG_FLOAT) {
            isDataChanged = true;
        }
        grid.setDragType(GridUtils.DRAG_NONE);
        grid.setDragRectangle(null);
        if (isDataChanged) {
            reportPane.setSupportDefaultParentCalculate(true);
            reportPane.fireTargetModified();
            reportPane.setSupportDefaultParentCalculate(false);
        }
        doWithFormatBrush(reportPane);
        reportPane.repaint();
    }

    private void doWithFormatBrush(ElementCasePane reportPane) {
        if (DesignerContext.getFormatState() == DesignerContext.FORMAT_STATE_NULL) {
            return;
        }

        if (reportPane.getCellNeedTOFormat() != null) {
            reportPane.getFormatBrushAction().updateFormatBrush(DesignerContext.getReferencedStyle(), reportPane.getCellNeedTOFormat(), reportPane);
            reportPane.fireTargetModified();

        }
        if (DesignerContext.getFormatState() == DesignerContext.FORMAT_STATE_ONCE) {
            reportPane.cancelFormatBrush();
        }
        if (DesignerContext.getFormatState() == DesignerContext.FORMAT_STATE_MORE) {
            reportPane.getFormatBrush().setSelected(true);
        }
    }

    private boolean outOfBounds(MouseEvent evt, ElementCasePane reportPane) {
        return evt.getY() > reportPane.getHeight() || evt.getY() < 0 || evt.getX() > reportPane.getWidth() || evt.getX() < 0;
    }

    /**
     * @param evt
     */
    public void mouseMoved(final MouseEvent evt) {
        ElementCasePane reportPane = grid.getElementCasePane();
        boolean isGridForSelection = !grid.isEnabled() || !grid.isEditable();
        if (isGridForSelection || grid.isEditing()) {
            if (grid.IsNotShowingTableSelectPane()) {
                grid.setCursor(UIConstants.CELL_DEFAULT_CURSOR);
                return;
            }
            if (DesignerContext.getFormatState() != DesignerContext.FORMAT_STATE_NULL) {
                grid.setCursor(UIConstants.FORMAT_BRUSH_CURSOR);
            } else {
                grid.setCursor(GUICoreUtils.createCustomCursor(BaseUtils.readImage("com/fr/design/images/buttonicon/select.png"),
                        new Point(0, 0), "select", grid));
            }

            return;
        }
        // peter:停留一段时间.
        long systemCurrentTime = System.currentTimeMillis();
        if (systemCurrentTime - lastMouseMoveTime <= TIME_DELAY) {
            return;
        }
        lastMouseMoveTime = systemCurrentTime;// 记录最后一次的时间.
        mouseMoveOnGrid(evt.getX(), evt.getY());
    }

    /**
     * @param evt
     */
    public void mouseDragged(MouseEvent evt) {
        if (!grid.isEnabled()) {
            return;
        }

        boolean isControlDown = InputEventBaseOnOS.isControlDown(evt);

        long systemCurrentTime = System.currentTimeMillis();
        if (systemCurrentTime - lastMouseMoveTime <= DRAG_REFRESH_TIME) {// alex:Drag
            return;
        } else {
            lastMouseMoveTime = systemCurrentTime;
        }

        // right mouse cannot Drag..
        if (SwingUtilities.isRightMouseButton(evt)) {
            return;
        }

        doWithMouseDragged(evt.getX(), evt.getY(), isControlDown);
    }

    private void doWithMouseDragged(int evtX, int evtY, boolean isControlDown) {
        ElementCasePane reportPane = grid.getElementCasePane();

        if (reportPane.mustInVisibleRange()) {
            Grid grid = reportPane.getGrid();
            if (evtX > grid.getWidth() - 2 || evtY > grid.getHeight() - 2) {
                return;
            }
        }
        Selection selection = reportPane.getSelection();

        if (selection instanceof FloatSelection && !DesignerMode.isAuthorityEditing()) {
            doWithFloatElementDragged(evtX, evtY, (FloatSelection) selection);
            grid.setDragType(GridUtils.DRAG_FLOAT);
        } else if (grid.getDragType() == GridUtils.DRAG_CELLSELECTION_BOTTOMRIGHT_CORNER && !DesignerMode.isAuthorityEditing()) {
            doWithCellElementDragged(evtX, evtY, (CellSelection) selection);
        } else if (grid.getDragType() == GridUtils.DRAG_CELLSELECTION && !DesignerMode.isAuthorityEditing()) {
            // peter:获得调整过的Selected Column Row.
            ColumnRow selectedCellPoint = GridUtils.getAdjustEventColumnRow_withresolution(reportPane, evtX, evtY, resolution);
            if (selectedCellPoint.getColumn() != grid.getDragRectangle().x || selectedCellPoint.getRow() != grid.getDragRectangle().y) {
                grid.getDragRectangle().x = selectedCellPoint.getColumn();
                grid.getDragRectangle().y = selectedCellPoint.getRow();
            }
        } else {// august: 拖拽选中多个单元格
            doShiftSelectCell(evtX, evtY);
        }
        grid.getElementCasePane().repaint();
    }

    /**
     * 拖拽悬浮元素
     *
     * @param evtX
     * @param evtY
     * @param fs
     */

    private void doWithFloatElementDragged(int evtX, int evtY, FloatSelection fs) {
        ElementCase report = grid.getElementCasePane().getEditingElementCase();
//        int resolution = ScreenResolution.getScreenResolution();
        String floatName = fs.getSelectedFloatName();
        FloatElement floatElement = report.getFloatElement(floatName);
        int cursorType = grid.getCursor().getType();

        if (cursorType == Cursor.NW_RESIZE_CURSOR || cursorType == Cursor.NE_RESIZE_CURSOR || cursorType == Cursor.SE_RESIZE_CURSOR || cursorType == Cursor.SW_RESIZE_CURSOR) {
            DynamicUnitList columnWidthList = ReportHelper.getColumnWidthList(report);
            DynamicUnitList rowHeightList = ReportHelper.getRowHeightList(report);
            FU floatX1_fu = FU.valueOfPix(Math.min(oldEvtX, evtX), resolution);
            FU floatY1_fu = FU.valueOfPix(Math.min(oldEvtY, evtY), resolution);
            FU leftDistance = floatX1_fu.add(columnWidthList.getRangeValue(0, grid.getHorizontalValue()));
            FU topDistance = floatY1_fu.add(rowHeightList.getRangeValue(0, grid.getVerticalValue()));
            floatElement.setLeftDistance(leftDistance);
            floatElement.setTopDistance(topDistance);
            floatElement.setWidth(FU.valueOfPix(Math.max(oldEvtX, evtX), resolution).subtract(floatX1_fu));
            floatElement.setHeight(FU.valueOfPix(Math.max(oldEvtY, evtY), resolution).subtract(floatY1_fu));
        } else if (cursorType == Cursor.S_RESIZE_CURSOR || cursorType == Cursor.N_RESIZE_CURSOR) {
            DynamicUnitList rowHeightList = ReportHelper.getRowHeightList(report);
            FU floatY1_fu = FU.valueOfPix(Math.min(oldEvtY, evtY), resolution);
            FU topDistance = floatY1_fu.add(rowHeightList.getRangeValue(0, grid.getVerticalValue()));
            floatElement.setTopDistance(topDistance);
            floatElement.setHeight(FU.valueOfPix(Math.max(oldEvtY, evtY), resolution).subtract(floatY1_fu));
        } else if (cursorType == Cursor.W_RESIZE_CURSOR || cursorType == Cursor.E_RESIZE_CURSOR) {
            DynamicUnitList columnWidthList = ReportHelper.getColumnWidthList(report);
            FU floatX1_fu = FU.valueOfPix(Math.min(oldEvtX, evtX), resolution);
            FU leftDistance = floatX1_fu.add(columnWidthList.getRangeValue(0, grid.getHorizontalValue()));
            floatElement.setLeftDistance(leftDistance);
            floatElement.setWidth(FU.valueOfPix(Math.max(oldEvtX, evtX), resolution).subtract(floatX1_fu));
        } else if (cursorType == Cursor.MOVE_CURSOR) {
            DynamicUnitList columnWidthList = ReportHelper.getColumnWidthList(report);
            DynamicUnitList rowHeightList = ReportHelper.getRowHeightList(report);
            int horizentalValue = grid.getHorizontalValue();
            int verticalValue = grid.getVerticalValue();
            String floatElementName = fs.getSelectedFloatName();
            FloatElement tempFolatElement = report.getFloatElement(floatElementName);
            Point tempFolatElementPoint = floatNamePointMap.get(floatElementName);
            int floatX1ForTempFloatElement = tempFolatElementPoint.x + Math.max(oldLocationX + (evtX - oldEvtX), 0);
            int floatY1ForTempFloatElement = tempFolatElementPoint.y + Math.max(oldLocationY + (evtY - oldEvtY), 0);
            FU floatX1ForTempFloatElement_fu = FU.valueOfPix(floatX1ForTempFloatElement, resolution);
            FU leftDistance = floatX1ForTempFloatElement_fu.add(columnWidthList.getRangeValue(0, horizentalValue));
            FU floatY1ForTempFloatElement_fu = FU.valueOfPix(floatY1ForTempFloatElement, resolution);
            FU topDistance = floatY1ForTempFloatElement_fu.add(rowHeightList.getRangeValue(0, verticalValue));
            tempFolatElement.setLeftDistance(leftDistance);
            tempFolatElement.setTopDistance(topDistance);
        }

    }

    /**
     * 拖拽单元格
     *
     * @param evtX
     * @param evtY
     * @param cs
     */

    private void doWithCellElementDragged(int evtX, int evtY, CellSelection cs) {
        ElementCasePane reportPane = grid.getElementCasePane();
        java.awt.Rectangle cellRectangle = cs.toRectangle();

        ColumnRow selectedCellPoint = GridUtils.getAdjustEventColumnRow_withresolution(reportPane, evtX, evtY, resolution);
        if (cellRectangle.contains(selectedCellPoint.getColumn(), selectedCellPoint.getRow())) {
            grid.getDragRectangle().setBounds(cellRectangle);
        } else {
            int xDistance = evtX - this.oldEvtX;
            int yDistance = evtY - this.oldEvtY;
            if (Math.abs(yDistance) > Math.abs(xDistance)) {
                grid.getDragRectangle().x = cellRectangle.x;
                grid.getDragRectangle().width = cellRectangle.width;
                if (yDistance >= 0) {
                    // 聚合报表要求拖拽的时候要在本块的内部进行 不能无限往下拖
                    if (reportPane instanceof ECBlockPane && evtY > reportPane.getBounds().height - ECBlockGap) {
                        return;
                    }
                    grid.getDragRectangle().y = cellRectangle.y;
                    grid.getDragRectangle().height = selectedCellPoint.getRow() - cellRectangle.y + 1;
                } else {
                    if (selectedCellPoint.getRow() >= cellRectangle.y && selectedCellPoint.getRow() < cellRectangle.y + cellRectangle.height) {
                        grid.getDragRectangle().y = cellRectangle.y;
                        grid.getDragRectangle().height = cellRectangle.height;
                    } else {
                        grid.getDragRectangle().y = cellRectangle.y;
                        grid.getDragRectangle().height = cellRectangle.y - selectedCellPoint.getRow() + cellRectangle.height;
                    }
                }
            } else {
                grid.getDragRectangle().y = cellRectangle.y;
                grid.getDragRectangle().height = cellRectangle.height;
                if (xDistance >= 0) {
                    if (reportPane instanceof ECBlockPane && evtX > reportPane.getBounds().width - ECBlockGap) {
                        return;
                    }
                    grid.getDragRectangle().x = cellRectangle.x;
                    grid.getDragRectangle().width = selectedCellPoint.getColumn() - cellRectangle.x + 1;
                } else {
                    if (selectedCellPoint.getColumn() >= cellRectangle.x && selectedCellPoint.getColumn() < cellRectangle.x + cellRectangle.width) {
                        grid.getDragRectangle().x = cellRectangle.x;
                        grid.getDragRectangle().width = cellRectangle.width;
                    } else {
                        grid.getDragRectangle().x = selectedCellPoint.getColumn();
                        grid.getDragRectangle().width = cellRectangle.x - selectedCellPoint.getColumn() + cellRectangle.width;
                    }
                }
            }
        }
        reportPane.ensureColumnRowVisible(selectedCellPoint.getColumn() + 1, selectedCellPoint.getRow() + 1);
    }

    private void doShiftSelectCell(double evtX, double evtY) {
        ElementCasePane reportPane = grid.getElementCasePane();
        Selection s = reportPane.getSelection();
        if (s instanceof FloatSelection) {
            return;
        }
        ColumnRow selectedCellPoint = GridUtils.getAdjustEventColumnRow_withresolution(reportPane, evtX, evtY, resolution);
        int selectedCellPointX = selectedCellPoint.getColumn();
        int selectedCellPointY = selectedCellPoint.getRow();
        CellSelection gridSelection = ((CellSelection) s).clone();
        //反向选择单元格
        int tempOldSelectedCellX = tempOldSelectedCell.getColumn();
        int tempOldSelectedCellY = tempOldSelectedCell.getRow();
//		int tempOldSelectedCellX = gridSelection.getEditRectangle().x;
//		int tempOldSelectedCellY = gridSelection.getEditRectangle().y;

        int column = selectedCellPointX >= tempOldSelectedCellX ? tempOldSelectedCellX : selectedCellPointX;
        int row = selectedCellPointY >= tempOldSelectedCellY ? tempOldSelectedCellY : selectedCellPointY;
        int columnSpan = Math.abs(selectedCellPointX - tempOldSelectedCellX) + 1;
        int rowSpan = Math.abs(selectedCellPointY - tempOldSelectedCellY) + 1;
        Rectangle oldrectangle = new Rectangle(column, row, columnSpan, rowSpan);
        // ajust them to got the correct selected bounds.
        Rectangle newrectangle = grid.caculateIntersectsUnion(reportPane.getEditingElementCase(), oldrectangle);
        gridSelection.setBounds(newrectangle.x, newrectangle.y, newrectangle.width, newrectangle.height);
        gridSelection.clearCellRectangles(gridSelection.getCellRectangleCount() - 1);
        gridSelection.addCellRectangle(newrectangle);
        reportPane.setSelection(gridSelection);
        if (!reportPane.mustInVisibleRange()) {
            reportPane.ensureColumnRowVisible(selectedCellPointX, selectedCellPointY);
        }
    }


    private void doControlSelectCell(double evtX, double evtY) {
        ElementCasePane reportPane = grid.getElementCasePane();
        ElementCase report = reportPane.getEditingElementCase();
        //上一次选中的单元格
        Selection s = reportPane.getSelection();
        if (s instanceof FloatSelection) {
            return;
        }

        ColumnRow selectedCellPoint = GridUtils.getAdjustEventColumnRow_withresolution(reportPane, evtX, evtY, resolution);
        //拷贝，而不是直接强制使用以监听单元格选择变化
        CellSelection gridSelection = ((CellSelection) s).clone();
        gridSelection.setSelectedType(((CellSelection) s).getSelectedType());
        CellElement cellElement = report.getCellElement(selectedCellPoint.getColumn(), selectedCellPoint.getRow());
        if (cellElement == null) {
            gridSelection.setBounds(selectedCellPoint.getColumn(), selectedCellPoint.getRow(), 1, 1);
            int point = gridSelection.containsCell(selectedCellPoint.getColumn(), selectedCellPoint.getRow());
            if (point == -1) {
                gridSelection.addCellRectangle(new Rectangle(selectedCellPoint.getColumn(), selectedCellPoint.getRow(), 1, 1));
            } else {
                gridSelection.clearCellRectangles(point);
            }

        } else {
            gridSelection.setBounds(cellElement.getColumn(), cellElement.getRow(), cellElement.getColumnSpan(), cellElement.getRowSpan());
            gridSelection.addCellRectangle(new Rectangle(cellElement.getColumn(), cellElement.getRow(), cellElement.getColumnSpan(), cellElement.getRowSpan()));


        }

        reportPane.setSelection(gridSelection);

        if (!reportPane.mustInVisibleRange()) {
            reportPane.ensureColumnRowVisible(selectedCellPoint.getColumn(), selectedCellPoint.getRow());
        }


    }


    /**
     * 鼠标在Grid上面移动.
     */
    private void mouseMoveOnGrid(int evtX, int evtY) {
        grid.setToolTipText(null);
        Object[] floatElementCursor = GridUtils.getAboveFloatElementCursor(grid.getElementCasePane(), evtX, evtY);
        if (!ArrayUtils.isEmpty(floatElementCursor)) {// 鼠标在悬浮元素上移动
            grid.setCursor((Cursor) floatElementCursor[1]);
        } else {// 鼠标在单元格上移动
            doMouseMoveOnCells(evtX, evtY);
        }

    }

    /**
     * 鼠标在单元格上移动
     *
     * @param evtX
     * @param evtY
     */
    private void doMouseMoveOnCells(int evtX, int evtY) {
        ElementCasePane reportPane = grid.getElementCasePane();
        TemplateElementCase report = reportPane.getEditingElementCase();
        //如果是格式刷状态
        if (DesignerContext.getFormatState() != DesignerContext.FORMAT_STATE_NULL) {
            grid.setCursor(UIConstants.FORMAT_BRUSH_CURSOR);
        } else {
            grid.setCursor(UIConstants.CELL_DEFAULT_CURSOR);
        }
        ColumnRow selectedCellColumnRow = GridUtils.getEventColumnRow_withresolution(reportPane, evtX, evtY, resolution);
        TemplateCellElement curCellElement = report.getTemplateCellElement(selectedCellColumnRow.getColumn(), selectedCellColumnRow.getRow());

        if (curCellElement != null) {
            setCursorAndToolTips(curCellElement, report);
        }

        int dragType = isMoveCellSelection(evtX, evtY);
        if (dragType == GridUtils.DRAG_CELLSELECTION) {// 判断是否移动选中的区域.
            grid.setCursor(new Cursor(Cursor.MOVE_CURSOR));
        } // peter:判断是否复制移动的角落.
        else if (dragType == GridUtils.DRAG_CELLSELECTION_BOTTOMRIGHT_CORNER) {
            grid.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
        }

    }

    /**
     * 只根据CellGUIAttr里面的tooltips显示了，原先的显示条件属性、形态、控件等无意义
     *
     * @param curCellElement
     * @param report
     */
    private void setCursorAndToolTips(TemplateCellElement curCellElement, TemplateElementCase report) {
//        int resolution = ScreenResolution.getScreenResolution();
        // 计算相对Grid的显示位置.
        DynamicUnitList columnWidthList = ReportHelper.getColumnWidthList(report);
        DynamicUnitList rowHeightList = ReportHelper.getRowHeightList(report);

        CellGUIAttr cellGUIAttr = curCellElement.getCellGUIAttr();
        if (cellGUIAttr == null) {
            cellGUIAttr = CellGUIAttr.DEFAULT_CELLGUIATTR;
        }
        grid.setToolTipText(cellGUIAttr.getTooltipText());
        double tooltipX = columnWidthList.getRangeValue(grid.getHorizontalValue(), curCellElement.getColumn()).toPixD(resolution) + TOOLTIP_X_Y_FIX;
        double tooltipY = rowHeightList.getRangeValue(grid.getVerticalValue(), curCellElement.getRow() + curCellElement.getRowSpan()).toPixD(resolution) + TOOLTIP_X_Y_FIX;

        // peter:显示tooltip
        if (StringUtils.isNotBlank(grid.getToolTipText())) {
            grid.setTooltipLocation(tooltipX + TOOLTIP_X, tooltipY);
        }
    }

    /**
     * 是否移动CellSelection
     */
    private int isMoveCellSelection(double evtX, double evtY) {
        ElementCasePane reportPane = grid.getElementCasePane();

        // p:判断是否在选中区域的边框，可以移动CellSelelction选中区域
        Selection selection = reportPane.getSelection();
        if (!(selection instanceof CellSelection)) {
            return GridUtils.DRAG_NONE;
        }

        if ((selection instanceof CellSelection)
                && ((CellSelection) selection).getCellRectangleCount() != 1) {// p:没有选择Cell.
            return GridUtils.DRAG_NONE;
        }

        CellSelection cs = (CellSelection) selection;

        ElementCase report = reportPane.getEditingElementCase();

        // peter:计算相对Grid的显示位置.
        DynamicUnitList columnWidthList = ReportHelper.getColumnWidthList(report);
        DynamicUnitList rowHeightList = ReportHelper.getRowHeightList(report);

//        int resolution = ScreenResolution.getScreenResolution();

        double leftColDistance = columnWidthList.getRangeValue(grid.getHorizontalValue(), cs.getColumn()).toPixD(resolution);
        double rightColDistance = columnWidthList.getRangeValue(grid.getHorizontalValue(), cs.getColumn() + cs.getColumnSpan()).toPixD(resolution);
        double topRowDistance = rowHeightList.getRangeValue(grid.getVerticalValue(), cs.getRow()).toPixD(resolution);
        double bottomRowDistance = rowHeightList.getRangeValue(grid.getVerticalValue(), cs.getRow() + cs.getRowSpan()).toPixD(resolution);

        // 首先判断是否在可以复制的右下角落.
        if (fitCellSelectionBottomRight(evtX, evtY, rightColDistance, bottomRowDistance)) {
            return GridUtils.DRAG_CELLSELECTION_BOTTOMRIGHT_CORNER;
        }

        // 这个dist值调小一点,尽量让用户不使用drag and drop 来编辑报表支持
        double dist = 1.0;
        if (fitCellSelection(evtX, leftColDistance, rightColDistance, dist)) {
            if (evtY >= (topRowDistance - dist) && evtY <= (bottomRowDistance + dist)) {
                return GridUtils.DRAG_CELLSELECTION;
            }
        } else if (fitCellSelection(evtY, topRowDistance, bottomRowDistance, dist)) {
            if (evtX >= (leftColDistance - dist) && evtX <= (rightColDistance + dist)) {
                return GridUtils.DRAG_CELLSELECTION;
            }
        }

        return GridUtils.DRAG_NONE;
    }

    private boolean fitCellSelection(double evt, double d1, double d2, double dist) {
        return (evt >= (d1 - dist) && evt <= (d1 + dist))
                || (evt >= (d2 - dist) && evt <= (d2 + dist));
    }

    private boolean fitCellSelectionBottomRight(double evtX, double evtY, double rightColDistance, double bottomRowDistance) {
        return evtX > rightColDistance - COPY_CROSS_INNER_DISTANCE && evtX < rightColDistance + COPY_CROSS_OUTER_DISTANCE
                && evtY > bottomRowDistance - COPY_CROSS_INNER_DISTANCE && bottomRowDistance < bottomRowDistance + COPY_CROSS_OUTER_DISTANCE;
    }

    /**
     * Do one click selection
     */
    private void doOneClickSelection(int evtX, int evtY, boolean isShiftDown, boolean isControlDown) {
        ElementCasePane reportPane = grid.getElementCasePane();
        // check float elements.
        Object[] tmpFloatElementCursor = GridUtils.getAboveFloatElementCursor(reportPane, evtX, evtY);
        if (!ArrayUtils.isEmpty(tmpFloatElementCursor)) {// p:选中了悬浮元素.
            doSelectFloatElement(tmpFloatElementCursor, evtX, evtY);
        } else if (isShiftDown) {
            doShiftSelectCell(evtX, evtY);
        } else if (isControlDown) {
            doControlSelectCell(evtX, evtY);
        } else {
            ColumnRow selectedCellPoint = GridUtils.getEventColumnRow_withresolution(reportPane, evtX, evtY, resolution);
            int type = reportPane.ensureColumnRowVisible(selectedCellPoint.getColumn(), selectedCellPoint.getRow());
            if (type == ElementCasePane.NO_OVER) {
                GridUtils.doSelectCell(reportPane, selectedCellPoint.getColumn(), selectedCellPoint.getRow());
            } else if (type == ElementCasePane.VERTICAL_OVER) {
                //聚合报表块选在下边界的时候，有时会向下移，阻止向下移
                GridUtils.doSelectCell(reportPane, selectedCellPoint.getColumn(), selectedCellPoint.getRow() - 1);
            } else if (type == ElementCasePane.HORIZONTAL_OVER) {
                //聚合报表块选在右边界的时候，有时会向右移，阻止向右移
                GridUtils.doSelectCell(reportPane, selectedCellPoint.getColumn() - 1, selectedCellPoint.getRow());
            } else {
                GridUtils.doSelectCell(reportPane, selectedCellPoint.getColumn() - 1, selectedCellPoint.getRow() - 1);
            }

            return;
        }

    }

    /**
     * 选中悬浮元素
     *
     * @param tmpFloatElementCursor
     * @param evtX
     * @param evtY
     */

    private void doSelectFloatElement(Object[] tmpFloatElementCursor, int evtX, int evtY) {
        ElementCasePane reportPane = grid.getElementCasePane();
        ElementCase report = reportPane.getEditingElementCase();
        FloatElement floatElement = (FloatElement) tmpFloatElementCursor[0];
        String floatName = floatElement.getName();
        reportPane.setSelection(new FloatSelection(floatName));
//        double[] floatArray = GridUtils.caculateFloatElementLocations(floatElement, ReportHelper.getColumnWidthList(report), ReportHelper.getRowHeightList(report), reportPane
//                .getGrid().getVerticalValue(), reportPane.getGrid().getHorizontalValue());
        double[] floatArray = GridUtils.caculateFloatElementLocations_withresolution(floatElement, ReportHelper.getColumnWidthList(report), ReportHelper.getRowHeightList(report), reportPane
                .getGrid().getVerticalValue(), reportPane.getGrid().getHorizontalValue(), grid.getResolution());

        int cursorType = ((Cursor) tmpFloatElementCursor[1]).getType();
        if (cursorType == Cursor.MOVE_CURSOR) {
            this.oldEvtX = evtX;
            this.oldEvtY = evtY;
            FloatElement el = report.getFloatElement(floatName);
//            int resolution = ScreenResolution.getScreenResolution();
            int verticalValue = grid.getVerticalValue();
            int horizentalValue = grid.getHorizontalValue();
            DynamicUnitList columnWidthList = ReportHelper.getColumnWidthList(report);
            DynamicUnitList rowHeightList = ReportHelper.getRowHeightList(report);
            this.oldLocationX = FU.getInstance(el.getLeftDistance().toFU() - columnWidthList.getRangeValue(0, horizentalValue).toFU()).toPixI(resolution);
            this.oldLocationY = FU.getInstance(el.getTopDistance().toFU() - rowHeightList.getRangeValue(0, verticalValue).toFU()).toPixI(resolution);
            if (floatNamePointMap == null) {
                floatNamePointMap = new HashMap<String, Point>();
            }
            floatNamePointMap.clear();
            FloatElement tempFolatElement = report.getFloatElement(floatName);
            int floatX1ForTempFloatElement = FU.getInstance(tempFolatElement.getLeftDistance().toFU() - columnWidthList.getRangeValue(0, horizentalValue).toFU())
                    .toPixI(resolution) - oldLocationX;
            int floatY1ForTempFloatElement = FU.getInstance(tempFolatElement.getTopDistance().toFU() - rowHeightList.getRangeValue(0, verticalValue).toFU()).toPixI(resolution)
                    - oldLocationY;
            floatNamePointMap.put(floatName, new Point(floatX1ForTempFloatElement, floatY1ForTempFloatElement));
        } else if (cursorType == Cursor.NW_RESIZE_CURSOR) {
            setOld_X_AndOld_Y(floatArray[2], floatArray[3]);
        } else if (cursorType == Cursor.NE_RESIZE_CURSOR) {
            setOld_X_AndOld_Y(floatArray[0], floatArray[3]);
        } else if (cursorType == Cursor.SE_RESIZE_CURSOR) {
            setOld_X_AndOld_Y(floatArray[0], floatArray[1]);
        } else if (cursorType == Cursor.SW_RESIZE_CURSOR) {
            setOld_X_AndOld_Y(floatArray[2], floatArray[1]);
        } else if (cursorType == Cursor.N_RESIZE_CURSOR) {
            setOld_X_AndOld_Y(floatArray[0], floatArray[3]);
        } else if (cursorType == Cursor.S_RESIZE_CURSOR) {
            setOld_X_AndOld_Y(floatArray[0], floatArray[1]);
        } else if (cursorType == Cursor.W_RESIZE_CURSOR) {
            setOld_X_AndOld_Y(floatArray[2], floatArray[1]);
        } else if (cursorType == Cursor.E_RESIZE_CURSOR) {
            setOld_X_AndOld_Y(floatArray[0], floatArray[1]);
        }
    }

    private void setOld_X_AndOld_Y(double x, double y) {
        this.oldEvtX = (int) x;
        this.oldEvtY = (int) y;
    }

    /**
     * @param e
     */
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (!InputEventBaseOnOS.isControlDown(e)) {
            ElementCasePane reportPane = grid.getElementCasePane();
            if (reportPane.isHorizontalScrollBarVisible()) {
                reportPane.getVerticalScrollBar().setValue(reportPane.getVerticalScrollBar().getValue() + e.getWheelRotation() * 3);
            }
        }
    }

    /**
     * @param e
     */
    public void mouseClicked(MouseEvent e) {
    }

    /**
     * @param e
     */
    public void mouseEntered(MouseEvent e) {
    }

    /**
     * @param e
     */
    public void mouseExited(MouseEvent e) {
    }
}