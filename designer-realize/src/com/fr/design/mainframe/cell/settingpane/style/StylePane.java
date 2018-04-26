package com.fr.design.mainframe.cell.settingpane.style;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.ChangeListener;

import com.fr.base.Style;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.gui.frpane.UIComboBoxPane;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.design.mainframe.ElementCasePane;

public class StylePane extends UIComboBoxPane<Style> {
	private CustomStylePane customStylePane;
	private PredefinedStylePane predefinedStylePane;

	@Override
	protected String title4PopupWindow() {
		return Inter.getLocText("Style");
	}

	@Override
	protected void comboBoxItemStateChanged() {
		if (jcb.getSelectedIndex() == 0 && predefinedStylePane.updateBean() != null) {
			customStylePane.populateBean(predefinedStylePane.updateBean());
		} else {
			predefinedStylePane.populateBean(null);
		}
	}

	public void addPredefinedChangeListener(ChangeListener changeListener) {
		predefinedStylePane.addChangeListener(changeListener);
	}

	public void addCustomTabChangeListener(ChangeListener changeListener) {
		customStylePane.addTabChangeListener(changeListener);
	}


	public void updateBorder() {
		if (getSelectedIndex() == 0 && customStylePane.isBorderPaneSelected()) {
			customStylePane.updateBorder();
		}
	}

	public void dealWithBorder(ElementCasePane ePane) {
		if (getSelectedIndex() == 0) {
			customStylePane.dealWithBorder(ePane);
		}
	}

	public void setSelctedByName(String id) {
		jcb.setSelectedIndex(ComparatorUtils.equals(Inter.getLocText("Custom"),id)? 0 : 1);
	}

	public Style updateStyle(Style style) {
		return customStylePane.updateStyle(style);
	}

	@Override
	protected List<FurtherBasicBeanPane<? extends Style>> initPaneList() {
		List<FurtherBasicBeanPane<? extends Style>> paneList = new ArrayList<FurtherBasicBeanPane<? extends Style>>();
		paneList.add(customStylePane = new CustomStylePane());
		paneList.add(predefinedStylePane = new PredefinedStylePane());
		return paneList;
	}

}