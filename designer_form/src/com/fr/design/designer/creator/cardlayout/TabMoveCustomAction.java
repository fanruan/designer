package com.fr.design.designer.creator.cardlayout;

import com.fr.base.BaseUtils;
import com.fr.design.designer.beans.actions.FormUndoableAction;
import com.fr.design.mainframe.FormDesigner;

/**
 * Created by zhouping on 2017/2/9.
 */
public class TabMoveCustomAction extends FormUndoableAction {
    private XCardSwitchButton xCardSwitchButton;

    public TabMoveCustomAction(FormDesigner t, XCardSwitchButton xCardSwitchButton) {
        super(t);
        this.setName("");
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/control/refresh.png"));
        this.xCardSwitchButton = xCardSwitchButton;
    }

    @Override
    public boolean executeActionReturnUndoRecordNeeded() {
        return false;
    }

    public XCardSwitchButton getxCardSwitchButton() {
        return xCardSwitchButton;
    }

    public void setxCardSwitchButton(XCardSwitchButton xCardSwitchButton) {
        this.xCardSwitchButton = xCardSwitchButton;
    }
}
