/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.mainframe;

import com.fr.base.BaseFormula;
import com.fr.base.DynamicUnitList;
import com.fr.base.Formula;
import com.fr.base.ScreenResolution;
import com.fr.base.Style;
import com.fr.base.vcs.DesignerMode;
import com.fr.design.DesignState;
import com.fr.design.DesignerEnvManager;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.actions.AllowAuthorityEditAction;
import com.fr.design.actions.ExitAuthorityEditAction;
import com.fr.design.actions.UpdateAction;
import com.fr.design.actions.cell.BorderAction;
import com.fr.design.actions.cell.CleanAuthorityAction;
import com.fr.design.actions.cell.style.AlignmentAction;
import com.fr.design.actions.cell.style.ReportFontBoldAction;
import com.fr.design.actions.cell.style.ReportFontForegroundAction;
import com.fr.design.actions.cell.style.ReportFontItalicAction;
import com.fr.design.actions.cell.style.ReportFontNameAction;
import com.fr.design.actions.cell.style.ReportFontSizeAction;
import com.fr.design.actions.cell.style.ReportFontUnderlineAction;
import com.fr.design.actions.cell.style.StyleBackgroundAction;
import com.fr.design.actions.columnrow.CancelColumnAction;
import com.fr.design.actions.columnrow.CancelRowAction;
import com.fr.design.actions.columnrow.ColumnHideAction;
import com.fr.design.actions.columnrow.ColumnWidthAction;
import com.fr.design.actions.columnrow.DeleteColumnAction;
import com.fr.design.actions.columnrow.DeleteRowAction;
import com.fr.design.actions.columnrow.FootColumnAction;
import com.fr.design.actions.columnrow.FootRowAction;
import com.fr.design.actions.columnrow.HeadColumnAction;
import com.fr.design.actions.columnrow.HeadRowAction;
import com.fr.design.actions.columnrow.InsertColumnAction;
import com.fr.design.actions.columnrow.InsertRowAction;
import com.fr.design.actions.columnrow.ResetColumnHideAction;
import com.fr.design.actions.columnrow.ResetRowHideAction;
import com.fr.design.actions.columnrow.RowHeightAction;
import com.fr.design.actions.columnrow.RowHideAction;
import com.fr.design.actions.core.ActionFactory;
import com.fr.design.actions.edit.CopyAction;
import com.fr.design.actions.edit.CutAction;
import com.fr.design.actions.edit.PasteAction;
import com.fr.design.actions.edit.merge.MergeCellAction;
import com.fr.design.actions.edit.merge.UnmergeCellAction;
import com.fr.design.base.mode.DesignModeContext;
import com.fr.design.cell.bar.DynamicScrollBar;
import com.fr.design.cell.clipboard.CellElementsClip;
import com.fr.design.cell.clipboard.ElementsTransferable;
import com.fr.design.cell.clipboard.FloatElementsClip;
import com.fr.design.cell.editor.BiasTextPainterCellEditor;
import com.fr.design.cell.editor.CellEditor;
import com.fr.design.cell.editor.ChartCellEditor;
import com.fr.design.cell.editor.ChartFloatEditor;
import com.fr.design.cell.editor.DSColumnCellEditor;
import com.fr.design.cell.editor.FormulaCellEditor;
import com.fr.design.cell.editor.FormulaFloatEditor;
import com.fr.design.cell.editor.ImageCellEditor;
import com.fr.design.cell.editor.ImageFloatEditor;
import com.fr.design.cell.editor.RichTextCellEditor;
import com.fr.design.cell.editor.SubReportCellEditor;
import com.fr.design.constants.UIConstants;
import com.fr.design.designer.EditingState;
import com.fr.design.designer.TargetComponent;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.fun.ElementUIProvider;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.imenu.UIPopupMenu;
import com.fr.design.mainframe.cell.QuickEditorRegion;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.menu.MenuDef;
import com.fr.design.menu.NameSeparator;
import com.fr.design.menu.ShortCut;
import com.fr.design.menu.ToolBarDef;
import com.fr.design.selection.QuickEditor;
import com.fr.design.selection.Selectedable;
import com.fr.design.selection.SelectionEvent;
import com.fr.design.selection.SelectionListener;
import com.fr.general.ComparatorUtils;
import com.fr.grid.Grid;
import com.fr.grid.GridColumn;
import com.fr.grid.GridCorner;
import com.fr.grid.GridRow;
import com.fr.grid.GridUtils;
import com.fr.grid.dnd.ElementCasePaneDropTarget;
import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.Selection;
import com.fr.log.FineLoggerFactory;
import com.fr.page.PageAttributeGetter;
import com.fr.page.ReportPageAttrProvider;
import com.fr.poly.creator.PolyElementCasePane;
import com.fr.report.ReportHelper;
import com.fr.report.cell.DefaultTemplateCellElement;
import com.fr.report.cell.FloatElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.cell.cellattr.core.RichText;
import com.fr.report.cell.cellattr.core.SubReport;
import com.fr.report.cell.cellattr.core.group.DSColumn;
import com.fr.report.cell.painter.BiasTextPainter;
import com.fr.report.cell.painter.CellImagePainter;
import com.fr.report.core.SheetUtils;
import com.fr.report.elementcase.ElementCase;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.stable.ArrayUtils;
import com.fr.stable.ColumnRow;
import com.fr.stable.unit.FU;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import java.awt.AWTEvent;
import java.awt.Adjustable;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Constructor;
import java.util.Set;

import static com.fr.design.gui.syntax.ui.rtextarea.RTADefaultInputMap.DEFAULT_MODIFIER;

/**
 * This class used to edit Report.
 */
public abstract class ElementCasePane<T extends TemplateElementCase> extends TargetComponent<T> implements Selectedable<Selection>, PageAttributeGetter {

    public enum Clear {
        ALL, FORMATS, CONTENTS, WIDGETS
    }

    public static final int NO_OVER = 0;
    public static final int HORIZONTAL_OVER = 1;
    public static final int VERTICAL_OVER = 2;

