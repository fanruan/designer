package com.fr.design.mainframe.alphafine.cell.cellModel;

import com.fr.design.mainframe.alphafine.AlphaFineHelper;
import com.fr.design.mainframe.alphafine.CellType;
import com.fr.general.FRLogger;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;

/**
 * Created by XiaXiang on 2017/4/20.
 */
public class FileModel extends AlphaCellModel{
    private String filePath;

    public FileModel(String name, String content, CellType type) {
        super(name, content, type);
    }

    public FileModel(String name, String content, String filePath) {
        super(name, content, CellType.FILE);
        this.filePath = filePath;
        setDescription(AlphaFineHelper.findFolderName(content));
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public JSONObject ModelToJson() {
        JSONObject object = JSONObject.create();
        try {
            object.put("name", getName()).put("content", getContent()).put("filePath", getFilePath()).put("cellType", getType().getCellType());
        } catch (JSONException e) {
            FRLogger.getLogger().error(e.getMessage());
        }
        return object;
    }

    public static FileModel jsonToModel(JSONObject object) {
        String name = object.optString("name");
        String content = object.optString("content");
        String filePath = object.optString("filePath");
        return new FileModel(name, content, filePath);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FileModel)) return false;

        FileModel fileModel = (FileModel) o;

        return filePath != null ? filePath.equals(fileModel.filePath) : fileModel.filePath == null;
    }

    @Override
    public int hashCode() {
        return filePath != null ? filePath.hashCode() : 0;
    }
}
