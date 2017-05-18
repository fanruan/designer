package com.fr.design.mainframe.alphafine.cell.cellModel;

import com.fr.design.mainframe.alphafine.CellType;
import com.fr.general.FRLogger;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;

import javax.swing.*;
import java.io.Serializable;

/**
 * Created by XiaXiang on 2017/4/20.
 */
public class ActionModel extends AlphaCellModel implements Serializable {
    private Action action;

    public ActionModel(String name, String content, CellType type) {
        super(name, content, type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ActionModel)) return false;

        ActionModel that = (ActionModel) o;

        return action != null ? action.equals(that.action) : that.action == null;
    }

    @Override
    public int hashCode() {
        return action != null ? action.hashCode() : 0;
    }

    public ActionModel(String name, Action action) {
        super(name, null, CellType.ACTION);
        this.action = action;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    @Override
    public JSONObject ModelToJson() {
        JSONObject object = JSONObject.create();
        try {
            object.put("name", getName()).put("action", getAction().getClass().getName()).put("cellType", getType().getCellType());
        } catch (JSONException e) {
            FRLogger.getLogger().error(e.getMessage());
        }
        return object;
    }

    public static ActionModel jsonToModel(JSONObject object ) {
        String name = object.optString("name");
        String actionName = object.optString("action");
        Action action = null;
        try {
            Class<?> className =  Class.forName(actionName);
            action = (Action) className.newInstance();
        } catch (ClassNotFoundException e) {
            FRLogger.getLogger().error(e.getMessage());
        } catch (IllegalAccessException e) {
            FRLogger.getLogger().error(e.getMessage());
        } catch (InstantiationException e) {
            FRLogger.getLogger().error(e.getMessage());
        }
        return new ActionModel(name, action);
    }
}
