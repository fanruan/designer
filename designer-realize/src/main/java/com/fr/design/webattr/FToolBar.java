package com.fr.design.webattr;

import java.util.ArrayList;
import java.util.List;

import com.fr.form.ui.ToolBar;
import com.fr.form.ui.Widget;
import com.fr.general.Background;

public class FToolBar {
	private List<ToolBarButton> buttonlist = new ArrayList<ToolBarButton>();
	private Background background = null;
	private boolean isDefault = true;

	public List<ToolBarButton> getButtonlist() {
		return buttonlist;
	}

	public void setButtonlist(List<ToolBarButton> buttonlist) {
		if (buttonlist == null ) {
			this.buttonlist = new ArrayList<ToolBarButton>();
		} else {
			this.buttonlist = buttonlist;
		}
	}

	public void addButton(ToolBarButton toolBarButton) {
		this.buttonlist.add(toolBarButton);
	}

	public void removeButton(ToolBarButton toolBarButton) {
		this.buttonlist.remove(toolBarButton);
	}

	public void clearButton() {
		this.buttonlist.clear();
	}

	public Background getBackground() {
		return background;
	}

	public void setBackground(Background background) {
		this.background = background;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public ToolBar getToolBar() {
		Widget[] n = new Widget[this.getButtonlist().size()];
		for (int j = 0; j < this.getButtonlist().size(); j++) {
			n[j] = this.getButtonlist().get(j).getWidget();
		}
		ToolBar toolBar = new ToolBar(n);
		toolBar.setBackground(this.background);
		toolBar.setDefault(this.isDefault);
		return toolBar;
	}
}