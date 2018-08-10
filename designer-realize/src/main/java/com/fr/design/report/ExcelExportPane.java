package com.fr.design.report;


import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.io.attr.ExcelExportAttr;
import com.fr.stable.StringUtils;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExcelExportPane extends BasicPane {
	private UICheckBox isExportHidedRow;
	private UICheckBox isExportHidenColumn;
	private UICheckBox isNeedPassword;
	private UITextField passwordField;
	private UICheckBox protectedWord;
	private UITextField protectedField;
	
	private JPanel passwordWritePane;
	private JPanel wordPane;

	public ExcelExportPane() {
		this.initComponents();
	}

	protected void initComponents() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		JPanel outnorthPane =FRGUIPaneFactory.createTitledBorderPane("Excel" + com.fr.design.i18n.Toolkit.i18nText("ReportD-Excel_Export"));
		JPanel northPane=FRGUIPaneFactory.createY_AXISBoxInnerContainer_M_Pane();
		this.add(outnorthPane);
		outnorthPane.add(northPane);
		JPanel rowAndColumnPane =FRGUIPaneFactory.createNormalFlowInnerContainer_M_Pane();
		northPane.add(rowAndColumnPane);
		isExportHidedRow = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_ReportD_Export_Hided_Row"));
		isExportHidedRow.setSelected(false);
		rowAndColumnPane.add(isExportHidedRow);
		isExportHidenColumn = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_ReportD_Export_Hided_Column"));
		isExportHidenColumn.setSelected(false);
		rowAndColumnPane.add(isExportHidenColumn);
		
		JPanel passwordPane =FRGUIPaneFactory.createNormalFlowInnerContainer_M_Pane();
		isNeedPassword = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_IS_Need_Password"), false);
		passwordPane.add(isNeedPassword);
		northPane.add(passwordPane);
		passwordWritePane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
		passwordPane.add(passwordWritePane);
//		UILabel passwordLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nTextArray(new String[]{"File", "Password"}) + ":");
		UILabel passwordLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_File_Password"));
		passwordWritePane.add(passwordLabel);
		passwordField = new UITextField(11);
		passwordWritePane.add(passwordField);
//		UIButton displayPasswordButton = new UIButton("...");
		isNeedPassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isNeedPassword.isSelected()) {
					passwordWritePane.setVisible(true);
				} else {
					passwordWritePane.setVisible(false);
				}
			}

		});
		
		
		JPanel protectedWordPane =FRGUIPaneFactory.createNormalFlowInnerContainer_M_Pane();
//		protectedWord = new UICheckBox(com.fr.design.i18n.Toolkit.i18nTextArray(new String[]{"Protected", "Password"}));
		protectedWord = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Protected_Password"));
		wordPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
//		wordPane.add(new UILabel(com.fr.design.i18n.Toolkit.i18nTextArray(new String[]{"Sheet", "Password"}) + ":"));
		wordPane.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Sheet_Password")));
		protectedField = new UITextField(11);
		wordPane.add(protectedField);
		protectedWordPane.add(protectedWord);
		protectedWordPane.add(wordPane);
		northPane.add(protectedWordPane);
		protectedWord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (protectedWord.isSelected()) {
					wordPane.setVisible(true);
				} else {
					wordPane.setVisible(false);
					protectedField.setText(null);
				}
			}

		});
	}
	
	@Override
	protected String title4PopupWindow() {
		return "ExcelExport";
	}

	public void populate(ExcelExportAttr excelExportAttr) {
		if(excelExportAttr == null){
			return;
		}
		isExportHidedRow.setSelected(excelExportAttr.isExportHidedRow());
		isExportHidenColumn.setSelected(excelExportAttr.isExportHidedColumn());
		if (StringUtils.isEmpty(excelExportAttr.getPassword())) {
			isNeedPassword.setSelected(false);
			passwordWritePane.setVisible(false);
		} else {
			isNeedPassword.setSelected(true);
			passwordField.setText(excelExportAttr.getPassword());
			passwordWritePane.setVisible(true);
		}
		if (StringUtils.isEmpty(excelExportAttr.getProtectedWord())) {
			this.protectedWord.setSelected(false);
			this.wordPane.setVisible(false);
		} else {
			this.protectedWord.setSelected(true);
			this.wordPane.setVisible(true);
			this.protectedField.setText(excelExportAttr.getProtectedWord());
		}
	}

	public ExcelExportAttr update() {

		ExcelExportAttr excelExportAttr = new ExcelExportAttr();
		
		excelExportAttr.setExportHidedColumn(isExportHidenColumn.isSelected());
		excelExportAttr.setExportHidedRow(isExportHidedRow.isSelected());
		if (!isNeedPassword.isSelected()) {
			passwordField.setText(null);
		}
		excelExportAttr.setPassword(passwordField.getText());
		excelExportAttr.setProtectedWord(this.protectedField.getText());
		return excelExportAttr;
	}
}
