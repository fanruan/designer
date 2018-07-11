/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.cell;

import com.fr.base.BaseUtils;
import com.fr.design.actions.UpdateAction;
import com.fr.design.mainframe.EastRegionContainerPane;
import com.fr.design.menu.KeySetUtils;

import java.awt.event.ActionEvent;

/**
 * Condition Attributes.
 */
public class ConditionAttributesAction extends UpdateAction {
    public ConditionAttributesAction() {
        this.setMenuKeySet(KeySetUtils.CONDITION_ATTR);
        this.setName(getMenuKeySet().getMenuKeySetName());
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_format/highlight.png"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        EastRegionContainerPane.getInstance().switchTabTo(EastRegionContainerPane.KEY_CONDITION_ATTR);
        EastRegionContainerPane.getInstance().setWindow2PreferWidth();
    }

    @Override
    public void update() {
        super.update();
        this.setEnabled(EastRegionContainerPane.getInstance().isConditionAttrPaneEnabled());
    }
}