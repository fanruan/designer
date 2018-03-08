package com.fr.design.report;


import com.fr.base.BaseUtils;
import com.fr.config.ServerPreferenceConfig;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class WriteShortCutsPane extends JPanel{
	
	private String nextColString = "Tab";
	private String nextRowString = "Enter";
	private UILabel nextColHK;
	private UILabel nextRowHK;
	private UILabel preCol;
	private UILabel preRow;
	
	public WriteShortCutsPane(){
		this.setLayout(null);
		this.add(getFeatureNamePane());
		this.add(getHintsPane());
		
		if(!ServerPreferenceConfig.getInstance().isWriteShortCuts()){
			nextColString = "Enter";
			nextRowString = "Tab";
			switchColRow();
		}
	}
	
	//todo:布局不要用绝对定位
	public JPanel getFeatureNamePane(){
		JPanel panel1 = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
		panel1.setBounds(20, 20, 600, 140);
		panel1.setLayout(null);
		panel1.setBorder(BorderFactory.createTitledBorder(Inter.getLocText("FR-Designer_Shortcut_Set")));
		UILabel name = new UILabel(Inter.getLocText("FR-Designer_Feature_Name"), SwingConstants.CENTER);
		name.setBounds(40, 30, 80, 50);
		UILabel nextCol = new UILabel(Inter.getLocText("FR-Designer_Cursor_to_next_column"), SwingConstants.CENTER);
		nextCol.setBounds(140, 30, 180, 50);
		UILabel nextRow = new UILabel(Inter.getLocText("FR-Designer_Cursor_to_next_row"), SwingConstants.CENTER);
		nextRow.setBounds(390, 30, 180, 50);
		UILabel shortName = new UILabel(Inter.getLocText("FR-Designer_Current_keys"), SwingConstants.CENTER);
		shortName.setBounds(40, 80, 80, 50);
		nextColHK = new UILabel(nextColString, SwingConstants.CENTER);
		nextColHK.setBounds(140, 80, 180, 50);
		UIButton switchbt = new UIButton(BaseUtils.readIcon("com/fr/design/images/buttonicon/switchShortCuts.png"));
		switchbt.addActionListener(getListener());
		switchbt.setToolTipText(Inter.getLocText("FR-Designer_Exchange_key"));
		switchbt.setBounds(337, 90, 36, 29);
		nextRowHK = new UILabel(nextRowString, SwingConstants.CENTER);
		nextRowHK.setBounds(390, 80, 180, 50);
		
		panel1.add(name);
		panel1.add(nextCol);
		panel1.add(nextRow);
		panel1.add(shortName);
		panel1.add(nextColHK);
		panel1.add(switchbt);
		panel1.add(nextRowHK);
		
		return panel1;
	}
	
	public JPanel getHintsPane(){
		JPanel panel2 =FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
		panel2.setBounds(20, 170, 600, 150);
		panel2.setLayout(null);
		panel2.setBorder(BorderFactory.createTitledBorder(Inter.getLocText("FR-Designer_Tooltips")));
		UILabel systemDefault = new UILabel(Inter.getLocText("FR-Designer_System_default"), SwingConstants.CENTER);
		systemDefault.setBounds(38, 30, 84, 50);
		UILabel preColText = new UILabel(Inter.getLocText("FR-Designer_Cursor_to_previous_column"), SwingConstants.CENTER);
		preColText.setBounds(140, 30, 190, 50);
		UILabel preRowText = new UILabel(Inter.getLocText("FR-Designer_Cursor_to_previous_row"), SwingConstants.CENTER);
		preRowText.setBounds(140, 80, 190, 50);
		preCol = new UILabel("Shift+" + nextColString, SwingConstants.CENTER);
		preCol.setBounds(370, 30, 100, 50);
		preRow = new UILabel("Shift+" + nextRowString, SwingConstants.CENTER);
		preRow.setBounds(370, 80, 100, 50);
		
		panel2.add(systemDefault);
		panel2.add(preColText);
		panel2.add(preRowText);
		panel2.add(preCol);
		panel2.add(preRow);
		
		return panel2;
	}
	
	public ActionListener getListener(){
		ActionListener actionListener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String temp= nextColString;
				nextColString = nextRowString;
				nextRowString = temp;
				switchColRow();

				ServerPreferenceConfig.getInstance().setWriteShortCuts(ComparatorUtils.equals(nextColString, "Tab"));
			}
		};
		
		return actionListener;
	}
	
	private void switchColRow(){
		nextColHK.setText(nextColString);
		nextRowHK.setText(nextRowString);
		preCol.setText("Shift+" + nextColString);
		preRow.setText("Shift+" + nextRowString);
	}

}