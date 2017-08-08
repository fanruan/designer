package com.fr.design.widget.ui;


import java.awt.*;
import java.awt.event.ActionEvent;
import com.fr.design.gui.ispinner.UIBasicSpinner;
import java.awt.event.ActionListener;

import com.fr.design.gui.ilable.UILabel;
import javax.swing.JPanel;
import javax.swing.SpinnerNumberModel;

import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.form.ui.ButtonGroup;
import com.fr.general.Inter;


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
		this.setLayout(new FlowLayout(0));
		JPanel pane = new JPanel(new FlowLayout());
		adaptiveCheckbox = new UICheckBox(Inter.getLocText("Adaptive"), true);
		adaptiveCheckbox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				columnSpinner.setVisible(!adaptiveCheckbox.isSelected());
				columnLabel.setVisible(!adaptiveCheckbox.isSelected());
			}
		});

		this.columnLabel = new UILabel(Inter.getLocText("Button-Group-Display-Columns"));
		columnSpinner = new UIBasicSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
        pane.add(adaptiveCheckbox);
        pane.add(columnLabel);
        pane.add(columnSpinner);

		this.add(pane);
	}

	public void populate(ButtonGroup buttonGroup) {
		adaptiveCheckbox.setSelected(buttonGroup.isAdaptive());
		columnSpinner.setVisible(!adaptiveCheckbox.isSelected());
		columnLabel.setVisible(!adaptiveCheckbox.isSelected());
		columnSpinner.setValue(buttonGroup.getColumnsInRow());
	}

	public void update(ButtonGroup buttonGroup) {
		buttonGroup.setAdaptive(adaptiveCheckbox.isSelected());
		buttonGroup.setColumnsInRow((Integer)(columnSpinner.getValue()));
	}

}