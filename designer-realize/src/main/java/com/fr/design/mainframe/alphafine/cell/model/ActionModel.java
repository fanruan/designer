package com.fr.design.mainframe.alphafine.cell.model;

import com.fr.design.actions.UpdateAction;
import com.fr.design.mainframe.alphafine.CellType;
import com.fr.log.FineLoggerFactory;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;

/**
 * Created by XiaXiang on 2017/4/20.
 */
public class ActionModel extends AlphaCellModel {
    private UpdateAction action;


    public ActionModel(String name, String description, UpdateAction action, int searchCount) {
        this(name, description, action);
        setSearchCount(searchCount);
    }

    public ActionModel(String name, String description, UpdateAction action) {
        super(name, null, CellType.ACTION);
        this.action = action;
        this.setDescription(description);
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

    public UpdateAction getAction() {
        return action;
    }

    public void setAction(UpdateAction action) {
        this.action = action;
    }

    @Override
    public JSONObject modelToJson() {
        JSONObject object = JSONObject.create();
        JSONObject modelObject = JSONObject.create();
        modelObject.put("className", getAction().getClass().getName()).put("searchCount", getSearchCount());
        object.put("result", modelObject).put("cellType", getType().getTypeValue());
        return object;
    }

    @Override
    public String getStoreInformation() {
        return getClassName();
    }

    @Override
    public void doAction() {
        getAction().actionPerformed(null);
    }

    public String getClassName() {
        return getAction().getClass().getName();
    }

}
