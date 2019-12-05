package com.fr.grid.selection;

import com.fr.base.BaseFormula;
import com.fr.base.BaseUtils;
import com.fr.base.NameStyle;
import com.fr.base.Utils;
import com.fr.base.vcs.DesignerMode;
import com.fr.cache.list.IntList;
import com.fr.config.ServerPreferenceConfig;
import com.fr.design.actions.UpdateAction;
import com.fr.design.actions.cell.CellAttributeAction;
import com.fr.design.actions.cell.CellExpandAttrAction;
import com.fr.design.actions.cell.CellWidgetAttrAction;
import com.fr.design.actions.cell.CleanAuthorityAction;
import com.fr.design.actions.cell.ConditionAttributesAction;
import com.fr.design.actions.cell.EditCellAction;
import com.fr.design.actions.cell.GlobalStyleMenuDef;
import com.fr.design.actions.cell.GlobalStyleMenuDef.GlobalStyleSelection;
import com.fr.design.actions.cell.StyleAction;
import com.fr.design.actions.core.ActionFactory;
import com.fr.design.actions.edit.CopyAction;
import com.fr.design.actions.edit.CutAction;
import com.fr.design.actions.edit.HyperlinkAction;
import com.fr.design.actions.edit.PasteAction;
import com.fr.design.actions.utils.DeprecatedActionManager;
import com.fr.design.cell.clipboard.CellElementsClip;
import com.fr.design.cell.clipboard.ElementsTransferable;
import com.fr.design.designer.TargetComponent;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.dialog.FineJOptionPane;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.imenu.UIMenu;
import com.fr.design.gui.imenu.UIPopupMenu;
import com.fr.design.mainframe.CellElementPropertyPane;
import com.fr.design.mainframe.CellWidgetPropertyPane;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.mainframe.ElementCasePane.Clear;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.report.RowColumnPane;
import com.fr.design.selection.QuickEditor;

import com.fr.grid.GridUtils;
import com.fr.report.cell.CellElement;
import com.fr.report.cell.DefaultTemplateCellElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.cell.cellattr.CellGUIAttr;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.stable.ColumnRow;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;
import com.fr.stable.unit.FU;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * the cell selection (column，row)是所选单元格中左上角的位置 ， 这个数据结构就是一个Rectangle
 *
 * @editor zhou 2012-3-22下午1:53:59
 */
public class CellSelection extends Selection {
    public static final int NORMAL = 0;
    public static final int CHOOSE_COLUMN = 1;
    public static final int CHOOSE_ROW = 2;

    private int column;
    private int row;
    private int columnSpan;
    private int rowSpan;
    private int selectedType = NORMAL;

    private Rectangle editRectangle = new Rectangle(0, 0, 1, 1);
    private List cellRectangleList = new ArrayList();

    public CellSelection() {
        this(0, 0, 1, 1);
        //this.cellRectangleList.add(new Rectangle(0, 0, 1, 1));
    }


    public CellSelection(int column, int row, int columnSpan, int rowSpan) {
        setBounds(column, row, columnSpan, rowSpan);
        this.cellRectangleList.add(new Rectangle(column, row, columnSpan, rowSpan));

    }

    public final void setBounds(int column, int row, int columnSpan, int rowSpan) {
        this.column = column;
        this.row = row;
        this.columnSpan = columnSpan;
        this.rowSpan = rowSpan;
        editRectangle.setBounds(column, row, columnSpan, rowSpan);
    }

    public void setLastRectangleBounds(int column, int row, int columnSpan, int rowSpan) {
        this.column = column;
        this.row = row;
        this.columnSpan = columnSpan;
        this.rowSpan = rowSpan;
        if (!cellRectangleList.isEmpty()) {
            ((Rectangle) cellRectangleList.get(cellRectangleList.size() - 1)).setBounds(column, row, columnSpan, rowSpan);
        }
    }

    public void setSelectedType(int chooseType) {
        this.selectedType = chooseType;
    }

    public int getSelectedType() {
        return selectedType;
    }

