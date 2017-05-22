package com.fr.design.mainframe.alphafine.cell;

import com.fr.design.mainframe.alphafine.CellType;
import com.fr.design.mainframe.alphafine.cell.cellModel.*;
import com.fr.design.mainframe.alphafine.searchManager.ActionSearchManager;
import com.fr.design.mainframe.alphafine.searchManager.DocumentSearchManager;
import com.fr.design.mainframe.alphafine.searchManager.FileSearchManager;
import com.fr.design.mainframe.alphafine.searchManager.PluginSearchManager;
import com.fr.json.JSONObject;

/**
 * Created by XiaXiang on 2017/5/17.
 */
public class CellModelHelper {
    public static AlphaCellModel jsonToModel(JSONObject object) {
        int cellType = object.optInt("cellType");
        switch (CellType.parse(cellType)) {
            case ACTION:
                return ActionSearchManager.getModelFromCloud(object.optString("result"));
            case DOCUMENT:
                return DocumentSearchManager.getModelFromCloud(object.optJSONObject("result"));
            case FILE:
                return FileSearchManager.getModelFromCloud(object.optString("result"));
            case PLUGIN:
            case REUSE:
                return PluginSearchManager.getModelFromCloud(object.optJSONObject("result"));

        }
        return null;
    }

    public static String getResultValueFromModel(AlphaCellModel cellModel) {
        switch (cellModel.getType()) {
            case ACTION:
                return ((ActionModel)cellModel).getActionName();
            case DOCUMENT:
                return ((DocumentModel)cellModel).getInformationUrl();
            case FILE:
                return ((FileModel)cellModel).getFilePath();
            case REUSE:
            case PLUGIN:
                return ((PluginModel)cellModel).getInformationUrl();
        }
        return null;
    }
}
