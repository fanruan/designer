package com.fr.design.expand;

import com.fr.report.cell.cellattr.CellExpandAttr;
import com.fr.stable.ColumnRow;

public class ExpandLeftFatherPane extends ExpandFatherPane {

	@Override
	protected ColumnRow getColumnRow(CellExpandAttr cellExpandAttr) {
		return cellExpandAttr.getLeftParentColumnRow();
	}

	@Override
	protected boolean isParentDefault(CellExpandAttr cellExpandAttr) {
		return cellExpandAttr.isLeftParentDefault();
	}

	@Override
	protected void setValue(CellExpandAttr cellExpandAttr, boolean isDefault, ColumnRow columnRow) {
		cellExpandAttr.setLeftParentDefault(isDefault);
		cellExpandAttr.setLeftParentColumnRow(columnRow);
	}

}