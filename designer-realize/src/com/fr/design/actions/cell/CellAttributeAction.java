package com.fr.design.actions.cell;

import com.fr.base.BaseUtils;
import com.fr.design.menu.KeySetUtils;
import com.fr.general.Inter;

/**
 * Cell Attribute.
 */
public class CellAttributeAction extends CellAttributeTableAction {
	public CellAttributeAction() {
        this.setMenuKeySet(KeySetUtils.CELL_OTHER_ATTR);
        this.setName(getMenuKeySet().getMenuKeySetName());
        this.setMnemonic(getMenuKeySet().getMnemonic());
		 this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_format/cellAttr.png"));
	}

	@Override
	protected String getID() {
		return Inter.getLocText("Datasource-Other_Attributes");
	}

}