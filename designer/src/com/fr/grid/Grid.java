/*
 * Copyright(c) 2001-2010, FineReport  Inc, All Rights Reserved.
 */
package com.fr.grid;

import com.fr.base.DynamicUnitList;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.cell.editor.*;
import com.fr.design.constants.UIConstants;
import com.fr.design.fun.GridUIProcessor;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.ComparatorUtils;
import com.fr.grid.event.CellEditorEvent;
import com.fr.grid.event.CellEditorListener;
import com.fr.grid.event.FloatEditorEvent;
import com.fr.grid.event.FloatEditorListener;
import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.FloatSelection;
import com.fr.grid.selection.Selection;
import com.fr.report.ReportHelper;
import com.fr.report.cell.CellElement;
import com.fr.report.cell.DefaultTemplateCellElement;
import com.fr.report.cell.FloatElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.cell.cellattr.CellGUIAttr;
import com.fr.report.cell.cellattr.CellImage;
import com.fr.report.cell.cellattr.core.RichText;
import com.fr.report.elementcase.ElementCase;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.stable.StringUtils;

import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * Grid used to paint and edit grid.
 *
 * @editor zhou 2012-3-22下午1:58:12
 */
// TODO ALEX_SEP 能不能把CellSelection作为Grid的属性,以此来分开CellSelection & FloatSelection
public class Grid extends BaseGridComponent {
    /**
     * If editing, the <code>Component</code> that is handling the editing.
     */
    public static final int MULTIPLE_PAGINATE_LINE = 1;  // 绘制多条分页线
    public static final int SINGLE_HORIZONTAL_PAGINATE_LINE = 2;  // 仅绘制一条水平分页线
    private static final int VERTICAL_EXTENT_INITIAL_VALUE = 50;
    private static final int HORIZONTAL_EXTENT_INITIAL_VALUE = 40;
    transient protected Component editorComponent;
    transient private Point2D editorComponentLoc;
    transient private TemplateCellElement editingCellElement;

    private boolean showGridLine = true;
    private Color gridLineColor = UIConstants.RULER_LINE_COLOR; // line color.

    private boolean isShowPaginateLine = true;
    private int paginateLineShowType = MULTIPLE_PAGINATE_LINE;  // 如何绘制分页线
    private Color paginationLineColor = Color.RED; // line color of paper

    private boolean isShowVerticalFrozenLine = true;
    private Color verticalFrozenLineColor = Color.black;

    private boolean isShowHorizontalFrozenLine = true;
    private Color horizontalFrozenLineColor = Color.black;

    private Color selectedBackground = UIConstants.SELECTED_BACKGROUND;
    private Color selectedBorderLineColor = UIConstants.SELECTED_BORDER_LINE_COLOR;
    private boolean editable = true; // 整体的总开关，控制格子是否可以编辑.

    private FloatElement drawingFloatElement = null;

    private int dragType = GridUtils.DRAG_NONE;// Drag的标志.
    // peter:Drag格子的时候,需要显示的边框,当为null的时候不画.
    private Rectangle dragRectangle = null;
    // ToolTip
    private Point tooltipLocation;

    /**
     * The object that overwrites the screen real estate occupied by the current
     * cell and allows the user to change its contents.
     */
    transient private CellEditor cellEditor;
    /**
     * The object that overwrites the screen real estate occupied by the current
     * float and allows the user to change its contents.
     */
    transient private FloatEditor floatEditor;
    /**
     * Identifies the column of the cell being edited.
     */
    transient private int editingColumn;
    /**
     * Identifies the row of the cell being edited.
     */
    transient private int editingRow;
    // A table of objects that display and edit the contents of a cell.
    transient private Hashtable<Class, CellEditor> defaultCellEditorsByClass;
    // A table of objects that display and edit the contents of a float.
    transient private Hashtable<Class, FloatEditor> defaultFloatEditorsByClass;
    // Vertical and Horizontal value.
    private int verticalValue = 0;
    private int verticalExtent = VERTICAL_EXTENT_INITIAL_VALUE;
    private int horizontalValue = 0;
    private int horizontalExtent = HORIZONTAL_EXTENT_INITIAL_VALUE;// marks:这个值从原来的10换成20，因为现在电脑都是宽屏，10已经没有办法满足需求！
    // denny: verticalBeginValue and horizantalBeginValue
    private int verticalBeginValue = 0;
    private int horizontalBeginValue = 0;

    private int resolution;
    // 判断SmartJTablePane是否显示，做为动态虚线标识符
    private boolean notShowingTableSelectPane = true;
    private GridMouseAdapter gridMouseAdapter;

    public Grid(int resolution) {
        this.resolution = resolution;
        // 能触发processEvent，不管是否给component增加listener
        enableEvents(AWTEvent.KEY_EVENT_MASK | AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK);

        GridKeyAction.initGridInputActionMap(this);
        gridMouseAdapter = new GridMouseAdapter(this);

        this.addMouseListener(gridMouseAdapter);
        this.addMouseMotionListener(gridMouseAdapter);
        this.addMouseWheelListener(gridMouseAdapter);

        this.addKeyListener(new GridKeyListener(this));

        // JDK1.4
        this.setFocusTraversalKeysEnabled(false);
        this.setOpaque(false);

        this.updateUI();
    }

    public void setResolution(int resolution) {
        this.resolution = resolution;
    }

    public int getResolution() {
        return this.resolution;
    }

