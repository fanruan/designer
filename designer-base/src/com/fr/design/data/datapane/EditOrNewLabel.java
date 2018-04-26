package com.fr.design.data.datapane;

import java.awt.Cursor;

import com.fr.design.gui.ilable.UILabel;
import javax.swing.JPanel;

import com.fr.base.BaseUtils;
import com.fr.design.data.datapane.FlashLookLabelMouseAdapter.ReleaseAction;
import com.fr.general.Inter;


public class EditOrNewLabel extends UILabel {
	JPanel editTablePane;
	Editable editable;
	
	public EditOrNewLabel(Editable editable, JPanel editPane) {
		super(BaseUtils.readIcon("/com/fr/design/images/m_file/edit.png"));
		this.setToolTipText(Inter.getLocText("Edit"));
		this.setCursor(new Cursor(Cursor.HAND_CURSOR));
		this.editable = editable;
		this.editTablePane = editPane;
		this.addMouseListener(new FlashLookLabelMouseAdapter(this, new ReleaseAction() {
			@Override
			public void releaseAction() {
				EditOrNewLabel.this.editable.edit(EditOrNewLabel.this.editTablePane);
			}
		}));
	}

	public static interface Editable {
		public void edit(JPanel jPanel);
	}
}