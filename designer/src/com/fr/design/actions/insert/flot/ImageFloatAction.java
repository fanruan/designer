/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.insert.flot;

import com.fr.base.BaseUtils;
import com.fr.base.DynamicUnitList;
import com.fr.base.ScreenResolution;
import com.fr.design.actions.ElementCaseAction;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.menu.MenuKeySet;
import com.fr.design.report.SelectImagePane;
import com.fr.general.Inter;
import com.fr.grid.Grid;
import com.fr.grid.selection.FloatSelection;
import com.fr.report.ReportHelper;
import com.fr.report.cell.FloatElement;
import com.fr.report.cell.cellattr.CellImage;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.stable.CoreGraphHelper;
import com.fr.stable.unit.FU;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Insert image.
 */
public class ImageFloatAction extends ElementCaseAction {

    private boolean returnValue = false;

    public ImageFloatAction(ElementCasePane t) {
        super(t);
        this.setMenuKeySet(FLOAT_INSERT_IMAGE);
        this.setName(getMenuKeySet().getMenuKeySetName());
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_insert/image.png"));
    }

    public static final MenuKeySet FLOAT_INSERT_IMAGE = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'I';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("FR-Designer_Insert_Image");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };

    /**
     * 执行动作
     *
     * @return 成功返回true
     */
    public boolean executeActionReturnUndoRecordNeeded() {
        final ElementCasePane reportPane = (ElementCasePane) HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().getCurrentElementCasePane();
        if (reportPane == null) {
            return false;
        }

        reportPane.stopEditing();

        final FloatElement floatElement = new FloatElement();

        final SelectImagePane selectImagePane = new SelectImagePane();
        selectImagePane.populate(floatElement);

        BasicDialog dialog = selectImagePane.showWindow(DesignerContext.getDesignerFrame());
        dialog.addDialogActionListener(new DialogActionAdapter() {

            @Override
            public void doOk() {
                File selectedFile = selectImagePane.getSelectedImage();

                if (selectedFile != null && selectedFile.isFile()) {
                    Image image = BaseUtils.readImage(selectedFile.getPath());
                    CellImage cellImage = selectImagePane.update();
                    CoreGraphHelper.waitForImage(image);

                    floatElement.setValue(image);
                    Grid grid = reportPane.getGrid();
                    int resolution = grid.getResolution();
                    floatElement.setWidth(FU.valueOfPix(image.getWidth(null), resolution));
                    floatElement.setHeight(FU.valueOfPix(image.getHeight(null), resolution));
                    TemplateElementCase report = reportPane.getEditingElementCase();
                    DynamicUnitList columnWidthList = ReportHelper.getColumnWidthList(report);
                    DynamicUnitList rowHeightList = ReportHelper.getRowHeightList(report);
                    int horizentalScrollValue = grid.getHorizontalValue();
                    int verticalScrollValue = grid.getVerticalValue();

                    int floatWdith = floatElement.getWidth().toPixI(resolution);
                    int floatHeight = floatElement.getHeight().toPixI(resolution);

                    int leftDifference = (grid.getWidth() - floatWdith) > 0 ? (grid.getWidth() - floatWdith) : 0;
                    int topDifference = (grid.getHeight() - floatHeight) > 0 ? (grid.getHeight() - floatHeight) : 0;
                    FU evtX_fu = FU.valueOfPix((leftDifference) / 2, resolution);
                    FU evtY_fu = FU.valueOfPix((topDifference) / 2, resolution);

                    FU leftDistance = FU.getInstance(evtX_fu.toFU() + columnWidthList.getRangeValue(0, horizentalScrollValue).toFU());
                    FU topDistance = FU.getInstance(evtY_fu.toFU() + rowHeightList.getRangeValue(0, verticalScrollValue).toFU());

                    floatElement.setLeftDistance(leftDistance);
                    floatElement.setTopDistance(topDistance);

                    floatElement.setStyle(cellImage.getStyle());
//                    reportPane.addFloatElementToCenterOfElementPane(floatElement);
                    reportPane.getEditingElementCase().addFloatElement(floatElement);
                    reportPane.setSelection(new FloatSelection(floatElement.getName()));

                    returnValue = true;
                }
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