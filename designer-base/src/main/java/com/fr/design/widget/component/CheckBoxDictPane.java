package com.fr.design.widget.component;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.fr.design.designer.IntervalConstants;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ilable.UILabel;

import javax.swing.*;

import com.fr.design.gui.icombobox.DictionaryComboBox;
import com.fr.design.gui.icombobox.DictionaryConstants;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.form.ui.CheckBoxGroup;
import com.fr.form.ui.ComboCheckBox;
import com.fr.general.Inter;

public class CheckBoxDictPane extends JPanel {
	
	private DictionaryComboBox delimiterComboBox;
	private UIButtonGroup returnTypeComboBox;
	private DictionaryComboBox startComboBox;
	private DictionaryComboBox endComboBox;
	private JPanel returnStringPane;
	
	public CheckBoxDictPane() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		delimiterComboBox = new DictionaryComboBox(DictionaryConstants.delimiters, DictionaryConstants.delimiterDisplays);
		delimiterComboBox.setEditable(true);
		startComboBox = new DictionaryComboBox(DictionaryConstants.symbols, DictionaryConstants.symbolDisplays);
		startComboBox.setEditable(true);
		endComboBox = new DictionaryComboBox(DictionaryConstants.symbols, DictionaryConstants.symbolDisplays);
		endComboBox.setEditable(true);
		Component[][] components = new Component[][]{
				new Component[]{new UILabel(Inter.getLocText("Form-Delimiter")), delimiterComboBox},
				new Component[]{new UILabel(Inter.getLocText("ComboCheckBox-Start_Symbol")),startComboBox},
				new Component[]{new UILabel(Inter.getLocText("ComboCheckBox-End_Symbol")),endComboBox}
		};
		returnStringPane = TableLayoutHelper.createGapTableLayoutPane(components, TableLayoutHelper.FILL_LASTCOLUMN, IntervalConstants.INTERVAL_W2, IntervalConstants.INTERVAL_L1);

		returnTypeComboBox = new UIButtonGroup(new String[]{Inter.getLocText("Widget-Array"), Inter.getLocText("String")});
		returnTypeComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				checkVisible(returnTypeComboBox.getSelectedIndex());
			}
		});
		JPanel headPane = TableLayoutHelper.createGapTableLayoutPane(
				new Component[][]{new Component[]{new UILabel(Inter.getLocText("Widget-Date_Selector_Return_Type")), returnTypeComboBox}}, TableLayoutHelper.FILL_LASTCOLUMN, IntervalConstants.INTERVAL_L2, IntervalConstants.INTERVAL_L1);
		JPanel jPanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
		jPanel.add(headPane, BorderLayout.NORTH);
		jPanel.add(returnStringPane, BorderLayout.CENTER);
		returnStringPane.setBorder(BorderFactory.createEmptyBorder(IntervalConstants.INTERVAL_L1, IntervalConstants.INTERVAL_L5, 0, 0));
		this.add(jPanel);
	}

	public void checkVisible(int selectIndex){
		returnStringPane.setVisible(selectIndex == 1);
	}

	public void populate(ComboCheckBox comboCheckBox) {
		this.delimiterComboBox.setSelectedItem(comboCheckBox.getDelimiter());
		this.returnTypeComboBox.setSelectedIndex(comboCheckBox.isReturnString() ? 1 : 0);
		this.startComboBox.setSelectedItem(comboCheckBox.getStartSymbol());
		this.endComboBox.setSelectedItem(comboCheckBox.getEndSymbol());
		checkVisible(this.returnTypeComboBox.getSelectedIndex());
	}
	public void update(ComboCheckBox comboCheckBox) {
		comboCheckBox.setDelimiter((String)this.delimiterComboBox.getSelectedItem());
		comboCheckBox.setReturnString(this.returnTypeComboBox.getSelectedIndex() != 0);
		comboCheckBox.setStartSymbol((String)this.startComboBox.getSelectedItem());
		comboCheckBox.setEndSymbol((String)this.endComboBox.getSelectedItem());
	}
	public void populate(CheckBoxGroup checkBoxGroup) {
		this.delimiterComboBox.setSelectedItem(checkBoxGroup.getDelimiter());
		this.returnTypeComboBox.setSelectedIndex(checkBoxGroup.isReturnString() ? 1 : 0);
		this.startComboBox.setSelectedItem(checkBoxGroup.getStartSymbol());
		this.endComboBox.setSelectedItem(checkBoxGroup.getEndSymbol());
		checkVisible(this.returnTypeComboBox.getSelectedIndex());
	}
	public void update(CheckBoxGroup checkBoxGroup) {
		checkBoxGroup.setDelimiter((String)this.delimiterComboBox.getSelectedItem());
		checkBoxGroup.setReturnString(this.returnTypeComboBox.getSelectedIndex() != 0);
		checkBoxGroup.setStartSymbol((String)this.startComboBox.getSelectedItem());
		checkBoxGroup.setEndSymbol((String)this.endComboBox.getSelectedItem());
	}
}