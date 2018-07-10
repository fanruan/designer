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
 * Insert Column
 */
public class InsertColumnAction extends CellSelectionAction {

    public InsertColumnAction(ElementCasePane t) {
        this(t, INSERT_COLUMN.getMenuName());
    }

    public InsertColumnAction(ElementCasePane t, String name) {
        super(t);

        this.setName(name);
        this.setMnemonic(INSERT_COLUMN.getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_insert/insertColumn.png"));
    }

    public static final MenuKeySet INSERT_COLUMN = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'I';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("Column");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };

    @Override
    protected boolean executeActionReturnUndoRecordNeededWithCellSelection(
            CellSelection cs) {
        ElementCasePane ePane = this.getEditingComponent();
        ElementCase report = ePane.getEditingElementCase();

        int[] columns = cs.getSelectedColumns();
        for (int i = 0; i < columns.length; i++) {
            report.insertColumn(columns[i]);
        }
        return true;
    }
}