    /**
     * 增加选中的区域
     *
     * @param cellRectangle 区域
     */
    public void addCellRectangle(Rectangle cellRectangle) {
        int index = this.cellRectangleList.indexOf(cellRectangle);
        if (index != -1) {
            this.cellRectangleList.remove(index);
        }
        this.cellRectangleList.add(cellRectangle);
    }

    /**
     * Gets edit rectangle
     */
    public Rectangle getEditRectangle() {
        return this.editRectangle;
    }

    /**
     * Gets the only cell rectangle
     */
    public Rectangle getFirstCellRectangle() {
        //p:这里不判断尺寸，直接留着抛错，在type==CELL的时候，List长度一定大于0.
        return (Rectangle) this.cellRectangleList.get(0);
    }

    /**
     * Gets the last cell rectangle
     */
    public Rectangle getLastCellRectangle() {
        return (Rectangle) this.cellRectangleList.get(this.cellRectangleList.size() - 1);
    }


    /**
     * Gets the count of cell rectangle
     */
    public int getCellRectangleCount() {
        return this.cellRectangleList.size();
    }

    /**
     * Gets the cell rectangle at given position
     */
    public Rectangle getCellRectangle(int index) {
        return (Rectangle) this.cellRectangleList.get(index);
    }


    /**
     * 清除区域块
     *
     * @param i 区域块
     */
    public void clearCellRectangles(int i) {
        this.cellRectangleList.remove(i);
    }

