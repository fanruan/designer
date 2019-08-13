package com.fr.design.widget.ui.designer;


import com.fr.design.designer.IntervalConstants;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UIBasicSpinner;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.widget.FRWidgetFactory;
import com.fr.form.ui.ButtonGroup;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SpinnerNumberModel;
import java.awt.Component;


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
		adaptiveCheckbox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Adaptive"), true);
		adaptiveCheckbox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		UILabel dictLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_DS_Dictionary"));
		this.columnLabel = FRWidgetFactory.createLineWrapLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Button_Group_Display_Columns") + ":", dictLabel.getPreferredSize().width);
		columnSpinner = new UIBasicSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
		double f = TableLayout.FILL;
		double p = TableLayout.PREFERRED;
		double[] rowSize = {p, p};
		double[] columnSize = {p, f};
		int[][] rowCount = {{1, 1}, {1, 1}};
		Component[][] components = {
				new Component[] {adaptiveCheckbox, null},
				new Component[] {columnLabel, columnSpinner},
		};
		JPanel jPanel = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, IntervalConstants.INTERVAL_W1, IntervalConstants.INTERVAL_L1);
		jPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

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