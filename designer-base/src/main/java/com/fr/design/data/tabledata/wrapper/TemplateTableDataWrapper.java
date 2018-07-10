package com.fr.design.data.tabledata.wrapper;

import javax.swing.Icon;

import com.fr.base.BaseUtils;
import com.fr.base.TableData;
import com.fr.data.impl.DBTableData;
import com.fr.design.icon.WarningIcon;
import com.fr.stable.StringUtils;

public final class TemplateTableDataWrapper extends AbstractTableDataWrapper {
	public TemplateTableDataWrapper(TableData tabledata) {
		super(tabledata);
	}

	public TemplateTableDataWrapper(TableData tabledata, String name) {
		super(tabledata, name);
	}

	@Override
	public Icon getIcon() {
		if (tabledata instanceof DBTableData && StringUtils.isBlank(((DBTableData) tabledata).getQuery())) {
			return new WarningIcon(BaseUtils.readImage("/com/fr/design/images/data/database.png"));
		}
		return BaseUtils.readIcon(TableDataFactory.getIconPath(tabledata));
	}

	@Override
	public boolean isUnusual() {
		// 目前只判断DBTableData的异常
		if (tabledata instanceof DBTableData && StringUtils.isBlank(((DBTableData) tabledata).getQuery())) {
			return true;
		}
		return false;
	}

}