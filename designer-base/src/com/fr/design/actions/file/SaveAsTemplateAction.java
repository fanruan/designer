/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.file;

import java.awt.event.ActionEvent;

import com.fr.base.BaseUtils;
import com.fr.design.actions.JTemplateAction;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.menu.KeySetUtils;

/**
 * Save as file
 */
public class SaveAsTemplateAction extends JTemplateAction<JTemplate<?, ?>> {

    public SaveAsTemplateAction(JTemplate<?, ?> dPane) {
        super(dPane);
        this.setMenuKeySet(KeySetUtils.SAVE_AS_TEMPLATE);
        this.setName(getMenuKeySet().getMenuKeySetName() + "...");
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_file/saveAs.png"));
    }

    /**
     * 动作
     * @param e 事件
     */
    public void actionPerformed(ActionEvent e) {
        JTemplate<?, ?> jt = this.getEditingComponent();
        // kunsnat: 保存前停止编辑状态,保存属性.
        jt.stopEditing();
        jt.saveAsTemplate();
        this.setEnabled(true);

        jt.requestFocus();
    }

    @Override
    public void update() {
        super.update();

//    	this.setEnabled(!this.getEditingComponent().isSaved());
//    	另存为按钮应该一直可用使用
        this.setEnabled(true);
    }
}