    // alex:既可以是选中一片单元格,也可以是选中一个悬浮元素
    //august:默认是个不存在的选择。方便初始化时触发GridSelectionChangeListener事件
    private Selection selection = new CellSelection(-1, -1, -1, -1);

    // alex:
    private boolean supportDefaultParentCalculate = false;
    // GUI.
    private Grid grid;
    private GridRow gridRow;
    private GridColumn gridColumn;
    private GridCorner gridCorner;
    private JScrollBar verScrollBar;
    private JScrollBar horScrollBar;
    // Visible
    private boolean columnHeaderVisible = true;
    private boolean rowHeaderVisible = true;
    private boolean verticalScrollBarVisible = true;
    private boolean horizontalScrollBarVisible = true;

    private int resolution;
    protected UIButton formatBrush = null;

    private CellSelection formatReferencedCell = null;
    private CellSelection cellNeedTOFormat = null;
    private FormatBrushAction formatBrushAction;
    private ActionListener keyListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!formatBrush.isSelected()) {
                DesignerContext.setFormatState(DesignerContext.FORMAT_STATE_ONCE);
                DesignerContext.setReferencedElementCasePane(ElementCasePane.this);
                DesignerContext.setReferencedIndex(
                        ((JWorkBook) HistoryTemplateListPane.getInstance().getCurrentEditingTemplate()).getEditingReportIndex());
                formatBrush.setSelected(true);
                formatBrushAction.executeActionReturnUndoRecordNeeded();
            } else {
                cancelFormatBrush();
            }
        }
    };
    private ActionListener escKey = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            cancelFormatBrush();
        }
    };


    /**
     * Constructor.
     */
    public ElementCasePane(T t) {
        super(t);
        // marks:能触发processEvent，不管是否给component增加listener。这里是使在reportPane中的任意位置滑动鼠标轮都能
        // 下拉grid。
        enableEvents(AWTEvent.MOUSE_WHEEL_EVENT_MASK);
        this.initComponents();

        new ElementCasePaneDropTarget(this);
        this.setFocusTraversalKeysEnabled(false);

    }

    /**
     * 取消格式化
     */
    @Override
    public void cancelFormat() {
        return;
    }

    protected void initComponents() {
        this.setLayout(new RGridLayout());

        //todo 直接修改分辨率
        if (this.resolution == 0) {
            this.resolution = ScreenResolution.getScreenResolution();
        }

        this.initGridComponent();

        this.grid.setElementCasePane(this);
        this.gridColumn.setElementCasePane(this);
        this.gridRow.setElementCasePane(this);
        this.gridCorner.setElementCasePane(this);

        this.add(RGridLayout.GridCorner, this.gridCorner);
        this.add(RGridLayout.GridColumn, this.gridColumn);
        this.add(RGridLayout.GridRow, this.gridRow);
        this.add(RGridLayout.Grid, this.grid);

        // ScrollBar
        verScrollBar = new DynamicScrollBar(Adjustable.VERTICAL, this, this.resolution);
        horScrollBar = new DynamicScrollBar(Adjustable.HORIZONTAL, this, this.resolution);
        this.add(RGridLayout.VerticalBar, this.verScrollBar);

        // Init input/action map defaultly.
        initInputActionMap();

        // 设置最小的尺寸,方便 ScrollPane.
        this.setMinimumSize(new Dimension(0, 0));

        // alex:初始化Editors
        initDefaultEditors();
        initFormatBrush();
    }

    @Override
    public int getMenuState() {
        return DesignState.WORK_SHEET;
    }


    protected void initFormatBrush() {
        formatBrushAction = new FormatBrushAction(this);
        formatBrush = (UIButton) formatBrushAction.createToolBarComponent();
        formatBrush.setSelected(DesignerContext.getFormatState() != DesignerContext.FORMAT_STATE_NULL);
        formatBrush.removeActionListener(formatBrushAction);
        formatBrush.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //如果没有格式刷，点击时就是想使用格式刷
                if (e.getClickCount() == 1) {
                    if (!formatBrush.isSelected()) {
                        DesignerContext.setFormatState(DesignerContext.FORMAT_STATE_ONCE);
                        DesignerContext.setReferencedElementCasePane(ElementCasePane.this);
                        DesignerContext.setReferencedIndex(
                                ((JTemplate) HistoryTemplateListPane.getInstance().getCurrentEditingTemplate()).getEditingReportIndex());
                        formatBrush.setSelected(true);
                        formatBrushAction.executeActionReturnUndoRecordNeeded();
                    } else {
                        cancelFormatBrush();
                    }

                } else if (e.getClickCount() == 2) {
                    if (!formatBrush.isSelected()) {
                        formatBrush.setSelected(true);
                    }
                    DesignerContext.setFormatState(DesignerContext.FORMAT_STATE_MORE);
                    DesignerContext.setReferencedElementCasePane(ElementCasePane.this);
                    DesignerContext.setReferencedIndex(
                            ((JTemplate) HistoryTemplateListPane.getInstance().getCurrentEditingTemplate()).getEditingReportIndex());
                    formatBrush.setSelected(true);
                    formatBrushAction.executeActionReturnUndoRecordNeeded();
                }
            }
        });
        formatBrush.registerKeyboardAction(keyListener, KeyStroke.getKeyStroke(KeyEvent.VK_B, DEFAULT_MODIFIER), JComponent.WHEN_IN_FOCUSED_WINDOW);
        formatBrush.registerKeyboardAction(escKey, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    /**
     * 取消格式刷
     */
    public void cancelFormatBrush() {
        //如果正在使用格式刷，点击就是想退出格式刷
        setFormatState(DesignerContext.FORMAT_STATE_NULL);
        formatBrush.setSelected(false);
        grid.setCursor(UIConstants.CELL_DEFAULT_CURSOR);
        if (DesignerContext.getReferencedElementCasePane() == null) {
            return;
        }

        ((ElementCasePane) DesignerContext.getReferencedElementCasePane()).getGrid().setNotShowingTableSelectPane(true);
        ((ElementCasePane) DesignerContext.getReferencedElementCasePane()).getGrid().setCursor(UIConstants.CELL_DEFAULT_CURSOR);
        DesignerContext.setReferencedElementCasePane(null);
        DesignerContext.setReferencedIndex(0);
        repaint();
    }

    public UIButton getFormatBrush() {
        return formatBrush;
    }

    public void setFormatState(int formatState) {
        DesignerContext.setFormatState(formatState);
        if (formatState == DesignerContext.FORMAT_STATE_NULL) {
            cellNeedTOFormat = null;
        }

    }

    public JPanel getEastUpPane() {
        return new JPanel();
    }


    public JPanel getEastDownPane() {
        return new JPanel();
    }


    public FormatBrushAction getFormatBrushAction() {
        return formatBrushAction;
    }

    protected void initGridComponent() {
        // Components
        if (this.grid == null) {
            this.grid = new Grid(this.resolution);
        }
        if (this.gridColumn == null) {
            this.gridColumn = new GridColumn();
        }
        if (this.gridRow == null) {
            this.gridRow = new GridRow();
        }
        if (this.gridCorner == null) {
            this.gridCorner = new GridCorner();
        }
    }


    public void setResolution(int resolution) {
        this.resolution = resolution;
    }

    public int getResolution() {
        return this.resolution;
    }

    /**
     * 所有的操作都必须在可见范围内，否则不做任何操作
     *
     * @return true 必须在可见范围内.
     */
    public boolean mustInVisibleRange() {
        return true;
    }

    /*
     * 初始化默认的Editor
     */
    private void initDefaultEditors() {
        Grid grid = this.getGrid();
        grid.setDefaultCellEditor(DSColumn.class, new DSColumnCellEditor(this));
        grid.setDefaultCellEditor(BaseFormula.class, new FormulaCellEditor(this));
        grid.setDefaultCellEditor(RichText.class, new RichTextCellEditor(this));

        grid.setDefaultCellEditor(BiasTextPainter.class, new BiasTextPainterCellEditor(this));
        grid.setDefaultCellEditor(Image.class, new ImageCellEditor(this));
        grid.setDefaultCellEditor(CellImagePainter.class, new ImageCellEditor(this));
        grid.setDefaultCellEditor(SubReport.class, new SubReportCellEditor(this));

        Class chartClass = ActionFactory.getChartCollectionClass();
        if (chartClass != null) {
            grid.setDefaultCellEditor(chartClass, new ChartCellEditor(this));
            grid.setDefaultFloatEditor(chartClass, new ChartFloatEditor());
        }

        addExtraCellEditor(grid);

        grid.setDefaultFloatEditor(Formula.class, new FormulaFloatEditor());
        grid.setDefaultFloatEditor(Image.class, new ImageFloatEditor());
        grid.setDefaultFloatEditor(CellImagePainter.class, new ImageFloatEditor());

        DesignerEnvManager designerEnvManager = DesignerEnvManager.getEnvManager();
        grid.setGridLineColor(designerEnvManager.getGridLineColor());
        grid.setPaginationLineColor(designerEnvManager.getPaginationLineColor());
    }

    private void addExtraCellEditor(Grid grid) {
        Set<ElementUIProvider> providers = ExtraDesignClassManager.getInstance().getArray(ElementUIProvider.MARK_STRING);
        for (ElementUIProvider provider : providers) {
            CellEditor editor = null;
            Class<?> clazz = provider.targetCellEditorClass();
            Constructor<?> c;
            try {
                c = clazz.getConstructor();
                editor = (CellEditor) c.newInstance();
            } catch (NoSuchMethodException e) {
                try {
                    c = clazz.getConstructor(ElementCase.class);
                    editor = (CellEditor) c.newInstance(this);
                } catch (Exception e1) {
                    FineLoggerFactory.getLogger().error(e1.getMessage(), e1);
                }
            } catch (Exception e) {
                FineLoggerFactory.getLogger().error(e.getMessage(), e);
            }
            grid.setDefaultCellEditor(provider.targetObjectClass(), editor);
        }
    }

    /**
     * 返回当前正在编辑的模板单元格组件
     */
    public final TemplateElementCase getEditingElementCase() {
        return this.getTarget();
    }

    /**
     * Gets the cell table of rpt pane. Grid implement the the most
     * functionalities of table.
     */
    public Grid getGrid() {
        return this.grid;
    }

    /**
     * Gets the column header of cell table.
     */
    public GridColumn getGridColumn() {
        return this.gridColumn;
    }

    /**
     * Gets the row header of cell table.
     */
    public GridRow getGridRow() {
        return this.gridRow;
    }

    /**
     * Gets the left top corner of cell table
     */
    public GridCorner getGridCorner() {
        return this.gridCorner;
    }

    /**
     * Gets vertical scroll bar
     */
    public JScrollBar getVerticalScrollBar() {
        return this.verScrollBar;
    }

    /**
     * Gets horizontal scroll bar
     */
    public JScrollBar getHorizontalScrollBar() {
        return this.horScrollBar;
    }

    /**
     * 是否支持默认计算父格
     *
     * @return 是则返回true
     */
    public boolean isSupportDefaultParentCalculate() {
        return supportDefaultParentCalculate;
    }

    /**
     * Sets whether to support auto calculate default parent
     *
     * @param supportDefaultParentCalculate whether to support auto calculate default parent.
     */
    public void setSupportDefaultParentCalculate(boolean supportDefaultParentCalculate) {
        this.supportDefaultParentCalculate = supportDefaultParentCalculate;
    }

    /**
     * 转换选择
     *
     * @return 转换
     */
    public ElementsTransferable transferSelection() {
        ElementsTransferable elementsTransferable = new ElementsTransferable();

        this.selection.asTransferable(elementsTransferable, this);

        return elementsTransferable;
    }

    /**
     * @return
     */
    public QuickEditor getCurrentEditor() {
        return this.selection.getQuickEditor(this);
    }

    @Override
    public void setSelection(Selection selection) {
        if (!ComparatorUtils.equals(this.selection, selection)
                || !ComparatorUtils.equals(EastRegionContainerPane.getInstance().getCellAttrPane(), CellElementPropertyPane.getInstance())
                || DesignModeContext.isAuthorityEditing()) {
            try {
                //旧选中内容编辑器释放模板对象
                QuickEditor editor = this.getCurrentEditor();
                if (editor != null) {
                    editor.release();
                }
            } catch (UnsupportedOperationException e) {
                FineLoggerFactory.getLogger().info("Nothing to release");
            }
            this.selection = selection;
            fireSelectionChanged();
        }
    }


    public void setOldSelecton(Selection selection) {
        this.selection = selection;
    }


    public void setFormatReferencedCell(CellSelection cellSelection) {
        this.formatReferencedCell = cellSelection;
        getOldStyles(formatReferencedCell);
    }


    private void getOldStyles(CellSelection oldSelection) {
        Style[][] referencedStyle = new Style[formatReferencedCell.getColumnSpan()][formatReferencedCell.getRowSpan()];
        int cellRectangleCount = oldSelection.getCellRectangleCount();
        TemplateElementCase elementCase = getEditingElementCase();
        for (int rect = 0; rect < cellRectangleCount; rect++) {
            Rectangle cellRectangle = oldSelection.getCellRectangle(rect);
            for (int j = 0; j < cellRectangle.height; j++) {
                for (int i = 0; i < cellRectangle.width; i++) {
                    int column = i + cellRectangle.x;
                    int row = j + cellRectangle.y;
                    TemplateCellElement cellElement = elementCase.getTemplateCellElement(column, row);
                    if (cellElement == null) {
                        cellElement = new DefaultTemplateCellElement(column, row);
                        elementCase.addCellElement(cellElement);
                    }
                    Style style = cellElement.getStyle();
                    if (style == null) {
                        style = Style.DEFAULT_STYLE;
                    }

                    referencedStyle[i][j] = style;
                }
            }
        }

        DesignerContext.setReferencedStyle(referencedStyle);
    }

    public CellSelection getFormatReferencedCell() {
        return this.formatReferencedCell;
    }


    @Override
    public Selection getSelection() {
        return selection;
    }

    /**
     * 是否只选中了一个单元格
     *
     * @return 是则返回true
     */
    public boolean isSelectedOneCell() {
        return (selection != null) && selection.isSelectedOneCell(this);
    }

    /**
     * 停止编辑
     */
    public void stopEditing() {
        this.getGrid().stopEditing();
    }

    /**
     * 剪切
     *
     * @return 成功返回true
     */
    public boolean cut() {
        if (DesignModeContext.isBanCopyAndCut()) {
            FineLoggerFactory.getLogger().debug("Prohibit Cut");
            return false;
        }

        this.copy();

        return this.clearAll();
    }

    /**
     * 复制
     */
    public void copy() {
        if (DesignModeContext.isBanCopyAndCut()) {
            return;
        }
        // p:Elements Transferable.
        ElementsTransferable elementsTransferable = this.transferSelection();

        // peter:这个地方产生String序列,方便从FR当中copy数据到Excel当中.
        Object firstObject = elementsTransferable.getFirstObject();
        if (firstObject instanceof CellElementsClip) {
            elementsTransferable.addObject(((CellElementsClip) firstObject).compateExcelPaste());
        }

        // p:加到Clipboard里面去.
        Clipboard clipboard = DesignerContext.getClipboard(this.getGrid());

        clipboard.setContents(elementsTransferable, elementsTransferable);
    }

    /**
     * 黏贴
     *
     * @return 成功返回 true
     */
    public boolean paste() {
        if (!this.isEditable()) {
            return false;
        }
        Object clipObject = getClipObject();
        //如果是悬浮元素，则不允许粘贴到表单 
        if (!DesignerContext.getDesignerFrame().getSelectedJTemplate().accept(clipObject)) {
            return false;
        }

        if (clipObject instanceof FloatElementsClip) {
            return this.selection.pasteFloatElementClip((FloatElementsClip) clipObject, this);
        } else if (clipObject instanceof CellElementsClip) {
            return this.selection.pasteCellElementsClip((CellElementsClip) clipObject, this);
        } else if (clipObject instanceof String) {
            return this.selection.pasteString((String) clipObject, this);
        } else {
            return this.selection.pasteOtherType(clipObject, this);
        }
    }

    public Object getClipObject() {
        // 需要检查是否可以编辑。
        Clipboard clipboard = DesignerContext.getClipboard(this.getGrid());
        Transferable clipData = clipboard.getContents(this);
        if (clipData == null) {// 剪贴板里面没有值,直接返回.
            return null;
        }
        Object clipObject;
        try {
            clipObject = ElementsTransferable.getElementNotStringTransderData(clipData);
        } catch (Exception exp) {
            // p:如果是其他不支持的对象，会抛出Exception的,所以变成String。
            // 所以这里不打印出Exception.
            try {
                clipObject = clipData.getTransferData(DataFlavor.stringFlavor);
            } catch (Exception e) {
                FineLoggerFactory.getLogger().error(e.getMessage(), e);
                return null;
            }
        }

        return clipObject;
    }

    /**
     * 确认行列是否可见
     *
     * @param column 列序号
     * @param row    行序号
     * @return 类型
     */
    public int ensureColumnRowVisible(int column, int row) {
        Grid grid = this.getGrid();

        int verticalEndValue = grid.getVerticalValue() + grid.getVerticalExtent() + 1;
        int horizontalEndValue = grid.getHorizontalValue() + grid.getHorizontalExtent() + 1;

        int overtype = NO_OVER;
        // 列.
        if (grid.getHorizontalValue() > column) {
            this.getHorizontalScrollBar().setValue(column);
        } else if (horizontalEndValue < (column + 1)) {
            if (this instanceof PolyElementCasePane) {
                //如果是聚合块中的Gird,选在边界的时候，会报表会自动向右移
                overtype += HORIZONTAL_OVER;
            } else {
                this.getHorizontalScrollBar().setValue(column - grid.getHorizontalExtent() + 1);
            }

        }

        // 行.
        if (grid.getVerticalValue() > row) {
            this.getVerticalScrollBar().setValue(row);
        } else if (verticalEndValue <= (row + 1)) {// p: +1是保证最后一行，不能显示半行.
            if (this instanceof PolyElementCasePane) {
                //如果是聚合块中的Gird,选在边界的时候，会报表会自动向下移
                overtype += VERTICAL_OVER;
            } else {
                this.getVerticalScrollBar().setValue(row - grid.getVerticalExtent() + 1);
            }

        }
        return overtype;
    }

    /**
     * 清空所有.
     *
     * @return 返回是否清除全部.
     */
    public boolean clearAll() {
        boolean b = this.selection.clear(Clear.ALL, this);
        fireSelectionChanged();
        return b;
    }

    /**
     * 清除格式.
     *
     * @return 返回是否清除格式.
     */
    public boolean clearFormats() {
        boolean b = this.selection.clear(Clear.FORMATS, this);
        fireSelectionChanged();
        return b;
    }

    /**
     * 清除内容
     *
     * @return 返回是否清除内容.
     */
    public boolean clearContents() {
        if (DesignerMode.isAuthorityEditing()) {
            return false;
        }
        boolean b = this.selection.clear(Clear.CONTENTS, this);
        fireSelectionChanged();
        return b;
    }

    /**
     * 清除控件
     *
     * @return 返回是否清除控件.
     */
    public boolean clearWidget() {
        boolean b = this.selection.clear(Clear.WIDGETS, this);
        fireSelectionChanged();
        return b;
    }

    /**
     * 是否能合并单元格
     *
     * @return 返回是否能合并单元格.
     */
    public boolean canMergeCell() {
        return this.selection.canMergeCells(this);
    }

    /**
     * 合并单元格
     *
     * @return 成功返回true
     */
    public boolean mergeCell() {
        boolean b = this.selection.mergeCells(this);
        fireSelectionChanged();
        return b;
    }

    /**
     * 是否撤销合并单元格
     *
     * @return 返回是否撤销合并单元格.
     */
    public boolean canUnMergeCell() {
        return this.selection.canUnMergeCells(this);
    }

    /**
     * 撤销合并单元格
     *
     * @return 成功返回true
     */
    public boolean unMergeCell() {
        boolean b = this.selection.unMergeCells(this);
        fireSelectionChanged();
        return b;
    }

    /**
     * Fire gridSelection Changed
     */
    private void fireSelectionChanged() {
        this.fireSelectionChangeListener();
        this.repaint(15);
    }

    /**
     * 添加选中的SelectionListener
     *
     * @param selectionListener 选中的listener
     */
    public void addSelectionChangeListener(SelectionListener selectionListener) {
        this.listenerList.add(SelectionListener.class, selectionListener);
    }


    /**
     * 删除选中的ChangeListener
     *
     * @param selectionListener 选中的listener
     */
    public void removeSelectionChangeListener(SelectionListener selectionListener) {
        this.listenerList.remove(SelectionListener.class, selectionListener);
    }

    /**
     * 响应选中的ChangeListener
     */
    public void fireSelectionChangeListener() {
        // Guaranteed to return a non-null array
        final Object[] listeners = listenerList.getListenerList();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Process the listeners last to first, notifying
                // those that are interested in this event
                for (int i = listeners.length - 2; i >= 0; i -= 2) {
                    if (listeners[i] == SelectionListener.class) {
                        ((SelectionListener) listeners[i + 1]).selectionChanged(new SelectionEvent(ElementCasePane.this));
                    }
                }
            }
        });
    }

    /**
     * 响应目标变动.
     */
    @Override
    public void fireTargetModified() {
        // marks:自动计算
        TemplateElementCase report = this.getEditingElementCase();

        // 计算父格
        if (this.isSupportDefaultParentCalculate()) {
            SheetUtils.calculateDefaultParent(report);
            setSupportDefaultParentCalculate(false);
        }

        super.fireTargetModified();

    }

    /**
     * Init input/action map.
     */
    protected void initInputActionMap() {
        InputMap inputMapAncestor = this.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        ActionMap actionMap = this.getActionMap();
        // clearReportPage old values.
        inputMapAncestor.clear();
        actionMap.clear();
        if (!DesignModeContext.isBanCopyAndCut()) {
            inputMapAncestor.put(KeyStroke.getKeyStroke(KeyEvent.VK_X, DEFAULT_MODIFIER), "cut");
            actionMap.put("cut", new AbstractAction() {
                public void actionPerformed(ActionEvent evt) {
                    if (cut()) {
                        fireTargetModified();
                    }
                }
            });
            inputMapAncestor.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, DEFAULT_MODIFIER), "copy");
            actionMap.put("copy", new AbstractAction() {
                public void actionPerformed(ActionEvent evt) {
                    copy();
                }
            });
        }
        inputMapAncestor.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, DEFAULT_MODIFIER), "paste");
        actionMap.put("paste", new AbstractAction() {
            public void actionPerformed(ActionEvent evt) {
                if (paste()) {
                    QuickEditorRegion.getInstance().populate(getCurrentEditor());
                    fireTargetModified();
                    QuickEditorRegion.getInstance().populate(getCurrentEditor());
                }
            }
        });
        inputMapAncestor.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "delete_content");
        actionMap.put("delete_content", new AbstractAction() {
            public void actionPerformed(ActionEvent evt) {
                if (clearContents()) {
                    fireTargetModified();
                }
            }
        });
        inputMapAncestor.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, DEFAULT_MODIFIER), "delete_all");
        actionMap.put("delete_all", new AbstractAction() {
            public void actionPerformed(ActionEvent evt) {
                if (clearAll()) {
                    fireTargetModified();
                }
            }
        });
    }

    /**
     * 是否列表头可见
     *
     * @return boolean 是否列表头可见.
     */
    public boolean isColumnHeaderVisible() {
        return this.columnHeaderVisible;
    }

    /**
     * Makes the column visible or invisible.
     *
     * @param isColumnVisible true to make the column visible; false to make it invisible
     */
    public void setColumnHeaderVisible(boolean isColumnVisible) {
        this.columnHeaderVisible = isColumnVisible;

        this.resizeAndRepaint();
    }

    /**
     * 是否行表头可见.
     *
     * @return boolean 是否行表头可见.
     */
    public boolean isRowHeaderVisible() {
        return this.rowHeaderVisible;
    }

    /**
     * Makes the row visible or invisible.
     *
     * @param isRowVisible true to make the row visible; false to make it invisible
     */
    public void setRowHeaderVisible(boolean isRowVisible) {
        this.rowHeaderVisible = isRowVisible;

        this.resizeAndRepaint();
    }

    /**
     * 是否垂直滚动条可见
     *
     * @return boolean 是否垂直滚动条可见.
     */
    public boolean isVerticalScrollBarVisible() {
        return verticalScrollBarVisible;
    }

    /**
     * Makes the vertical scrollbar visible or invisible.
     *
     * @param verticalScrollBarVisible true to make the vertical scrollbar visible; false to make it
     *                                 invisible
     */
    public void setVerticalScrollBarVisible(boolean verticalScrollBarVisible) {
        this.verticalScrollBarVisible = verticalScrollBarVisible;
    }

    /**
     * 是否水平滚动条可见.
     *
     * @return boolean  是否水平滚动条可见.
     */
    public boolean isHorizontalScrollBarVisible() {
        return horizontalScrollBarVisible;
    }

    /**
     * Makes the horizontal scrollbar visible or invisible.
     *
     * @param horizontalScrollBarVisible true to make the horizontal scrollbar visible; false to make
     *                                   it invisible
     */
    public void setHorizontalScrollBarVisible(boolean horizontalScrollBarVisible) {
        this.horizontalScrollBarVisible = horizontalScrollBarVisible;
    }

    // //////////////////////////////Max Row and Column.

    /**
     * 是否可编辑.
     *
     * @return boolean  是否可编辑.
     */
    public boolean isEditable() {
        return this.grid.isEditable();
    }

    /**
     * Sets whether to editable.
     *
     * @param editable 是否可编辑.
     */
    public void setEditable(boolean editable) {
        this.grid.setEditable(editable);
    }


    /**
     * 弹出列表.
     *
     * @return 弹出列表.
     */
    public JPopupMenu createPopupMenu() {
        return this.selection.createPopupMenu(this);
    }

    /**
     * 弹出列表
     *
     * @param evt          鼠标的响应事件.
     * @param selectedRows 选中的行..
     * @return UIPopupMenu 返回行的列表
     */
    public UIPopupMenu createRowPopupMenu(MouseEvent evt, int selectedRows) {
        UIPopupMenu popupMenu = new UIPopupMenu();
        if (DesignerMode.isAuthorityEditing()) {
            popupMenu.add(new CleanAuthorityAction(this).createMenuItem());
            return popupMenu;
        }

        InsertRowAction insertRowAction = new InsertRowAction(this, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Utils_Insert_Row"));

        DeleteRowAction deleteRowAction = new DeleteRowAction(this, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Utils_Delete_Row"));

        RowHeightAction rowHeightAction = new RowHeightAction(this, selectedRows);

        RowHideAction rowHideAction = new RowHideAction(this, selectedRows);

        ResetRowHideAction resetRowHideAction = new ResetRowHideAction(this, selectedRows);
        popupMenu.add(insertRowAction.createMenuItem());
        popupMenu.add(deleteRowAction.createMenuItem());
        popupMenu.addSeparator();
        popupMenu.add(rowHeightAction.createMenuItem());
        if (supportRepeatedHeaderFooter()) {
            CellSelection cs = (CellSelection) this.getSelection();
            addRowMenu(popupMenu, evt, cs.getRow(), cs.getRow() + cs.getRowSpan() - 1);
        }
        popupMenu.add(rowHideAction.createMenuItem());
        popupMenu.add(resetRowHideAction.createMenuItem());
        return popupMenu;
    }

    private void addRowMenu(JPopupMenu popupMenu, MouseEvent evt, int selectedRowsFrom, int selectedRowsTo) {
        HeadRowAction headrowAction = new HeadRowAction(this);
        FootRowAction footrowAction = new FootRowAction(this);

        ElementCase elementCase = this.getEditingElementCase();
        boolean cancel = false;
        ColumnRow selectedCellPoint = GridUtils.getAdjustEventColumnRow_withresolution(this, evt.getX(), evt.getY(), this.resolution);
        ReportPageAttrProvider reportPageAttr = elementCase.getReportPageAttr();
        ElementCase report = this.getEditingElementCase();
        if (reportPageAttr != null) {
            popupMenu.addSeparator();
            popupMenu.add(headrowAction.createMenuItem());
            popupMenu.add(footrowAction.createMenuItem());
            int from = report.getReportPageAttr().getRepeatHeaderRowFrom();
            int to = report.getReportPageAttr().getRepeatHeaderRowTo();
            int from2 = report.getReportPageAttr().getRepeatFooterRowFrom();
            int to2 = report.getReportPageAttr().getRepeatFooterRowTo();

            if (selectedCellPoint.getRow() >= selectedRowsFrom && selectedCellPoint.getRow() <= selectedRowsTo) {

                cancel = isCancel(report, selectedRowsFrom, selectedRowsTo, from, to, from2, to2);

            } else {

                int row = selectedCellPoint.getRow();
                if (report.getReportPageAttr() != null) {
                    if (row == from || row == to || row == from2 || row == to2) {
                        cancel = true;
                    }
                }
            }

            if (cancel) {
                CancelRowAction cancelAction = new CancelRowAction(this);
                popupMenu.add(cancelAction.createMenuItem());
            }
        }
    }

    private boolean isCancel(ElementCase report, int selectedColumnsFrom, int selectedColumnsTo, int from, int to, int from2, int to2) {

        boolean cancel = false;

        if (report.getReportPageAttr() != null) {

            if (from == selectedColumnsFrom && to == selectedColumnsTo) {
                cancel = true;
            }

            if (from2 == selectedColumnsFrom && to2 == selectedColumnsTo) {
                cancel = true;
            }
        }
        return cancel;
    }

    /**
     * 弹出列表.
     *
     * @param evt            鼠标事件
     * @param selectedColumn 选中的列.
     * @return 弹出的列表.
     */
    public UIPopupMenu createColumnPopupMenu(MouseEvent evt, int selectedColumn) {
        UIPopupMenu popupMenu = new UIPopupMenu();

        if (DesignerMode.isAuthorityEditing()) {
            popupMenu.add(new CleanAuthorityAction(this).createMenuItem());
            return popupMenu;
        }

        InsertColumnAction insertColumnAction = new InsertColumnAction(this, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Utils_Insert_Column"));

        DeleteColumnAction deleteColumnAction = new DeleteColumnAction(this, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Utils_Delete_Column"));

        ColumnWidthAction columnWidthAction = new ColumnWidthAction(this, selectedColumn);

        ColumnHideAction columnHideAction = new ColumnHideAction(this, selectedColumn);

        ResetColumnHideAction resetRowHideAction = new ResetColumnHideAction(this, selectedColumn);
        popupMenu.add(insertColumnAction.createMenuItem());
        popupMenu.add(deleteColumnAction.createMenuItem());
        popupMenu.addSeparator();
        popupMenu.add(columnWidthAction.createMenuItem());

        if (supportRepeatedHeaderFooter()) {
            CellSelection cs = (CellSelection) this.getSelection();
            addColumnMenu(popupMenu, evt, cs.getColumn(), cs.getColumn() + cs.getColumnSpan() - 1);
        }
        popupMenu.add(columnHideAction.createMenuItem());
        popupMenu.add(resetRowHideAction.createMenuItem());

        return popupMenu;
    }

    private void addColumnMenu(UIPopupMenu popupMenu, MouseEvent evt, int selectedColumnsFrom, int selectedColumnsTo) {
        HeadColumnAction headcolumnAction = new HeadColumnAction(this);
        FootColumnAction footcolumnAction = new FootColumnAction(this);

        ColumnRow selectedCellPoint = GridUtils.getAdjustEventColumnRow_withresolution(this, evt.getX(), evt.getY(), this.resolution);
        ElementCase elementCase = this.getEditingElementCase();
        boolean cancel = false;
        ReportPageAttrProvider reportPageAttr = elementCase.getReportPageAttr();

        if (reportPageAttr != null) {
            popupMenu.addSeparator();
            popupMenu.add(headcolumnAction.createMenuItem());
            popupMenu.add(footcolumnAction.createMenuItem());
            int from = reportPageAttr.getRepeatHeaderColumnFrom();
            int to = reportPageAttr.getRepeatHeaderColumnTo();
            int from2 = reportPageAttr.getRepeatFooterColumnFrom();
            int to2 = reportPageAttr.getRepeatFooterColumnTo();
            int column = selectedCellPoint.getColumn();

            if (column >= selectedColumnsFrom && column <= selectedColumnsTo) {
                cancel = isCancel(elementCase, selectedColumnsFrom, selectedColumnsTo, from, to, from2, to2);
            } else {

                if (elementCase.getReportPageAttr() != null) {

                    if (column == from || column == to || column == from2 || column == to2) {
                        cancel = true;
                    }
                }
            }
            if (cancel) {
                CancelColumnAction cancelAction = new CancelColumnAction(this);
                popupMenu.add(cancelAction.createMenuItem());
            }
        }
    }

    protected abstract boolean supportRepeatedHeaderFooter();

    /**
     * 请求焦点
     */
    @Override
    public void requestFocus() {
        super.requestFocus();
        this.getGrid().requestFocus();
    }

    /**
     * Equivalent to <code>revalidate</code> followed by <code>repaint</code>.
     */
    protected void resizeAndRepaint() {
        revalidate();
        repaint();
    }

    /**
     * 模板menu
     *
     * @return 返回弹出列表数组.
     */
    public ShortCut[] shortcut4TemplateMenu() {
        return new ShortCut[0];
    }

    /**
     * 权限细粒度状态下的菜单
     *
     * @return 菜单
     */
    public ShortCut[] shortCuts4Authority() {
        return new ShortCut[]{
                new NameSeparator(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Edit_DashBoard_Potence")),
                DesignerMode.isAuthorityEditing() ? new ExitAuthorityEditAction(this) : new AllowAuthorityEditAction(this),
        };

    }

    /**
     * 目标的下列列表
     *
     * @return 返回MenuDef数组.
     */
    public MenuDef[] menus4Target() {
        return new MenuDef[0];
    }

    /**
     * kunsnat: 工具栏 快捷键菜单按钮. 最后位置为 "插入悬浮元素"  文本, 公式, 图表, 图片.
     *
     * @return 返回工具栏数组.
     */
    public ToolBarDef[] toolbars4Target() {
        return new ToolBarDef[]{
                createFontToolBar(),
                createAlignmentToolBar(),
                createStyleToolBar(),
                createCellToolBar(),
                createInsertToolBar()};
    }

    /**
     * 表单下的工具按钮
     *
     * @return 工具按钮
     */
    public JComponent[] toolBarButton4Form() {
        formatBrush.setSelected(DesignerContext.getFormatState() != DesignerContext.FORMAT_STATE_NULL);
        return new JComponent[]{new CutAction(this).createToolBarComponent(), new CopyAction(this).createToolBarComponent(),
                new PasteAction(this).createToolBarComponent(), formatBrush};
    }

    protected ToolBarDef createFontToolBar() {
        return ShortCut.asToolBarDef(new ShortCut[]{
                new ReportFontNameAction(this),
                new ReportFontSizeAction(this),
                new ReportFontBoldAction(this),
                new ReportFontItalicAction(this),
                new ReportFontUnderlineAction(this),});
    }

    protected ToolBarDef createAlignmentToolBar() {
        return ShortCut.asToolBarDef(new ShortCut[]{
                new AlignmentAction(this)}
        );
    }

    protected ToolBarDef createStyleToolBar() {
        return ShortCut.asToolBarDef(new ShortCut[]{
                new BorderAction(this),
                new StyleBackgroundAction(this),
                new ReportFontForegroundAction(this),});
    }

    protected ToolBarDef createCellToolBar() {
        return ShortCut.asToolBarDef(new ShortCut[]{
                new MergeCellAction(this),
                new UnmergeCellAction(this),});
    }

    protected ToolBarDef createInsertToolBar() {
        MenuDef insertFloatMenu = new MenuDef();
        insertFloatMenu.setName(KeySetUtils.INSERT_FLOAT.getMenuKeySetName());
        insertFloatMenu.setTooltip(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_T_Insert_Float"));
        insertFloatMenu.setIconPath("/com/fr/design/images/m_insert/floatPop.png");

        UpdateAction[] actions = ActionFactory.createFloatInsertAction(ElementCasePane.class, this);
        for (int i = 0; i < actions.length; i++) {
            insertFloatMenu.addShortCut(actions[i]);
        }
        UpdateAction[] cellInsertActions = ActionFactory.createCellInsertAction(ElementCasePane.class, this);
        ShortCut[] shortCuts = new ShortCut[cellInsertActions.length];
        System.arraycopy(cellInsertActions, 0, shortCuts, 0, cellInsertActions.length);
        return ShortCut.asToolBarDef((ShortCut[]) ArrayUtils.add(shortCuts, insertFloatMenu));
    }


    /**
     * 创建权限犀利段编辑面板
     *
     * @return 面板
     */
    public AuthorityEditPane createAuthorityEditPane() {
        return new ElementCasePaneAuthorityEditPane(this);
    }

    /**
     * 创建正在编辑的状态.
     *
     * @return 返回正在编辑的状态.
     */
    @Override
    public EditingState createEditingState() {
        return new ElementCaseEditingState(this.selection, this.verScrollBar.getValue(), this.horScrollBar.getValue(), this.resolution);
    }

    public void setCellNeedTOFormat(CellSelection selection) {
        cellNeedTOFormat = selection;
    }

    public CellSelection getCellNeedTOFormat() {
        return cellNeedTOFormat;
    }


    private class ElementCaseEditingState implements EditingState {
        protected Selection selection;
        protected int verticalValue = 0;
        protected int horizontalValue = 0;
        protected int resolution = ScreenResolution.getScreenResolution();

        protected ElementCaseEditingState(Selection selection, int verticalValue, int horizontalValue, int resolution) {
            try {
                this.selection = selection.clone();
                this.resolution = resolution;
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
            this.verticalValue = verticalValue;
            this.horizontalValue = horizontalValue;
            this.resolution = resolution;
        }

        @Override
        public void revert() {
            try {
                ElementCasePane.this.selection = this.selection.clone();
                ElementCasePane.this.fireSelectionChangeListener();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }

            ElementCasePane.this.getVerticalScrollBar().setValue(this.verticalValue);
            ElementCasePane.this.getHorizontalScrollBar().setValue(this.horizontalValue);
            HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().setScale(this.resolution);
            // 重绘.
            ElementCasePane.this.repaint();
        }
    }

    /**
     * 增加悬浮元素在面板中
     *
     * @param floatElement 元素
     */
    public void addFloatElementToCenterOfElementPane(FloatElement floatElement) {
        ElementCase report = this.getEditingElementCase();
        // 500, 270
        long floatWidth = floatElement.getWidth().toFU();
        long floatHeight = floatElement.getHeight().toFU();

        int horizontalValue = this.getGrid().getHorizontalValue();
        int horizontalExtent = this.getGrid().getHorizontalExtent();
        int verticalValue = this.getGrid().getVerticalValue();
        int verticalExtent = this.getGrid().getVerticalExtent();

        DynamicUnitList columnWidthList = ReportHelper.getColumnWidthList(report);
        DynamicUnitList rowHeightList = ReportHelper.getRowHeightList(report);

        long totalWidth = columnWidthList.getRangeValue(horizontalValue, horizontalValue + horizontalExtent).toFU() - floatWidth;
        long totalHeight = rowHeightList.getRangeValue(verticalValue, verticalValue + verticalExtent).toFU() - floatHeight;

        int columnIndex = horizontalValue;
        long leftDistance = 0;
        int rowIndex = verticalValue;
        long topDistance = 0;
        if (totalWidth > 0) {
            int tmpTotalValue = 0;
            while (true) {
                tmpTotalValue += columnWidthList.get(columnIndex).toFU();
                if (tmpTotalValue > totalWidth / 2) {
                    leftDistance = tmpTotalValue - totalWidth / 2 - columnWidthList.get(columnIndex).toFU();
                    break;
                }
                columnIndex++;
            }
        }
        if (totalHeight > 0) {
            int tmpTotalValue = 0;
            while (true) {
                tmpTotalValue += rowHeightList.get(rowIndex).toFU();
                if (tmpTotalValue > totalHeight / 2) {
                    topDistance = tmpTotalValue - totalHeight / 2 - rowHeightList.get(rowIndex).toFU();
                    break;
                }
                rowIndex++;
            }
        }

        floatElement.setLeftDistance(FU.getInstance(Math.max(leftDistance, 0)));
        floatElement.setTopDistance(FU.getInstance(Math.max(topDistance, 0)));
        floatElement.setWidth(FU.getInstance(floatWidth));
        floatElement.setHeight(FU.getInstance(floatHeight));

        report.addFloatElement(floatElement);
    }

}
