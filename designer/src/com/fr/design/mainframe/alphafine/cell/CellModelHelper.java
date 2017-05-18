package com.fr.design.mainframe.alphafine.cell;

import com.fr.design.mainframe.alphafine.CellType;
import com.fr.design.mainframe.alphafine.cell.cellModel.*;
import com.fr.json.JSONObject;

/**
 * Created by XiaXiang on 2017/5/17.
 */
public class CellModelHelper {
    public static AlphaCellModel jsonToModel(JSONObject object) {
        int cellType = object.optInt("cellType");
        switch (CellType.parse(cellType)) {
            case ACTION:
                return ActionModel.jsonToModel(object);
            case DOCUMENT:
                return DocumentModel.jsonToModel(object);
            case FILE:
                return FileModel.jsonToModel(object);
            case PLUGIN:
            case REUSE:
                return PluginModel.jsonToModel(object);

        }
        return null;
    }

    public static String getResultValueFromModel(AlphaCellModel cellModel) {
        switch (cellModel.getType()) {
            case ACTION:
                return cellModel.getName();
            case DOCUMENT:
                return ((DocumentModel)cellModel).getDocumentUrl();
            case FILE:
                return ((FileModel)cellModel).getFilePath();
            case REUSE:
            case PLUGIN:
                return ((PluginModel)cellModel).getPluginUrl();
        }
        return null;
    }
}
