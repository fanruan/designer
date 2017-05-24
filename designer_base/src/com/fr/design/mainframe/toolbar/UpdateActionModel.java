package com.fr.design.mainframe.toolbar;

import com.fr.design.actions.UpdateAction;

/**
 * Created by XiaXiang on 2017/5/24.
 */
public class UpdateActionModel {
    private String parentName;
    private UpdateAction action;

    public UpdateActionModel(String parentName, UpdateAction action) {
        this.parentName = parentName;
        this.action = action;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public UpdateAction getAction() {
        return action;
    }

    public void setAction(UpdateAction action) {
        this.action = action;
    }
}
