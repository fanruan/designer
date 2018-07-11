/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.poly.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import com.fr.base.BaseUtils;
import com.fr.design.actions.TemplateComponentActionInterface;
import com.fr.design.actions.UpdateAction;
import com.fr.general.Inter;
import com.fr.poly.PolyDesigner;

/**
 * @author richer
 * @since 6.5.4 创建于2011-4-11
 */
public class DeleteBlockAction extends UpdateAction implements TemplateComponentActionInterface<PolyDesigner> {

	public DeleteBlockAction(PolyDesigner pd) {
		this.pd = pd;
		this.setName(Inter.getLocText("M_Edit-Delete"));
		this.setMnemonic('D');
		this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
		this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_report/delete.png"));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (BaseUtils.isAuthorityEditing()) {
			return;
		}
		final PolyDesigner designer = this.getEditingComponent();
		if (designer == null) {
			return;
		}

		if (designer.delete()) {
			designer.fireTargetModified();
		}
	}

	private PolyDesigner pd;

	@Override
	public PolyDesigner getEditingComponent() {
		return pd;
	}
}