    /**
     * 应用界面设置
     *
     * @date 2014-12-21-下午6:32:43
     */
    public void updateUI() {
        GridUIProcessor localGridUIProcessor = ExtraDesignClassManager.getInstance().getSingle(GridUIProcessor.MARK_STRING, new DefaultGridUIProcessor());
        ComponentUI localComponentUI = localGridUIProcessor.appearanceForGrid(this.resolution);
        setUI(localComponentUI);
    }


    /**
     * 是否显示格子线
     *
     * @return 是否显示格子线
     * @date 2014-12-21-下午6:32:13
     */
    public boolean isShowGridLine() {
        return showGridLine;
    }

    /**
     * Sets whether to show grid line.
     *
     * @param isShowGridLine whether to show grid line.
     */
    public void setShowGridLine(boolean isShowGridLine) {
        this.showGridLine = isShowGridLine;

        this.getElementCasePane().repaint();
    }

    public GridMouseAdapter getGridMouseAdapter() {
        return this.gridMouseAdapter;
    }

    /**
     * Gets grid line color.
     *
     * @return grid line color.
     */
    public Color getGridLineColor() {
        return this.gridLineColor;
    }

    /**
     * Sets grid line color.
     *
     * @param gridLineColor the new color of line.
     */
    public void setGridLineColor(Color gridLineColor) {
        Color old = this.gridLineColor;
        this.gridLineColor = gridLineColor;

        this.firePropertyChange("girdLineColor", old, this.gridLineColor);
        this.getElementCasePane().repaint();
    }

    /**
     * 是否显示分页线
     *
     * @return 是否显示分页线
     * @date 2014-12-21-下午6:31:45
     */
    public boolean isShowPaginateLine() {
        return isShowPaginateLine;
    }

    /**
     * Sets to show pagination line.
     */
    public void setShowPaginateLine(boolean showPaginateLine) {
        this.isShowPaginateLine = showPaginateLine;

        this.getElementCasePane().repaint();
    }

    /**
     * Gets pagination line color.
     */
    public Color getPaginationLineColor() {
        return this.paginationLineColor;
    }

    /**
     * Sets pagination line color.
     *
     * @param paginationLineColor the new color of pagination line.
     */
    public void setPaginationLineColor(Color paginationLineColor) {
        Color old = this.paginationLineColor;
        this.paginationLineColor = paginationLineColor;

        this.firePropertyChange("paginationLineColor", old, this.paginationLineColor);
        this.getElementCasePane().repaint();
    }


    /**
     * 是否显示垂直冻结线
     *
     * @return 是否显示垂直冻结线
     * @date 2014-12-21-下午6:29:35
     */
    public boolean isShowVerticalFrozenLine() {
        return isShowVerticalFrozenLine;
    }

    /**
     * Sets to show vertical frozen line.
     */
    public void setShowVerticalFrozenLine(boolean showVerticalFrozenLine) {
        this.isShowVerticalFrozenLine = showVerticalFrozenLine;

        this.getElementCasePane().repaint();
    }

    /**
     * Gets vertical frozen line color.
     */
    public Color getVerticalFrozenLineColor() {
        return verticalFrozenLineColor;
    }

    /**
     * Sets vertical frozen line color.
     *
     * @param verticalFrozenLineColor the new color of vertical frozen line.
     */
    public void setVerticalFrozenLineColor(Color verticalFrozenLineColor) {
        Color old = this.verticalFrozenLineColor;
        this.verticalFrozenLineColor = verticalFrozenLineColor;

        this.firePropertyChange("verticalFrozenLineColor", old, this.verticalFrozenLineColor);
        this.getElementCasePane().repaint();
    }

    /**
     * 是否显示水平冻结线
     *
     * @return 是否显示水平冻结线
     * @date 2014-12-21-下午6:29:35
     */
    public boolean isShowHorizontalFrozenLine() {
        return isShowHorizontalFrozenLine;
    }

    /**
     * Sets to show horizontal frozen line.
     */
    public void setShowHorizontalFrozenLine(boolean showHorizontalFrozenLine) {
        this.isShowHorizontalFrozenLine = showHorizontalFrozenLine;

        this.getElementCasePane().repaint();
    }

    /**
     * Gets horizontal frozen line color.
     */
    public Color getHorizontalFrozenLineColor() {
        return horizontalFrozenLineColor;
    }

    /**
     * Sets horizontal frozen line color.
     *
     * @param horizontalFrozenLineColor the new color of horizontal frozen line.
     */
    public void setHorizontalFrozenLineColor(Color horizontalFrozenLineColor) {
        Color old = this.horizontalFrozenLineColor;
        this.horizontalFrozenLineColor = horizontalFrozenLineColor;

        this.firePropertyChange("horizontalFrozenLineColor", old, this.horizontalFrozenLineColor);
        this.getElementCasePane().repaint();
    }

    /**
     * Gets the selected background.
     */
    public Color getSelectedBackground() {
        return this.selectedBackground;
    }

    /**
     * Sets the selected background.
     *
     * @param selectedBackground the new selected background.
     */
    public void setSelectedBackground(Color selectedBackground) {
        Color old = this.selectedBackground;
        this.selectedBackground = selectedBackground;

        this.firePropertyChange("selectedBackground", old, this.selectedBackground);
        this.getElementCasePane().repaint();
    }

    /**
     * Gets the selected border line color.
     */
    public Color getSelectedBorderLineColor() {
        return selectedBorderLineColor;
    }

