package com.fr.design.designer.creator.cardlayout;

import com.fr.design.mainframe.FormDesigner;
import com.fr.general.IOUtils;

/**
 * Created by zhouping on 2017/2/9.
 */
public class TabMoveCustomAction extends TabMoveAction {

    public TabMoveCustomAction(FormDesigner t, XCardSwitchButton xCardSwitchButton) {
        super(t, xCardSwitchButton);
        this.setName("");
        this.setSmallIcon(IOUtils.readIcon("/com/fr/design/images/control/refresh.png"));
    }

}
