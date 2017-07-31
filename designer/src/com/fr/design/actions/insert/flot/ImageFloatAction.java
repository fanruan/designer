/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.insert.flot;

import com.fr.base.BaseUtils;
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
import com.fr.grid.selection.FloatSelection;
import com.fr.report.cell.FloatElement;
import com.fr.report.cell.cellattr.CellImage;
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
        this.setName(getMenuKeySet().getMenuKeySetName() + "...");
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

                    int resolution = ScreenResolution.getScreenResolution();
                    floatElement.setWidth(FU.valueOfPix(image.getWidth(null), resolution));
                    floatElement.setHeight(FU.valueOfPix(image.getHeight(null), resolution));
                    floatElement.setStyle(cellImage.getStyle());
                    reportPane.addFloatElementToCenterOfElementPane(floatElement);
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