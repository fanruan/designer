package com.fr.design.mainframe.toolbar;

import com.fr.design.actions.UpdateAction;

import java.util.List;

/**
 * Created by XiaXiang on 2017/4/13.
 */
public class UpdateActionManager {
    private static UpdateActionManager updateActionManager = null;
    private List<UpdateAction> updateActions;

    public synchronized static UpdateActionManager getUpdateActionManager() {
        if (updateActionManager == null) {
            updateActionManager = new UpdateActionManager();
        }
        return updateActionManager;
    }

    public List<UpdateAction> getUpdateActions() {
        return updateActions;
    }

    public void setUpdateActions(List<UpdateAction> updateActions) {
        this.updateActions = updateActions;
    }
}