    /**
     * Sets the selected border line color.
     *
     * @param selectedBorderLineColor the new color of selected border line.
     */
    public void setSelectedBorderLineColor(Color selectedBorderLineColor) {
        Color old = this.selectedBorderLineColor;
        this.selectedBorderLineColor = selectedBorderLineColor;

        this.firePropertyChange("selectedBorderLineColor", old, this.selectedBorderLineColor);
        this.getElementCasePane().repaint();
    }

    /**
     * 组件是否可以被编辑
     *
     * @return 组件是否可以被编辑
     * @date 2014-12-21-下午6:29:09
     */
    public boolean isEditable() {
        return editable;
    }

    /**
     * Sets whether to editable.
     */
    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    /**
     * @return
     */
    public FloatElement getDrawingFloatElement() {
        return drawingFloatElement;
    }

    /**
     * @param drawingFloatElement
     */
    public void setDrawingFloatElement(FloatElement drawingFloatElement) {
        this.drawingFloatElement = drawingFloatElement;
    }

    /**
     * @return
     */
    public int getVerticalValue() {
        return verticalValue;
    }

    /**
     * @param verticalValue
     */
    public void setVerticalValue(int verticalValue) {
        this.verticalValue = verticalValue;
    }

    /**
     * @return
     */
    public int getVerticalExtent() {
        return verticalExtent;
    }

    /**
     * @param verticalExtent
     */
    public void setVerticalExtent(int verticalExtent) {
        this.verticalExtent = verticalExtent;
    }

    /**
     * // denny: add the get and set method of verticalBeginValue
     *
     * @return
     */
    public int getVerticalBeginValue() {
        return verticalBeginValue;
    }

    /**
     * @param verticalBeginValue
     */
    public void setVerticalBeinValue(int verticalBeginValue) {
        this.verticalBeginValue = verticalBeginValue;
    }

    /**
     * @return
     */
    public int getHorizontalExtent() {
        return horizontalExtent;
    }

    /**
     * @param horizontalExtent
     */
    public void setHorizontalExtent(int horizontalExtent) {
        this.horizontalExtent = horizontalExtent;
    }

    /**
     * @return
     */
    public int getHorizontalValue() {
        return horizontalValue;
    }

    /**
     * @param horizontalValue
     */
    public void setHorizontalValue(int horizontalValue) {
        this.horizontalValue = horizontalValue;
    }

    /**
     * denny: add the get and set method of horizontalBeginValue
     *
     * @return
     */
    public int getHorizontalBeginValue() {
        return this.horizontalBeginValue;
    }

    /**
     * @param horizontalBeginValue
     */
    public void setHorizontalBeginValue(int horizontalBeginValue) {
        this.horizontalBeginValue = horizontalBeginValue;
    }

    // /////////////editor begin

    /**
     * 是否处于编辑状态
     *
     * @return 是否处于编辑状态
     * @date 2014-12-21-下午6:28:45
     */
    public boolean isEditing() {
        return this.editorComponent != null;
    }

    /**
     * 当前编辑对象是否为单元格
     *
     * @return 当前编辑对象是否为单元格
     * @date 2014-12-21-下午6:28:18
     */
    public boolean isCellEditing() {
        return this.isEditing() && cellEditor != null && notShowingTableSelectPane;
    }

    /**
     * @param f
     */
    public void setNotShowingTableSelectPane(boolean f) {
        this.notShowingTableSelectPane = f;
    }

    /**
     * 是否处于智能选择单元格阶段
     *
     * @return 是否处于智能选择单元格阶段
     * @date 2014-12-21-下午6:27:36
     */
    public boolean IsNotShowingTableSelectPane() {
        return this.notShowingTableSelectPane;
    }

    /**
     * 当前是否在编辑悬浮元素
     *
     * @return 是否在编辑悬浮元素
     * @date 2014-12-21-下午6:26:46
     */
    public boolean isFloatEditing() {
        return this.isEditing() && floatEditor != null;
    }

    /**
     * Returns an appropriate editor for the cell specified by
     * <code>column</code> and <code>row</code>.
     *
     * @param column the column of the cell to edit, where 0 is the first column;
     * @param row    the row of the cell to edit, where 0 is the first row
     * @return the editor for this cell; if <code>null</code> return the default
     * editor for this type of cell
     * @see com.fr.design.cell.editor.CellEditor
     */
    public CellEditor getCellEditor(int column, int row) {
        ElementCasePane reportPane = this.getElementCasePane();
        ElementCase report = reportPane.getEditingElementCase();
        CellElement cellElement = report.getCellElement(column, row);

        // 获得对象.
        Class objClass = Object.class;// 默认对象是Object.
        if (cellElement != null && cellElement.getValue() != null) {
            objClass = cellElement.getValue().getClass();
        }

        return this.getDefaultCellEditor(objClass);
    }

    /**
     * Gets the component that is handling the editing session. If nothing is
     * being edited, returns null.
     *
     * @return Component handling editing session
     */
    public Component getEditorComponent() {
        return this.editorComponent;
    }

    /**
     * Gets the index of the column that contains the cell currently being
     * edited. If nothing is being edited, returns -1.
     *
     * @return the index of the column that contains the cell currently being
     * edited; returns -1 if nothing being edited
     */
    public int getEditingColumn() {
        return editingColumn;
    }

    /**
     * Sets the <code>editingColumn</code> variable.
     *
     * @param editingColumn the column of the cell to be edited
     */
    public void setEditingColumn(int editingColumn) {
        this.editingColumn = editingColumn;
    }

    /**
     * Gets the index of the row that contains the cell currently being edited.
     * If nothing is being edited, returns -1.
     *
     * @return the index of the row that contains the cell currently being
     * edited; returns -1 if nothing being edited
     */
    public int getEditingRow() {
        return editingRow;
    }

