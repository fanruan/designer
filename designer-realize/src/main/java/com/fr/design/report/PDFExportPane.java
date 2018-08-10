package com.fr.design.report;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.dialog.BasicPane;

import com.fr.io.attr.PDFExportAttr;
import com.fr.stable.StringUtils;

public class PDFExportPane extends BasicPane {
	private UICheckBox isNeedPassword;
	private UITextField passwordField;
	private JPanel passwordWritePane;

	public PDFExportPane() {
		this.initComponents();
	}

	protected void initComponents() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		JPanel outnorthPane =FRGUIPaneFactory.createTitledBorderPane("PDF" + com.fr.design.i18n.Toolkit.i18nText("ReportD-Excel_Export"));
		JPanel northPane=FRGUIPaneFactory.createY_AXISBoxInnerContainer_M_Pane();
		this.add(outnorthPane);
		outnorthPane.add(northPane);
		JPanel rowAndColumnPane =FRGUIPaneFactory.createNormalFlowInnerContainer_M_Pane();
		northPane.add(rowAndColumnPane);
		
		JPanel passwordPane =FRGUIPaneFactory.createNormalFlowInnerContainer_M_Pane();
		isNeedPassword = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("IS_Need_Password"), false);
		passwordPane.add(isNeedPassword);
		northPane.add(passwordPane);
		passwordWritePane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
		passwordPane.add(passwordWritePane);
//		UILabel passwordLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nTextArray(new String[]{"File", "Password"}) + ":");
		UILabel passwordLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_File_Password") + ":");
		passwordWritePane.add(passwordLabel);
		passwordField = new UITextField(11);
		passwordWritePane.add(passwordField);
		isNeedPassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				passwordWritePane.setVisible(isNeedPassword.isSelected());
			}
		});
		
	}
	
	@Override
	protected String title4PopupWindow() {
		return "PDFExport";
	}

	public void populate(PDFExportAttr pdfExportAttr) {
		if(pdfExportAttr == null){
			return;
		}
		if (StringUtils.isEmpty(pdfExportAttr.getPassword())) {
			isNeedPassword.setSelected(false);
			passwordWritePane.setVisible(false);
		} else {
			isNeedPassword.setSelected(true);
			passwordField.setText(pdfExportAttr.getPassword());
			passwordWritePane.setVisible(true);
		}
	}

	public PDFExportAttr update() {

		PDFExportAttr pdfExportAttr = new PDFExportAttr();
		
		if (!isNeedPassword.isSelected()) {
			passwordField.setText(null);
		}
		pdfExportAttr.setPassword(passwordField.getText());
		return pdfExportAttr;
	}
}