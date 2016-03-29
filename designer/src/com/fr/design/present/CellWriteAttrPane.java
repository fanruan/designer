package com.fr.design.present;

import com.fr.base.FRContext;
import com.fr.base.Style;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.widget.WidgetPane;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.form.ui.DateEditor;
import com.fr.form.ui.NoneWidget;
import com.fr.form.ui.Widget;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.Selection;
import com.fr.privilege.finegrain.WidgetPrivilegeControl;
import com.fr.report.cell.DefaultTemplateCellElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.elementcase.TemplateElementCase;

import java.awt.*;
import java.text.Format;
import java.text.SimpleDateFormat;

public class CellWriteAttrPane extends BasicPane {

    private WidgetPane cellEditorDefPane;

    public CellWriteAttrPane(ElementCasePane elementCasePane) {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        cellEditorDefPane = new WidgetPane(elementCasePane);
        this.add(cellEditorDefPane, BorderLayout.CENTER);
    }

    /**
     * 创建cellWriteAttrPane
     *
     * @param elementCasePane 这个用于获取一些ElementCasePane信息
     */
    public static void showWidgetWindow(ElementCasePane elementCasePane) {
        final CellWriteAttrPane wp = new CellWriteAttrPane(elementCasePane);
        Selection selection = elementCasePane.getSelection();
        if (!(selection instanceof CellSelection)) {
            return;
        }

        CellSelection cs = (CellSelection) selection;

        // got simple cell element from column and row.
        TemplateElementCase report = elementCasePane.getEditingElementCase();
        final TemplateCellElement editCellElement = report.getTemplateCellElement(cs.getColumn(), cs.getRow());
        wp.populate(editCellElement);
        BasicDialog dialog = wp.showWindow(DesignerContext.getDesignerFrame());
        dialog.addDialogActionListener(new DialogActionAdapter() {

            @Override
            public void doOk() {
                wp.update(editCellElement);
                DesignerContext.getDesignerFrame().getSelectedJTemplate().fireTargetModified();
            }
        });
        DesignerContext.setReportWritePane(dialog);
        dialog.setVisible(true);
    }

    @Override
    protected String title4PopupWindow() {
        return Inter.getLocText("FR-Designer-Widget_Settings");
    }

    public void populate(TemplateCellElement cellElement) {
        if (cellElement == null) {// 利用默认的CellElement.
            cellElement = new DefaultTemplateCellElement(0, 0, null);
        }

        Widget cellWidget = cellElement.getWidget();

        if (cellWidget != null && cellWidget instanceof DateEditor) {
            // p:日期的格式需要设置到单元格子里面.
            DateEditor dateCellEditorDef = (DateEditor) cellWidget;

            // p:需要把下拉的编辑器,日期格式，都放到CellElement的Style里面
            // 这个地方很方便用户，是alex提出的.
            // p:日期的格式需要设置到单元格子里面.
            Style style = cellElement.getStyle();
            if (style != null) {
                Format format = style.getFormat();
                if (format != null && format instanceof SimpleDateFormat) {
                    SimpleDateFormat simpleDateFormat = (SimpleDateFormat) format;
                    dateCellEditorDef.setFormatText(simpleDateFormat.toPattern());
                }
            }
        }
        // 这里进行克隆的原因是为了保留原始的Widget以便和新的Widget做比较来判断是否发生了改变
        if (cellWidget != null) {
            try {
                cellWidget = (Widget) cellWidget.clone();
            } catch (CloneNotSupportedException e) {
                FRContext.getLogger().error(e.getMessage(), e);
            }
        }
        cellEditorDefPane.populate(cellWidget);
    }

    public void update(TemplateCellElement cellElement) {
        if (cellElement == null) {// 利用默认的CellElement.
            return;
        }

        Widget cellWidget = this.cellEditorDefPane.update();
        // p:需要把下拉的编辑器,日期格式，都放到CellElement的Style里面
        if (cellWidget instanceof DateEditor) {
            // p:日期的格式需要设置到单元格子里面.
            DateEditor dateCellEditorDef = (DateEditor) cellWidget;
            String formatText = dateCellEditorDef.getFormatText();
            if (formatText != null) {
                Style style = cellElement.getStyle();
                if (style != null) {
                    cellElement.setStyle(style.deriveFormat(new SimpleDateFormat(formatText)));
                }
            }
        }

        // p:最后把这个cellEditorDef设置到CellGUIAttr.
        if (cellWidget instanceof NoneWidget) {
            cellElement.setWidget(null);
        } else {
            if (cellElement.getWidget() != null) {
                cellWidget = upDateWidgetAuthority(cellElement, cellWidget);
            }
            cellElement.setWidget(cellWidget);
        }

    }


    private Widget upDateWidgetAuthority(TemplateCellElement cellElement, Widget newWidget) {
        try {
            Widget oldWidget = (Widget) cellElement.getWidget().clone();
            if (newWidget.getClass() == oldWidget.getClass()) {
                newWidget.setWidgetPrivilegeControl((WidgetPrivilegeControl) oldWidget.getWidgetPrivilegeControl().clone());
            }
        } catch (Exception e) {
            FRLogger.getLogger().error(e.getMessage());
        }
        return newWidget;
    }

    @Override
    /**
     *检测是否有效
     */
    public void checkValid() throws Exception {
        this.cellEditorDefPane.checkValid();
    }

}