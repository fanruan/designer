package com.fr.design.data.tabledata.tabledatapane;

import com.fr.design.gui.frpane.LoadingBasicPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.file.ProcedureConfig;


import javax.swing.*;
import java.awt.*;

public class ProcedureManagerPane extends LoadingBasicPane {
	private ProcedureListPane procedureListPane;

	protected void initComponents(JPanel container) {
		container.setLayout(FRGUIPaneFactory.createBorderLayout());

        procedureListPane = new ProcedureListPane();
		container.add(procedureListPane, BorderLayout.CENTER);
	}
	
	@Override
	protected String title4PopupWindow() {
		return com.fr.design.i18n.Toolkit.i18nText("Datasource-Stored_Procedure");
	}

	public void populate(ProcedureConfig procedureConfig) {
		//todo 原来界面上显示的xml路径
//		this.connectionTextField.setText(WorkContext.getCurrent().getPath() + File.separator + ProjectConstants.RESOURCES_NAME
//				+ File.separator + datasourceManager.fileName());
		this.procedureListPane.populate(procedureConfig);
	}

	public void update(ProcedureConfig procedureConfig) {
		this.procedureListPane.update(procedureConfig);
	}

}