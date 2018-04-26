package com.fr.design.data.tabledata.wrapper;

import com.fr.base.BaseUtils;
import com.fr.base.TableData;
import com.fr.data.impl.ClassTableData;
import com.fr.data.impl.DBTableData;
import com.fr.data.impl.EmbeddedTableData;
import com.fr.data.impl.RecursionTableData;
import com.fr.data.impl.storeproc.StoreProcedure;
import com.fr.design.icon.WarningIcon;
import com.fr.stable.StringUtils;

import javax.swing.*;

public final class ServerTableDataWrapper extends AbstractTableDataWrapper {
	public ServerTableDataWrapper(TableData tabledata) {
		super(tabledata);
	}

	public ServerTableDataWrapper(TableData tabledata, String name) {
		super(tabledata, name);
	}

	@Override
	public Icon getIcon() {
		if (tabledata instanceof DBTableData) {
			if (StringUtils.isBlank(((DBTableData) tabledata).getQuery()))
				return new WarningIcon(BaseUtils.readImage("/com/fr/design/images/data/dock/serverdatabase.png"));
			else
				return BaseUtils.readIcon("/com/fr/design/images/data/dock/serverdatabase.png");
		} else if (tabledata instanceof ClassTableData) {
			return BaseUtils.readIcon("/com/fr/design/images/data/dock/serverclasstabledata.png");
		} else if (tabledata instanceof EmbeddedTableData) {
			return BaseUtils.readIcon("/com/fr/design/images/data/dock/serverdatatable.png");
		}else if(tabledata instanceof RecursionTableData){
			return BaseUtils.readIcon("/com/fr/design/images/data/tree.png");
		} else if (tabledata instanceof StoreProcedure) {
            return BaseUtils.readIcon("/com/fr/design/images/data/store_procedure.png");
        }
		return BaseUtils.readIcon("/com/fr/design/images/data/dock/serverdatabase.png");
	}

    /**
     * 是否异常
     * @return
     */
	@Override
	public boolean isUnusual() {
		//目前只判断DBTableData的异常
		if (tabledata instanceof DBTableData && StringUtils.isBlank(((DBTableData) tabledata).getQuery())) {
			return true;
		}
		return false;
	}


}