    /**
     * Sets the <code>editingRow</code> variable.
     *
     * @param editingColumn the row of the cell to be edited
     */
    public void setEditingRow(int editingColumn) {
        this.editingRow = editingColumn;
    }

    /**
     * Gets the cell editor.
     *
     * @return the <code>CellEditor</code> that does the editing
     */
    public CellEditor getCellEditor() {
        return cellEditor;
    }

    /**
     * Gets the float editor.
     *
     * @return the <code>FloatEditor</code> that does the editing
     */
    public FloatEditor getFloatEditor() {
        return this.floatEditor;
    }

    /**
     * Sets the <code>cellEditor</code> variable.
     *
     * @param anEditor the CellEditor that does the editing
     */
    public void setCellEditor(CellEditor anEditor) {
        CellEditor old = this.cellEditor;
        this.cellEditor = anEditor;

        firePropertyChange("CellEditor", old, this.cellEditor);
    }

    /**
     * Sets the <code>FloatEditor</code> variable.
     *
     * @param anEditor the FloatEditor that does the editing
     */
    public void setFloatEditor(FloatEditor anEditor) {
        FloatEditor old = this.floatEditor;
        this.floatEditor = anEditor;

        firePropertyChange("FloatEditor", old, this.floatEditor);
    }

    /**
     * Gets the cell editor to be edit Object class.
     *
     * @return the default cell editor to be used for Object Class
     * @see #setDefaultCellEditor
     * @see com.fr.report.cell.CellElement#getValue
     */
    public CellEditor getDefaultCellEditor() {
        return this.getDefaultCellEditor(Object.class);
    }

    /**
     * Gets the float editor to be edit Object class.
     *
     * @return the default float editor to be used for Object Class
     * @see #setDefaultFloatEditor
     * @see com.fr.report.cell.FloatElement#getValue
     */
    public FloatEditor getDefaultFloatEditor() {
        return this.getDefaultFloatEditor(Object.class);
    }

    /**
     * Sets the cell editor to be edit the Object class.
     *
     * @param editor default cell editor to be used for Object Class
     * @see #getDefaultCellEditor
     */
    public void setDefaultCellEditor(CellEditor editor) {
        this.setDefaultCellEditor(Object.class, editor);
    }

    /**
     * Sets the float editor to be edit the Object class.
     *
     * @param editor default float editor to be used for Object Class
     * @see #getDefaultFloatEditor
     */
    public void setDefaultFloatEditor(FloatEditor editor) {
        this.setDefaultFloatEditor(Object.class, editor);
    }

    /**
     * Gets the editor to be edit the class. The <code>Grid</code> installs
     * entries for <code>Object</code>, <code>Number</code>,
     * <code>Boolean</code>,and all values that supported by CellElement.
     *
     * @param objectClass return the default cell editor for this Class
     * @return the default cell editor to be used for this Class
     * @see #setDefaultCellEditor
     * @see com.fr.report.cell.CellElement#getValue
     */
    public CellEditor getDefaultCellEditor(Class objectClass) {
        if (objectClass == null) {
            objectClass = Object.class;
        }

        CellEditor editor = this.prepareDefaultCellEditorsByClass().get(objectClass);
        if (editor != null) {
            return editor;
        } else {
            return getDefaultCellEditor(objectClass.getSuperclass());
        }
    }

    /**
     * Gets the float editor to be edit the class. The <code>Grid</code>
     * installs entries for <code>Object</code>, <code>Number</code>,
     * <code>Boolean</code>,and all values that supported by FloatElement.
     *
     * @param objectClass return the default float editor for this Class
     * @return the default cell editor to be used for this Class
     * @see #setDefaultFloatEditor
     * @see com.fr.report.cell.FloatElement#getValue
     */
    public FloatEditor getDefaultFloatEditor(Class objectClass) {
        if (objectClass == null) {
            objectClass = Object.class;
        }

        FloatEditor editor = this.prepareDefaultFloatEditorsByClass().get(objectClass);
        if (editor != null) {
            return editor;
        } else {
            return getDefaultFloatEditor(objectClass.getSuperclass());
        }
    }

    /**
     * Sets the editor to be edit the class. The <code>Grid</code> installs
     * entries for <code>Object</code>, <code>Number</code>,
     * <code>Boolean</code>,and all values that supported by CellElement.
     *
     * @param objectClass set the default cell editor for this Class
     * @param editor      default cell editor to be used for this Class
     * @see #getDefaultCellEditor
     * @see com.fr.report.cell.CellElement#getValue
     */
    public void setDefaultCellEditor(Class objectClass, CellEditor editor) {
        if (editor != null) {
            this.prepareDefaultCellEditorsByClass().put(objectClass, editor);
        } else {
            this.prepareDefaultCellEditorsByClass().remove(objectClass);
        }
    }

    /**
     * Sets the float editor to be edit the class. The <code>Grid</code>
     * installs entries for <code>Object</code>, <code>Number</code>,
     * <code>Boolean</code>,and all values that supported by CellElement.
     *
     * @param objectClass set the default float editor for this Class
     * @param floatEditor default float editor to be used for this Class
     * @see #getDefaultFloatEditor
     * @see com.fr.report.cell.FloatElement#getValue
     */
    public void setDefaultFloatEditor(Class objectClass, FloatEditor floatEditor) {
        if (floatEditor != null) {
            this.prepareDefaultFloatEditorsByClass().put(objectClass, floatEditor);
        } else {
            this.prepareDefaultFloatEditorsByClass().remove(objectClass);
        }
    }

