/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.insert.cell;

import com.fr.base.BaseUtils;
import com.fr.design.actions.core.ActionUtils;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.menu.MenuKeySet;
import com.fr.general.Inter;

import javax.swing.*;

/**
 * .
 */
public class ChartCellAction extends AbstractCellAction {
	public ChartCellAction(ElementCasePane t) {
		super(t);
        this.setMenuKeySet(INSERT_CHART);
        this.setName(getMenuKeySet().getMenuKeySetName()+ "...");
        this.setMnemonic(getMenuKeySet().getMnemonic());
		this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_insert/chart.png"));
	}

	public static final MenuKeySet INSERT_CHART = new MenuKeySet() {
		@Override
		public char getMnemonic() {
			return 'C';
		}

		@Override
		public String getMenuName() {
			return Inter.getLocText("M_Insert-Chart");
		}

		@Override
		public KeyStroke getKeyStroke() {
			return null;
		}
	};


	@Override
	public Class getCellValueClass() {
		return ActionUtils.getChartCollectionClass();
	}
}