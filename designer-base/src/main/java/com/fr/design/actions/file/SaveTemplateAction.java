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
 * @author richer
 * @since 6.5.3
 */
public class SaveTemplateAction extends JTemplateAction<JTemplate<?, ?>> {

	public SaveTemplateAction(JTemplate<?, ?> jt) {
		super(jt);
        this.setMenuKeySet(KeySetUtils.SAVE_TEMPLATE);
        this.setName(getMenuKeySet().getMenuKeySetName());
        this.setMnemonic(getMenuKeySet().getMnemonic());
		this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_file/save.png"));
        this.setAccelerator(getMenuKeySet().getKeyStroke());
	}

    /**
     * 动作
     * @param e 事件
     */
	public void actionPerformed(ActionEvent e) {
		JTemplate<?, ?> jt = this.getEditingComponent();
		jt.stopEditing();
		jt.saveTemplate();
		jt.requestFocus();
	}

	@Override
	public void update() {
		 super.update();
		 this.setEnabled(!this.getEditingComponent().isSaved());
	}

}