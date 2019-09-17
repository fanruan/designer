package com.fr.design.data.tabledata.tabledatapane;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.fr.design.constants.UIConstants;
import com.fr.design.gui.ilable.UILabel;

import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.gui.itoolbar.UIToolbar;


public class MaxMemRowCountPanel extends UIToolbar {
	private static final int ALL_IN_MEMORY = 0;
	private static final int MAX_IN_MEMORY = 1;

	private UISpinner numberSpinner;
	private UIComboBox switchCache;

	private boolean showAll; // alex:不知道怎么通过界面判断当前的状态,只好加一个boolean量了

	private ActionListener switchStateL = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			int index = switchCache.getSelectedIndex();
			switch (index) {
			case 0:
				showAllPanel();
				break;

			case 1:
				showMaxPanel();
				break;

			default:
				showAllPanel();
				break;
			}
		}
	};

	@Override
	public Dimension getPreferredSize() {
		Dimension dim = super.getPreferredSize();
		dim.width = 340;
		return dim;
	};

	public MaxMemRowCountPanel() {
		this.setFloatable(false);
		this.setRollover(true);
		this.setBackground(UIConstants.NORMAL_BACKGROUND);
		String[] cacheList = { com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Save_All_Records_In_Memory"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Max_Mem_Row_Count") };
		switchCache = new UIComboBox(cacheList);
		switchCache.addActionListener(switchStateL);
		numberSpinner = new UISpinner(0, Integer.MAX_VALUE, 1);
	}

	private void showAllPanel() {
		showAll = true;
		this.removeAll();
		this.add(switchCache);
		switchCache.setSelectedIndex(ALL_IN_MEMORY);
		this.validate();
		this.repaint();
	}

	private void showMaxPanel() {
		showAll = false;
		this.removeAll();
		this.add(switchCache);
		switchCache.setSelectedIndex(MAX_IN_MEMORY);
		this.add(new UILabel(" "));
		this.add(numberSpinner);
		this.add(new UILabel(" " + com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Row")));
		this.validate();
		this.repaint();
	}

	public void setValue(int value) {
		if (value >= 0) {
			showMaxPanel();
			numberSpinner.setValue(value);
		} else {
			showAllPanel();
		}
	}

	public int getValue() {
		if (showAll) {
			return -1;
		} else {
			return ((Number) numberSpinner.getValue()).intValue();
		}
	}
}
