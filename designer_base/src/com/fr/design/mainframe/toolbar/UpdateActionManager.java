package com.fr.design.mainframe.toolbar;

import com.fr.design.actions.UpdateAction;
import com.fr.general.ComparatorUtils;

import java.util.List;

/**
 * Created by XiaXiang on 2017/4/13.
 */
public class UpdateActionManager {
    private static UpdateActionManager updateActionManager = null;
    private List<UpdateActionModel> updateActions;

    public synchronized static UpdateActionManager getUpdateActionManager() {
        if (updateActionManager == null) {
            updateActionManager = new UpdateActionManager();
        }
        return updateActionManager;
    }

    public List<UpdateActionModel> getUpdateActions() {
        return updateActions;
    }

    public void setUpdateActions(List<UpdateActionModel> updateActions) {
        this.updateActions = updateActions;
    }

    /**
     * 根据action name获取action对象
     *
     * @param name
     * @return
     */
    public UpdateAction getActionByName(String name) {
        for (UpdateActionModel action : updateActions) {
            if (ComparatorUtils.equals(name, action.getActionName()) && action.getAction().isEnabled()) {
                return action.getAction();
            }
        }
        return null;
    }
}