    /**
     * 包含单元格
     *
     * @param column 列
     * @param row    行
     * @return 若不包含返回-1
     */
    public int containsCell(int column, int row) {
        for (int i = 0; i < this.cellRectangleList.size(); i++) {
            Rectangle tmpRectangle = (Rectangle) this.cellRectangleList.get(i);
            if (tmpRectangle.contains(column, row)) {
                return i;
            }
        }

        return -1;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public int getColumnSpan() {
        return columnSpan;
    }

    public int getRowSpan() {
        return rowSpan;
    }

    /**
     * 转换成矩形
     *
     * @return 矩形
     */
    public Rectangle toRectangle() {
        return new Rectangle(column, row, columnSpan, rowSpan);
    }

    /**
     * 是否选择一个单元格
     *
     * @param ePane 区域
     * @return 是则返回rue
     */
    public boolean isSelectedOneCell(ElementCasePane ePane) {
        if (getCellRectangleCount() > 1) {
            return false;
        }

        if (columnSpan == 1 && rowSpan == 1) {
            return true;
        }
        TemplateElementCase ec = ePane.getEditingElementCase();
        Iterator containedCellElementIterator = ec.intersect(column, row, columnSpan, rowSpan);
        while (containedCellElementIterator.hasNext()) {
            CellElement cellElement = (CellElement) containedCellElementIterator.next();
            if (cellElement.getColumnSpan() == columnSpan && cellElement.getRowSpan() == rowSpan) {
                return true;
            }
        }
        return false;

    }


    /**
     * 作为可传输的
     *
     * @param transferable 传输介质
     * @param ePane        区域
     */
    public void asTransferable(ElementsTransferable transferable, ElementCasePane ePane) {
        java.util.List<TemplateCellElement> list = new java.util.ArrayList<TemplateCellElement>();

        TemplateElementCase ec = ePane.getEditingElementCase();
        Iterator cells = ec.intersect(column, row, columnSpan, rowSpan);
        while (cells.hasNext()) {
            TemplateCellElement cellElement = (TemplateCellElement) cells.next();
            list.add((TemplateCellElement) cellElement.deriveCellElement(cellElement.getColumn() - column, cellElement.getRow() - row));
        }
        FU[] columnWidth = new FU[columnSpan];
        FU[] rowHeight = new FU[rowSpan];
        for (int i = 0; i < columnSpan; i++) {
            columnWidth[i] = ec.getColumnWidth(this.column + i);
        }
        for (int j = 0; j < rowSpan; j++) {
            rowHeight[j] = ec.getRowHeight(this.row + j);
        }
        transferable.addObject(new CellElementsClip(this.columnSpan, this.rowSpan, columnWidth, rowHeight, list.toArray(new TemplateCellElement[list.size()])));
    }

    /**
     * 黏贴单元格
     *
     * @param ceClip 单元格
     * @param ePane  区域
     * @return 成功返回true
     */
    @Override
    public boolean pasteCellElementsClip(CellElementsClip ceClip, ElementCasePane ePane) {
        TemplateElementCase ec = ePane.getEditingElementCase();
        CellSelection cs = ceClip.pasteAt(ec, column, row);
        if (cs != null) {
            ePane.setSelection(cs);
        }

        return true;
    }

    /**
     * 黏贴字符串
     *
     * @param str   字符串
     * @param ePane 区域
     * @return 成功返回true
     */
    @Override
    public boolean pasteString(String str, ElementCasePane ePane) {
        // 主要需要处理Excel当中的类型.
        // Excel 的剪贴板格式
        // Excel 的剪贴板格式非常简单。它采用制表符分隔同一行上的元素，
        // 并用换行符分隔行。这样，当您复制一组连续的和/或相邻的单元格时，Excel
        // 只将电子表格数据标记到一个长字符串中，各个单元格值由该字符串内的制表符和换行符分隔。
        // 如果所选的单元格不相邻时怎么办？很简单：Excel 不会让您将所选内容复制到剪贴板。
        // set value to current edit cell element.

        TemplateElementCase ec = ePane.getEditingElementCase();

        String[] allTextArray = StableUtils.splitString(str, '\n');
        for (int r = 0; r < allTextArray.length; r++) {
            String[] lineTextArray = StableUtils.splitString(allTextArray[r], '\t');
            for (int c = 0; c < lineTextArray.length; c++) {
                String textValue = lineTextArray[c];
                if (textValue.length() > 0 && textValue.charAt(0) == '=') {
                    ec.setCellValue(column + c, row + r, BaseFormula.createFormulaBuilder().build(textValue));
                } else {
                    Number number = Utils.string2Number(lineTextArray[c]);
                    if (number != null) {
                        ec.setCellValue(column + c, row + r, number);
                    } else {
                        // alex:对于100,000,000这种数值,先做一个取巧的解决方法
                        String newStr = Utils.replaceAllString(lineTextArray[c], ",", StringUtils.EMPTY);
                        number = Utils.string2Number(newStr);
                        if (number != null) {
                            ec.setCellValue(column + c, row + r, Utils.string2Number(newStr));
                        } else {
                            ec.setCellValue(column + c, row + r, lineTextArray[c]);
                        }
                    }
                }
            }
        }

        ePane.setSelection(new CellSelection(column, row, this.columnSpan, this.rowSpan));

        return true;
    }

    /**
     * 黏贴其他
     *
     * @param ob    要黏贴的东西
     * @param ePane 区域
     * @return 成功返回true
     */
    @Override
    public boolean pasteOtherType(Object ob, ElementCasePane ePane) {
        TemplateElementCase ec = ePane.getEditingElementCase();

        TemplateCellElement cellElement = ec.getTemplateCellElement(column, row);
        if (cellElement == null) {
            cellElement = new DefaultTemplateCellElement(column, row, ob);
            ec.addCellElement(cellElement, false);
        } else {
            cellElement.setValue(ob);
        }

        ePane.setSelection(new CellSelection(column, row, 1, 1));

        return true;
    }

    /**
     * 是否能合并单元格
     *
     * @param ePane 区域
     * @return 是则返回true
     */
    @Override
    public boolean canMergeCells(ElementCasePane ePane) {

        return !this.isSelectedOneCell(ePane);
    }

    /**
     * 合并单元格
     *
     * @param ePane 区域
     * @return 成功返回true
     */
    @Override
    public boolean mergeCells(ElementCasePane ePane) {

        TemplateElementCase ec = ePane.getEditingElementCase();
        Iterator cells = ec.intersect(column, row, columnSpan, rowSpan);
        if (cells.hasNext()) { // alex:有两个以上的格子在这个区域内
            int returnValue = FineJOptionPane.showConfirmDialog(SwingUtilities.getWindowAncestor(ePane), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Des_Merger_Cell"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Utils_Merge_Cell"),
                    JOptionPane.OK_CANCEL_OPTION);
            if (returnValue != JOptionPane.OK_OPTION) {
                return false;
            }
        }

        ec.merge(row, row + rowSpan - 1, column, column + columnSpan - 1);

        return true;
    }

