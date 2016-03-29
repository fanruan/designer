package com.fr.design.designer.beans.actions;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import com.fr.base.BaseUtils;
import com.fr.general.Inter;
import com.fr.design.mainframe.FormDesigner;

public class PasteAction extends FormEditAction {

	public PasteAction(FormDesigner t) {
		super(t);
		this.setName(Inter.getLocText("M_Edit-Paste"));
		this.setMnemonic('P');
		this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_edit/paste.png"));
		this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK));
	}

	@Override
	public boolean executeActionReturnUndoRecordNeeded() {
		FormDesigner tc = getEditingComponent();
		if (tc == null) {
			return false;
		}
		return tc.paste();
	}
	@Override
	public void update() {
		this.setEnabled(true);
	}

}