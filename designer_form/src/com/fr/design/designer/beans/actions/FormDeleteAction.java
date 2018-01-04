/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.designer.beans.actions;

import java.awt.event.KeyEvent;

import javax.swing.*;

import com.fr.base.BaseUtils;
import com.fr.general.Inter;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.FormSelection;

/**
 * @author richer
 * @since 6.5.3
 */
public class FormDeleteAction extends FormWidgetEditAction {

    public FormDeleteAction(FormDesigner t) {
        super(t);

        this.setName(Inter.getLocText("M_Edit-Delete"));
        this.setMnemonic('D');
        // Richie:删除菜单图标
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_report/delete.png"));
        this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0));
    }

    @Override
    public JComponent createToolBarComponent() {
        JComponent comp = super.createToolBarComponent();
        // 除了 BACKSPACE 之外，DELETE 键也要能删除（直接在此处添加绑定，没有按钮提示）
        comp.registerKeyboardAction(this, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        return comp;
    }

    /**
     * 删除
     *
     * @return 是否删除成功
     */
    @Override
    public boolean executeActionReturnUndoRecordNeeded() {
        FormDesigner designer = getEditingComponent();
        if (designer == null) {
            return false;
        }
        FormSelection selection = designer.getSelectionModel().getSelection();
        XCreator creator = selection.getSelectedCreator();
        designer.getSelectionModel().deleteSelection();

        creator.deleteRelatedComponent(creator, designer);
        return false;
    }
}