    /**
     * 是否撤销合并单元格
     *
     * @param ePane 区域
     * @return 是则返回true
     */
    @Override
    public boolean canUnMergeCells(ElementCasePane ePane) {
        TemplateElementCase ec = ePane.getEditingElementCase();

        Iterator containedCellElementIterator = ec.intersect(column, row, columnSpan, rowSpan);
        while (containedCellElementIterator.hasNext()) {
            CellElement cellElement = (CellElement) containedCellElementIterator.next();

            if (cellElement.getColumnSpan() > 1 || cellElement.getRowSpan() > 1) {
                return true;
            }
        }

        return false;
    }

    /**
     * 撤销合并单元格
     *
     * @param ePane 区域
     * @return 成功返回true
     */
    @Override
    public boolean unMergeCells(ElementCasePane ePane) {
        TemplateElementCase ec = ePane.getEditingElementCase();

        Iterator containedCellElementIterator = ec.intersect(column, row, columnSpan, rowSpan);
        while (containedCellElementIterator.hasNext()) {
            TemplateCellElement cellElement = (TemplateCellElement) containedCellElementIterator.next();

            int columnSpan = cellElement.getColumnSpan();
            int rowSpan = cellElement.getRowSpan();
            ec.removeCellElement(cellElement);

            ec.addCellElement((TemplateCellElement) cellElement.deriveCellElement(cellElement.getColumn(), cellElement.getRow(), 1, 1));

            for (int kc = cellElement.getColumn(); kc < cellElement.getColumn() + columnSpan; kc++) {
                for (int kr = cellElement.getRow(); kr < cellElement.getRow() + rowSpan; kr++) {
                    if (kc == cellElement.getColumn() && kr == cellElement.getRow()) {
                        continue;
                    }

                    // 不覆盖以前的元素
                    ec.addCellElement(new DefaultTemplateCellElement(kc, kr), false);
                }
            }
        }

        this.setBounds(column, row, 1, 1);

        return true;
    }

