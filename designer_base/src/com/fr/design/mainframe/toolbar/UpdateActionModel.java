package com.fr.design.mainframe.toolbar;

import com.fr.design.actions.UpdateAction;

/**
 * Created by XiaXiang on 2017/5/24.
 */

/**
 * action对象
 */
public class UpdateActionModel {
    private String parentName;
    private String actionName;
    private UpdateAction action;

    public UpdateActionModel(String parentName, UpdateAction action) {
        this.parentName = parentName;
        this.action = action;
        this.actionName = action.getName();
    }

    /**
     * 获取上一层级菜单name
     * @return
     */
    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    /**
     * 获取action
     * @return
     */
    public UpdateAction getAction() {
        return action;
    }

    public void setAction(UpdateAction action) {
        this.action = action;
    }

    /**
     * 获取actionName
     * @return
     */
    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }
}