    /**
     * 开始单元格编辑
     *
     * @date 2014-12-21-下午6:25:17
     */
    public void startEditing() {
        this.startEditing(false);
    }

    /**
     * 开始单元格编辑
     *
     * @param byKeyEvent 是否为键盘触发
     * @date 2014-12-21-下午6:25:17
     */
    protected void startEditing(boolean byKeyEvent) {
        ElementCasePane reportPane = this.getElementCasePane();
        ElementCase report = reportPane.getEditingElementCase();

        Selection s = reportPane.getSelection();
        if (s instanceof FloatSelection) {
            FloatElement selectedFloatElement = report.getFloatElement(((FloatSelection) s).getSelectedFloatName());
            this.stopEditing();// 需要先停止.

            Object value = selectedFloatElement.getValue();
            if (value == null) {
                this.floatEditor = this.getDefaultFloatEditor();
            } else {
                this.floatEditor = this.getDefaultFloatEditor(value.getClass());
            }

            if (this.floatEditor == null) {
                // peter:清空editorComponent.
                this.editorComponent = null;
                return;
            }

            this.editorComponent = this.floatEditor.getFloatEditorComponent(this, selectedFloatElement, resolution);
            if (this.editorComponent == null) {
                removeEditor();
                return;
            }
            floatEditor.addFloatEditorListener(innerFloatEditorListener);

            this.setFloatEditor(floatEditor);

            if (this.editorComponent instanceof Window) {
                this.editorComponent.setVisible(true);
            } else {
                this.ajustEditorComponentBounds();
                this.add(this.editorComponent);
                this.validate();
                this.editorComponent.requestFocus();
                this.repaint(10);
            }
        } else {// james：
            // Edit CellElement.
            CellSelection cs = (CellSelection) s;
            startCellEditingAt_DEC(cs.getColumn(), cs.getRow(), null, byKeyEvent);
        }
    }

    /**
     * 开始单元格编辑
     *
     * @param column        列
     * @param row           行
     * @param cellTypeClass 单元格类型
     * @param byKeyEvent    是否为键盘触发
     * @return 编辑是否成功
     * @date 2014-12-21-下午6:25:17
     */
    public boolean startCellEditingAt_DEC(int column, int row, Class cellTypeClass, boolean byKeyEvent) {
        if (this.isEditing()) {
            this.stopEditing();// 需要先停止正在进行的编辑.
        }
        if (!this.isEditable()) {// 判断总开关，是否可以编辑.
            return false;
        }
        if (row < 0 || column < 0) {
            return false;
        }

        ElementCasePane reportPane = this.getElementCasePane();
        TemplateElementCase report = reportPane.getEditingElementCase();
        editingCellElement = report.getTemplateCellElement(column, row);
        this.cellEditor = cellTypeClass == null ? this.getCellEditor(column, row) : this.getDefaultCellEditor(cellTypeClass);
        if (this.cellEditor == null) {
            this.editorComponent = null;
            return false;
        }
        // 必须保证editingCellElement不是null。
        if (editingCellElement == null) {
            editingCellElement = new DefaultTemplateCellElement(column, row);
        }
        editorComponent = getCellEditingComp();
        if (editorComponent == null) {
            return false;
        }
        this.setEditingColumn(column);
        this.setEditingRow(row);
        if (editorComponent instanceof Window) {
            editorComponent.setVisible(true);
        } else {
            // 如果是从KeyEvent启动的话，如果是文本或者数字，需要清空内容.
            if (byKeyEvent && editorComponent instanceof UITextField) {
                ((UITextField) editorComponent).setText(StringUtils.EMPTY);
            }
            startInnerEditing(column, row);

        }
        return false;
    }

    private Component getCellEditingComp() {
        // marks:这个地方获得editor
        Component eComp = this.cellEditor.getCellEditorComponent(this, editingCellElement, resolution);

        if (eComp == null) {
            removeEditor();
        } else {
            this.editorComponentLoc = this.cellEditor.getLocationOnCellElement();
            cellEditor.addCellEditorListener(innerCellEditorListener);
            this.setCellEditor(cellEditor);
        }
        return eComp;

    }

    /**
     * 就在单元格里面编辑
     */
    private void startInnerEditing(int column, int row) {
        this.editingRow = this.editingCellElement.getRow();
        this.editingColumn = this.editingCellElement.getColumn();

        this.ajustEditorComponentBounds();
        this.add(this.editorComponent);
        this.getElementCasePane().ensureColumnRowVisible(column, row);
        this.validate();
        // 需要重新绘制界面
        this.repaint(10);
        this.editorComponent.requestFocus();
    }

    /**
     * 停止编辑状态
     *
     * @date 2014-12-21-下午6:24:54
     */
    public void stopEditing() {
        // 首先判断是哪种类型的编辑.
        if (this.isCellEditing()) {
            this.stopCellEditingInner(true);
        }
        if (this.isFloatEditing()) {
            this.stopFloatEditingInner(true);
        }
    }