    /**
     * 创建弹出菜单
     *
     * @param ePane 区域
     * @return 菜单
     */
    public UIPopupMenu createPopupMenu(ElementCasePane ePane) {
        UIPopupMenu popup = new UIPopupMenu();
        if (DesignerMode.isAuthorityEditing()) {
            popup.add(new CleanAuthorityAction(ePane).createMenuItem());
            return popup;
        }
        popup.add(new EditCellAction(ePane).createMenuItem());
        popup.add(DeprecatedActionManager.getCellMenu(ePane).createJMenu());
        // richer:add global style menu
        popup.add(new CellExpandAttrAction().createMenuItem());
        if (!ServerPreferenceConfig.getInstance().hasStyle()) {
            UIMenu styleMenu = new UIMenu(KeySetUtils.GLOBAL_STYLE.getMenuName());
            styleMenu.setIcon(BaseUtils.readIcon("/com/fr/design/images/m_format/cell.png"));
            Iterator iterato = ServerPreferenceConfig.getInstance().getStyleNameIterator();
            while (iterato.hasNext()) {
                String name = (String) iterato.next();
                name = GlobalStyleMenuDef.judgeChina(name);
                NameStyle nameStyle = NameStyle.getInstance(name);
                UpdateAction.UseMenuItem useMenuItem = new GlobalStyleSelection(ePane, nameStyle).createUseMenuItem();
                useMenuItem.setNameStyle(nameStyle);
                styleMenu.add(useMenuItem);
            }
            styleMenu.addSeparator();
            styleMenu.add(new GlobalStyleMenuDef.CustomStyleAction(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Custom")));
            popup.add(styleMenu);
        } else {
            popup.add(new StyleAction().createMenuItem());
        }
        popup.add(DeprecatedActionManager.getPresentMenu(ePane).createJMenu());
        popup.add(new CellAttributeAction().createMenuItem());
        JTemplate jTemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
        if (jTemplate.isJWorkBook()) { //表单中报表块编辑屏蔽掉  控件设置
            popup.add(new CellWidgetAttrAction().createMenuItem());
        }
        popup.add(new ConditionAttributesAction().createMenuItem());
        popup.add(new HyperlinkAction().createMenuItem());
        // cut, copy and paste
        popup.addSeparator();
        popup.add(new CutAction(ePane).createMenuItem());
        popup.add(new CopyAction(ePane).createMenuItem());
        popup.add(new PasteAction(ePane).createMenuItem());

        popup.addSeparator();
        popup.add(DeprecatedActionManager.getInsertMenu(ePane));
        popup.add(DeprecatedActionManager.getDeleteMenu(ePane));
        popup.add(DeprecatedActionManager.getClearMenu(ePane));

        popup.addSeparator();

        addExtraMenu(ePane, popup);
        return popup;
    }

    /**
     * 清除
     *
     * @param type  要清除的类型
     * @param ePane 区域
     * @return 成功返回true
     */
    @Override
    public boolean clear(Clear type, ElementCasePane ePane) {
        TemplateElementCase ec = ePane.getEditingElementCase();
        boolean isClear = true;
        int cellRectangleCount = getCellRectangleCount();
        for (int rect = 0; rect < cellRectangleCount; rect++) {
            isClear = hasclearCell(type, ec, rect);
        }
        return isClear;
    }

    private boolean hasclearCell(Clear type, TemplateElementCase ec, int rect) {
        List<CellElement> removeElementList = new ArrayList<CellElement>();
        Rectangle cellRectangle = getCellRectangle(rect);
        column = cellRectangle.x;
        row = cellRectangle.y;
        columnSpan = cellRectangle.width;
        rowSpan = cellRectangle.height;

        Iterator cells = ec.intersect(column, row, columnSpan, rowSpan);
        while (cells.hasNext()) {
            CellElement cellElement = (CellElement) cells.next();
            CellGUIAttr cellGUIAttr = cellElement.getCellGUIAttr();
            if (cellGUIAttr == null) {
                cellGUIAttr = CellGUIAttr.DEFAULT_CELLGUIATTR;
            }
            removeElementList.add(cellElement);
        }
        if (removeElementList.isEmpty()) {
            return false;
        }
        switch (type) {
            case ALL:
                for (int i = 0; i < removeElementList.size(); i++) {
                    CellElement element = removeElementList.get(i);
                    ec.removeCellElement((TemplateCellElement) element);
                }
                break;

            case FORMATS:
                for (int i = 0; i < removeElementList.size(); i++) {
                    CellElement element = removeElementList.get(i);
                    element.setStyle(null);
                }
                break;

            case CONTENTS:
                for (int i = 0; i < removeElementList.size(); i++) {
                    CellElement element = removeElementList.get(i);
                    element.setValue(null);
                }
                break;

            case WIDGETS:
                for (int i = 0; i < removeElementList.size(); i++) {
                    CellElement element = removeElementList.get(i);
                    ((TemplateCellElement) element).setWidget(null);
                }
                break;
            default:
                break;
        }
        return true;
    }


    @Override
    public int[] getSelectedColumns() {
        return IntList.range(column, column + columnSpan);

    }

    @Override
    public int[] getSelectedRows() {
        return IntList.range(row, row + rowSpan);
    }


    /**
     * 向左移动
     *
     * @param ePane 区域
     */
    public void moveLeft(ElementCasePane ePane) {
        if (column - 1 < 0) {
            return;
        }
        moveTo(ePane, column - 1, row);
    }

    /**
     * 向右移动
     *
     * @param ePane 区域
     */
    public void moveRight(ElementCasePane ePane) {
        moveTo(ePane, column + columnSpan, row);
    }

    /**
     * 向上移动
     *
     * @param ePane 区域
     */
    public void moveUp(ElementCasePane ePane) {
        if (row - 1 < 0) {
            return;
        }
        moveTo(ePane, column, row - 1);
    }

    /**
     * 向下移动
     *
     * @param ePane 区域
     */
    public void moveDown(ElementCasePane ePane) {
        moveTo(ePane, column, row + rowSpan);
    }

    private static void moveTo(ElementCasePane ePane, int column, int row) {
        if (GridUtils.canMove(ePane, column, row)) {
            GridUtils.doSelectCell(ePane, column, row);
            ePane.ensureColumnRowVisible(column, row);
        }
    }

    /**
     * 触发删除动作
     *
     * @param ePane 区域
     * @return 成功返回true
     */
    @Override
    public boolean triggerDeleteAction(ElementCasePane ePane) {
        final TemplateElementCase ec = ePane.getEditingElementCase();
        final RowColumnPane rcPane = new RowColumnPane();
        rcPane.setTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Delete"));
        rcPane.showWindow(SwingUtilities.getWindowAncestor(ePane), new DialogActionAdapter() {

            @Override
            public void doOk() {
                if (rcPane.isEntireRow()) {
                    int[] rows = CellSelection.this.getSelectedRows();
                    for (int i = 0; i < rows.length; i++) {
                        ec.removeRow(rows[i] - i);
                    }
                } else {
                    int[] columns = CellSelection.this.getSelectedColumns();
                    for (int i = 0; i < columns.length; i++) {
                        ec.removeColumn(columns[i] - i);
                    }
                }
            }
        }).setVisible(true);

        return true;
    }

    /**
     * 包含行列
     *
     * @param cr 行列
     * @return 包含返回true
     */
    @Override
    public boolean containsColumnRow(ColumnRow cr) {
        return new Rectangle(column, row, columnSpan, rowSpan).contains(cr.column, cr.row);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CellSelection)) {
            return false;
        }
        CellSelection cs = (CellSelection) obj;
        return this.getColumn() == cs.getColumn() && this.getRow() == cs.getRow() && this.getColumnSpan() == cs.getColumnSpan() && this.getRowSpan() == cs.getRowSpan();
    }

