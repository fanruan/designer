package com.fr.design.widget.ui.designer;


import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UIBasicSpinner;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.form.ui.ButtonGroup;
import com.fr.general.Inter;

import javax.swing.*;


public class ButtonGroupDictPane extends JPanel {
	private UIBasicSpinner columnSpinner;
	private UICheckBox adaptiveCheckbox;
	private UILabel columnLabel;
	
	public ButtonGroupDictPane() {
		this.initComponents();
	}

    /**
    *
     */
	public void initComponents() {
		this.setLayout(FRGUIPaneFactory.createLabelFlowLayout());
		adaptiveCheckbox = new UICheckBox(Inter.getLocText("Adaptive"), true);
		this.add(adaptiveCheckbox);

		this.columnLabel = new UILabel(Inter.getLocText("Button-Group-Display-Columns") + ":");
		this.add(columnLabel);
		columnSpinner = new UIBasicSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
		this.add(columnSpinner);
	}
	
	public void populate(ButtonGroup buttonGroup) {
		adaptiveCheckbox.setSelected(buttonGroup.isAdaptive());
		columnSpinner.setVisible(!adaptiveCheckbox.isSelected());
		columnLabel.setVisible(!adaptiveCheckbox.isSelected());
		columnSpinner.setValue(buttonGroup.getColumnsInRow());
	}
	
	public void update(ButtonGroup buttonGroup) {
		columnSpinner.setVisible(!adaptiveCheckbox.isSelected());
		columnLabel.setVisible(!adaptiveCheckbox.isSelected());
		buttonGroup.setAdaptive(adaptiveCheckbox.isSelected());
		buttonGroup.setColumnsInRow((Integer)(columnSpinner.getValue()));
	}
	
}