    /**
     * Stop editing. 当编辑器因为点击按钮失去焦点后，不需要Request Focus.
     */
    private void stopFloatEditingInner(boolean isRequestFocus) {
        if (!this.isFloatEditing()) {
            return;
        }

        if (floatEditor == null) {
            if (this.editorComponent != null) {
                this.remove(this.editorComponent);
            }
            return;
        }

        Object newValue = null;
        try {
            newValue = floatEditor.getFloatEditorValue();
        } catch (Exception exp) { // 捕捉错误信息.
        }
        if (newValue == null) {// If return null, do nothing.
            removeEditor();
            return;
        }

        ElementCasePane reportPane = this.getElementCasePane();
        Selection selection = reportPane.getSelection();
        if (selection instanceof FloatSelection) {// kunsnat: 类型判断, 经常在删除悬浮元素时 遇到停止编辑错误.
            FloatSelection fs = (FloatSelection) reportPane.getSelection();

            FloatElement selectedFloatElement = reportPane.getEditingElementCase().getFloatElement(fs.getSelectedFloatName());
            Object oldValue = selectedFloatElement.getValue();
            if (!ComparatorUtils.equals_exactly(oldValue, newValue)) {
                if (newValue instanceof CellImage) {
                    CellImage cellImage = (CellImage) newValue;
                    newValue = cellImage.getImage();
                    if (cellImage.getStyle() != null) {
                        selectedFloatElement.setStyle(cellImage.getStyle());
                    }
                }
                selectedFloatElement.setValue(newValue);
                reportPane.fireTargetModified();
                //加这句话是为了在编辑完悬浮元素公式的时候，点击确定，右上角面板会立即刷新
                reportPane.getCurrentEditor();
            }
        }

        removeEditor();
        if (isRequestFocus && !this.hasFocus()) {
            this.requestFocus();
        }
    }

    /**
     * Stop editing. 当编辑器因为点击按钮失去焦点后，不需要Request Focus.
     */
    private void stopCellEditingInner(boolean isRequestFocus) {
        if ((!this.isCellEditing())) {
            return;
        }
        if (cellEditor == null) {
            if (this.editorComponent != null) {
                this.remove(this.editorComponent);
            }
            return;
        }

        ElementCasePane reportPane = this.getElementCasePane();
        TemplateElementCase tplEC = reportPane.getEditingElementCase();

        Object newValue = null;
        Object oldValue = null;
        // CellAdapter
        this.editingCellElement = tplEC.getTemplateCellElement(editingColumn, editingRow);
        try {
            newValue = cellEditor.getCellEditorValue();
        } catch (Exception exp) { // 捕捉错误信息.
        }

        if (cellEditor instanceof TextCellEditor) {
            oldValue = ((TextCellEditor) cellEditor).getOldValue();
        }
        if (isValueEmpty(newValue)) {
            reportPane.clearContents();
            if (cellEditor instanceof FormulaCellEditor || !isValueEmpty(oldValue)) {
                reportPane.fireTargetModified();
            }
            removeEditor();
            return;
        }
        // 必须保证editingCellElement不是null。
        if (editingCellElement == null) {
            editingCellElement = new DefaultTemplateCellElement(editingColumn, editingRow);
            tplEC.addCellElement(editingCellElement);
        }
        if (setValue4EditingElement(newValue)) {
            shrinkToFit(tplEC);
            reportPane.fireTargetModified();
        }
        removeEditor();

        if (isRequestFocus && !this.hasFocus()) {
            this.requestFocus();
        }
    }

    private boolean isValueEmpty(Object newValue) {
        return (newValue == null || ComparatorUtils.equals(newValue, StringUtils.EMPTY));

    }

    /**
     * @return editingCellElement 的字符串表示
     */
    public String getEditingCellElement() {
        return editingCellElement.toString();
    }

    /**
     * 将新值赋给editingCellElement
     *
     * @param newValue
     * @return true if the value changed
     */
    private boolean setValue4EditingElement(Object newValue) {
        if (newValue instanceof TemplateCellElement) {
            TemplateCellElement cellElement = (TemplateCellElement) newValue;
            editingCellElement.setValue(cellElement.getValue());
            editingCellElement.setCellExpandAttr(cellElement.getCellExpandAttr());
            return true;
        } else if (newValue instanceof CellImage) {
            CellImage cellImage = (CellImage) newValue;
            newValue = cellImage.getImage();
            boolean styleChange = false;
            if (!ComparatorUtils.equals_exactly(cellImage.getStyle(), editingCellElement.getStyle())) {
                editingCellElement.setStyle(cellImage.getStyle());
                styleChange = true;
            }
            Object oldValue = this.editingCellElement.getValue();
            boolean imageChange = false;
            if (!ComparatorUtils.equals_exactly(oldValue, newValue)) {
                editingCellElement.setValue(newValue);
                imageChange = true;
            }
            if (styleChange || imageChange) {
                return true;
            }
        } else {
            if (newValue instanceof RichText) {
                setShowAsHtml(this.editingCellElement);
            }

            Object oldValue = this.editingCellElement.getValue();
            if (!ComparatorUtils.equals_exactly(oldValue, newValue)) {
                editingCellElement.setValue(newValue);
                return true;
            }
        }
        return false;
    }

    private void setShowAsHtml(CellElement cellElement) {
        CellGUIAttr guiAttr = cellElement.getCellGUIAttr();
        if (guiAttr == null) {
            guiAttr = new CellGUIAttr();
            cellElement.setCellGUIAttr(guiAttr);
        }

        guiAttr.setShowAsHTML(true);
    }

    /**
     * 当单元格里的内容过长时，自动调整单元格
     *
     * @param tplEC
     */
    private void shrinkToFit(TemplateElementCase tplEC) {
        if (editingCellElement == null) {
            return;
        }

        Object editElementValue = editingCellElement.getValue();
        if (valueNeedFit(editElementValue)) {
            int mode = this.getElementCasePane().getReportSettings().getShrinkToFitMode();
            GridUtils.shrinkToFit(mode, tplEC, editingCellElement);
        }
    }

