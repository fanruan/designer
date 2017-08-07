package com.fr.quickeditor.cellquick;

import com.fr.design.actions.insert.cell.SubReportCellAction;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.general.Inter;
import com.fr.quickeditor.CellQuickEditor;

import javax.swing.*;
import java.awt.*;

/**
 * 单元格元素子报表编辑器
 *
 * @author yaoh.wu
 * @version 2017年8月7日10点53分
 */
public class CellSubReportEditor extends CellQuickEditor {
    private UIButton subReportButton;


    private CellSubReportEditor() {
        super();
    }

    @SuppressWarnings("Duplicates")
    @Override
    public JComponent createCenterBody() {
        JPanel content = new JPanel(new BorderLayout());
        content.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 15));
        subReportButton = new UIButton();
        subReportButton.setOpaque(false);
        content.add(subReportButton, BorderLayout.CENTER);
        return content;
    }

    @Override
    protected void refreshDetails() {
        SubReportCellAction subReportCellAction = new SubReportCellAction(tc);
        subReportCellAction.setName(Inter.getLocText(new String[]{"Edit", "Sub_Report"}));
        subReportButton.setAction(subReportCellAction);
    }

}