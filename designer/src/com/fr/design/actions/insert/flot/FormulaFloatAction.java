/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.insert.flot;

import com.fr.base.BaseFormula;
import com.fr.base.BaseUtils;
import com.fr.base.DynamicUnitList;
import com.fr.design.actions.ElementCaseAction;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.formula.FormulaFactory;
import com.fr.design.formula.UIFormula;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.menu.MenuKeySet;
import com.fr.general.Inter;
import com.fr.grid.Grid;
import com.fr.grid.selection.FloatSelection;
import com.fr.report.ReportHelper;
import com.fr.report.cell.FloatElement;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.stable.unit.FU;

import javax.swing.*;

/**
 * Insert formula.
 */
public class FormulaFloatAction extends ElementCaseAction {

    private boolean returnValue = false;

    public FormulaFloatAction(ElementCasePane t) {
        super(t);
        this.setMenuKeySet(FLOAT_INSERT_FORMULA);
        this.setName(getMenuKeySet().getMenuKeySetName());
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_insert/formula.png"));
    }

    public static final MenuKeySet FLOAT_INSERT_FORMULA = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'F';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("FR-Designer_Insert_Formula");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };

    private void doWithDrawingFloatElement() {
        ElementCasePane jws = (ElementCasePane) HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().getCurrentElementCasePane();
        Grid grid = jws.getGrid();

        ElementCasePane reportPane = grid.getElementCasePane();
        TemplateElementCase report = reportPane.getEditingElementCase();
        DynamicUnitList columnWidthList = ReportHelper.getColumnWidthList(report);
        DynamicUnitList rowHeightList = ReportHelper.getRowHeightList(report);

        int horizentalScrollValue = grid.getHorizontalValue();
        int verticalScrollValue = grid.getVerticalValue();

        int resolution = grid.getResolution();
        int floatWdith = grid.getDrawingFloatElement().getWidth().toPixI(resolution);
        int floatHeight = grid.getDrawingFloatElement().getWidth().toPixI(resolution);

        FU evtX_fu = FU.valueOfPix((grid.getWidth() - floatWdith) / 2, resolution);
        FU evtY_fu = FU.valueOfPix((grid.getHeight() - floatHeight) / 2, resolution);

        FU leftDistance = FU.getInstance(evtX_fu.toFU() + columnWidthList.getRangeValue(0, horizentalScrollValue).toFU());
        FU topDistance = FU.getInstance(evtY_fu.toFU() + rowHeightList.getRangeValue(0, verticalScrollValue).toFU());

        grid.getDrawingFloatElement().setLeftDistance(leftDistance);
        grid.getDrawingFloatElement().setTopDistance(topDistance);

        report.addFloatElement(grid.getDrawingFloatElement());
        reportPane.setSelection(new FloatSelection(grid.getDrawingFloatElement().getName()));
    }

    /**
     * 执行动作
     *
     * @return 成功返回true
     */
    @Override
    public boolean executeActionReturnUndoRecordNeeded() {
        final ElementCasePane reportPane = (ElementCasePane) HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().getCurrentElementCasePane();
        if (reportPane == null) {
            return false;
        }

        reportPane.stopEditing();
        final FloatElement floatElement = new FloatElement();
        final UIFormula formulaPane = FormulaFactory.createFormulaPane();
        formulaPane.populate(BaseFormula.createFormulaBuilder().build());

        BasicDialog dialog = formulaPane.showLargeWindow(DesignerContext.getDesignerFrame(), new DialogActionAdapter() {

            @Override
            public void doOk() {
                floatElement.setValue(formulaPane.update());
                if (reportPane == null) {
                    return;
                }
                reportPane.getGrid().setDrawingFloatElement(floatElement);
                doWithDrawingFloatElement();
                returnValue = true;
            }

            @Override
            public void doCancel() {
                returnValue = false;
            }
        });
        dialog.setVisible(true);
        return returnValue;
    }

}