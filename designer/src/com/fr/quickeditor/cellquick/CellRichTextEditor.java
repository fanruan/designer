package com.fr.quickeditor.cellquick;

import com.fr.design.actions.core.ActionFactory;
import com.fr.design.actions.insert.cell.RichTextCellAction;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.general.Inter;
import com.fr.quickeditor.CellQuickEditor;

import javax.swing.*;
import java.awt.*;

/**
 * 单元格元素富文本编辑器
 *
 * @author yaoh.wu
 * @version 2017年8月7日10点53分
 */
public class CellRichTextEditor extends CellQuickEditor {
    private UIButton richTextButton;

    private CellRichTextEditor() {
        super();
    }

    @SuppressWarnings("Duplicates")
    @Override
    public JComponent createCenterBody() {
        JPanel content = new JPanel(new BorderLayout());
        content.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 15));
        richTextButton = new UIButton();
        richTextButton.setOpaque(false);
        content.add(richTextButton, BorderLayout.CENTER);
        return content;
    }

    @Override
    public Object getComboBoxSelected() {
        return ActionFactory.createAction(RichTextCellAction.class);
    }

    @Override
    protected void refreshDetails() {
        RichTextCellAction subReportCellAction = new RichTextCellAction(tc);
        subReportCellAction.setName(Inter.getLocText("FR-Designer_RichTextEditor"));
        richTextButton.setAction(subReportCellAction);
    }

}