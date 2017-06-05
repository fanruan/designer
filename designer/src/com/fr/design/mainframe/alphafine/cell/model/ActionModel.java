package com.fr.design.mainframe.alphafine.cell.model;

import com.fr.design.mainframe.alphafine.CellType;
import com.fr.general.FRLogger;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;

import javax.swing.*;

/**
 * Created by XiaXiang on 2017/4/20.
 */
public class ActionModel extends AlphaCellModel {
    private Action action;

    private String className;

    public ActionModel(String name, String content, CellType type) {
        super(name, content, type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ActionModel)) {
            return false;
        }
        ActionModel that = (ActionModel) o;

        return action != null ? action.equals(that.action) : that.action == null;
    }

    @Override
    public int hashCode() {
        return action != null ? action.hashCode() : 0;
    }

    public ActionModel(String name, String description, Action action) {
        super(name, null, CellType.ACTION);
        this.action = action;
        this.setDescription(description);
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
            object.put("result", getAction().getClass().getName()).put("cellType", getType().getTypeValue());
        } catch (JSONException e) {
            FRLogger.getLogger().error(e.getMessage());
        }
        return object;
    }

    @Override
    public String getStoreInformation() {
        return getClassName();
    }

    public String getClassName() {
        return getAction().getClass().getName();
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
