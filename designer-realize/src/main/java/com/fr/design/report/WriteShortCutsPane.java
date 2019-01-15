package com.fr.design.report;


import com.fr.base.BaseUtils;
import com.fr.config.ServerPreferenceConfig;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.UIComponentUtils;
import com.fr.general.ComparatorUtils;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class WriteShortCutsPane extends JPanel{
	private static final int V_GAP = 20;
	private static final int MAX_LABEL_WIDTH = 100;

	private String nextColString = "Tab";
	private String nextRowString = "Enter";
	private UILabel nextColHK;
	private UILabel nextRowHK;
	private UILabel preCol;
	private UILabel preRow;
	
	public WriteShortCutsPane(){
		this.setLayout(new BorderLayout());
		this.add(createContentPane(), BorderLayout.NORTH);
		
		if(!ServerPreferenceConfig.getInstance().isWriteShortCuts()){
			nextColString = "Enter";
			nextRowString = "Tab";
			switchColRow();
		}
	}

	private JPanel createContentPane() {
		JPanel contentPane = new JPanel();
		contentPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

		// 纵向布局，横向自适应
		contentPane.setLayout(new GridBagLayout());
		GridBagConstraints cons = new GridBagConstraints();
		cons.fill = GridBagConstraints.HORIZONTAL;
		cons.weightx = 1;
		cons.gridx = 0;
		cons.insets = new Insets(20, 0, 0, 0);

		contentPane.add(getFeatureNamePane(), cons);
		contentPane.add(getHintsPane(), cons);

		return contentPane;
	}
	
	private JPanel getFeatureNamePane(){
		JPanel featureNamePane = FRGUIPaneFactory.createTitledBorderPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Shortcut_Set"));
		featureNamePane.setLayout(new BorderLayout());

		UILabel name = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Feature_Name"), SwingConstants.CENTER);
		UILabel nextCol = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Cursor_To_Next_Column"), SwingConstants.CENTER);
		UILabel nextRow = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Cursor_To_Next_Row"), SwingConstants.CENTER);
		UILabel shortName = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Current_Keys"), SwingConstants.CENTER);
		UIComponentUtils.setLineWrap(shortName, MAX_LABEL_WIDTH);
		nextColHK = new UILabel(nextColString, SwingConstants.CENTER);
		JPanel switchBtnPane = getSwitchBtnPane();
		nextRowHK = new UILabel(nextRowString, SwingConstants.CENTER);

		JPanel centerPane = new JPanel(new GridLayout(2, 4, 0, 0));
		centerPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
		centerPane.add(name);
		centerPane.add(nextCol);
		centerPane.add(new JPanel());
		centerPane.add(nextRow);
		centerPane.add(shortName);
		centerPane.add(nextColHK);
		centerPane.add(switchBtnPane);
		centerPane.add(nextRowHK);

		featureNamePane.add(centerPane, BorderLayout.CENTER);
		
		return featureNamePane;
	}

	private JPanel getSwitchBtnPane() {
		UIButton switchbt = new UIButton(BaseUtils.readIcon("com/fr/design/images/buttonicon/switchShortCuts.png")) {
			public Dimension getPreferredSize() {
				return new Dimension(40, 30);
			}
		};
		switchbt.addActionListener(getListener());
		switchbt.setToolTipText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Exchange_Key"));
		JPanel switchBtnPane = FRGUIPaneFactory.createCenterFlowInnerContainer_S_Pane();
		switchBtnPane.add(switchbt);
		return switchBtnPane;
	}

	private JPanel getHintsPane(){
		JPanel hintsPane = FRGUIPaneFactory.createTitledBorderPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Tool_Tips"));
		hintsPane.setLayout(new BorderLayout());

		UILabel systemDefault = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_System_Default"), SwingConstants.CENTER);
		UILabel preColText = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Cursor_To_Previous_Column"), SwingConstants.CENTER);
		UILabel preRowText = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Cursor_To_Previous_Row"), SwingConstants.CENTER);
		preCol = new UILabel("Shift+" + nextColString, SwingConstants.CENTER);
		preRow = new UILabel("Shift+" + nextRowString, SwingConstants.CENTER);

		JPanel centerPane = new JPanel(new GridLayout(2, 3, 0, V_GAP));
		centerPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 100));

		centerPane.add(systemDefault);
		centerPane.add(preColText);
		centerPane.add(preCol);

		centerPane.add(new JPanel());
		centerPane.add(preRowText);
		centerPane.add(preRow);

		hintsPane.add(centerPane, BorderLayout.CENTER);
		
		return hintsPane;
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
