package com.fr.design.actions;

import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.selection.SelectionEvent;
import com.fr.design.selection.SelectionListener;
import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.Selection;

/**
 * Created by daniel on 2016/10/10.
 */
public abstract class SelectionListenerAction extends TemplateComponentAction<ElementCasePane> {

    protected SelectionListenerAction() {
    }

    protected SelectionListenerAction(ElementCasePane elementCasePane) {
        super(elementCasePane);
    }

    protected SelectionListener createSelectionListener() {
        return new SelectionListener() {

            @Override
            public void selectionChanged(SelectionEvent e) {
                update();
                if (DesignerContext.getFormatState() != DesignerContext.FORMAT_STATE_NULL) {
                    Selection selection = getEditingComponent().getSelection();
                    if (selection instanceof CellSelection) {
                        CellSelection cellselection = (CellSelection) selection;
                        //样式处理
                        getEditingComponent().setCellNeedTOFormat(cellselection);
                    }
                }
            }
        };
    }
}
