/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.insert.cell;

import com.fr.base.BaseUtils;
import com.fr.design.actions.core.WorkBookSupportable;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.menu.MenuKeySet;

import com.fr.report.cell.painter.BiasTextPainter;

import javax.swing.KeyStroke;

/**
 * Bias
 */
public class BiasCellAction extends AbstractCellAction implements WorkBookSupportable {

    public BiasCellAction() {
        initAction();
    }

    public BiasCellAction(ElementCasePane t) {
        super(t);
        initAction();
    }

    private void initAction() {
        this.setMenuKeySet(INSERT_SLOPE_LINE);
        this.setName(getMenuKeySet().getMenuKeySetName()+ "...");
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_insert/bias.png"));
    }

    public static final MenuKeySet INSERT_SLOPE_LINE = new MenuKeySet() {
        @Override
        public char getMnemonic() {
        	//之前是S, 跟子报表subreport重复了
            return 'X';
        }

        @Override
        public String getMenuName() {
            return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_M_Insert_Slash");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };

    @Override
	public Class getCellValueClass() {
        return BiasTextPainter.class;
    }
}
