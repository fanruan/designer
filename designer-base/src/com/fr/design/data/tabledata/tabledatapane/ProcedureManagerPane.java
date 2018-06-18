package com.fr.design.data.tabledata.tabledatapane;

import com.fr.design.gui.frpane.LoadingBasicPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.file.ProcedureConfig;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;

public class ProcedureManagerPane extends LoadingBasicPane {
	private UITextField connectionTextField;
	private ProcedureListPane procedureListPane;

	protected void initComponents(JPanel container) {
		container.setLayout(FRGUIPaneFactory.createBorderLayout());

		JPanel connectionPathPane = FRGUIPaneFactory.createBorderLayout_L_Pane();
		container.add(connectionPathPane, BorderLayout.NORTH);

		connectionPathPane.setBorder(BorderFactory.createEmptyBorder(6, 2, 2, 2));

		connectionPathPane.add(new UILabel(Inter.getLocText("FR-Designer_Save_Path") + ":"), BorderLayout.WEST);
		this.connectionTextField = new UITextField();
		connectionPathPane.add(connectionTextField, BorderLayout.CENTER);
		this.connectionTextField.setEditable(false);
        procedureListPane = new ProcedureListPane();
		container.add(procedureListPane, BorderLayout.CENTER);
	}
	
	@Override
	protected String title4PopupWindow() {
		return Inter.getLocText("Datasource-Stored_Procedure");
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