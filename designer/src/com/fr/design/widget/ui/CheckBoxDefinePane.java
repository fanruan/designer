package com.fr.design.widget.ui;

import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.gui.ilable.UILabel;

import javax.swing.*;

import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.form.ui.CheckBox;
import com.fr.general.Inter;

public class CheckBoxDefinePane extends AbstractDataModify<CheckBox> {
	private UITextField text;

	public CheckBoxDefinePane() {
		this.iniComoponents();
	}
	
	private void iniComoponents() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		this.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
		JPanel textPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
		textPane.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
		UIExpandablePane uiExpandablePane = new UIExpandablePane(Inter.getLocText("FR-Designer_Advanced"), 280, 20, textPane);
		textPane.add(new UILabel(Inter.getLocText("Text") + ":"));
		text = new UITextField(8);
		textPane.add(text);
		this.add(uiExpandablePane);
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