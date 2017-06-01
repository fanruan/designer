/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.report;

import com.fr.base.BaseUtils;
import com.fr.base.ConfigManager;
import com.fr.base.Env;
import com.fr.base.FRContext;
import com.fr.design.actions.JWorkBookAction;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.JWorkBook;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.webattr.ReportWebAttrPane;
import com.fr.general.IOUtils;
import com.fr.main.TemplateWorkBook;
import com.fr.base.ConfigManagerProvider;

import java.awt.event.ActionEvent;

/**
 * ReportWebAttr
 */
public class ReportWebAttrAction extends JWorkBookAction {

	public ReportWebAttrAction(JWorkBook jwb) {
		super(jwb);
        this.setMenuKeySet(KeySetUtils.REPORT_WEB_ATTR);
        this.setName(getMenuKeySet().getMenuKeySetName()+"...");
        this.setMnemonic(getMenuKeySet().getMnemonic());
		this.setSmallIcon(IOUtils.readIcon("/com/fr/design/images/m_report/webreportattribute.png"));
		this.setSearchText(new ReportWebAttrPane());
	}

    /**
     * 动作
     * @param evt 事件
     */
	public void actionPerformed(ActionEvent evt) {
		final JWorkBook jwb = getEditingComponent();
		if (jwb == null) {
			return;
		}
		final TemplateWorkBook wbTpl = jwb.getTarget();
		final ReportWebAttrPane reportWebAttrPane = new ReportWebAttrPane() {
			@Override
			public void complete() {
				populate(wbTpl.getReportWebAttr());
			}
		};
		final BasicDialog dialog = reportWebAttrPane.showWindow(
			DesignerContext.getDesignerFrame()
		);

		dialog.addDialogActionListener(new DialogActionAdapter() {
			@Override
			public void doOk() {
				wbTpl.setReportWebAttr(reportWebAttrPane.update());
				final ConfigManagerProvider configManager = ConfigManager.getProviderInstance();
				Env currentEnv = FRContext.getCurrentEnv();
				try {
					currentEnv.writeResource(configManager);
				} catch (Exception ex) {
					FRContext.getLogger().error(ex.getMessage(), ex);
				}
				jwb.fireTargetModified();
			}
		});

		dialog.setVisible(true);
		
	}
}