    @Override
    public CellSelection clone() {
        CellSelection cs = new CellSelection(column, row, columnSpan, rowSpan);

        if (this.editRectangle != null) {
            cs.editRectangle = (Rectangle) this.editRectangle.clone();
        }
        java.util.List newCellRectList = new java.util.ArrayList(this.cellRectangleList.size());
        cs.cellRectangleList = newCellRectList;
        for (int i = 0, len = this.cellRectangleList.size(); i < len; i++) {
            newCellRectList.add((Rectangle) ((Rectangle) this.cellRectangleList.get(i)).clone());
        }
        cs.selectedType = this.selectedType;

        return cs;
    }

    @Override
    public QuickEditor getQuickEditor(TargetComponent tc) {
        ElementCasePane ePane = (ElementCasePane) tc;
        TemplateElementCase tplEC = ePane.getEditingElementCase();
        TemplateCellElement cellElement = tplEC.getTemplateCellElement(column, row);
        Object value = null;
        boolean b = ePane.isSelectedOneCell();
        if (cellElement != null && b) {
            value = cellElement.getValue();
        }
        value = value == null ? StringUtils.EMPTY : value;
        //之前是少了个bigInteger,刚kunsnat又发现少了个bigDecimal，数字类型的都用stringEditor，没必要那个样子
        QuickEditor editor = ActionFactory.getCellEditor((value instanceof Number) ? (Number.class) : (value.getClass()));
        if (editor == null) {
            return null;
        }
        editor.populate(tc);
        return editor;
    }

    @Override
    public void populatePropertyPane(ElementCasePane ePane) {
        CellElementPropertyPane.getInstance().reInit(ePane);
    }

    public void populateWidgetPropertyPane(ElementCasePane ePane) {
        CellWidgetPropertyPane.getInstance().reInit(ePane);
    }

}