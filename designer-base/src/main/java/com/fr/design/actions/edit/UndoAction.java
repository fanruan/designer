/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.edit;

import java.awt.event.ActionEvent;

import com.fr.base.BaseUtils;
import com.fr.design.DesignerEnvManager;
import com.fr.design.actions.TemplateComponentActionInterface;
import com.fr.design.actions.UpdateAction;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.menu.KeySetUtils;

/**
 * Undo.
 */
public class UndoAction extends UpdateAction implements TemplateComponentActionInterface<JTemplate<?,?>> {
	private JTemplate<?, ?> t;

    public UndoAction(JTemplate<?, ?> t) {
		this.t = t;
        this.setMenuKeySet(KeySetUtils.UNDO);
        this.setName(getMenuKeySet().getMenuKeySetName());
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_edit/undo.png"));
        this.setAccelerator(getMenuKeySet().getKeyStroke());
    }
    
    @Override
    public JTemplate<?, ?> getEditingComponent() {
    	return t;
    }

    /**
     * 动作
     * @param e 事件
     */
    public void actionPerformed(ActionEvent e) {
    	JTemplate<?, ?> undoComponent = getEditingComponent();
        if (undoComponent != null) {
            undoComponent.undo();
        }
    }

    /**
     * update enable
     */
    @Override
    public void update() {
    	JTemplate<?, ?> undoComponent = getEditingComponent();
		if (DesignerEnvManager.getEnvManager().isSupportUndo()) {
			this.setEnabled(undoComponent != null && undoComponent.canUndo());
		} else {
			this.setEnabled(false);
		}


	}
}