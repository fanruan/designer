package com.fr.design.actions.cell;

import com.fr.base.BaseUtils;
import com.fr.design.menu.KeySetUtils;


public class CellExpandAttrAction extends  CellAttributeTableAction{

	public CellExpandAttrAction() {
		super();
        this.setMenuKeySet(KeySetUtils.CELL_EXPAND_ATTR);
        this.setName(getMenuKeySet().getMenuKeySetName());
        this.setMnemonic(getMenuKeySet().getMnemonic());
		this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/expand/cellAttr.gif"));
	}



	@Override
	public String getID() {
		return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_ExpandD_Expand_Attribute");
	}

}