    //是否需要根据内容自动调整, 目前只有字符串, 数字, 富文本需要
    private boolean valueNeedFit(Object value) {
        if (value == null) {
            return false;
        }

        return value instanceof String ||
                value instanceof Number ||
                value instanceof RichText;
    }

    /**
     * 取消编辑状态
     *
     * @date 2014-12-21-下午6:24:34
     */
    public void cancelEditing() {
        if (this.isEditing()) {
            removeEditor();
            this.requestFocus();
        }
    }

    /**
     * 移除选中组件
     *
     * @date 2014-12-21-下午6:24:16
     */
    public void removeEditor() {
        if (this.isCellEditing()) {
            this.removeCellEditor();
        } else {
            this.removeFloatEditor();
        }
    }

    /**
     * 移除单元格组件
     *
     * @date 2014-12-21-下午6:24:00
     */
    public void removeCellEditor() {
        CellEditor cellEditor = getCellEditor();
        if (cellEditor == null) {
            return;
        }

        if (this.editorComponent != null) {
            if (this.editorComponent instanceof Window) {
                editorComponent.setVisible(false);
                ((Window) editorComponent).dispose();
            } else {
                this.remove(this.editorComponent);
                this.validate();
            }
        }

        cellEditor.removeCellEditorListener(innerCellEditorListener);

        setCellEditor(null);
        setEditingColumn(-1);
        setEditingRow(-1);
        this.editorComponent = null;
        this.editingCellElement = null;

        this.getElementCasePane().repaint();
    }

    /**
     * 移除悬浮元素组件
     *
     * @date 2014-12-21-下午6:23:38
     */
    public void removeFloatEditor() {
        FloatEditor floatEditor = getFloatEditor();
        if (floatEditor != null) {
            if (this.editorComponent != null) {
                if (this.editorComponent instanceof Window) {
                    editorComponent.setVisible(false);
                    ((Window) editorComponent).dispose();
                } else {
                    this.remove(this.editorComponent);
                    this.validate();
                }
            }

            floatEditor.removeFloatEditorListener(innerFloatEditorListener);
        }

        setFloatEditor(null);
        this.editorComponent = null;

        this.getElementCasePane().repaint();
    }

    /**
     * 用这个方法来初始化defaultEditorsByClass, 是为了加快Grid对象的初始化速度.
     * 当用户编辑的时候,才将那个CellEditor的初始化.
     */
    private Hashtable<Class, CellEditor> prepareDefaultCellEditorsByClass() {
        if (this.defaultCellEditorsByClass == null) {
            this.defaultCellEditorsByClass = new Hashtable<Class, CellEditor>();
            defaultCellEditorsByClass.put(Object.class, new GeneralCellEditor());
        }

        return this.defaultCellEditorsByClass;
    }

    /**
     * 用这个方法来初始化defaultFloatEditorsByClass, 是为了加快Grid对象的初始化速度.
     * 当用户编辑的时候,才将那个FloatEditor的初始化.
     */
    private Hashtable<Class, FloatEditor> prepareDefaultFloatEditorsByClass() {
        if (this.defaultFloatEditorsByClass == null) {
            this.defaultFloatEditorsByClass = new Hashtable<Class, FloatEditor>();
            defaultFloatEditorsByClass.put(Object.class, new GeneralFloatEditor());
        }

        return this.defaultFloatEditorsByClass;
    }

    // /////////////editor end

    /**
     * 鼠标点击事件
     *
     * @param evtX x坐标
     * @param evtY y坐标
     * @date 2014-12-21-下午6:22:56
     */
    public void doMousePress(double evtX, double evtY) {
        dispatchEvent(new MouseEvent(this, MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0, (int) evtX, (int) evtY, 1, false));
    }

    /**
     * 计算oldRectangle,因为CellElement的合并会变成多大的区域.
     *
     * @param report       当前格子报表
     * @param oldRectangle 之前的选中区域
     * @return 插入的区域
     * @date 2014-12-21-下午6:22:21
     */
    public Rectangle caculateIntersectsUnion(ElementCase report, Rectangle oldRectangle) {
        Rectangle newRectangle = new Rectangle(oldRectangle);

        Iterator cells = report.intersect(newRectangle.x, newRectangle.y, newRectangle.width, newRectangle.height);
        while (cells.hasNext()) {
            CellElement cellElement = (CellElement) cells.next();

            Rectangle tmpCellElementRect = new Rectangle(cellElement.getColumn(), cellElement.getRow(), cellElement.getColumnSpan(), cellElement.getRowSpan());
            if (newRectangle.intersects(tmpCellElementRect) && !newRectangle.contains(tmpCellElementRect)) {
                newRectangle = newRectangle.union(tmpCellElementRect);
            }
        }

        // 检查十分需要从新再循环一遍.
        if (!GUICoreUtils.isTheSameRect(newRectangle, oldRectangle)) {
            return this.caculateIntersectsUnion(report, newRectangle);
        }

        return newRectangle;
    }

    /**
     * @param event
     * @return
     */
    public Point getToolTipLocation(MouseEvent event) {
        if (StringUtils.isEmpty(this.getToolTipText())) {
            return null;
        }
        if (tooltipLocation == null) {
            tooltipLocation = new Point();
        }

        return this.tooltipLocation;
    }

