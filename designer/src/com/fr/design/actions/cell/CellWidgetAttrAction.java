package com.fr.design.actions.cell;

import com.fr.base.BaseUtils;
import com.fr.design.actions.UpdateAction;
import com.fr.design.mainframe.EastRegionContainerPane;
import com.fr.design.menu.KeySetUtils;

import java.awt.event.ActionEvent;

/**
 * Cell Widget Attribute.
 */
public class CellWidgetAttrAction extends UpdateAction {

    public CellWidgetAttrAction() {
        this.setMenuKeySet(KeySetUtils.CELL_WIDGET_ATTR);
        this.setName(getMenuKeySet().getMenuKeySetName());
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_format/modified.png"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        EastRegionContainerPane.getInstance().switchTabTo(EastRegionContainerPane.KEY_WIDGET_SETTINGS);
        EastRegionContainerPane.getInstance().setWindow2PreferWidth();
    }

    @Override
    public void update() {
        super.update();
        this.setEnabled(EastRegionContainerPane.getInstance().isWidgetSettingsPaneEnabled());
    }
}