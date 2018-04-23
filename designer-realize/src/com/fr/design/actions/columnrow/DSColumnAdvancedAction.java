package com.fr.design.actions.columnrow;

import com.fr.base.BaseUtils;
import com.fr.design.actions.cell.AbstractCellElementAction;
import com.fr.design.dscolumn.DSColumnAdvancedPane;
import com.fr.design.dialog.BasicPane;
import com.fr.general.Inter;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.report.cell.TemplateCellElement;

public class DSColumnAdvancedAction extends AbstractCellElementAction {
	private boolean  returnValue=false;
	private TemplateCellElement editCellElement;
	public DSColumnAdvancedAction(ElementCasePane t) {
		super(t);
		
		this.setName(Inter.getLocText("Advanced"));
		this.setMnemonic('A');
		this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/expand/cellAttr.gif"));
    }

	@Override
	protected BasicPane populateBasicPane(TemplateCellElement cellElement) {
		this.editCellElement = cellElement;
		DSColumnAdvancedPane dSColumnAdvancedPane = new DSColumnAdvancedPane();
		dSColumnAdvancedPane.populate(cellElement);
		return dSColumnAdvancedPane;
	}


	@Override
	protected void updateBasicPane(BasicPane basicPane,
			TemplateCellElement cellElement) {
		((DSColumnAdvancedPane)basicPane).update(editCellElement);
	}
}