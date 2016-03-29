package com.fr.design.actions;

//ElementCaseAction应该有GridSelectionChangeListener，就从悬浮元素和单元格来讲，就必须有了，用来判断这些ElementCaseAction是否可以编辑,当然还可以做些其他事情
//

import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.Selection;
import com.fr.design.selection.SelectionEvent;
import com.fr.design.selection.SelectionListener;

public abstract class ElementCaseAction extends TemplateComponentAction<ElementCasePane> {
	protected ElementCaseAction(ElementCasePane t) {
		super(t);
		t.addSelectionChangeListener(new SelectionListener() {

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
		});
	}

}