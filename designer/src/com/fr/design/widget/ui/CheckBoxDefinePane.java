package com.fr.design.widget.ui;

import com.fr.design.gui.frpane.TreeSettingPane;
import com.fr.design.gui.ilable.UILabel;

import javax.swing.*;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.present.dict.DictionaryPane;
import com.fr.form.ui.CheckBox;
import com.fr.general.Inter;
import com.fr.design.widget.DataModify;

public class CheckBoxDefinePane extends AbstractDataModify<CheckBox> {
	private UITextField text;

	public CheckBoxDefinePane() {
		this.iniComoponents();
	}
	
	private void iniComoponents() {
//		this.setLayout(FRGUIPaneFactory.createBorderLayout());
//
//		UILabel infoLabel = new UILabel();
//        FRFont frFont = FRContext.getDefaultValues().getFRFont();
//        infoLabel.setFont(new Font(frFont.getFamily(), Font.BOLD, 24));
//	    infoLabel.setText(Inter.getLocText(
//				"No_Editor_Property_Definition") + ".");
//	    infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
//
//		this.add(infoLabel, BorderLayout.CENTER);
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		this.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
		JPanel textPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
		textPane.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
		JPanel advancedPane = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText("Advanced"));
		advancedPane.add(textPane);
		textPane.add(new UILabel(Inter.getLocText("Text") + ":"));
		text = new UITextField(8);
		textPane.add(text);
		this.add(advancedPane);
	}
	
	@Override
	protected String title4PopupWindow() {
		return "CheckBox";
	}
	
	@Override
	public void populateBean(CheckBox check) {
		text.setText(check.getText());
	}

	@Override
	public CheckBox updateBean() {
		CheckBox box = new CheckBox();
		box.setText(text.getText());
		return box;
	}
}