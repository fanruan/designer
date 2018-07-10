/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.edit;

import com.fr.base.BaseUtils;
import com.fr.design.actions.UpdateAction;
import com.fr.design.mainframe.EastRegionContainerPane;
import com.fr.design.menu.KeySetUtils;

import java.awt.event.ActionEvent;

/**
 * HyperlinkAction.
 */
public class HyperlinkAction extends UpdateAction {

    public HyperlinkAction() {
        this.setMenuKeySet(KeySetUtils.HYPER_LINK);
        this.setName(getMenuKeySet().getMenuKeySetName());
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_insert/hyperLink.png"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        EastRegionContainerPane.getInstance().switchTabTo(EastRegionContainerPane.KEY_HYPERLINK);
        EastRegionContainerPane.getInstance().setWindow2PreferWidth();
    }
}