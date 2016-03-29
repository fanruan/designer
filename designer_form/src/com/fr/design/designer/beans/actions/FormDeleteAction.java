/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.designer.beans.actions;

import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import com.fr.base.BaseUtils;
import com.fr.general.Inter;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.FormSelection;

/**
 * @author richer
 * @since 6.5.3
 */
public class FormDeleteAction extends FormUndoableAction {

	public FormDeleteAction(FormDesigner t) {
		super(t);

		this.setName(Inter.getLocText("M_Edit-Delete"));
		this.setMnemonic('D');
		// Richie:删除菜单图标
		this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_report/delete.png"));
		this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
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
	
	@Override
	public void update() {
//		FormDesigner f = this.getEditingComponent();
//		if (f == null) {
//			this.setEnabled(false);
//			return;
//		}
//		SelectionModel selection = f.getSelectionModel();
//		this.setEnabled(selection.hasSelectionComponent());
		this.setEnabled(true);
	}
}