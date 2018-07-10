package com.fr.design.widget.ui.designer;


import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UIBasicSpinner;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.form.ui.ButtonGroup;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;


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



		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		adaptiveCheckbox = new UICheckBox(Inter.getLocText("Adaptive"), true);
		adaptiveCheckbox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		this.columnLabel = new UILabel(Inter.getLocText("Button-Group-Display-Columns") + ":");
		columnSpinner = new UIBasicSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));

		JPanel jPanel = TableLayoutHelper.createGapTableLayoutPane(new Component[][]{new Component[]{adaptiveCheckbox, columnLabel, columnSpinner}}, TableLayoutHelper.FILL_LASTCOLUMN, 18, 7);

		this.add(jPanel);
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