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
 * Delete Column
 */
public class DeleteColumnAction extends CellSelectionAction {

    public DeleteColumnAction(ElementCasePane t) {
        this(t, DELETE_COLUMN.getMenuName());
    }

    public DeleteColumnAction(ElementCasePane t, String name) {
        super(t);
        this.setName(name);
        this.setMnemonic(DELETE_COLUMN.getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_report/deleteColumn.png"));
    }

    @Override
    protected boolean executeActionReturnUndoRecordNeededWithCellSelection(
            CellSelection cs) {
        ElementCasePane ePane = this.getEditingComponent();
        ElementCase report = ePane.getEditingElementCase();

        // 删掉多行
        int[] columns = cs.getSelectedColumns();
        for (int i = 0; i < columns.length; i++) {
            report.removeColumn(columns[i] - i);
        }
        return true;
    }

    public static final MenuKeySet DELETE_COLUMN= new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'D';
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
}