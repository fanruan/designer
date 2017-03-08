package com.fr.design.mainframe;

import com.fr.form.ui.ToolBar;
import com.fr.form.ui.Widget;
import com.fr.general.Background;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by harry on 2017-3-2.
 */
public class FToolBar {
    private List<FToolBarButton> buttonlist = new ArrayList<FToolBarButton>();
    private Background background = null;
    private boolean isDefault = true;

    public List<FToolBarButton> getButtonlist() {
        return buttonlist;
    }

    public void setButtonlist(List<FToolBarButton> buttonlist) {
        if (buttonlist == null || buttonlist.size() < 0) {
            this.buttonlist = new ArrayList<FToolBarButton>();
        } else {
            this.buttonlist = buttonlist;
        }
    }

    public void addButton(FToolBarButton toolBarButton) {
        this.buttonlist.add(toolBarButton);
    }

    public void removeButton(FToolBarButton toolBarButton) {
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
