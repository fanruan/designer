package com.fr.design.actions;

import com.fr.base.BaseUtils;
import com.fr.design.data.datapane.TableDataTreePane;
import com.fr.design.DesignModelAdapter;
import com.fr.design.designer.TargetComponent;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.mainframe.*;


/**
 * Author : daisy
 * Date: 13-9-2
 * Time: 下午3:36
 */
public class ExitAuthorityEditAction extends TemplateComponentAction {

	public ExitAuthorityEditAction(TargetComponent t) {
		super(t);
		this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Authority_Edit_Status_Exit"));
		this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_report/exit_authority_edit.png"));
	}


	public void prepare4Undo() {
		// 遗留代码
	}

	@Override
	public boolean executeActionReturnUndoRecordNeeded() {
		TargetComponent tc = getEditingComponent();
		if (tc == null) {
			return false;
		}

		if (BaseUtils.isAuthorityEditing()) {
			BaseUtils.setAuthorityEditing(false);
			WestRegionContainerPane.getInstance().replaceDownPane(TableDataTreePane.getInstance(DesignModelAdapter.getCurrentModelAdapter()));
			HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().refreshEastPropertiesPane();
			DesignerContext.getDesignerFrame().resetToolkitByPlus(tc.getToolBarMenuDockPlus());
			DesignerContext.getDesignerFrame().needToAddAuhtorityPaint();
			DesignerContext.getDesignerFrame().refreshDottedLine();

			fireAuthorityStateToNomal();
		}
		return true;
	}


	/**
	 * 退出权限编辑时，将所有的做过权限编辑的状态，作为一个状态赋给报、报表主体
	 */
	private void fireAuthorityStateToNomal() {
		java.util.List<JTemplate<?, ?>> opendedTemplate = HistoryTemplateListPane.getInstance().getHistoryList();
		for (int i = 0; i < opendedTemplate.size(); i++) {
			//如果在权限编辑时做过操作，则将做过的操作作为一个整体状态赋给正在报表
			if (opendedTemplate.get(i).isDoSomethingInAuthority()) {
				opendedTemplate.get(i).fireAuthorityStateToNomal();
			}
		}
	}
}