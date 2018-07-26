/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.insert.cell;

import com.fr.base.BaseUtils;
import com.fr.design.actions.core.ActionFactory;
import com.fr.design.actions.core.WorkBookSupportable;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.menu.MenuKeySet;


import javax.swing.*;

/**
 * .
 */
public class ChartCellAction extends AbstractCellAction implements WorkBookSupportable {
	public ChartCellAction(){
		initAction();
	}

	public ChartCellAction(ElementCasePane t) {
		super(t);
		initAction();
	}

	private void initAction() {
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
			return com.fr.design.i18n.Toolkit.i18nText("M_Insert-Chart");
		}

		@Override
		public KeyStroke getKeyStroke() {
			return null;
		}
	};


	@Override
	public Class getCellValueClass() {
		return ActionFactory.getChartCollectionClass();
	}
}