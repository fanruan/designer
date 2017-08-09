package com.fr.design.widget.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.fr.design.gui.ibutton.UIHeadGroup;
import com.fr.design.gui.ilable.UILabel;
import javax.swing.JPanel;

import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.icombobox.DictionaryComboBox;
import com.fr.design.gui.icombobox.DictionaryConstants;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.form.ui.CheckBoxGroup;
import com.fr.form.ui.ComboCheckBox;
import com.fr.general.Inter;

public class CheckBoxDictPane extends JPanel {
	
	private DictionaryComboBox delimiterComboBox;
	private UIHeadGroup returnTypeComboBox;
	private DictionaryComboBox startComboBox;
	private DictionaryComboBox endComboBox;
	
	private JPanel delimiterPane;
	private JPanel startPane;
	private JPanel endPane;
	
	public CheckBoxDictPane() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		JPanel returnTypePane = FRGUIPaneFactory.createBorderLayout_S_Pane();
		delimiterPane =FRGUIPaneFactory.createBorderLayout_S_Pane();
		UILabel label = new UILabel(Inter.getLocText("Form-Delimiter") + ":");
		delimiterPane.add(label, BorderLayout.WEST);
		delimiterPane.add(delimiterComboBox = new DictionaryComboBox(DictionaryConstants.delimiters, DictionaryConstants.delimiterDisplays), BorderLayout.CENTER);
		delimiterComboBox.setEditable(true);

		startPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
		startPane.add(new UILabel(Inter.getLocText("ComboCheckBox-Start_Symbol") + ":"), BorderLayout.WEST);
		startPane.add(startComboBox = new DictionaryComboBox(DictionaryConstants.symbols, DictionaryConstants.symbolDisplays), BorderLayout.CENTER);
		startComboBox.setEditable(true);

		endPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
		endPane.add(new UILabel(Inter.getLocText("ComboCheckBox-End_Symbol") + ":"), BorderLayout.WEST);
		endPane.add(endComboBox = new DictionaryComboBox(DictionaryConstants.symbols, DictionaryConstants.symbolDisplays), BorderLayout.CENTER);
		endComboBox.setEditable(true);
		returnTypeComboBox = new UIHeadGroup(new String[]{Inter.getLocText("Widget-Array"), Inter.getLocText("String")}){
			protected void tabChanged(int newSelectedIndex) {
//				checkVisible(newSelectedIndex);
				//todo
			}
		};
		returnTypePane.add(returnTypeComboBox, BorderLayout.CENTER);
		JPanel jPanel = TableLayoutHelper.createGapTableLayoutPane(new Component[][]{new Component[]{new UILabel(Inter.getLocText("Widget-Date_Selector_Return_Type") + ":"), returnTypePane}}, TableLayoutHelper.FILL_LASTCOLUMN, 18, 7);
		this.add(jPanel);
	}
	
	private void checkVisible(int selectIndex){
		delimiterPane.setVisible(selectIndex == 1);
		startPane.setVisible(selectIndex == 1);
		endPane.setVisible(selectIndex == 1);
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