    /**
     * peter:重新调整editorComponent的宽度和高度,几乎是界面有变化就调整.
     */
    public void ajustEditorComponentBounds() {
        // 没有编辑器或者对于弹出的Window不需要调整Bound.
        if (this.editorComponent == null || this.editorComponent instanceof Window) {
            return;
        }
        ElementCasePane reportPane = this.getElementCasePane();
        ElementCase report = reportPane.getEditingElementCase();
        // four anchor values.
        DynamicUnitList rowHeightList = ReportHelper.getRowHeightList(report);
        DynamicUnitList columnWidthList = ReportHelper.getColumnWidthList(report);

        // 调整X, Y, width, height
        double x, y, width, height;
        Selection s = reportPane.getSelection();

        if (s instanceof FloatSelection) {
            FloatElement selectedFloatElement = report.getFloatElement(((FloatSelection) s).getSelectedFloatName());
            int h = reportPane.getHorizontalScrollBar().getValue();
            int v = reportPane.getVerticalScrollBar().getValue();
            x = selectedFloatElement.getLeftDistance().toPixD(this.resolution) + columnWidthList.getRangeValue(h, 0).toPixD(this.resolution);
            y = selectedFloatElement.getTopDistance().toPixD(this.resolution) + rowHeightList.getRangeValue(v, 0).toPixD(this.resolution);
            width = selectedFloatElement.getWidth().toPixD(this.resolution);
            height = selectedFloatElement.getHeight().toPixD(this.resolution);
        } else {
            x = columnWidthList.getRangeValue(this.horizontalBeginValue, this.editingColumn).toPixD(this.resolution);
            y = rowHeightList.getRangeValue(this.verticalBeginValue, this.editingRow).toPixD(this.resolution);
            int columnSpan = this.editingCellElement != null ? this.editingCellElement.getColumnSpan() : 1;
            int rowSpan = this.editingCellElement != null ? this.editingCellElement.getRowSpan() : 1;
            width = columnWidthList.getRangeValue(this.editingColumn, this.editingColumn + columnSpan).toPixD(this.resolution) - 1;
            height = rowHeightList.getRangeValue(this.editingRow, this.editingRow + rowSpan).toPixD(this.resolution) - 1;
        }
        applayRect(x, y, width, height);
    }

    private void applayRect(double x, double y, double width, double height) {
        // peter:需要检查Loc来调整editorComponent的location.
        if (this.editorComponentLoc == null) {
            this.editorComponent.setLocation((int) (x + 1), (int) (y + 1));
        } else {
            this.editorComponent.setLocation((int) (x + this.editorComponentLoc.getX()), (int) (y + this.editorComponentLoc.getY()));
        }
        // 专门处理TextField, TextField的长度需要跟着文本的总长度来变化.
        if (this.editorComponent instanceof UITextField) {
            Dimension textPrefSize = this.editorComponent.getPreferredSize();
            // peter:对于文本左右需要调整 4 个像素，来让显示更加完美.
            // peter:需要检查Loc来调整editorComponent的location.
            if (this.editorComponentLoc == null) {
                this.editorComponent.setSize((int) Math.max(width, textPrefSize.getWidth() + 1), (int) height);
            } else {
                this.editorComponent.setSize((int) (Math.max(width, textPrefSize.getWidth() + 1) - this.editorComponentLoc.getX()), (int) (height - this.editorComponentLoc.getY()));
            }
        } else {
            if (this.editorComponentLoc == null) {
                this.editorComponent.setSize((int) width, (int) height);
            } else {
                this.editorComponent.setSize((int) (width - this.editorComponentLoc.getX()), (int) (height - this.editorComponentLoc.getY()));
            }
        }
    }

    // CellEditorListener, 当开始编辑的时候,这个Listener会被加入其中.停止编辑后这个listener会被删除.
    private CellEditorListener innerCellEditorListener = new CellEditorListener() {

        /**
         * This tells the listeners the editor has stopped editing
         */
        public void editingStopped(CellEditorEvent evt) {
            Grid.this.stopCellEditingInner(false);
            // if(Grid.this.)
        }

        /**
         * This tells the listeners the editor has canceled editing
         */
        public void editingCanceled(CellEditorEvent evt) {
            Grid.this.cancelEditing();
        }
    };
    // FloatEditorListener, 当开始编辑的时候,这个Listener会被加入其中.停止编辑后这个listener会被删除.
    private FloatEditorListener innerFloatEditorListener = new FloatEditorListener() {

        /**
         * This tells the listeners the editor has stopped editing
         */
        public void editingStopped(FloatEditorEvent evt) {
            Grid.this.stopFloatEditingInner(false);
        }

        /**
         * This tells the listeners the editor has canceled editing
         */
        public void editingCanceled(FloatEditorEvent evt) {
            Grid.this.cancelEditing();
        }
    };

    /**
     * @param dragType
     */
    public void setDragType(int dragType) {
        this.dragType = dragType;
    }

    /**
     * @return
     */
    public int getDragType() {
        return dragType;
    }

    /**
     * @param dragRectangle
     */
    public void setDragRectangle(Rectangle dragRectangle) {
        this.dragRectangle = dragRectangle;
    }

    /**
     * @return
     */
    public Rectangle getDragRectangle() {
        return dragRectangle;
    }

    /**
     * @param x
     * @param y
     */
    public void setTooltipLocation(double x, double y) {
        if (tooltipLocation == null) {
            tooltipLocation = new Point();
        }
        this.tooltipLocation.setLocation(x, y);
    }

    public int getPaginateLineShowType() {
        return paginateLineShowType;
    }

    public void setPaginateLineShowType(int paginateLineShowType) {
        this.paginateLineShowType = paginateLineShowType;
    }
}