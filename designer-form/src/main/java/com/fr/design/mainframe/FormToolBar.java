package com.fr.design.mainframe;

import com.fr.form.ui.ToolBar;
import com.fr.form.ui.Widget;
import com.fr.general.Background;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by harry on 2017-3-2.
 */
public class FormToolBar {
    private List<FormToolBarButton> buttonlist = new ArrayList<FormToolBarButton>();
    private Background background = null;
    private boolean isDefault = true;

    public List<FormToolBarButton> getButtonlist() {
        return buttonlist;
    }

    public void setButtonlist(List<FormToolBarButton> buttonlist) {
        if (buttonlist == null) {
            this.buttonlist = new ArrayList<FormToolBarButton>();
        } else {
            this.buttonlist = buttonlist;
        }
    }

    public void addButton(FormToolBarButton toolBarButton) {
        this.buttonlist.add(toolBarButton);
    }

    public void removeButton(FormToolBarButton toolBarButton) {
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
        Widget[] widgets = new Widget[this.getButtonlist().size()];
        for (int j = 0; j < this.getButtonlist().size(); j++) {
            widgets[j] = this.getButtonlist().get(j).getWidget();
        }
        ToolBar toolBar = new ToolBar(widgets);
        toolBar.setBackground(this.background);
        toolBar.setDefault(this.isDefault);
        return toolBar;
    }
}
