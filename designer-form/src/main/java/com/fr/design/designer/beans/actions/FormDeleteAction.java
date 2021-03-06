/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.designer.beans.actions;

import java.awt.event.KeyEvent;

import javax.swing.*;

import com.fr.base.BaseUtils;
import com.fr.design.designer.beans.actions.behavior.ComponentEnable;

import com.fr.design.mainframe.FormDesigner;

/**
 * @author richer
 * @since 6.5.3
 */
public class FormDeleteAction extends FormWidgetEditAction {

    public FormDeleteAction(FormDesigner t) {
        super(t);

        this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_M_Edit_Delete"));
        this.setMnemonic('D');
        // Richie:删除菜单图标
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_report/delete.png"));
        this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0));
        this.setUpdateBehavior(new ComponentEnable());
    }

    @Override
    protected String getToolTipText() {
        String originText = super.getToolTipText();
        return originText.replace(KeyEvent.getKeyText(KeyEvent.VK_BACK_SPACE), KeyEvent.getKeyText(KeyEvent.VK_DELETE));
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
        designer.getSelectionModel().deleteSelection();

        return false;
    }
}
