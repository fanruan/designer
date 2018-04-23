package com.fr.design.actions.cell;

import com.fr.design.actions.CellSelectionAction;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.grid.GridUtils;
import com.fr.grid.selection.CellSelection;
import com.fr.report.cell.DefaultTemplateCellElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.core.SheetUtils;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.report.stable.ReportConstants;

/**
 * TODO ALEX_SEP 这个类与AbstractCellAction有什么关系?
 *
 * @author null
 */
public abstract class AbstractCellElementAction extends CellSelectionAction {

    protected AbstractCellElementAction() {
        super();
    }

    protected AbstractCellElementAction(ElementCasePane t) {
        super(t);
    }

    @Override
    protected boolean executeActionReturnUndoRecordNeededWithCellSelection(CellSelection cs) {
        final ElementCasePane ePane = this.getEditingComponent();
        final TemplateElementCase tplEC = ePane.getEditingElementCase();
        TemplateCellElement editCellElement = tplEC.getTemplateCellElement(cs.getColumn(), cs.getRow());
        if (editCellElement == null) {
            editCellElement = new DefaultTemplateCellElement(cs.getColumn(), cs.getRow());
            tplEC.addCellElement(editCellElement);
        }
        SheetUtils.calculateDefaultParent(tplEC);
        final CellSelection finalCS = cs;
        final BasicPane bp = populateBasicPane(editCellElement);
        BasicDialog dialog = bp.showWindow(DesignerContext.getDesignerFrame());
        dialog.addDialogActionListener(new DialogActionAdapter() {
            @Override
            public void doOk() {
                // 需要先行后列地增加新元素。
                for (int j = 0; j < finalCS.getRowSpan(); j++) {
                    for (int i = 0; i < finalCS.getColumnSpan(); i++) {
                        int column = i + finalCS.getColumn();
                        int row = j + finalCS.getRow();
                        TemplateCellElement editCellElement = tplEC.getTemplateCellElement(column, row);
                        if (editCellElement == null) {
                            editCellElement = new DefaultTemplateCellElement(column, row);
                            tplEC.addCellElement(editCellElement);
                        }
                        // alex:不加这一句话会导致跨行跨列的格子被多次update
                        if (editCellElement.getColumn() != column || editCellElement.getRow() != row) {
                            continue;
                        }
                        updateBasicPane(bp, editCellElement);
                        // update cell attributes
                        if (isNeedShinkToFit()) {
                            // shink to fit.(如果value是String)
                            Object editElementValue = editCellElement.getValue();
                            if (editElementValue != null && (editElementValue instanceof String || editElementValue instanceof Number)) {
                                // TODO ALEX_SEP 暂时用FIT_DEFAULT替代,不取reportsetting里面的设置,因为也不知道是应该放在report里面还是elementcase里面
                                GridUtils.shrinkToFit(ReportConstants.AUTO_SHRINK_TO_FIT_DEFAULT, tplEC, editCellElement);
                            }
                        }
                    }
                }
                ePane.fireTargetModified();
            }
        });
        //控件设置记住dlg，提交入库智能添加单元格后可以show出来
        DesignerContext.setReportWritePane(dialog);
        dialog.setVisible(true);
        return false;
    }

    /**
     * 初始化对话框
     *
     * @param cellElement 单元格
     * @return 对话框
     */
    protected abstract BasicPane populateBasicPane(TemplateCellElement cellElement);

    /**
     * 更新对话框之后，改变值
     *
     * @param cellElement 单元格
     */
    protected abstract void updateBasicPane(BasicPane basicPane, TemplateCellElement cellElement);

    /**
     * if isNeedShinkToFit，please override this method
     *
     * @return isNeedShinkToFit
     */
    protected boolean isNeedShinkToFit() {
        return false;
    }
}