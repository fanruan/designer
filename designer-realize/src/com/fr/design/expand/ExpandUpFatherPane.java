package com.fr.design.expand;

import com.fr.report.cell.cellattr.CellExpandAttr;
import com.fr.stable.ColumnRow;

public class ExpandUpFatherPane extends ExpandFatherPane {

	@Override
	protected ColumnRow getColumnRow(CellExpandAttr cellExpandAttr) {
		return cellExpandAttr.getUpParentColumnRow();
	}

	@Override
	protected boolean isParentDefault(CellExpandAttr cellExpandAttr) {
		return cellExpandAttr.isUpParentDefault();
	}

	@Override
	protected void setValue(CellExpandAttr cellExpandAttr, boolean isDefault, ColumnRow columnRow) {
		cellExpandAttr.setUpParentDefault(isDefault);
		cellExpandAttr.setUpParentColumnRow(columnRow);
	}

}