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
 * Delete Row
 */
public class DeleteRowAction extends CellSelectionAction {

    public DeleteRowAction(ElementCasePane t) {
        this(t, DELETE_ROW.getMenuName());
    }

    public DeleteRowAction(ElementCasePane t, String name) {
        super(t);

        this.setName(name);
        this.setMnemonic(DELETE_ROW.getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_report/deleteRow.png"));
    }

    @Override
    protected boolean executeActionReturnUndoRecordNeededWithCellSelection(
            CellSelection cs) {
        ElementCasePane ePane = this.getEditingComponent();
        ElementCase report = ePane.getEditingElementCase();

        // 删掉多行
        int[] rows = cs.getSelectedRows();
        for (int i = 0; i < rows.length; i++) {
            report.removeRow(rows[i] - i);
        }
        return true;
    }

    public static final MenuKeySet DELETE_ROW = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'D';
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
}