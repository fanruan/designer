package com.fr.design.widget.ui;


import java.awt.*;
import java.awt.event.ActionEvent;

import com.fr.data.Dictionary;
import com.fr.design.designer.IntervalConstants;
import com.fr.design.gui.ispinner.UIBasicSpinner;
import java.awt.event.ActionListener;

import com.fr.design.gui.ilable.UILabel;

import javax.swing.*;

import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.widget.accessibles.AccessibleDictionaryEditor;
import com.fr.design.widget.FRWidgetFactory;
import com.fr.form.ui.ButtonGroup;



public class ButtonGroupDictPane extends JPanel {
	private UIBasicSpinner columnSpinner;
	private UICheckBox adaptiveCheckbox;
	private UILabel columnLabel;
	private AccessibleDictionaryEditor dictPane;


	public ButtonGroupDictPane() {
		this.initComponents();
	}

    /**
    *
     */
	public void initComponents() {
		dictPane = new AccessibleDictionaryEditor();
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		adaptiveCheckbox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Adaptive"), true);
		adaptiveCheckbox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		adaptiveCheckbox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				columnSpinner.setVisible(!adaptiveCheckbox.isSelected());
				columnLabel.setVisible(!adaptiveCheckbox.isSelected());
			}
		});
		UILabel dictLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_DS_Dictionary"));
		this.columnLabel = FRWidgetFactory.createLineWrapLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Button_Group_Display_Columns") + ":", dictLabel.getPreferredSize().width);
		columnSpinner = new UIBasicSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));

		double f = TableLayout.FILL;
		double p = TableLayout.PREFERRED;
		double[] rowSize = {p, p, p};
		double[] columnSize = {p, f};
		int[][] rowCount = {{1, 1}, {1, 1}, {1, 1}};
		Component[][] components = {
				new Component[] {dictLabel, dictPane},
				new Component[] {adaptiveCheckbox, null},
				new Component[] {columnLabel, columnSpinner}
		};
		JPanel panel = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, IntervalConstants.INTERVAL_W1, IntervalConstants.INTERVAL_L1);
		panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
		this.add(panel);


	}
	
	public void populate(ButtonGroup buttonGroup) {
		dictPane.setValue(buttonGroup.getDictionary());
		adaptiveCheckbox.setSelected(buttonGroup.isAdaptive());
		columnSpinner.setVisible(!adaptiveCheckbox.isSelected());
		columnLabel.setVisible(!adaptiveCheckbox.isSelected());
		columnSpinner.setValue(buttonGroup.getColumnsInRow());
	}
	
	public void update(ButtonGroup buttonGroup) {
		buttonGroup.setDictionary((Dictionary) this.dictPane.getValue());
		buttonGroup.setAdaptive(adaptiveCheckbox.isSelected());
		buttonGroup.setColumnsInRow((Integer)(columnSpinner.getValue()));
	}
	
}
