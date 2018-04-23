/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.columnrow;

import com.fr.base.BaseUtils;
import com.fr.design.actions.CellSelectionAction;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.menu.MenuKeySet;
import com.fr.general.Inter;
import com.fr.grid.selection.CellSelection;
import com.fr.report.elementcase.ElementCase;

import javax.swing.*;

/**
 * Insert Row
 */
public class InsertRowAction extends CellSelectionAction {

	public InsertRowAction(ElementCasePane t) {
		this(t,INSERT_ROW.getMenuName());
	}

	public InsertRowAction(ElementCasePane t, String name) {
		super(t);
        this.setName(name);
        this.setMnemonic(INSERT_ROW.getMnemonic());
		this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_insert/insertRow.png"));
	}

	public static final MenuKeySet INSERT_ROW = new MenuKeySet() {
		@Override
		public char getMnemonic() {
			return 'I';
		}

		@Override
		public String getMenuName() {
			return Inter.getLocText("Row");
		}

		@Override
		public KeyStroke getKeyStroke() {
			return null;
		}
	};

	@Override
	protected boolean executeActionReturnUndoRecordNeededWithCellSelection(CellSelection cs) {
		ElementCasePane ePane = this.getEditingComponent();

		ElementCase report = ePane.getEditingElementCase();

		int[] rows = cs.getSelectedRows();
		for (int i = 0; i < rows.length; i++) {
			report.insertRow(rows[i]);
		}

		return true;
	}
}