package com.fr.design.report;


import java.awt.Color;
import java.awt.Dimension;

import javax.swing.*;

import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.MultilineLabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.dialog.BasicPane;

import com.fr.io.attr.WordExportAttr;

public class WordExportPane extends BasicPane {
	private UICheckBox isExportAsTable;
	private JPanel southPane;

	public WordExportPane() {
		this.initComponents();
	}

	protected void initComponents() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		JPanel outnorthPane =FRGUIPaneFactory.createTitledBorderPane("Word" + com.fr.design.i18n.Toolkit.i18nText("ReportD-Excel_Export"));
		this.add(outnorthPane);
		
		JPanel northPane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_M_Pane();
		outnorthPane.add(northPane);
		
		JPanel checkBoxPane =FRGUIPaneFactory.createNormalFlowInnerContainer_M_Pane();
		isExportAsTable = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("is_need_word_adjust"), false);
		checkBoxPane.add(isExportAsTable);
		
		southPane = FRGUIPaneFactory.createNormalFlowInnerContainer_M_Pane();
		JPanel innerAlertBorderPane = FRGUIPaneFactory.createTitledBorderPane(com.fr.design.i18n.Toolkit.i18nText("Attention"));
		JPanel alertPane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_M_Pane();
		
        MultilineLabel wordLineLabel = new MultilineLabel();
        wordLineLabel.setPreferredSize(new Dimension(250, 100));
        wordLineLabel.setText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Alert_Word"));
        wordLineLabel.setForeground(Color.RED);
		alertPane.add(wordLineLabel);
		
		southPane.add(innerAlertBorderPane);
		innerAlertBorderPane.add(alertPane);
		
		northPane.add(checkBoxPane);
		northPane.add(southPane);
	}
	
	@Override
	protected String title4PopupWindow() {
		return "WordExport";
	}

	public void populate(WordExportAttr wordExportAttr) {
		if(wordExportAttr == null){
			return;
		}
		
		if(wordExportAttr.isExportAsTable()){
			isExportAsTable.setSelected(true);
//			southPane.setVisible(true);
		}
	}

	public WordExportAttr update() {
		WordExportAttr wordExportAttr = new WordExportAttr();
		wordExportAttr.setExportAsTable(isExportAsTable.isSelected());
		return wordExportAttr;
	}
}