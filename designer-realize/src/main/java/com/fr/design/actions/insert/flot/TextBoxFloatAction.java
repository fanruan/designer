/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.insert.flot;

import com.fr.base.BaseUtils;
import com.fr.base.DynamicUnitList;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.menu.MenuKeySet;

import com.fr.grid.Grid;
import com.fr.grid.selection.FloatSelection;
import com.fr.report.ReportHelper;
import com.fr.report.cell.FloatElement;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.stable.unit.FU;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Insert textbox.
 */
public class TextBoxFloatAction extends AbstractShapeAction {
    public TextBoxFloatAction(ElementCasePane t) {
        super(t);
        this.setMenuKeySet(FLOAT_INSERT_TEXT);
        this.setName(getMenuKeySet().getMenuKeySetName());
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_insert/text.png"));
    }

    public static final MenuKeySet FLOAT_INSERT_TEXT = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'T';
        }

        @Override
        public String getMenuName() {
            return com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Insert_Text");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };


    /**
     * 动作
     *
     * @param e 动作
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        ElementCasePane jws = getEditingComponent();
        if (jws == null) {
            return;
        }
        FloatElement floatElement = new FloatElement("Text");
        this.startDraw(floatElement);
        doWithDrawingFloatElement();
    }


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
        reportPane.fireTargetModified();
    }
}