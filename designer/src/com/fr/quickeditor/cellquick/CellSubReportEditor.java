package com.fr.quickeditor.cellquick;

import com.fr.design.actions.insert.cell.SubReportCellAction;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.general.Inter;
import com.fr.quickeditor.CellQuickEditor;

import javax.swing.*;

/**
 * 单元格元素子报表编辑器
 * TODO 9.0大体上没有改动
 */
public class CellSubReportEditor extends CellQuickEditor {
    private UIButton subReportButton;
    private static CellSubReportEditor THIS;

    public static final CellSubReportEditor getInstance() {
        if (THIS == null) {
            THIS = new CellSubReportEditor();
        }
        return THIS;
    }

    private CellSubReportEditor() {
        super();
    }

    @Override
    public JComponent createCenterBody() {
        subReportButton = new UIButton();
        subReportButton.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        subReportButton.setMargin(null);
        subReportButton.setOpaque(false);
        return subReportButton;
    }

    @Override
    protected void refreshDetails() {
        SubReportCellAction subReportCellAction = new SubReportCellAction(tc);
        subReportCellAction.setName(Inter.getLocText(new String[]{"Edit", "Sub_Report"}));
        subReportButton.setAction(subReportCellAction);
    }

}