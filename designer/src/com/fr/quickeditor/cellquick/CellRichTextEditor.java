package com.fr.quickeditor.cellquick;

import com.fr.design.actions.core.ActionFactory;
import com.fr.design.actions.insert.cell.RichTextCellAction;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;
import com.fr.quickeditor.CellQuickEditor;

import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

/**
 * 单元格元素富文本编辑器
 *
 * @author yaoh.wu
 * @version 2017年8月7日10点53分
 */
public class CellRichTextEditor extends CellQuickEditor {
    private UIButton richTextButton;

    public CellRichTextEditor() {
        super();
    }

    @Override
    public JComponent createCenterBody() {
        JPanel content = new JPanel(new BorderLayout());
        richTextButton = new UIButton();
        richTextButton.setOpaque(false);
        content.add(TableLayoutHelper.createGapTableLayoutPane(new Component[][]{
                        new Component[]{EMPTY_LABEL, richTextButton}},
                new double[]{TableLayout.PREFERRED},
                new double[]{TableLayout.PREFERRED, TableLayout.FILL}, HGAP, VGAP), BorderLayout.CENTER);
        return content;
    }

    @Override
    public Object getComboBoxSelected() {
        return ActionFactory.createAction(RichTextCellAction.class);
    }

    @Override
    protected void refreshDetails() {
        RichTextCellAction subReportCellAction = new RichTextCellAction(tc);
        subReportCellAction.setName(Inter.getLocText("Edit"));
        subReportCellAction.setSmallIcon(null);
        richTextButton.setAction(subReportCellAction);
    }

    @Override
    public boolean isScrollAll() {
        return true;
    }

}