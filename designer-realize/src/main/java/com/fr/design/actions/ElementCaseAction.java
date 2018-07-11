package com.fr.design.actions;

//ElementCaseAction应该有GridSelectionChangeListener，就从悬浮元素和单元格来讲，就必须有了，用来判断这些ElementCaseAction是否可以编辑,当然还可以做些其他事情
//

import com.fr.design.mainframe.ElementCasePane;

public abstract class ElementCaseAction extends SelectionListenerAction {
    protected ElementCaseAction() {
    }

    protected ElementCaseAction(ElementCasePane t) {
        super(t);
        t.addSelectionChangeListener(createSelectionListener());
    }

}